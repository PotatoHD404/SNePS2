// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.util.StringTokenizer;

public class JLCommonSocket extends JLCommonImplementation
{
    final int version = 8001001;
    public static Transport client_object;
    public JavaLinkNotifier lisp_notifier;
    public int notify_seq;
    public static String default_l2j;
    public String l2j_ior;
    public static String default_j2l;
    public String j2l_ior;
    public static JavaLinkRoot server_object;
    int linkPort;
    String linkHost;
    String javaHost;
    int javaPort;
    
    public int version() {
        return 8001001;
    }
    
    public JLCommonSocket(final String[] array) throws IllegalArgumentException {
        this.notify_seq = -1;
        this.l2j_ior = "";
        this.j2l_ior = "";
        this.linkPort = 0;
        this.linkHost = "";
        this.javaHost = "";
        this.javaPort = 0;
        JavaLinkCommon.ltoj_anchor = this;
        this.linkPort = 0;
        this.linkHost = "";
        this.javaHost = "";
        this.javaPort = 0;
        this.j2l_ior = "";
        this.scanargs(array);
        JLCommonSocket.server_object = null;
        this.startL2JServer(true);
    }
    
    public JLCommonSocket(final String j2l_ior, final String linkHost, final int linkPort, final String javaHost, final int javaPort) throws IllegalArgumentException {
        this.notify_seq = -1;
        this.l2j_ior = "";
        this.j2l_ior = "";
        this.linkPort = 0;
        this.linkHost = "";
        this.javaHost = "";
        this.javaPort = 0;
        JavaLinkCommon.ltoj_anchor = this;
        this.linkPort = linkPort;
        this.linkHost = linkHost;
        this.javaHost = javaHost;
        this.javaPort = javaPort;
        this.j2l_ior = j2l_ior;
        JLCommonSocket.server_object = null;
        this.startL2JServer(true);
    }
    
    public JLCommonSocket(final JavaLinkRoot server_object) throws IllegalArgumentException {
        this.notify_seq = -1;
        this.l2j_ior = "";
        this.j2l_ior = "";
        this.linkPort = 0;
        this.linkHost = "";
        this.javaHost = "";
        this.javaPort = 0;
        JavaLinkCommon.ltoj_anchor = this;
        JLCommonSocket.server_object = server_object;
        this.linkPort = server_object.linkPort;
        this.linkHost = server_object.linkHost;
        this.j2l_ior = "";
        this.startL2JServer(false);
    }
    
    void startL2JServer(final boolean b) throws IllegalArgumentException {
        dprint("JLCommonSocket: starting up.");
        JLCommonSocket.client_object = null;
        dprint("JLCommonSocket: set lisp_notifier.");
        this.lisp_notifier = new JavaLinkNotifier();
        dprint("JLCommonSocket: lisp_notifier is set.");
        Object o;
        if (this.linkPort == 0) {
            if (this.j2l_ior == "") {
                this.j2l_ior = JLCommonSocket.default_j2l;
            }
            o = Transport.connectToServer(this.j2l_ior);
        }
        else {
            o = Transport.connectToServer(this.linkHost, this.linkPort);
        }
        if (Transport.isP(o)) {
            if (b) {
                JLCommonSocket.server_object = new JavaLinkRoot("", this.javaHost, this.javaPort);
            }
            JLCommonSocket.client_object = Transport.coerce(o);
            dprint("JLCommonSocket: client_object is set - " + JLCommonSocket.client_object);
            if (b) {
                this.notifyLisp("announceServer", 0, JLCommonSocket.server_object.advertisedHost, new int[] { JLCommonSocket.server_object.advertisingOnPort() });
            }
            JavaLinkDist.openDist(new JavaLinkDistIF());
            if (b) {
                JLCommonSocket.server_object.start();
            }
            dprint("JLCommonSocket: serverState==" + JLCommonSocket.server_object.serverState);
            dprint("Java VM version " + versionStringOfJDK());
            dprint("jLinker version 8001001");
            return;
        }
        dprint("JLCommonSocket: connectToServer failed " + o);
        throw new IllegalArgumentException("Lisp to Java: connectToServer failed " + o);
    }
    
    public int message(final int i, final String[] array) {
        final int length = array.length;
        int j = 0;
        String string = null;
        switch (length) {
            case 1: {
                string = array[0];
                break;
            }
            case 0: {
                string = "<String[0]>";
                break;
            }
            default: {
                string = array[0] + "..." + length;
                break;
            }
        }
        dprint("JLCommonSocket.message: " + i + "  Data: " + string);
        switch (i) {
            case 1: {
                if (JLCommonSocket.client_object == null) {
                    j = 2;
                    break;
                }
                this.notifyLisp("connectToServer", 0, "");
                j = 1;
                break;
            }
            case 2: {
                if (JavaLinkDist.defaultRemoteP(true)) {
                    j = 1;
                    break;
                }
                break;
            }
            case 3: {
                j = 3;
                break;
            }
            default: {
                dprint("JLCommonSocket: round-trip message ignored.");
                j = -1;
                break;
            }
        }
        dprint("JLCommonSocket.message: returning " + j);
        return j;
    }
    
    public void activate(final int value, final String[] array) {
        dprint("JLCommonSocket.activate: " + new Integer(value).toString());
        switch (value) {
            case 1: {
                dprint("JLCommonSocket: simple round-trip query.");
                if (JLCommonSocket.client_object == null) {
                    dprint("JLCommonSocket: client_object==null");
                    break;
                }
                this.notifyLisp("serverIsLive", 0, "");
                break;
            }
            case 99: {
                dprint("JLCommonSocket: shutdown.");
                JavaLinkDist.connectionComplete();
                try {
                    Thread.yield();
                    if (JLCommonSocket.client_object != null) {
                        JLCommonSocket.client_object.disconnect();
                    }
                    Thread.sleep(100L);
                    JavaLinkDist.closeDist();
                }
                catch (Exception ex) {}
                break;
            }
            default: {
                dprint("JLCommonSocket: one-way message ignored.");
                break;
            }
        }
    }
    
    public void scanargs(final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals("-debug")) {
                JavaLinkCommon.sdebug = true;
            }
            else if (array[i].equals("-lhost")) {
                ++i;
                this.linkHost = array[i];
            }
            else if (array[i].equals("-jhost")) {
                ++i;
                this.javaHost = array[i];
            }
            else if (array[i].equals("-lport")) {
                try {
                    ++i;
                    this.linkPort = Integer.parseInt(array[i]);
                }
                catch (Exception ex) {}
            }
            else if (array[i].equals("-jport")) {
                try {
                    ++i;
                    this.javaPort = Integer.parseInt(array[i]);
                }
                catch (Exception ex2) {}
            }
        }
        dprint("JLCommonSocket: scanargs: " + this.linkHost + " " + this.linkPort + " " + this.javaHost + " " + this.javaPort);
//        this.javaPort = 0;
    }
    
    static void dprint(final String s) {
        JavaLinkCommon.dsprint(s);
    }
    
    void notifyLisp(final String s, final int n, final String s2) {
        ++this.notify_seq;
        dprint("notifyLisp sending: " + s);
        this.lisp_notifier.notifyLispShort(s, n, this.notify_seq, s2);
        dprint("notifyLisp sent: " + s);
    }
    
    void notifyLisp(final String s, final int n, final String s2, final int[] array) {
        ++this.notify_seq;
        dprint("notifyLisp sending: " + s);
        this.lisp_notifier.notifyLispLongs(s, n, this.notify_seq, s2, array);
        dprint("notifyLisp sent: " + s);
    }
    
    void notifyLisp(final String s, final int n, final String[] array) {
        ++this.notify_seq;
        dprint("notifyLisp sending: " + s);
        this.lisp_notifier.notifyLispStrings(s, n, this.notify_seq, array);
        dprint("notifyLisp sent: " + s);
    }
    
    void notifyLisp(final String s, final int n, final String[] array, final int[] array2) {
        ++this.notify_seq;
        dprint("notifyLisp sending: " + s);
        this.lisp_notifier.notifyLispBoth(s, n, this.notify_seq, array, array2);
        dprint("notifyLisp sent: " + s);
    }
    
    int isRegistered(final Object o) {
        if (JLCommonSocket.client_object == null) {
            dprint("JLCommonSocket.callLisp: client_object==null, ignoring");
            return 0;
        }
        final int sObHandle = JavaLinkCommon.getSObHandle(o);
        if (sObHandle == 0) {
            return 0;
        }
        return sObHandle;
    }
    
    public int callLisp(final String s, final Object o) {
        final int registered = this.isRegistered(o);
        if (registered == 0) {
            return 0;
        }
        this.notifyLisp(s, registered, "");
        return this.notify_seq;
    }
    
    public int callLisp(final String s, final Object o, final String s2) {
        final int registered = this.isRegistered(o);
        if (registered == 0) {
            return 0;
        }
        this.notifyLisp(s, registered, s2);
        return this.notify_seq;
    }
    
    public int callLisp(final String s, final Object o, final String s2, final int[] array) {
        final int registered = this.isRegistered(o);
        if (registered == 0) {
            return 0;
        }
        this.notifyLisp(s, registered, s2, array);
        return this.notify_seq;
    }
    
    public int callLisp(final String s, final Object o, final String[] array, final int[] array2) {
        final int registered = this.isRegistered(o);
        if (registered == 0) {
            return 0;
        }
        this.notifyLisp(s, registered, array, array2);
        return this.notify_seq;
    }
    
    public static int versionOfJDK() {
        int n = 0;
        final StringTokenizer stringTokenizer = new StringTokenizer(versionStringOfJDK(), ".");
        for (int i = 0; i < 3; ++i) {
            int int1;
            if (stringTokenizer.hasMoreTokens()) {
                final String nextToken = stringTokenizer.nextToken();
                try {
                    int1 = Integer.parseInt(nextToken);
                }
                catch (NumberFormatException ex) {
                    int1 = 0;
                }
            }
            else {
                int1 = 0;
            }
            n = int1 + n * 100;
        }
        return n;
    }
    
    public static String versionStringOfJDK() {
        try {
            return System.getProperty("java.vm.version", "1.1");
        }
        catch (Exception ex) {
            return ex.toString();
        }
    }
    
    Class nameToClass(final Object o) {
        final String name = o.getClass().getName();
        if (name.equals("java.lang.Class")) {
            return (Class)o;
        }
        if (name.equals("java.lang.String")) {
            try {
                return Class.forName((String)o);
            }
            catch (Exception ex) {}
        }
        return new Object().getClass();
    }
    
    public Object invoke0(final Object obj, final String name) {
        try {
            return obj.getClass().getMethod(name, (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public Object invoke1(final Object obj, final String name, final Object o, final Object o2) {
        try {
            return obj.getClass().getMethod(name, this.nameToClass(o)).invoke(obj, o2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public Object invoke2(final Object obj, final String name, final Object o, final Object o2, final Object o3, final Object o4) {
        try {
            return obj.getClass().getMethod(name, this.nameToClass(o), this.nameToClass(o3)).invoke(obj, o2, o4);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public Object[] lispValues(final Object o, final String str, final int n, final int n2, final boolean b) {
        final TranStruct[] array = (TranStruct[])o;
        String s = "";
        int intValue = -1;
        final int length = array.length;
        if (length == 1 && JavaLinkDist.errorP(array[0])) {
            s = "LispError from " + str + ": " + JavaLinkDist.stringValue(array[0], 0) + " " + JavaLinkDist.stringValue(array[0], 1);
            JavaLinkDist.discardInLisp(array[0]);
        }
        else if (JavaLinkDist.integerP(array[0])) {
            intValue = JavaLinkDist.intValue(array[0]);
            if (intValue != length - 1) {
                s = "Strange result (r[0] not length) " + str + ": " + array.toString();
            }
            else if (intValue < n || intValue > n2) {
                s = "Unexpected values count from " + str + ": " + new Integer(intValue).toString();
            }
            else if (intValue > 0 && b && !JavaLinkDist.pointerP(array[1])) {
                s = "Unexpected value returned from " + str + ": " + array[1].toString();
            }
        }
        else if (length == 0) {
            s = "Strange zero-length result.";
        }
        else if (length == 1) {
            s = "Strange result(not err, not String) " + str + ": len=" + length + " " + array[0];
        }
        else if (length == 2) {
            s = "Strange result(not err, not String) " + str + ": len=" + length + " " + array[0] + " " + array[1];
        }
        else {
            s = "Strange result(not err, not String) " + str + ": len=" + length + " " + array[0] + " " + array[1] + " ...";
        }
        return new Object[] { new Integer(intValue), s };
    }
    
    public static void endRequest(final Object o) {
        synchronized (o) {
            ((Object[])o)[1] = "open";
            o.notify();
        }
    }
    
    public static void failRequest(final Object o, final String s) {
        synchronized (o) {
            ((Object[])o)[1] = "open";
            ((Object[])o)[0] = s;
            o.notify();
        }
    }
    
    public Object[] newGate() {
        return new Object[] { "", "closed" };
    }
    
    public String testGate(final Object[] array) {
        int i = 1;
        while (i != 0) {
            try {
                synchronized (array) {
                    if (((String)array[1]).equals("open")) {
                        i = 0;
                    }
                    else {
                        array.wait();
                    }
                }
            }
            catch (InterruptedException ex) {}
        }
        return (String)array[0];
    }
    
    public static void main(final String[] array) throws IllegalArgumentException, InterruptedException {
        dprint("JLCommonSocket: starting main.");
        new JLCommonSocket(array);
        LispCall.prototype = new LispCallSocket();
        JLCommonSocket.server_object.join();
        dprint("JLCommonSocket: leaving main.");
        System.exit(0);
    }
    
    static {
        JLCommonSocket.default_l2j = "LispToJava.trp";
        JLCommonSocket.default_j2l = "JavaToLisp.trp";
    }
}
