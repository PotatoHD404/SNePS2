// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class Transport
{
    public static final int ONE_WAY_CODE = 15;
    public static final int MESSAGE_CODE = 17;
    public static final int RESPONSE_CODE = 33;
    public static final int REQUEST_CODE = 18;
    public static final int REPLY_CODE = 34;
    public static final int ERROR_CODE = 62;
    public static final int DISCONNECT_CODE = 63;
    public static final int INVOKE_CODE = 19;
    public static final int ARGLIST_CODE = 20;
    public static final int RESULT_CODE = 35;
    public static final int ANSWER_CODE = 36;
    public static final int STRING_CODE = 66;
    public static final int INT_CODE = 65;
    public static final int SEQINT_CODE = 99;
    public static final int SEQSTRING_CODE = 101;
    public static final int SEQREAL_CODE = 103;
    public static final int EMPTY_SEQI_CODE = 98;
    public static final int EMPTY_SEQS_CODE = 100;
    public static final int EMPTY_SEQR_CODE = 102;
    public static final int EMPTY_SEQB_CODE = 104;
    public static final int SEQBYTE_CODE = 105;
    public static final int EMPTY_SEQK_CODE = 106;
    public static final int SEQSHORT_CODE = 107;
    public static final int EMPTY_SEQF_CODE = 110;
    public static final int SEQFLOAT_CODE = 111;
    public static final int NULL_CODE = 96;
    public static final int DEFAULT_BUFFER_SIZE = 512;
    public static final int SEQ_LENGTH_LIMIT = 16777216;
    public static final int PORT_IDLE = 0;
    public static final int PORT_CLOSED = -1;
    public static final int PORT_REQUEST = 3;
    public static final int PORT_WAITING_RESPONSE = 2;
    public static final int PORT_WAITING_REPLY = 4;
    public static final int PORT_MESSAGE = 1;
    public static final int PORT_INVOKE = 5;
    public static final int PORT_WAITING_RESULT = 6;
    public static final int TM_EXTNMASK = 57344;
    public static final int TM_EXBYTES = 8192;
    public static final int TM_EXSHORTS = 16384;
    public static final int TM_EXFLOATS = 24576;
    public static final String lispHomeName = "Lisp";
    public static int varInt;
    public static int exData;
    String kind;
    int state;
    int oneWay;
    public static final int ERR_PORT_CLASS = -101;
    public static final int ERR_PORT_STATE = -102;
    public static final int ERR_CALLBACK = -103;
    public static final int ERR_PROTOCOL = -104;
    public static final int ERR_REQUEST = -105;
    public static final int ERR_VAL_TYPE = -106;
    public static final int ERR_TOO_LONG = -107;
    public static final int ERR_PORT_IO = -108;
    public static final int ERR_FLUSH_IO = -108;
    public static final int ERR_THROW = -109;
    public static final int ERR_RESPONSE = -110;
    public static final int ERR_BUSY = -111;
    Socket socket;
    InputStream inStream;
    OutputStream outStream;
    byte[] buffer;
    int endpos;
    Object[] iData;
    Thread softLock;
    static int debugClient;
    static int debugServer;
    public static final Class thisClass;
    public static final String thisName;
    static boolean makeFlag;
    static boolean connectFlag;
    int haveCode;
    
    public static String debug(final int debugClient, final int debugServer) {
        final int debugClient2 = Transport.debugClient;
        if (debugClient >= 0) {
            Transport.debugClient = debugClient;
        }
        final int debugServer2 = Transport.debugServer;
        if (debugServer >= 0) {
            Transport.debugServer = debugServer;
        }
        return "Client was " + debugClient2 + " now " + Transport.debugClient + "  Server was " + debugServer2 + " now " + Transport.debugServer;
    }
    
    boolean debugP(final int n) {
        return (this.kind.equals("server") && n <= Transport.debugServer) || (this.kind.equals("client") && n <= Transport.debugClient);
    }
    
    public static boolean isP(final Object o) {
        return o != null && o.getClass().getName().equals(Transport.thisName);
    }
    
    public static Transport coerce(final Object o) {
        try {
            return (Transport)o;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static boolean isInt(final Object o) {
        return o.getClass().getName().equals("java.lang.Integer");
    }
    
    public static int getInt(final Object o) {
        if (isInt(o)) {
            try {
                return (int)o;
            }
            catch (Exception ex) {}
        }
        return 0;
    }
    
    public static boolean isString(final Object o) {
        return o.getClass().getName().equals("java.lang.String");
    }
    
    public static String getString(final Object o) {
        if (isString(o)) {
            try {
                return (String)o;
            }
            catch (Exception ex) {}
        }
        return "";
    }
    
    public static boolean isSeqInt(final Object o) {
        return o.getClass().getName().equals("[I");
    }
    
    public static int[] getSeqInt(final Object o) {
        if (isSeqInt(o)) {
            try {
                return (int[])o;
            }
            catch (Exception ex) {}
        }
        return new int[0];
    }
    
    public static boolean isSeqString(final Object o) {
        return o.getClass().getName().equals("[Ljava.lang.String;");
    }
    
    public static String[] getSeqString(final Object o) {
        if (isSeqString(o)) {
            try {
                return (String[])o;
            }
            catch (Exception ex) {}
        }
        return new String[0];
    }
    
    public static boolean isSeqReal(final Object o) {
        return o.getClass().getName().equals("[D");
    }
    
    public static double[] getSeqReal(final Object o) {
        if (isSeqReal(o)) {
            try {
                return (double[])o;
            }
            catch (Exception ex) {}
        }
        return new double[0];
    }
    
    Transport() {
        this.state = -1;
        this.oneWay = 0;
        this.buffer = new byte[512];
        this.endpos = 0;
        this.softLock = null;
        this.haveCode = 1000000;
    }
    
    Transport(final String kind, final Socket socket) throws IOException {
        this.state = -1;
        this.oneWay = 0;
        this.buffer = new byte[512];
        this.endpos = 0;
        this.softLock = null;
        this.haveCode = 1000000;
        if (kind.equals("server")) {
            this.kind = kind;
        }
        else {
            this.kind = "client";
        }
        this.socket = socket;
        this.inStream = socket.getInputStream();
        this.outStream = socket.getOutputStream();
        this.state = 0;
        this.softLock = null;
    }
    
    public static Object makeServerDataPort(final String s, final int n) {
        return makeServerDataPort(s, "", n);
    }
    
    public static Object makeServerDataPort(final String pathname, String str, int localPort) {
        try {
            synchronized (Transport.thisClass) {
                if (Transport.makeFlag) {
                    return "makeServerDataPort is busy, try again later";
                }
                Transport.makeFlag = true;
            }
            File file = null;
            FileWriter fileWriter = null;
            ServerSocket serverSocket = null;
            Object o = null;
            if (pathname.equals("") && localPort == 0) {
                return "Port number is required if advertising file is suppressed.";
            }
            try {
                serverSocket = new ServerSocket(localPort);
                localPort = serverSocket.getLocalPort();
                if (!pathname.equals("")) {
                    file = new File(pathname);
                    fileWriter = new FileWriter(file);
                    if (str.equals("")) {
                        str = "localhost";
                    }
                    final String string = str + " " + String.valueOf(localPort) + "\n";
                    fileWriter.write(string, 0, string.length());
                    fileWriter.close();
                    fileWriter = null;
                }
                final Socket accept = serverSocket.accept();
                accept.setTcpNoDelay(true);
                o = new Transport("server", accept);
            }
            catch (Exception ex) {
                o = ex;
            }
            finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    }
                    catch (Exception ex2) {}
                }
                if (file != null) {
                    try {
                        file.delete();
                    }
                    catch (Exception ex3) {}
                }
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    }
                    catch (Exception ex4) {}
                }
            }
            return o;
        }
        finally {
            Transport.makeFlag = false;
        }
    }
    
    public static Object[] makeServerDataPort(final int port) {
        final Object[] array = new Object[2];
        try {
            synchronized (Transport.thisClass) {
                if (Transport.makeFlag) {
                    array[0] = new Integer(-111);
                    array[1] = "makeServerDataPort is busy, try again later";
                    return array;
                }
                Transport.makeFlag = true;
            }
            try {
                final ServerSocket serverSocket = new ServerSocket(port);
                array[0] = new Integer(serverSocket.getLocalPort());
                array[1] = serverSocket;
            }
            catch (Exception ex) {
                array[0] = new Integer(0);
                array[1] = ex;
            }
            return array;
        }
        finally {
            Transport.makeFlag = false;
        }
    }
    
    public static Object makeServerDataPort(final Object[] array) {
        try {
            synchronized (Transport.thisClass) {
                if (Transport.makeFlag) {
                    return "makeServerDataPort is busy, try again later";
                }
                Transport.makeFlag = true;
            }
            ServerSocket serverSocket = null;
            Object o = null;
            try {
                serverSocket = (ServerSocket)array[1];
                final Socket accept = serverSocket.accept();
                accept.setTcpNoDelay(true);
                o = new Transport("server", accept);
            }
            catch (Exception ex) {
                o = ex;
            }
            finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    }
                    catch (Exception ex2) {}
                }
            }
            return o;
        }
        finally {
            Transport.makeFlag = false;
        }
    }
    
    public static Object makeServerDataPort(final int port, final String host) {
        try {
            synchronized (Transport.thisClass) {
                if (Transport.makeFlag) {
                    return "makeServerDataPort is busy, try again later";
                }
                Transport.makeFlag = true;
            }
            Transport transport;
            try {
                final Socket socket = new Socket(host, port);
                socket.setTcpNoDelay(true);
                transport = new Transport("server", socket);
            }
            catch (Exception ex) {
                return ex;
            }
            return transport;
        }
        finally {
            Transport.makeFlag = false;
        }
    }
    
    public static Object connectToServer(final String pathname) {
        try {
            synchronized (Transport.thisClass) {
                if (Transport.connectFlag) {
                    return "connectToServer is busy, try again later";
                }
                Transport.connectFlag = true;
            }
            InputStreamReader inputStreamReader = null;
            Object o = null;
            try {
                inputStreamReader = new FileReader(new File(pathname));
                final char[] array = new char[100];
                final int read = inputStreamReader.read(array, 0, 100);
                inputStreamReader.close();
                final String s = new String(array, 0, read);
                int index = s.indexOf(" ", 0);
                if (index < 1) {
                    throw new IOException("Ill-formed file");
                }
                final String substring = s.substring(0, index);
                while (s.startsWith(" ", index)) {
                    ++index;
                }
                final Socket socket = new Socket(substring, Integer.parseInt(s.substring(index, read).trim()));
                socket.setTcpNoDelay(true);
                o = new Transport("client", socket);
            }
            catch (Exception ex) {
                return ex;
            }
            finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    }
                    catch (Exception ex2) {}
                }
            }
            return o;
        }
        finally {
            Transport.connectFlag = false;
        }
    }
    
    public static Object connectToServer(final int n) {
        return connectToServer("localhost", n);
    }
    
    public static Object connectToServer(final String host, final int port) {
        try {
            synchronized (Transport.thisClass) {
                if (Transport.connectFlag) {
                    return "connectToServer is busy, try again later";
                }
                Transport.connectFlag = true;
            }
            Transport transport;
            try {
                final Socket socket = new Socket(host, port);
                socket.setTcpNoDelay(true);
                transport = new Transport("client", socket);
            }
            catch (Exception ex) {
                return ex;
            }
            return transport;
        }
        finally {
            Transport.connectFlag = false;
        }
    }
    
    public synchronized boolean disconnect() {
        switch (this.state) {
            case -1: {
                return false;
            }
            case 0: {
                if (this.kind.equals("client")) {
                    this.message(-1, 63, true);
                    try {
                        Thread.sleep(100L);
                    }
                    catch (Exception ex) {}
                    break;
                }
                break;
            }
        }
        try {
            this.state = -1;
            this.socket = null;
            this.inStream = null;
            this.outStream = null;
            this.socket.close();
            return true;
        }
        catch (Exception ex2) {
            return false;
        }
    }
    
    public Object doServer(final Object o) {
        Method method = null;
        Object method2 = null;
        Object method3 = null;
        Object o2 = "";
        final Class<?> class1 = o.getClass();
        try {
            method = class1.getMethod("doMessage", Integer.TYPE, Transport.thisClass);
            if (Integer.TYPE != method.getReturnType()) {
                method = null;
            }
        }
        catch (Exception ex) {}
        try {
            method2 = class1.getMethod("doRequest", "".getClass(), Integer.TYPE, new int[0].getClass(), new String[0].getClass(), new double[0].getClass());
        }
        catch (Exception ex2) {}
        try {
            method3 = class1.getMethod("doInvoke", new TranStruct[0].getClass());
        }
        catch (Exception ex3) {}
        this.oneWay = 0;
        while (this.state == 0) {
            final int streamInCode = this.streamInCode();
            switch (streamInCode) {
                case 15: {
                    this.oneWay = 1;
                    break;
                }
                case 63: {
                    this.disconnect();
                    o2 = "Disconnected";
                    break;
                }
                case 17: {
                    o2 = this.doServerMessage(o, method);
                    this.oneWay = 0;
                    break;
                }
                case 18: {
                    o2 = this.doServerRequest(o, method2);
                    this.oneWay = 0;
                    break;
                }
                case 19: {
                    o2 = this.doServerInvoke();
                    break;
                }
                case 20: {
                    o2 = this.doServerArglist(o, method3);
                    break;
                }
                default: {
                    o2 = "Bad code " + streamInCode;
                    break;
                }
            }
            if (!this.stringEqual("", o2)) {
                return o2;
            }
        }
        return "Bad server state";
    }
    
    boolean stringEqual(final Object o, final Object o2) {
        return this.stringP(o) && this.stringP(o2) && ((String)o).equals(o2);
    }
    
    boolean stringP(final Object o) {
        return o.getClass().getName().equals("java.lang.String");
    }
    
    int streamInInt() throws IOException {
        return (int)this.streamInValue();
    }
    
    private Object doServerMessage(final Object obj, final Object o) {
        int portInInt;
        try {
            portInInt = this.portInInt();
        }
        catch (Exception ex) {
            portInInt = -1;
        }
        if (portInInt <= 0) {
            return "Bad message code " + portInInt;
        }
        int intValue = -1;
        if (o != null) {
            try {
                intValue = (int)((Method)o).invoke(obj, new Integer(portInInt), this);
            }
            catch (Exception obj2) {
                if (this.oneWay == 0) {
                    this.respond(obj2);
                }
                if (this.debugP(1)) {
                    return "Message " + portInInt + " throws " + obj2;
                }
                return "";
            }
        }
        try {
            if (this.oneWay == 0) {
                this.respond(intValue);
            }
        }
        catch (Exception obj3) {
            return "Respond to " + portInInt + " throws " + obj3;
        }
        return "";
    }
    
    private Object doServerRequest(final Object obj, final Object o) {
        Object invoke = "No handler";
        String s = "";
        int streamInInt = 0;
        if (o != null) {
            try {
                s = (String)this.streamInValue();
                streamInInt = this.streamInInt();
                invoke = ((Method)o).invoke(obj, s, new Integer(streamInInt), (int[])this.streamInValue(), (String[])this.streamInValue(), (double[])this.streamInValue());
            }
            catch (Exception obj2) {
                if (this.oneWay == 0) {
                    this.respond(obj2);
                }
                return "Request " + s + " " + streamInInt + " throws " + obj2;
            }
        }
        try {
            if (this.oneWay == 0) {
                this.reply(invoke);
            }
        }
        catch (Exception obj3) {
            return "Reply to " + s + " " + streamInInt + " throws " + obj3;
        }
        return "";
    }
    
    private Object doServerInvoke() {
        int portInInt = -1;
        try {
            portInInt = this.portInInt();
        }
        catch (Exception ex) {}
        if (portInInt < 0) {
            return "Bad invoke length " + portInInt;
        }
        final TranStruct[] array = new TranStruct[portInInt];
        if (this.debugP(2)) {
            System.out.println("Transport.doInvoke length=" + portInInt);
        }
        this.iData = new Object[] { new Integer(0), new Integer(portInInt), array };
        return "";
    }
    
    private Object doServerArglist(final Object obj, final Object o) {
        if (this.iData == null) {
            return "bad server state";
        }
        final Object[] array = this.iData;
        int intValue = (int)array[0];
        final int intValue2 = (int)array[1];
        final TranStruct[] array2 = (TranStruct[])array[2];
        try {
            final int portInInt = this.portInInt();
            if (portInInt != intValue) {
                return "protocol error";
            }
            array2[portInInt] = this.newTS(portInInt, "Transport.doInvoke arg=");
            ++intValue;
        }
        catch (Exception obj2) {
            return "doInvokeArglist throws " + obj2;
        }
        if (intValue == intValue2) {
            if (this.debugP(2)) {
                System.out.println("Transport.doInvoke calling");
            }
            Object[] array3;
            try {
                array3 = (Object[])((Method)o).invoke(obj, array2);
            }
            catch (Exception obj3) {
                if (this.oneWay == 0) {
                    this.respond(obj3);
                }
                return "doInvoke throws " + obj3;
            }
            if (this.debugP(2)) {
                System.out.println("Transport.doInvoke returns");
            }
            Label_0341: {
                if (this.oneWay == 0) {
                    try {
                        final int length = array3.length;
                        if (this.debugP(2)) {
                            System.out.println("Transport.doInvoke result length=" + length);
                        }
                        this.streamOutCode(35);
                        this.portOut(length);
                        this.streamOutFlush();
                        for (int i = 0; i < length; ++i) {
                            this.sendResultPart(i, (TranStruct)array3[i]);
                        }
                        break Label_0341;
                    }
                    catch (Exception obj4) {
                        return "sendResultPart throws " + obj4;
                    }
                }
                this.oneWay = 0;
            }
            this.iData = null;
        }
        else {
            array[0] = new Integer(intValue);
        }
        return "";
    }
    
    TranStruct newTS(final int i, final String str) throws IOException {
        final String s = (String)this.streamInValue();
        final int streamInInt = this.streamInInt();
        final int[] array = (int[])this.streamInValue();
        final String[] array2 = (String[])this.streamInValue();
        final double[] array3 = (double[])this.streamInValue();
        if (this.debugP(2)) {
            System.out.println(str + i);
        }
        final TranStruct tranStruct = new TranStruct(s, streamInInt, array, array2, array3);
        switch (0xE000 & streamInInt) {
            case 8192:
            case 16384:
            case 24576: {
                tranStruct.setExdata(this.streamInValue());
                break;
            }
        }
        return tranStruct;
    }
    
    public Object message(final int n) {
        return this.message(n, 17, false);
    }
    
    public Object message(final int n, final boolean b) {
        return this.message(n, 17, b);
    }
    
    synchronized String grabSoftLock(final String str) {
        final Thread currentThread = Thread.currentThread();
        if (this.softLock == null) {
            this.softLock = currentThread;
        }
        else {
            if (this.softLock == currentThread) {
                return "Recursive call to" + str;
            }
            while (this.softLock != null) {
                try {
                    this.wait();
                }
                catch (Exception ex) {}
            }
            this.softLock = currentThread;
        }
        return "";
    }
    
    synchronized void dropSoftLock() {
        if (this.softLock == null) {
            return;
        }
        this.softLock = null;
        this.notifyAll();
    }
    
    Object message(final int n, final int n2, final boolean b) {
        try {
            final String grabSoftLock = this.grabSoftLock("message()");
            if (grabSoftLock.length() > 0) {
                return grabSoftLock;
            }
            final int state = this.state;
            int value = 0;
            final String s;
            Object responseIn = s = "";
            try {
                switch (this.state) {
                    case 0: {
                        this.state = 1;
                        if (b) {
                            value = this.streamOutCode(15);
                        }
                        if (value >= 0) {
                            value = this.streamOutCode(n2);
                        }
                        if (value >= 0 && n >= 0) {
                            value = this.portOut(n);
                        }
                        if (value >= 0) {
                            value = this.streamOutFlush();
                        }
                        if (value >= 0 && !b) {
                            responseIn = this.responseIn();
                            break;
                        }
                        break;
                    }
                    default: {
                        value = -102;
                        break;
                    }
                }
            }
            finally {
                this.state = state;
            }
            if (responseIn == s) {
                return new Integer(value);
            }
            return responseIn;
        }
        finally {
            this.dropSoftLock();
        }
    }
    
    public Object request(final String s) {
        return this.request(s, 0, new int[0], new String[0], new double[0]);
    }
    
    public Object request(final String s, final int n) {
        return this.request(s, n, new int[0], new String[0], new double[0]);
    }
    
    public Object request(final String s, final int n, final int[] array) {
        return this.request(s, n, array, new String[0], new double[0]);
    }
    
    public Object request(final String s, final int n, final int[] array, final String[] array2) {
        return this.request(s, n, array, array2, new double[0]);
    }
    
    public Object request(final String s, final int n, final int[] array, final String[] array2, final double[] array3) {
        try {
            final String grabSoftLock = this.grabSoftLock("request()");
            if (grabSoftLock.length() > 0) {
                return new Object[] { new Integer(-111), grabSoftLock };
            }
            final int state = this.state;
            int value = 0;
            if (!this.kind.equals("client")) {
                return new Object[] { new Integer(-101), "request" };
            }
            try {
                switch (this.state) {
                    case 0: {
                        this.state = 3;
                        value = this.streamOutCode(18);
                        if (value >= 0) {
                            value = this.streamOut(s);
                        }
                        if (value >= 0) {
                            value = this.streamOut(n);
                        }
                        if (value >= 0) {
                            value = this.streamOut(array);
                        }
                        if (value >= 0) {
                            value = this.streamOutStrings(s, array2);
                        }
                        if (value >= 0) {
                            value = this.streamOut(array3);
                        }
                        if (value >= 0) {
                            return this.replyIn();
                        }
                        break;
                    }
                }
                value = -102;
            }
            finally {
                this.state = state;
            }
            return new Object[] { new Integer(value), "request" };
        }
        finally {
            this.dropSoftLock();
        }
    }
    
    int streamOutStrings(final String s, final String[] array) {
        if (s.equalsIgnoreCase("Lisp")) {
            return this.streamOut(new String[0]);
        }
        return this.streamOut(array);
    }
    
    public Object invoke(final TranStruct[] array) {
        return this.invoke(array, false);
    }
    
    public Object invoke(final TranStruct[] array, final boolean b) {
        try {
            final String grabSoftLock = this.grabSoftLock("invoke()");
            if (grabSoftLock.length() > 0) {
                return new Object[] { new Integer(-111), grabSoftLock };
            }
            final int state = this.state;
            int value = 0;
            if (!this.kind.equals("client")) {
                return new Object[] { new Integer(-101), "invoke" };
            }
            try {
                switch (this.state) {
                    case 0: {
                        this.state = 5;
                        if (b) {
                            value = this.streamOutCode(15);
                        }
                        if (value >= 0) {
                            value = this.streamOutCode(19);
                        }
                        if (value >= 0) {
                            value = this.portOut(array.length);
                        }
                        if (value >= 0) {
                            value = this.streamOutFlush();
                        }
                        if (value >= 0) {
                            for (int i = 0; i < array.length; ++i) {
                                if (null != array[i] && value >= 0) {
                                    value = this.invokeArg(i, array[i]);
                                }
                            }
                        }
                        if (b) {
                            return new Object[0];
                        }
                        if (value >= 0) {
                            final Object resultIn = this.resultIn();
                            if (this.debugP(1)) {
                                System.out.println(this.kind + " invoke =>" + " isArrayLen:" + TranStruct.isArrayLen(resultIn) + " isArrayOfLen:" + TranStruct.isArrayOfLen(resultIn) + "  " + resultIn);
                            }
                            return resultIn;
                        }
                        break;
                    }
                    default: {
                        value = -102;
                        break;
                    }
                }
            }
            finally {
                this.state = state;
            }
            return new Object[] { new Integer(value), "invoke" };
        }
        finally {
            this.dropSoftLock();
        }
    }
    
    int invokeArg(final int n, final TranStruct tranStruct) {
        int sendTS = 0;
        switch (this.state) {
            case 5: {
                sendTS = this.sendTS(20, n, tranStruct, "invokeArg");
                break;
            }
            default: {
                sendTS = -102;
                break;
            }
        }
        return sendTS;
    }
    
    int sendTS(final int n, final int i, final TranStruct tranStruct, final String str) {
        if (this.debugP(2)) {
            System.out.println(str + i);
        }
        int n2 = this.streamOutCode(n);
        if (n2 >= 0) {
            n2 = this.portOut(i);
        }
        if (n2 >= 0) {
            n2 = this.streamOut(tranStruct.home);
        }
        if (n2 >= 0) {
            n2 = this.streamOut(tranStruct.type);
        }
        if (n2 >= 0) {
            n2 = this.streamOut(tranStruct.nums);
        }
        if (n2 >= 0) {
            n2 = this.streamOutStrings(tranStruct.home, tranStruct.strings);
        }
        if (n2 >= 0) {
            n2 = this.streamOut(tranStruct.reals);
        }
        Object exdata = tranStruct.getExdata();
        switch (tranStruct.type & 0xE000) {
            case 8192: {
                if (exdata == null) {
                    exdata = new byte[0];
                }
                this.streamOut((byte[])exdata);
                break;
            }
            case 16384: {
                if (exdata == null) {
                    exdata = new short[0];
                }
                this.streamOut((short[])exdata);
                break;
            }
            case 24576: {
                if (exdata == null) {
                    exdata = new float[0];
                }
                this.streamOut((float[])exdata);
                break;
            }
        }
        this.streamOutFlush();
        return n2;
    }
    
    public synchronized int respond(final int n) {
        this.streamOutCode(33);
        this.portOut(n);
        this.streamOutFlush();
        return 0;
    }
    
    public synchronized int respond(final Exception ex) {
        this.streamOutCode(62);
        this.portOut(ex.toString());
        this.streamOutFlush();
        return 0;
    }
    
    public synchronized int reply(final Object o) {
        this.streamOutCode(34);
        this.streamOut(o);
        this.streamOutFlush();
        return 0;
    }
    
    public synchronized int sendResultPart(final int n, final TranStruct tranStruct) {
        return this.sendTS(36, n, tranStruct, "Transport.doInvoke resultPart ");
    }
    
    public Object responseIn() {
        final int state = this.state;
        int portInInt = 0;
        final String s;
        Serializable obj = s = "";
        if (!this.kind.equals("client")) {
            return "responseIn: ERR_PORT_CLASS";
        }
        try {
            Label_0183: {
                switch (this.state) {
                    case 1: {
                        this.streamOutFlush();
                        this.state = 2;
                        switch (this.streamInCode()) {
                            case 33: {
                                try {
                                    portInInt = this.portInInt();
                                }
                                catch (Exception obj2) {
                                    obj = "responseIn->portInInt: " + obj2;
                                }
                                break Label_0183;
                            }
                            case 62: {
                                try {
                                    obj = this.portInString();
                                }
                                catch (Exception obj3) {
                                    obj = "responseIn->portInString: " + obj3;
                                }
                                break Label_0183;
                            }
                            case 18: {
                                obj = "responseIn: ERR_CALLBACK";
                                break Label_0183;
                            }
                            default: {
                                obj = "responseIn: ERR_PROTOCOL";
                                break Label_0183;
                            }
                        }
                    }
                    default: {
                        obj = "responseIn: ERR_PORT_STATE";
                        break;
                    }
                }
            }
        }
        finally {
            this.state = state;
        }
        if (obj == s) {
            obj = new Integer(portInInt);
        }
        if (this.debugP(1)) {
            System.out.println(this.kind + " responseIn " + obj);
        }
        return obj;
    }
    
    public Object replyIn() {
        final int state = this.state;
        Object o = null;
        if (!this.kind.equals("client")) {
            return "replyIn: ERR_PORT_CLASS";
        }
        try {
            Label_0231: {
                switch (this.state) {
                    case 3: {
                        this.streamOutFlush();
                        this.state = 4;
                        switch (this.streamInCode()) {
                            case 34: {
                                try {
                                    o = this.streamInValue();
                                }
                                catch (Exception ex) {
                                    o = ex;
                                }
                                if (this.debugP(1)) {
                                    System.out.println(this.kind + " replyIn " + o);
                                }
                                return o;
                            }
                            case 62: {
                                try {
                                    o = this.portInString();
                                }
                                catch (Exception ex2) {
                                    o = ex2;
                                }
                                if (this.debugP(1)) {
                                    System.out.println(this.kind + " replyIn Err" + o);
                                    break Label_0231;
                                }
                                break Label_0231;
                            }
                            case 18: {
                                o = "replyIn: ERR_CALLBACK";
                                break Label_0231;
                            }
                            default: {
                                o = "replyIn: ERR_PROTOCOL";
                                break Label_0231;
                            }
                        }
                    }
                    default: {
                        o = "replyIn: ERR_PORT_STATE";
                        break;
                    }
                }
            }
        }
        finally {
            this.state = state;
        }
        return o;
    }
    
    public Object resultIn() {
        final int state = this.state;
        if (!this.kind.equals("client")) {
            return "resultIn: ERR_PORT_CLASS";
        }
        Object portInString = null;
        try {
            Label_0258: {
                switch (this.state) {
                    case 5: {
                        this.streamOutFlush();
                        this.state = 6;
                        switch (this.streamInCode()) {
                            case 35: {
                                try {
                                    final int portInInt = this.portInInt();
                                    final Object[] array = new Object[portInInt];
                                    if (this.debugP(1)) {
                                        System.out.println(this.kind + " resultIn " + portInInt);
                                    }
                                    for (int i = 0; i < portInInt; ++i) {
                                        array[i] = this.resultPartIn(i);
                                    }
                                    portInString = array;
                                }
                                catch (Exception ex) {
                                    portInString = ex;
                                }
                                break Label_0258;
                            }
                            case 62: {
                                try {
                                    portInString = this.portInString();
                                }
                                catch (Exception ex2) {
                                    portInString = ex2;
                                }
                                if (this.debugP(1)) {
                                    System.out.println(this.kind + " resultIn Err" + portInString);
                                    break Label_0258;
                                }
                                break Label_0258;
                            }
                            case 18: {
                                portInString = "resultIn: ERR_CALLBACK";
                                break Label_0258;
                            }
                            default: {
                                portInString = "resultIn: ERR_PROTOCOL";
                                break Label_0258;
                            }
                        }
                    }
                    default: {
                        portInString = "resultIn: ERR_PORT_STATE";
                        break;
                    }
                }
            }
        }
        finally {
            this.state = state;
        }
        return portInString;
    }
    
    Object resultPartIn(final int value) {
        final int streamInCode = this.streamInCode();
        switch (streamInCode) {
            case 36: {
                try {
                    final int portInInt = this.portInInt();
                    if (value != portInInt) {
                        return new Object[] { new Integer(-104), new Integer(value), new Integer(portInInt), "resultPartIn" };
                    }
                    return this.newTS(portInInt, "resultPartIn");
                }
                catch (Exception ex) {
                    return ex;
                }
            }
        }
        return new Object[] { new Integer(-104), new Integer(streamInCode), "resultPartIn" };
    }
    
    int streamOutFlush() {
        return this.portFlush();
    }
    
    int streamOutCode(final int n) {
        return this.portOut((byte)n);
    }
    
    int streamOut(final Object o) {
        if (o == null) {
            return this.streamOutCode(96);
        }
        final String name = o.getClass().getName();
        if (name.equals("java.lang.Integer")) {
            return this.streamOut((int)o);
        }
        if (name.equals("java.lang.String")) {
            return this.streamOut((String)o);
        }
        if (name.equals("[I")) {
            return this.streamOut((int[])o);
        }
        if (name.equals("[D")) {
            return this.streamOut((double[])o);
        }
        if (name.equals("[Ljava.lang.String;")) {
            return this.streamOut((String[])o);
        }
        return -106;
    }
    
    int streamOut(final String str) {
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut STRING: " + str);
        }
        this.portOut((byte)66);
        return this.portOut(str);
    }
    
    int streamOut(final int i) {
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut INT: " + i);
        }
        this.portOut((byte)65);
        return this.portOut(i);
    }
    
    int streamOut(final byte[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQB");
            }
            this.portOut((byte)104);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQBYTE " + array.length);
        }
        this.portOut((byte)105);
        this.portOut(array.length);
        this.portOut(array);
        return array.length;
    }
    
    int streamOut(final short[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQK");
            }
            this.portOut((byte)106);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQSHORT " + array.length);
        }
        this.portOut((byte)107);
        this.portOut(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.portOut(array[i]);
        }
        return array.length;
    }
    
    int streamOut(final int[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQI");
            }
            this.portOut((byte)98);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQINT " + array.length);
        }
        this.portOut((byte)99);
        this.portOut(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.portOut(array[i]);
        }
        return array.length;
    }
    
    int streamOut(final float[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQF");
            }
            this.portOut((byte)110);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQFLOAT " + array.length);
        }
        this.portOut((byte)111);
        this.portOut(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.portOut(array[i]);
        }
        return array.length;
    }
    
    int streamOut(final double[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQR");
            }
            this.portOut((byte)102);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQREAL " + array.length);
        }
        this.portOut((byte)103);
        this.portOut(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.portOut(array[i]);
        }
        return array.length;
    }
    
    int streamOut(final String[] array) {
        if (0 == array.length) {
            if (this.debugP(2)) {
                System.out.println(this.kind + " streamOut EMPTY_SEQS");
            }
            this.portOut((byte)100);
            return 0;
        }
        if (array.length >= 16777216) {
            return -107;
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamOut SEQSTRING " + array.length);
        }
        this.portOut((byte)101);
        this.portOut(array.length);
        for (int i = 0; i < array.length; ++i) {
            this.portOut(array[i]);
        }
        return array.length;
    }
    
    int streamInCode() {
        if (this.haveCode == 1000000) {
            return this.portIn_8();
        }
        final int haveCode = this.haveCode;
        this.haveCode = 1000000;
        return haveCode;
    }
    
    int streamInCode(final int haveCode) {
        if (this.haveCode == 1000000) {
            return this.haveCode = haveCode;
        }
        return -104;
    }
    
    Object streamInValue() throws IOException {
        final int streamInCode = this.streamInCode();
        String str = null;
        Object obj = null;
        switch (streamInCode) {
            case 66: {
                str = "STRING";
                obj = this.portInString();
                break;
            }
            case 65: {
                str = "INT";
                obj = new Integer(this.portInInt());
                break;
            }
            case 99: {
                str = "SEQINT";
                obj = this.portInSeqInt();
                break;
            }
            case 101: {
                str = "SEQSTRING";
                obj = this.portInSeqString();
                break;
            }
            case 103: {
                str = "SEQREAL";
                obj = this.portInSeqReal();
                break;
            }
            case 105: {
                str = "SEQBYTE";
                obj = this.portInSeqByte();
                break;
            }
            case 107: {
                str = "SEQSHORT";
                obj = this.portInSeqShort();
                break;
            }
            case 111: {
                str = "SEQFLOAT";
                obj = this.portInSeqFloat();
                break;
            }
            case 98: {
                str = "EMPTY_SEQI";
                obj = new int[0];
                break;
            }
            case 102: {
                str = "EMPTY_SEQR";
                obj = new double[0];
                break;
            }
            case 100: {
                str = "EMPTY_SEQS";
                obj = new String[0];
                break;
            }
            case 104: {
                str = "EMPTY_SEQB";
                obj = new byte[0];
                break;
            }
            case 106: {
                str = "EMPTY_SEQK";
                obj = new short[0];
                break;
            }
            case 110: {
                str = "EMPTY_SEQF";
                obj = new float[0];
                break;
            }
            case 96: {
                str = "NULL";
                obj = null;
                break;
            }
            default: {
                str = "NOT_DATA";
                obj = null;
                this.streamInCode(streamInCode);
                break;
            }
        }
        if (this.debugP(2)) {
            System.out.println(this.kind + " streamIn " + str + ": " + obj);
        }
        return obj;
    }
    
    int portIn_8() {
        int read;
        try {
            read = this.inStream.read();
            if (this.debugP(5)) {
                System.out.println(this.kind + " portIn_8: " + read);
            }
        }
        catch (IOException ex) {
            read = -108;
        }
        return read;
    }
    
    int portIn_16() {
        final int portIn_8 = this.portIn_8();
        if (portIn_8 < 0) {
            return portIn_8;
        }
        final int portIn_9 = this.portIn_8();
        if (portIn_9 < 0) {
            return portIn_9;
        }
        final int i = portIn_8 + (portIn_9 << 8);
        if (this.debugP(4)) {
            System.out.println(this.kind + " portIn_16: " + i);
        }
        return i;
    }
    
    int portInIntOLD() throws IOException {
        final int portIn_8 = this.portIn_8();
        if (portIn_8 < 0) {
            throw new IOException("portInInt->portIn_8=" + portIn_8);
        }
        int i = 0;
        for (int j = 0; j < 32; j += 8) {
            final int portIn_9 = this.portIn_8();
            if (portIn_9 < 0) {
                throw new IOException("portInInt->portIn_8=" + portIn_9);
            }
            i |= portIn_9 << j;
        }
        if (portIn_8 != 0) {
            i = -i;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInInt: " + i);
        }
        return i;
    }
    
    int portInInt() throws IOException {
        final int portIn_8 = this.portIn_8();
        if (portIn_8 < 0) {
            throw new IOException("portInInt->portIn_8=" + portIn_8);
        }
        int n = 0;
        boolean b = false;
        switch (portIn_8) {
            case 0: {
                n = 4;
                b = false;
                break;
            }
            case 1: {
                n = 4;
                b = true;
                break;
            }
            case 2: {
                n = 3;
                b = false;
                break;
            }
            case 3: {
                n = 3;
                b = true;
                break;
            }
            case 4: {
                n = 2;
                b = false;
                break;
            }
            case 5: {
                n = 2;
                b = true;
                break;
            }
            case 6: {
                n = 1;
                b = false;
                break;
            }
            case 7: {
                n = 1;
                b = true;
                break;
            }
            case 8: {
                return -1;
            }
            default: {
                return portIn_8 - 9;
            }
        }
        int i = 0;
        int n2 = 0;
        for (int j = 0; j < n; ++j) {
            final int portIn_9 = this.portIn_8();
            if (portIn_9 < 0) {
                throw new IOException("portInInt->portIn_8=" + portIn_9);
            }
            i |= portIn_9 << n2;
            n2 += 8;
        }
        if (b) {
            i = -i;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInInt: " + i);
        }
        return i;
    }
    
    String portInString() throws IOException {
        final int portInInt = this.portInInt();
        if (portInInt < 0) {
            return null;
        }
        final StringBuffer sb = new StringBuffer(portInInt);
        for (int i = 0; i < portInInt; ++i) {
            final int portIn_16 = this.portIn_16();
            if (portIn_16 < 0) {
                throw new IOException("portIn=" + portIn_16);
            }
            sb.append((char)portIn_16);
        }
        final String string = sb.toString();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInString: " + string);
        }
        return string;
    }
    
    int[] portInSeqInt() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqInt: " + portInInt);
        }
        final int[] array = new int[portInInt];
        for (int i = 0; i < portInInt; ++i) {
            array[i] = this.portInInt();
        }
        return array;
    }
    
    short[] portInSeqShort() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqShort: " + portInInt);
        }
        final short[] array = new short[portInInt];
        for (int i = 0; i < portInInt; ++i) {
            array[i] = (short)this.portInInt();
        }
        return array;
    }
    
    byte[] portInSeqByte() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqByte: " + portInInt);
        }
        final byte[] b = new byte[portInInt];
        final int read = this.inStream.read(b);
        final int i = portInInt - read;
        if (read < portInInt) {
            throw new IOException("streamInSeqByte short: " + i);
        }
        return b;
    }
    
    String[] portInSeqString() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqString: " + portInInt);
        }
        final String[] array = new String[portInInt];
        for (int i = 0; i < portInInt; ++i) {
            array[i] = this.portInString();
        }
        return array;
    }
    
    float[] portInSeqFloat() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqFloat: " + portInInt);
        }
        final float[] array = new float[portInInt];
        for (int i = 0; i < portInInt; ++i) {
            array[i] = this.portInFloat();
        }
        return array;
    }
    
    double[] portInSeqReal() throws IOException {
        final int portInInt = this.portInInt();
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInSeqReal: " + portInInt);
        }
        final double[] array = new double[portInInt];
        for (int i = 0; i < portInInt; ++i) {
            array[i] = this.portInReal();
        }
        return array;
    }
    
    double portInReal() throws IOException {
        final int portIn_8 = this.portIn_8();
        if (portIn_8 < 0) {
            throw new IOException("portInReal->portIn_8=" + portIn_8);
        }
        final long lng = this.portIn_8();
        if (lng < 0L) {
            throw new IOException("portInReal->portIn_8=" + lng);
        }
        final long lng2 = this.portIn_8();
        if (lng2 < 0L) {
            throw new IOException("portInReal->portIn_8=" + lng2);
        }
        long n = 0L;
        for (int i = 0; i < 51; i += 8) {
            final long lng3 = this.portIn_8();
            if (lng3 < 0L) {
                throw new IOException("portInReal->portIn_8=" + lng3);
            }
            n |= lng3 << i;
        }
        long n2 = n | (lng2 << 8 | lng) << 52;
        if (portIn_8 != 0) {
            n2 |= Long.MIN_VALUE;
        }
        final double longBitsToDouble = Double.longBitsToDouble(n2);
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInReal: " + longBitsToDouble);
        }
        return longBitsToDouble;
    }
    
    float portInFloat() throws IOException {
        final int portIn_8 = this.portIn_8();
        if (portIn_8 < 0) {
            throw new IOException("portInFloat->portIn_8=" + portIn_8);
        }
        final int portIn_9 = this.portIn_8();
        if (portIn_9 < 0) {
            throw new IOException("portInFloat->portIn_8=" + portIn_9);
        }
        final int portIn_10 = this.portIn_8();
        if (portIn_10 < 0) {
            throw new IOException("portInFloat->portIn_8=" + portIn_10);
        }
        int n = 0;
        for (int i = 0; i < 23; i += 8) {
            final int portIn_11 = this.portIn_8();
            if (portIn_11 < 0) {
                throw new IOException("portInFloat->portIn_8=" + portIn_11);
            }
            n |= portIn_11 << i;
        }
        int n2 = n | (portIn_10 << 8 | portIn_9) << 23;
        if (portIn_8 != 0) {
            n2 |= Integer.MIN_VALUE;
        }
        final float intBitsToFloat = Float.intBitsToFloat(n2);
        if (this.debugP(3)) {
            System.out.println(this.kind + " portInFloat: " + intBitsToFloat);
        }
        return intBitsToFloat;
    }
    
    int portFlush() {
        if (this.endpos > 0) {
            try {
                if (this.debugP(4)) {
                    System.out.println(this.kind + " portFlush " + this.endpos + " bytes: " + this.buffer[0] + " " + this.buffer[1] + "...");
                }
                this.outStream.write(this.buffer, 0, this.endpos);
                this.outStream.flush();
            }
            catch (Exception ex) {
                return -108;
            }
        }
        return this.endpos = 0;
    }
    
    int portReserveSpace(final int n) {
        int portFlush = 0;
        if (this.endpos + n > this.buffer.length) {
            portFlush = this.portFlush();
        }
        if (portFlush < 0) {
            return portFlush;
        }
        return this.endpos + n;
    }
    
    int portOut(final byte i) {
        final int portReserveSpace = this.portReserveSpace(1);
        if (portReserveSpace < 0) {
            return portReserveSpace;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portOut byte: " + i);
        }
        this.bufferOut_8(i);
        return this.endpos;
    }
    
    int portOut(final byte[] array) {
        if (this.buffer.length - this.endpos < array.length) {
            this.portFlush();
        }
        for (int i = 0; i < array.length; i += 100) {
            final int portReserveSpace = this.portReserveSpace(100);
            if (portReserveSpace < 0) {
                return portReserveSpace;
            }
            for (int j = i; j < array.length; ++j) {
                this.bufferOut_8(array[j]);
            }
        }
        return this.endpos;
    }
    
    int portOut(final char c) {
        final int portReserveSpace = this.portReserveSpace(2);
        if (portReserveSpace < 0) {
            return portReserveSpace;
        }
        if (this.debugP(4)) {
            System.out.println(this.kind + " portOut char: " + c);
        }
        this.bufferOut_16(c);
        return this.endpos;
    }
    
    int portOut(final short n) {
        return this.portOut((int)n);
    }
    
    int portOut(final int i) {
        long n = i;
        int n2;
        int n3;
        int n4;
        if (n < 0L) {
            n = -n;
            if (Transport.varInt == 0) {
                n2 = 1;
                n3 = 32;
                n4 = 5;
            }
            else if (n == 1L) {
                n2 = 8;
                n3 = 0;
                n4 = 1;
            }
            else if (n < 256L) {
                n2 = 7;
                n3 = 8;
                n4 = 2;
            }
            else if (n < 65536L) {
                n2 = 5;
                n3 = 16;
                n4 = 3;
            }
            else if (n < 16777216L) {
                n2 = 3;
                n3 = 24;
                n4 = 4;
            }
            else {
                n2 = 1;
                n3 = 32;
                n4 = 5;
            }
        }
        else if (Transport.varInt == 0) {
            n2 = 0;
            n3 = 32;
            n4 = 5;
        }
        else if (n < 247L) {
            n2 = i + 9;
            n3 = 0;
            n4 = 1;
        }
        else if (n < 256L) {
            n2 = 6;
            n3 = 8;
            n4 = 2;
        }
        else if (n < 65536L) {
            n2 = 4;
            n3 = 16;
            n4 = 3;
        }
        else if (n < 16777216L) {
            n2 = 2;
            n3 = 24;
            n4 = 4;
        }
        else {
            n2 = 0;
            n3 = 32;
            n4 = 5;
        }
        final int portReserveSpace = this.portReserveSpace(n4);
        if (portReserveSpace < 0) {
            return portReserveSpace;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portOut int: " + i);
        }
        this.bufferOut_8(n2);
        for (int j = 0; j < n3; j += 8) {
            this.bufferOut_8((int)(0xFFL & n >> j));
        }
        return this.endpos;
    }
    
    int portOut(final float f) {
        final int portReserveSpace = this.portReserveSpace(6);
        if (portReserveSpace < 0) {
            return portReserveSpace;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portOut float: " + f);
        }
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        int n = 0;
        if (floatToRawIntBits < 0) {
            floatToRawIntBits ^= Integer.MIN_VALUE;
            n = 1;
        }
        final int n2 = floatToRawIntBits >> 23;
        final int n3 = (floatToRawIntBits | 0x7F800000) ^ 0x7F800000;
        this.bufferOut_8(n);
        this.bufferOut_16(n2);
        for (int i = 0; i < 23; i += 8) {
            this.bufferOut_8((int)(0xFFL & (long)(n3 >> i)));
        }
        return this.endpos;
    }
    
    int portOut(final double d) {
        final int portReserveSpace = this.portReserveSpace(10);
        if (portReserveSpace < 0) {
            return portReserveSpace;
        }
        if (this.debugP(3)) {
            System.out.println(this.kind + " portOut double: " + d);
        }
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        int n = 0;
        if (doubleToRawLongBits < 0L) {
            doubleToRawLongBits ^= Long.MIN_VALUE;
            n = 1;
        }
        final int n2 = (int)(doubleToRawLongBits >> 52);
        final long n3 = (doubleToRawLongBits | 0x7FF0000000000000L) ^ 0x7FF0000000000000L;
        this.bufferOut_8(n);
        this.bufferOut_16(n2);
        for (int i = 0; i < 52; i += 8) {
            this.bufferOut_8((int)(0xFFL & n3 >> i));
        }
        return this.endpos;
    }
    
    int portOut(final String s) {
        if (s == null) {
            int n;
            if (Transport.varInt == 0) {
                n = this.portOut(0);
            }
            else {
                n = this.portOut(-1);
            }
            if (n < 0) {
                return n;
            }
            if (this.debugP(3)) {
                System.out.println(this.kind + " portOut String: " + s);
            }
        }
        else {
            final int portOut = this.portOut(s.length());
            if (portOut < 0) {
                return portOut;
            }
            if (this.debugP(3)) {
                System.out.println(this.kind + " portOut String: " + s);
            }
            for (int i = 0; i < s.length(); ++i) {
                final int portOut2 = this.portOut(s.charAt(i));
                if (portOut2 < 0) {
                    return portOut2;
                }
            }
        }
        return this.endpos;
    }
    
    int bufferOut_8(final int i) {
        if (this.debugP(5)) {
            System.out.println(this.kind + " bufferOut_8: " + i);
        }
        this.buffer[this.endpos] = (byte)(0xFF & i);
        return ++this.endpos;
    }
    
    int bufferOut_16(final int i) {
        if (this.debugP(5)) {
            System.out.println(this.kind + " bufferOut_16: " + i);
        }
        this.buffer[this.endpos] = (byte)(0xFF & i);
        ++this.endpos;
        this.buffer[this.endpos] = (byte)(0xFF & i >> 8);
        return ++this.endpos;
    }
    
    static {
        Transport.varInt = 0;
        Transport.exData = 0;
        Transport.debugClient = 0;
        Transport.debugServer = 0;
        thisClass = new Transport().getClass();
        thisName = Transport.thisClass.getName();
        Transport.makeFlag = false;
        Transport.connectFlag = false;
    }
}
