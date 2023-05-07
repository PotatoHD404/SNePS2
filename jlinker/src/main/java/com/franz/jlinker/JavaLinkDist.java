// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.lang.reflect.Field;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

public class JavaLinkDist
{
    public static final String TR_HOMEImmediate = "Self";
    public static final int TR_NONE = 0;
    public static final int TR_RemoteWidth = 3;
    public static final int TR_IndirectShift = 0;
    public static final int TR_IMMEDIATE = 0;
    public static final int TR_INDIRECT = 1;
    public static final int TR_LastShift = 1;
    public static final int TR_KEEP = 0;
    public static final int TR_DISCARD = 1;
    public static final int TR_DiscardShift = 2;
    public static final int TR_LIVE = 0;
    public static final int TR_DISCARDED = 1;
    public static final int TR_RankWidth = 5;
    public static final int TR_RankShift = 3;
    public static final int TR_HIRANK = 31;
    public static final int TR_BaseWidth = 5;
    public static final int TR_ExtnWidth = 3;
    public static final int TR_BaseShift = 8;
    public static final int TR_ExtnShift = 13;
    public static final int TR_BYTE = 1;
    public static final int TR_SHORT = 2;
    public static final int TR_INT = 3;
    public static final int TR_LONG = 4;
    public static final int TR_CHAR = 5;
    public static final int TR_STRING = 6;
    public static final int TR_SINGLE = 7;
    public static final int TR_DOUBLE = 8;
    public static final int TR_BOOL = 9;
    public static final int TR_SYMBOL = 10;
    public static final int TR_MESSAGE = 30;
    public static final int TR_NULL = 31;
    public static final int TR_AggrWidth = 8;
    public static final int TR_AggrShift = 16;
    public static final int TR_AggrMask = 255;
    public static final int TR_POINTER = 1;
    public static final int TR_ARRAY = 4;
    public static final int TR_CLASS = 5;
    public static final int TR_CONSTRUCTOR = 6;
    public static final int TR_METHOD = 7;
    public static final int TR_ERROR = 61;
    public static final int TR_INDEXWIDTH = 29;
    public static final int TR_MKActivate = 1;
    public static final int TR_MKMessage = 2;
    public static final int TR_MKDiscard = 3;
    public static final int TR_MKInvoke = 4;
    public static final int TR_MKNotify = 5;
    public static final int TR_EXTNMASK = 57344;
    public static final int TR_EXBYTES = 8192;
    public static final int TR_EXSHORTS = 16384;
    public static final int TR_EXFLOATS = 24576;
    static JavaLinkDistIF remoteServer;
    static JavaLinkDist localServer;
    static boolean remoteReady;
    JavaLinkDistIF remoteSelf;
    static Hashtable table;
    static int connectIndex;
    static int nextIndex;
    static int tableLive;
    static int tableFree;
    static int oldestFree;
    static int newestFree;
    static int topIndex;
    String localName;
    static final Class structClass;
    static final String structName;
    Object nullMarker;
    Object[] ans;
    static final int IGNORE = 0;
    static final int setIMM = 1;
    static final int setIND = 2;
    static final int setLIVE = 1;
    static final int setLAST = 2;
    static final int setDEAD = 3;
    
    public static Object lispError(final LispException ex) {
        return ex.lispErr;
    }
    
    public static boolean query() {
        return query(false);
    }
    
    public static boolean query(final boolean b) {
        if (defaultRemoteP()) {
            if (!b) {
                return true;
            }
            final TranStruct[] invokeInLisp = invokeInLisp(2, newDistOb("cl:values"), new TranStruct[0]);
            if (invokeInLisp.length == 1 && integerP(invokeInLisp[0]) && 0 == intValue(invokeInLisp[0])) {
                return true;
            }
            closeDist();
        }
        return false;
    }
    
    public static void disconnect() {
        JavaLinkCommon.ltoj_anchor.activate(99, new String[0]);
    }
    
    public static boolean advertise(final int n, final int n2) {
        return advertiseInner(false, "", "", n, n2);
    }
    
    public static boolean advertise(final String s, final String s2, final int n, final int n2) {
        return advertiseInner(true, s, s2, n, n2);
    }
    
    static boolean advertiseInner(final boolean b, String s, final String s2, final int n, int n2) {
        if (query(false)) {
            return true;
        }
        JavaLinkRoot javaLinkRoot;
        if (b) {
            if (s.equals("")) {
                s = "LispToJava.trp";
            }
            javaLinkRoot = new JavaLinkRoot(s, s2, n);
        }
        else {
            if (n < 1) {
                return false;
            }
            javaLinkRoot = new JavaLinkRoot("", "", n);
        }
        javaLinkRoot.start();
        int n3 = 1;
        int n4 = 1;
        while (javaLinkRoot.linkPort == 0) {
            try {
                if ((!javaLinkRoot.isAlive() || n2 >= 0) && (!javaLinkRoot.isAlive() || n2 == 0)) {
                    Object o;
                    if (b) {
                        o = Transport.connectToServer(s);
                    }
                    else {
                        o = Transport.connectToServer(n);
                    }
                    if (Transport.isP(o)) {
                        Transport.coerce(o).disconnect();
                    }
                    JavaLinkCommon.dsprint("JavaLinkDist.advertise FAILED.");
                    return false;
                }
                if (n3 < n4) {
                    ++n3;
                }
                else {
                    n3 = 0;
                    n4 *= 2;
                    JavaLinkCommon.dsprint("JavaLinkDist.advertise: waiting for Lisp.");
                }
                synchronized (javaLinkRoot) {
                    javaLinkRoot.wait(1000L);
                }
            }
            catch (InterruptedException ex) {}
            if (n2 >= 0) {
                --n2;
            }
        }
        JavaLinkCommon.dsprint("JavaLinkDist.advertise: starting Anchor.");
        try {
            new JLCommonSocket(javaLinkRoot);
        }
        catch (Exception ex2) {
            return false;
        }
        if (!connectionComplete()) {
            return false;
        }
        JavaLinkCommon.dsprint("JavaLinkDist.advertise done.");
        return true;
    }
    
    public static boolean connectionComplete() {
        int n = 0;
        while (!defaultRemoteP()) {
            try {
                if (defaultRemoteP()) {
                    return true;
                }
                Thread.sleep(100L);
                if (defaultRemoteP()) {
                    return true;
                }
                if (++n > 300) {
                    JavaLinkCommon.dsprint("JavaLinkDist: failing with timeout.");
                    return false;
                }
                if (n != 4 && n != 24 && n != 154) {
                    continue;
                }
                JavaLinkCommon.dsprint("JavaLinkDist: waiting for remote server...");
            }
            catch (InterruptedException ex) {}
        }
        return true;
    }
    
    public static boolean connect(final String s, final String s2, final int n, final int n2, final int n3) {
        return connectInner(true, s, "", 0, s2, n, n2, n3);
    }
    
    public static boolean connect(final String s, final int n, final String s2, final int n2, final int n3, final int n4) {
        return connectInner(false, "", s, n, s2, n2, n3, n4);
    }
    
    static boolean connectInner(final boolean b, final String s, final String s2, final int n, final String s3, final int n2, final int n3, int n4) {
        if (query(false)) {
            return true;
        }
        while (!connectInner(b, s, s2, n, s3, n2)) {
            if (n3 < 0) {
                n4 = -1;
            }
            else {
                --n4;
            }
            if (n4 < 0) {
                return false;
            }
            try {
                Thread.sleep(n3);
            }
            catch (Exception ex) {}
        }
        return true;
    }
    
    static boolean connectInner(final boolean b, final String str, final String str2, final int i, final String s, final int n) {
        if (query(false)) {
            return true;
        }
        Label_0208: {
            if (b) {
                JavaLinkCommon.dsprint("JavaLinkDist.connect(" + str + ", " + s + ":" + n + ")");
                try {
                    new JLCommonSocket(str, "", 0, s, n);
                    break Label_0208;
                }
                catch (Exception obj) {
                    JavaLinkCommon.dsprint("JavaLinkDist.connect failed: " + obj);
                    return false;
                }
            }
            JavaLinkCommon.dsprint("JavaLinkDist.connect(" + str2 + ":" + i + ", " + s + ":" + n + ")");
            try {
                new JLCommonSocket("", str2, i, s, n);
            }
            catch (Exception obj2) {
                JavaLinkCommon.dsprint("JavaLinkDist.connect failed: " + obj2);
                return false;
            }
        }
        if (!connectionComplete()) {
            return false;
        }
        JavaLinkCommon.dsprint("JavaLinkDist.connect done.");
        return true;
    }
    
    public static synchronized void initDist() {
        JavaLinkDist.table = new Hashtable();
        JavaLinkDist.nextIndex = 1001;
        JavaLinkDist.tableLive = 0;
        JavaLinkDist.tableFree = 0;
        JavaLinkDist.oldestFree = 0;
        JavaLinkDist.newestFree = 0;
        JavaLinkDist.connectIndex = (int)(System.currentTimeMillis() >> 13 & (long)(JavaLinkDist.topIndex - 1));
    }
    
    public static synchronized void closeDist() {
        initDist();
        JavaLinkDist.remoteServer = null;
        JavaLinkDist.localServer = null;
        JavaLinkDist.remoteReady = false;
    }
    
    public static int openDist(final Object o) {
        if (JavaLinkDist.localServer == null) {
            JavaLinkDist.localServer = new JavaLinkDist();
        }
        return openDist(o, JavaLinkDist.localServer);
    }
    
    public static int openDist(final Object obj, final JavaLinkDist javaLinkDist) {
        boolean b;
        try {
            javaLinkDist.remoteSelf = (JavaLinkDistIF)obj.getClass().getMethod("openDist", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
            b = true;
        }
        catch (NoSuchMethodException ex) {
            return -1;
        }
        catch (SecurityException ex2) {
            return -2;
        }
        catch (IllegalAccessException ex3) {
            return -3;
        }
        catch (InvocationTargetException ex4) {
            return -4;
        }
        if (b && JavaLinkDist.remoteServer == null) {
            JavaLinkDist.remoteServer = javaLinkDist.remoteSelf;
        }
        return 1;
    }
    
    public JavaLinkDist() {
        this.remoteSelf = null;
        this.nullMarker = new Integer(0);
        this.ans = new Object[3];
        this.localName = "Java";
        JavaLinkDist.localServer = this;
        initDist();
    }
    
    synchronized void registerJavaObject(final Object value, final TranStruct tranStruct) {
        int value2;
        if (JavaLinkDist.nextIndex < JavaLinkDist.topIndex && (JavaLinkDist.tableFree == 0 || 2 * JavaLinkDist.tableFree < JavaLinkDist.tableLive)) {
            value2 = JavaLinkDist.nextIndex;
            ++JavaLinkDist.nextIndex;
        }
        else {
            value2 = JavaLinkDist.oldestFree;
            JavaLinkDist.oldestFree = (int) JavaLinkDist.table.get(new Integer(JavaLinkDist.oldestFree));
            --JavaLinkDist.tableFree;
        }
        JavaLinkDist.table.put(new Integer(value2), value);
        tranStruct.nums[0] = value2;
        tranStruct.nums[1] = JavaLinkDist.connectIndex;
        ++JavaLinkDist.tableLive;
    }
    
    synchronized String extractJavaType(final TranStruct tranStruct) {
        if (tranStruct == this.ans[2]) {
            return (String)this.ans[0];
        }
        this.ans[1] = null;
        this.extractJavaItem((TranStruct)(this.ans[2] = tranStruct));
        return (String)this.ans[0];
    }
    
    synchronized Object extractJavaRef(final TranStruct tranStruct) {
        if (tranStruct == this.ans[2]) {
            return this.ans[1];
        }
        this.ans[1] = null;
        this.extractJavaItem((TranStruct)(this.ans[2] = tranStruct));
        return this.ans[1];
    }
    
    void extractJavaItem(final TranStruct tranStruct) {
        if (!liveP(tranStruct)) {
            this.ans[0] = "err";
            this.ans[1] = "Discarded Object 1";
            return;
        }
        if (remoteP(tranStruct)) {
            this.ans[0] = "remote";
            this.ans[1] = tranStruct;
            return;
        }
        if (indirectP(tranStruct)) {
            final Object value = JavaLinkDist.table.get(new Integer(tranStruct.nums[0]));
            if (value == null) {
                this.ans[0] = "err";
                this.ans[1] = "Unregistered object 2";
                return;
            }
            if (value == this.nullMarker) {
                this.ans[0] = "err";
                this.ans[1] = "Discarded object";
                return;
            }
            this.ans[0] = "indirect";
            this.ans[1] = value;
        }
        else {
            if (rankP(tranStruct, 0)) {
                this.ans[0] = "immediate";
                switch (getTypeBits(tranStruct, 5, 8)) {
                    case 10: {
                        this.ans[0] = "remote";
                        this.ans[1] = tranStruct;
                    }
                    case 1: {
                        this.ans[1] = new Byte((byte)tranStruct.nums[0]);
                        break;
                    }
                    case 2: {
                        this.ans[1] = new Short((short)tranStruct.nums[0]);
                        break;
                    }
                    case 3: {
                        this.ans[1] = new Integer(tranStruct.nums[0]);
                        break;
                    }
                    case 4: {
                        this.ans[1] = new Long(longValue(tranStruct));
                        break;
                    }
                    case 7: {
                        this.ans[1] = new Float((float)tranStruct.reals[0]);
                        break;
                    }
                    case 8: {
                        this.ans[1] = new Double(tranStruct.reals[0]);
                        break;
                    }
                    case 5: {
                        this.ans[1] = new Character(tranStruct.strings[0].charAt(0));
                        break;
                    }
                    case 6: {
                        this.ans[1] = tranStruct.strings[0];
                        break;
                    }
                    case 9: {
                        if (0 == tranStruct.nums[0]) {
                            this.ans[1] = Boolean.FALSE;
                            break;
                        }
                        this.ans[1] = Boolean.TRUE;
                        break;
                    }
                    case 31: {
                        break;
                    }
                    default: {
                        this.ans[1] = "Unknown object base type";
                        this.ans[0] = "err";
                        break;
                    }
                }
                return;
            }
            if (rankP(tranStruct, 1)) {
                this.ans[0] = "immediate";
                switch (getTypeBits(tranStruct, 5, 8)) {
                    case 3: {
                        this.ans[1] = tranStruct.nums.clone();
                        break;
                    }
                    case 6: {
                        this.ans[1] = tranStruct.strings.clone();
                        break;
                    }
                    case 8: {
                        this.ans[1] = tranStruct.reals.clone();
                        break;
                    }
                    case 1: {
                        if (0x2000 == (0xE000 & tranStruct.type)) {
                            this.ans[1] = tranStruct.getExdata();
                            break;
                        }
                        final byte[] array = new byte[tranStruct.nums.length];
                        for (int i = 0; i < array.length; ++i) {
                            array[i] = (byte)tranStruct.nums[i];
                        }
                        this.ans[1] = array;
                        break;
                    }
                    case 2: {
                        if (0x4000 == (0xE000 & tranStruct.type)) {
                            this.ans[1] = tranStruct.getExdata();
                            break;
                        }
                        final short[] array2 = new short[tranStruct.nums.length];
                        for (int j = 0; j < array2.length; ++j) {
                            array2[j] = (short)tranStruct.nums[j];
                        }
                        this.ans[1] = array2;
                        break;
                    }
                    case 7: {
                        if (0x6000 == (0xE000 & tranStruct.type)) {
                            this.ans[1] = tranStruct.getExdata();
                            break;
                        }
                        final float[] array3 = new float[tranStruct.reals.length];
                        for (int k = 0; k < array3.length; ++k) {
                            array3[k] = (float)tranStruct.reals[k];
                        }
                        this.ans[1] = array3;
                        break;
                    }
                    default: {
                        this.ans[1] = "Unknown object base type";
                        this.ans[0] = "err";
                        break;
                    }
                }
                return;
            }
            this.ans[0] = "err";
            this.ans[1] = "Unknown object type";
        }
    }
    
    public Class extractClassRef(final TranStruct tranStruct, final Object o) throws ClassNotFoundException, ClassCastException {
        if (!stringP(tranStruct)) {
            return (Class)o;
        }
        final String className = (String)o;
        if (className.equals("bool")) {
            return Boolean.TYPE;
        }
        if (className.equals("boolean")) {
            return Boolean.TYPE;
        }
        if (className.equals("byte")) {
            return Byte.TYPE;
        }
        if (className.equals("char")) {
            return Character.TYPE;
        }
        if (className.equals("double")) {
            return Double.TYPE;
        }
        if (className.equals("float")) {
            return Float.TYPE;
        }
        if (className.equals("int")) {
            return Integer.TYPE;
        }
        if (className.equals("long")) {
            return Long.TYPE;
        }
        if (className.equals("short")) {
            return Short.TYPE;
        }
        if (className.equals("void")) {
            return Void.TYPE;
        }
        return Class.forName(className);
    }
    
    public void discard(final int[] array) {
        for (int i = 0; i < array.length; ++i) {
            this.discard(array[i]);
        }
    }
    
    public void discard(final int n) {
        if (JavaLinkDist.tableFree == 0) {
            JavaLinkDist.oldestFree = n;
        }
        else {
            JavaLinkDist.table.put(new Integer(JavaLinkDist.newestFree), new Integer(n));
        }
        ++JavaLinkDist.tableFree;
        JavaLinkDist.newestFree = n;
        JavaLinkDist.table.put(new Integer(n), this.nullMarker);
        --JavaLinkDist.tableLive;
    }
    
    public void invokeOneWay(final TranStruct tranStruct, final TranStruct[] array) {
        this.invoke(-1, tranStruct, array);
    }
    
    public TranStruct[] invoke(final int n, final TranStruct tranStruct, final TranStruct[] array) {
        int n2 = 1;
        String s = "";
        Class componentType = null;
        String name = "";
        int n3 = 0;
        String str = "Arg err:";
        boolean b = false;
        Object o = null;
        String s2 = "";
        Serializable targetException = "";
        try {
            int n4 = 99;
            final String javaType = this.extractJavaType(tranStruct);
            final Object javaRef = this.extractJavaRef(tranStruct);
            Object obj = new Object();
            if (javaType.equalsIgnoreCase("err")) {
                s2 = "Op err: " + javaRef;
            }
            else if (stringP(tranStruct)) {
                final String str2 = (String)javaRef;
                if (array.length == 1 && str2.equalsIgnoreCase("forName")) {
                    n4 = 1;
                }
                else if (array.length > 1 && str2.equalsIgnoreCase("getMethod")) {
                    n4 = 2;
                }
                else if (array.length > 0 && str2.equalsIgnoreCase("getConstructor")) {
                    n4 = 3;
                }
                else if (array.length > 1 && str2.equalsIgnoreCase("newArray")) {
                    n4 = 6;
                }
                else if (array.length == 3 && str2.equalsIgnoreCase("getField")) {
                    n4 = 7;
                }
                else if (array.length == 4 && str2.equalsIgnoreCase("setField")) {
                    n4 = 8;
                }
                else if (array.length == 2 && str2.equalsIgnoreCase("getStatic")) {
                    n4 = 9;
                }
                else if (array.length == 3 && str2.equalsIgnoreCase("setStatic")) {
                    n4 = 10;
                }
                else if (array.length == 0 && str2.equalsIgnoreCase("queryFree")) {
                    n4 = 51;
                }
                else if (array.length == 0 && str2.equalsIgnoreCase("queryLive")) {
                    n4 = 52;
                }
                else {
                    s2 = "Unknown op string: " + str2;
                }
            }
            else if (javaType.equalsIgnoreCase("indirect")) {
                final String name2 = ((Constructor<Object>)javaRef).getClass().getName();
                if (array.length > 0 && name2.equals("java.lang.reflect.Method")) {
                    n4 = 4;
                }
                else if (name2.equals("java.lang.reflect.Constructor")) {
                    n4 = 5;
                }
                else {
                    s2 = "Bad op class: " + name2;
                }
            }
            else {
                s2 = "Unknown op: " + javaType;
            }
            switch (n4) {
                case 1: {
                    o = this.extractClassRef(array[0], this.extractJavaRef(array[0]));
                    break;
                }
                case 2: {
                    final Class[] parameterTypes = new Class[array.length - 2];
                    for (int i = 0; i < array.length; ++i) {
                        final TranStruct tranStruct2 = array[i];
                        final String javaType2 = this.extractJavaType(tranStruct2);
                        final Object javaRef2 = this.extractJavaRef(tranStruct2);
                        if (javaType2.equalsIgnoreCase("err")) {
                            ++n3;
                            str = str + " " + i + ": " + (String)javaRef2 + " ";
                        }
                        switch (i) {
                            case 0: {
                                componentType = this.extractClassRef(tranStruct2, javaRef2);
                                break;
                            }
                            case 1: {
                                name = (String)javaRef2;
                                break;
                            }
                            default: {
                                parameterTypes[i - 2] = this.extractClassRef(tranStruct2, javaRef2);
                                break;
                            }
                        }
                    }
                    if (n3 > 0) {
                        b = true;
                        s2 = str;
                        break;
                    }
                    o = componentType.getMethod(name, (Class[])parameterTypes);
                    break;
                }
                case 3: {
                    final Class[] parameterTypes2 = new Class[array.length - 1];
                    for (int j = 0; j < array.length; ++j) {
                        final TranStruct tranStruct3 = array[j];
                        final String javaType3 = this.extractJavaType(tranStruct3);
                        final Object javaRef3 = this.extractJavaRef(tranStruct3);
                        if (javaType3.equalsIgnoreCase("err")) {
                            ++n3;
                            str = str + " " + j + ": " + (String)javaRef3 + " ";
                        }
                        switch (j) {
                            case 0: {
                                componentType = this.extractClassRef(tranStruct3, javaRef3);
                                break;
                            }
                            default: {
                                parameterTypes2[j - 1] = this.extractClassRef(tranStruct3, javaRef3);
                                break;
                            }
                        }
                    }
                    if (n3 > 0) {
                        b = true;
                        s2 = str;
                        break;
                    }
                    o = componentType.getConstructor((Class<?>[])parameterTypes2);
                    break;
                }
                case 6: {
                    final int[] dimensions = new int[array.length - 1];
                    for (int k = 0; k < array.length; ++k) {
                        final TranStruct tranStruct4 = array[k];
                        final String javaType4 = this.extractJavaType(tranStruct4);
                        final Object javaRef4 = this.extractJavaRef(tranStruct4);
                        if (javaType4.equalsIgnoreCase("err")) {
                            ++n3;
                            str = str + " " + k + ": " + (String)javaRef4 + " ";
                        }
                        switch (k) {
                            case 0: {
                                componentType = this.extractClassRef(tranStruct4, javaRef4);
                                break;
                            }
                            default: {
                                dimensions[k - 1] = ((Number)javaRef4).intValue();
                                break;
                            }
                        }
                    }
                    if (n3 > 0) {
                        b = true;
                        s2 = str;
                        break;
                    }
                    o = Array.newInstance(componentType, dimensions);
                    break;
                }
                case 7: {
                    final Class classRef = this.extractClassRef(array[0], this.extractJavaRef(array[0]));
                    final String stringValue = stringValue(array[1]);
                    final Object javaRef5 = this.extractJavaRef(array[2]);
                    final Field field = classRef.getField(stringValue);
                    o = field.get(javaRef5);
                    n2 = 2;
                    s = field.getType().getName();
                    break;
                }
                case 8: {
                    this.extractClassRef(array[0], this.extractJavaRef(array[0])).getField(stringValue(array[1])).set(this.extractJavaRef(array[2]), this.extractJavaRef(array[3]));
                    n2 = 0;
                    o = new Integer(0);
                    break;
                }
                case 9: {
                    final Field field2 = this.extractClassRef(array[0], this.extractJavaRef(array[0])).getField(stringValue(array[1]));
                    o = field2.get(null);
                    n2 = 2;
                    s = field2.getType().getName();
                    break;
                }
                case 10: {
                    this.extractClassRef(array[0], this.extractJavaRef(array[0])).getField(stringValue(array[1])).set(null, this.extractJavaRef(array[2]));
                    n2 = 0;
                    o = new Integer(0);
                    break;
                }
                case 4: {
                    final Object[] args = new Object[array.length - 1];
                    for (int l = 0; l < array.length; ++l) {
                        final TranStruct tranStruct5 = array[l];
                        final String javaType5 = this.extractJavaType(tranStruct5);
                        final Object javaRef6 = this.extractJavaRef(tranStruct5);
                        if (javaType5.equalsIgnoreCase("err")) {
                            ++n3;
                            str = str + " " + l + ": " + (String)javaRef6 + " ";
                        }
                        if (l == 0) {
                            obj = javaRef6;
                        }
                        else {
                            args[l - 1] = javaRef6;
                        }
                    }
                    if (n3 > 0) {
                        b = true;
                        s2 = str;
                        break;
                    }
                    o = ((Method)javaRef).invoke(obj, args);
                    n2 = 2;
                    s = ((Method)javaRef).getReturnType().getName();
                    break;
                }
                case 5: {
                    final Object[] initargs = new Object[array.length];
                    for (int m = 0; m < array.length; ++m) {
                        final TranStruct tranStruct6 = array[m];
                        final String javaType6 = this.extractJavaType(tranStruct6);
                        final Object javaRef7 = this.extractJavaRef(tranStruct6);
                        if (javaType6.equalsIgnoreCase("err")) {
                            ++n3;
                            str = str + " " + m + ": " + (String)javaRef7 + " ";
                        }
                        initargs[m] = javaRef7;
                    }
                    if (n3 > 0) {
                        b = true;
                        s2 = str;
                        break;
                    }
                    o = ((Constructor<Object>)javaRef).newInstance(initargs);
                    break;
                }
                case 51: {
                    o = new Integer(JavaLinkDist.tableFree);
                    n2 = 0;
                    break;
                }
                case 52: {
                    o = new Integer(JavaLinkDist.tableLive);
                    n2 = 0;
                    break;
                }
                case 99: {
                    b = true;
                    break;
                }
                default: {
                    b = true;
                    s2 = "Bad method ref";
                    break;
                }
            }
        }
        catch (InvocationTargetException ex) {
            b = true;
            targetException = ex.getTargetException();
            s2 = targetException.toString();
        }
        catch (Throwable t) {
            b = true;
            s2 = t.toString();
            targetException = t;
        }
        TranStruct[] array2;
        if (b) {
            array2 = new TranStruct[] { newDistOb(s2), newDistOb(targetException) };
        }
        else if (n < 1) {
            array2 = new TranStruct[0];
        }
        else {
            array2 = new TranStruct[] { newDistOb(1), null };
            if (o == null) {
                array2[1] = nullDistOb();
            }
            else if (n2 == 0) {
                array2[1] = this.unwrap(o);
            }
            else if (n2 == 1) {
                array2[1] = this.wrap(o);
            }
            else if (s.equals("java.lang.String")) {
                if (n == 1) {
                    array2[1] = this.wrap(o);
                }
                else {
                    array2[1] = this.unwrap(o);
                }
            }
            else if (s.equals("boolean") || s.equals("byte") || s.equals("char") || s.equals("double") || s.equals("float") || s.equals("int") || s.equals("long") || s.equals("short")) {
                array2[1] = this.unwrap(o);
            }
            else if (n == 1) {
                array2[1] = this.wrap(o);
            }
            else {
                array2[1] = this.unwrap(o);
            }
        }
        if (lastUseP(tranStruct)) {
            this.discard(tranStruct.nums[0]);
        }
        for (int n5 = 0; n5 < array.length; ++n5) {
            if (lastUseP(array[n5])) {
                this.discard(array[n5].nums[0]);
            }
        }
        return array2;
    }
    
    TranStruct wrap(final Object o) {
        if (o.getClass().getName().equals(JavaLinkDist.structName)) {
            return (TranStruct)o;
        }
        return newDistOb(o);
    }
    
    TranStruct unwrap(final Object o) {
        final String name = o.getClass().getName();
        if (name.equals("java.lang.Boolean")) {
            return newDistOb((boolean)o);
        }
        if (name.equals("java.lang.Character")) {
            return newDistOb((char)o);
        }
        if (name.equals("java.lang.Byte")) {
            return newDistOb((byte)o);
        }
        if (name.equals("java.lang.Short")) {
            return newDistOb((short)o);
        }
        if (name.equals("java.lang.Integer")) {
            return newDistOb((int)o);
        }
        if (name.equals("java.lang.Long")) {
            return newDistOb((long)o);
        }
        if (name.equals("java.lang.Float")) {
            return newDistOb((float)o);
        }
        if (name.equals("java.lang.Double")) {
            return newDistOb((double)o);
        }
        if (name.equals("java.lang.String")) {
            return newDistOb((String)o);
        }
        if (name.equals("[Ljava.lang.String;")) {
            return newDistOb((String[])o);
        }
        if (name.equals("[B")) {
            return newDistOb((byte[])o);
        }
        if (name.equals("[S")) {
            return newDistOb((short[])o);
        }
        if (name.equals("[I")) {
            return newDistOb((int[])o);
        }
        if (name.equals("[F")) {
            return newDistOb((float[])o);
        }
        if (name.equals("[D")) {
            return newDistOb((double[])o);
        }
        return this.wrap(o);
    }
    
    public static int getBits(final int n, final int n2, final int n3) {
        return (n & (1 << n2) - 1 << n3) >> n3;
    }
    
    public static int setBits(final int n, final int n2, final int n3, final int n4) {
        final int n5 = (1 << n3) - 1 << n4;
        return ((n | n5) ^ n5) | (n5 & n2 << n4);
    }
    
    static int getTypeBits(final TranStruct tranStruct, final int n, final int n2) {
        return getBits(tranStruct.type, n, n2);
    }
    
    static TranStruct setImmediate(final TranStruct tranStruct) {
        return setRemote(tranStruct, 1, 1);
    }
    
    static TranStruct setRemote(final TranStruct tranStruct, final int n, final int n2) {
        int type = tranStruct.type;
        switch (n) {
            case 1: {
                type = setBits(type, 0, 1, 0);
                break;
            }
            case 2: {
                type = setBits(type, 1, 1, 0);
                break;
            }
        }
        switch (n2) {
            case 1: {
                type = setBits(setBits(type, 0, 1, 1), 0, 1, 2);
                break;
            }
            case 2: {
                type = setBits(type, 1, 1, 1);
                break;
            }
            case 3: {
                type = setBits(setBits(type, 0, 1, 1), 1, 1, 2);
                break;
            }
        }
        tranStruct.type = type;
        return tranStruct;
    }
    
    static TranStruct setType(final TranStruct tranStruct, final int n, final int n2, final int n3) {
        tranStruct.type = setBits(setBits(setBits(tranStruct.type, n, 5, 8), n2, 5, 3), n3, 8, 16);
        return tranStruct;
    }
    
    public static TranStruct nullDistOb() {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[0], new double[0]);
        setImmediate(immediate);
        setType(immediate, 31, 0, 0);
        if (0 == Transport.exData) {
            immediate.type |= 0xE000;
        }
        return immediate;
    }
    
    public static TranStruct newDistOb(final boolean b) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[1], new String[0], new double[0]);
        if (b) {
            immediate.nums[0] = 1;
        }
        else {
            immediate.nums[0] = 0;
        }
        setImmediate(immediate);
        setType(immediate, 9, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final byte b) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[1], new String[0], new double[0]);
        immediate.nums[0] = b;
        setImmediate(immediate);
        setType(immediate, 1, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final short n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[1], new String[0], new double[0]);
        immediate.nums[0] = n;
        setImmediate(immediate);
        setType(immediate, 2, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final int n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[1], new String[0], new double[0]);
        immediate.nums[0] = n;
        setImmediate(immediate);
        setType(immediate, 3, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(long n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[4], new String[0], new double[0]);
        final int n2 = 536870911;
        int n3 = 1;
        if (n < 0L) {
            n3 = -1;
            n = -(n + 1L);
        }
        immediate.nums[0] = (int)(n & (long)n2);
        n >>= 29;
        immediate.nums[1] = (int)(n & (long)n2);
        n >>= 29;
        immediate.nums[2] = (int)n;
        immediate.nums[3] = n3;
        setImmediate(immediate);
        setType(immediate, 4, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final char c) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[1], new double[0]);
        immediate.strings[0] = new String(new char[] { c });
        setImmediate(immediate);
        setType(immediate, 5, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final String s) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[1], new double[0]);
        immediate.strings[0] = s;
        setImmediate(immediate);
        setType(immediate, 6, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final float n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[0], new double[1]);
        immediate.reals[0] = n;
        setImmediate(immediate);
        setType(immediate, 7, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final double n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[0], new double[1]);
        immediate.reals[0] = n;
        setImmediate(immediate);
        setType(immediate, 8, 0, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final String[] array) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], array, new double[0]);
        setImmediate(immediate);
        setType(immediate, 6, 1, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final byte[] exdata) {
        if (0 == Transport.exData) {
            final int[] array = new int[exdata.length];
            for (int i = 0; i < exdata.length; ++i) {
                array[i] = exdata[i];
            }
            final TranStruct immediate = new TranStruct("Self", 0, array, new String[0], new double[0]);
            setImmediate(immediate);
            setType(immediate, 1, 1, 0);
            return immediate;
        }
        final TranStruct immediate2 = new TranStruct("Self", 0, new int[0], new String[0], new double[0]);
        immediate2.setExdata(exdata);
        setImmediate(immediate2);
        setType(immediate2, 1, 1, 0);
        immediate2.type |= 0x2000;
        return immediate2;
    }
    
    public static TranStruct newDistOb(final short[] exdata) {
        if (0 == Transport.exData) {
            final int[] array = new int[exdata.length];
            for (int i = 0; i < exdata.length; ++i) {
                array[i] = exdata[i];
            }
            final TranStruct immediate = new TranStruct("Self", 0, array, new String[0], new double[0]);
            setImmediate(immediate);
            setType(immediate, 2, 1, 0);
            return immediate;
        }
        final TranStruct immediate2 = new TranStruct("Self", 0, new int[0], new String[0], new double[0]);
        immediate2.setExdata(exdata);
        setImmediate(immediate2);
        setType(immediate2, 2, 1, 0);
        immediate2.type |= 0x4000;
        return immediate2;
    }
    
    public static TranStruct newDistOb(final int[] array) {
        final TranStruct immediate = new TranStruct("Self", 0, array, new String[0], new double[0]);
        setImmediate(immediate);
        setType(immediate, 3, 1, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final float[] exdata) {
        if (0 == Transport.exData) {
            final double[] array = new double[exdata.length];
            for (int i = 0; i < exdata.length; ++i) {
                array[i] = exdata[i];
            }
            final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[0], array);
            setImmediate(immediate);
            setType(immediate, 7, 1, 0);
            return immediate;
        }
        final TranStruct immediate2 = new TranStruct("Self", 0, new int[0], new String[0], new double[0]);
        immediate2.setExdata(exdata);
        setImmediate(immediate2);
        setType(immediate2, 7, 1, 0);
        immediate2.type |= 0x6000;
        return immediate2;
    }
    
    public static TranStruct newDistOb(final double[] array) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[0], new String[0], array);
        setImmediate(immediate);
        setType(immediate, 8, 1, 0);
        return immediate;
    }
    
    public static TranStruct newDistOb(final TranStruct tranStruct) {
        return tranStruct;
    }
    
    public static TranStruct newDistOb(final Object o) {
        return newDistOb(o, JavaLinkDist.localServer);
    }
    
    public static TranStruct newDistErr(final Object o) {
        return newDistErr(o, JavaLinkDist.localServer);
    }
    
    public static TranStruct newDistOb(final Object o, final JavaLinkDist javaLinkDist) {
        if (o == null) {
            return nullDistOb();
        }
        final String name = o.getClass().getName();
        if (name.equals(JavaLinkDist.structName)) {
            return (TranStruct)o;
        }
        try {
            return newDistOb(((LispException)o).lispErr);
        }
        catch (Exception ex) {
            try {
                final Throwable t = (Throwable)o;
                return newDistErr(o, javaLinkDist);
            }
            catch (Exception ex2) {
                int[] array;
                String[] array2;
                int n;
                int n2;
                if (name.startsWith("[")) {
                    array = new int[] { 0, 0, Array.getLength(o) };
                    array2 = new String[] { name };
                    n = 1;
                    n2 = 4;
                }
                else if (name.equals("java.lang.Class")) {
                    array = new int[] { 0, 0 };
                    array2 = new String[] { name, ((Class)o).getName() };
                    n = 0;
                    n2 = 5;
                }
                else if (name.equals("java.lang.reflect.Constructor")) {
                    array = new int[] { 0, 0 };
                    array2 = addSig(((Constructor)o).getParameterTypes(), name, ((Constructor)o).getDeclaringClass().getName(), "");
                    n = 0;
                    n2 = 6;
                }
                else if (name.equals("java.lang.reflect.Method")) {
                    array = new int[] { 0, 0 };
                    array2 = addSig(((Method)o).getParameterTypes(), name, ((Method)o).getDeclaringClass().getName(), ((Method)o).getName());
                    n = 0;
                    n2 = 7;
                }
                else {
                    array = new int[] { 0, 0 };
                    array2 = new String[] { name };
                    n = 0;
                    n2 = 1;
                }
                final TranStruct tranStruct = new TranStruct(javaLinkDist.localName, 0, array, array2, new double[0]);
                setRemote(tranStruct, 2, 1);
                setType(tranStruct, 0, n, n2);
                javaLinkDist.registerJavaObject(o, tranStruct);
                return tranStruct;
            }
        }
    }
    
    static String[] addSig(final Class[] array, final String s, final String s2, final String s3) {
        int n = 2;
        if (0 < s3.length()) {
            ++n;
        }
        final String[] array2 = new String[n + array.length];
        array2[0] = s;
        array2[1] = s2;
        if (n == 3) {
            array2[2] = s3;
        }
        for (int i = 0; i < array.length; ++i) {
            array2[n + i] = array[i].getName();
        }
        return array2;
    }
    
    public static TranStruct newDistErr(final Object o, final JavaLinkDist javaLinkDist) {
        final TranStruct tranStruct = new TranStruct(javaLinkDist.localName, 0, new int[2], new String[2], new double[0]);
        tranStruct.strings[0] = o.getClass().getName();
        tranStruct.strings[1] = o.toString();
        setRemote(tranStruct, 2, 1);
        setType(tranStruct, 0, 0, 61);
        javaLinkDist.registerJavaObject(o, tranStruct);
        return tranStruct;
    }
    
    public static TranStruct newDistSym(final String s, final String s2, final int n) {
        final TranStruct immediate = new TranStruct("Self", 0, new int[1], new String[2], new double[0]);
        immediate.strings[0] = s;
        immediate.strings[1] = s2;
        immediate.nums[0] = n;
        setImmediate(immediate);
        setType(immediate, 10, 0, 0);
        return immediate;
    }
    
    static boolean liveP(final TranStruct tranStruct) {
        synchronized (tranStruct) {
            if (immediateP(tranStruct)) {
                return true;
            }
            if (localP(tranStruct) && JavaLinkDist.connectIndex != tranStruct.nums[1]) {
                setRemote(tranStruct, 0, 3);
                return false;
            }
            return 0 == getTypeBits(tranStruct, 1, 2);
        }
    }
    
    static boolean immediateP(final TranStruct tranStruct) {
        return 0 == getTypeBits(tranStruct, 1, 0);
    }
    
    static boolean indirectP(final TranStruct tranStruct) {
        return 1 == getTypeBits(tranStruct, 1, 0);
    }
    
    static boolean localP(final TranStruct tranStruct) {
        return tranStruct.home.equalsIgnoreCase(defaultLocal().localName);
    }
    
    static boolean remoteP(final TranStruct tranStruct) {
        return !tranStruct.home.equalsIgnoreCase("Self") && !tranStruct.home.equalsIgnoreCase(defaultLocal().localName);
    }
    
    static boolean lastUseP(final TranStruct tranStruct) {
        return 1 == getTypeBits(tranStruct, 1, 1);
    }
    
    public static TranStruct lastUse(final TranStruct tranStruct) {
        return setRemote(tranStruct, 0, 2);
    }
    
    static boolean aggrP(final TranStruct tranStruct, final int n) {
        return n == getTypeBits(tranStruct, 8, 16);
    }
    
    static boolean rankP(final TranStruct tranStruct, final int n) {
        final int typeBits = getTypeBits(tranStruct, 5, 3);
        if (typeBits == 31) {
            return n == tranStruct.nums[2];
        }
        return n == typeBits;
    }
    
    static boolean baseP(final TranStruct tranStruct, final int n) {
        return n == getTypeBits(tranStruct, 5, 8);
    }
    
    public static boolean nullP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 31);
    }
    
    public static boolean booleanP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 9);
    }
    
    public static boolean integerP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && (baseP(tranStruct, 1) || baseP(tranStruct, 2) || baseP(tranStruct, 3));
    }
    
    public static boolean byteP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 1);
    }
    
    public static boolean shortP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 2);
    }
    
    public static boolean intP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 3);
    }
    
    public static boolean longP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 4);
    }
    
    public static boolean charP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 5);
    }
    
    public static boolean realP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && (baseP(tranStruct, 7) || baseP(tranStruct, 8));
    }
    
    public static boolean singleP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 7);
    }
    
    public static boolean doubleP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 8);
    }
    
    public static boolean stringP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 6);
    }
    
    public static boolean symbolP(final TranStruct tranStruct) {
        return immediateP(tranStruct) && rankP(tranStruct, 0) && baseP(tranStruct, 10);
    }
    
    public static boolean errorP(final TranStruct tranStruct) {
        return indirectP(tranStruct) && liveP(tranStruct) && remoteP(tranStruct) && rankP(tranStruct, 0) && aggrP(tranStruct, 61);
    }
    
    public static boolean pointerP(final TranStruct tranStruct) {
        return indirectP(tranStruct) && liveP(tranStruct);
    }
    
    public static boolean boolValue(final TranStruct tranStruct) {
        return 0 != intValue(tranStruct);
    }
    
    public static int intValue(final TranStruct tranStruct) {
        return intValue(tranStruct, 0);
    }
    
    public static int intValue(final TranStruct tranStruct, final int n) {
        if (n < tranStruct.nums.length) {
            return tranStruct.nums[n];
        }
        return 0;
    }
    
    public static long longValue(final TranStruct tranStruct) {
        final long n = intValue(tranStruct, 0);
        final long n2 = intValue(tranStruct, 1);
        final long n3 = intValue(tranStruct, 2);
        long n4 = intValue(tranStruct, 3);
        if (n4 == 0L) {
            n4 = 1L;
        }
        return n4 * (n + (n2 + (n3 << 29) << 29));
    }
    
    public static double doubleValue(final TranStruct tranStruct) {
        return doubleValue(tranStruct, 0);
    }
    
    public static double doubleValue(final TranStruct tranStruct, final int n) {
        if (n < tranStruct.reals.length) {
            return tranStruct.reals[0];
        }
        return 0.0;
    }
    
    public static char charValue(final TranStruct tranStruct) {
        return charValue(tranStruct, 0, 0);
    }
    
    public static char charValue(final TranStruct tranStruct, final int n) {
        return charValue(tranStruct, n, 0);
    }
    
    public static char charValue(final TranStruct tranStruct, final int index, final int n) {
        if (n < tranStruct.strings.length && index < tranStruct.strings[n].length()) {
            return tranStruct.strings[n].charAt(index);
        }
        return '\0';
    }
    
    public static String stringValue(final TranStruct tranStruct) {
        return stringValue(tranStruct, 0);
    }
    
    public static String stringValue(final TranStruct tranStruct, final int n) {
        if (n < tranStruct.strings.length) {
            return tranStruct.strings[n];
        }
        return "";
    }
    
    public static String symbolName(final TranStruct tranStruct) {
        return stringValue(tranStruct, 0);
    }
    
    public static String symbolPackage(final TranStruct tranStruct) {
        return stringValue(tranStruct, 1);
    }
    
    public static int symbolCaseModes(final TranStruct tranStruct) {
        return intValue(tranStruct, 0);
    }
    
    public static Object pointerValue(final TranStruct tranStruct) {
        return pointerValue(tranStruct, JavaLinkDist.localServer);
    }
    
    public static Object pointerValue(final TranStruct tranStruct, final JavaLinkDist javaLinkDist) {
        return javaLinkDist.extractJavaRef(tranStruct);
    }
    
    public static JavaLinkDist defaultLocal() {
        return JavaLinkDist.localServer;
    }
    
    public static JavaLinkDistIF defaultRemote() {
        return JavaLinkDist.remoteServer;
    }
    
    public static boolean defaultRemoteP() {
        return null != JavaLinkDist.remoteServer && JavaLinkDist.remoteReady;
    }
    
    public static boolean defaultRemoteP(final boolean b) {
        return b && null != JavaLinkDist.remoteServer && (JavaLinkDist.remoteReady = true);
    }
    
    public static JavaLinkDistIF defaultRemote(final JavaLinkDist javaLinkDist) {
        if (javaLinkDist.remoteSelf != null) {
            return javaLinkDist.remoteSelf;
        }
        return JavaLinkDist.remoteServer;
    }
    
    public static void discardInLisp(final TranStruct tranStruct) {
        if (liveP(tranStruct) && remoteP(tranStruct)) {
            defaultRemote().discard(new int[] { tranStruct.nums[0] });
            setRemote(tranStruct, 0, 3);
        }
    }
    
    public static TranStruct[] applyInLisp(final int n, final TranStruct tranStruct, final TranStruct tranStruct2) {
        return invokeInLisp(n, newDistOb("cl:apply"), new TranStruct[] { tranStruct, tranStruct2 });
    }
    
    public static TranStruct[] applyInLispEx(final int n, final TranStruct tranStruct, final TranStruct tranStruct2) throws JLinkerException {
        return invokeInLispEx(n, newDistOb("cl:apply"), new TranStruct[] { tranStruct, tranStruct2 });
    }
    
    public static TranStruct[] invokeInLisp(final int n, final String s, final int n2) {
        return invokeInLisp(n, newDistOb(s), new TranStruct[] { newDistOb(n2) });
    }
    
    public static TranStruct[] invokeInLispEx(final int n, final String s, final int n2) throws JLinkerException {
        return invokeInLispEx(n, newDistOb(s), new TranStruct[] { newDistOb(n2) });
    }
    
    public static TranStruct[] invokeInLisp(final int n, final String s, final String s2) {
        return invokeInLisp(n, newDistOb(s), new TranStruct[] { newDistOb(s2) });
    }
    
    public static TranStruct[] invokeInLispEx(final int n, final String s, final String s2) throws JLinkerException {
        return invokeInLispEx(n, newDistOb(s), new TranStruct[] { newDistOb(s2) });
    }
    
    public static TranStruct[] invokeInLisp(final int n, final String s) {
        return invokeInLisp(n, newDistOb(s), new TranStruct[0]);
    }
    
    public static TranStruct[] invokeInLispEx(final int n, final String s) throws JLinkerException {
        return invokeInLispEx(n, newDistOb(s), new TranStruct[0]);
    }
    
    public static TranStruct[] invokeInLisp(final int n, final TranStruct tranStruct, final TranStruct[] array) {
        final TranStruct[] invoke = defaultRemote().invoke(n, tranStruct, array);
        for (int i = 0; i < array.length; ++i) {
            if (lastUseP(array[i])) {
                setRemote(array[i], 0, 3);
            }
        }
        return invoke;
    }
    
    public static TranStruct[] invokeInLispEx(final int n, final TranStruct tranStruct, final TranStruct[] array) throws JLinkerException {
        final TranStruct[] invokeInLisp = invokeInLisp(n, tranStruct, array);
        if (0 == invokeInLisp.length) {
            if (n == -1) {
                return invokeInLisp;
            }
            if (n == 0) {
                return invokeInLisp;
            }
            throw new InvokeException("Nothing returned from Lisp");
        }
        else {
            if (integerP(invokeInLisp[0]) && intValue(invokeInLisp[0]) == invokeInLisp.length - 1) {
                return invokeInLisp;
            }
            if (1 == invokeInLisp.length && errorP(invokeInLisp[0])) {
                throw new LispException(stringValue(invokeInLisp[0], 1), invokeInLisp[0]);
            }
            if (1 == invokeInLisp.length && stringP(invokeInLisp[0]) && stringValue(invokeInLisp[0]).equalsIgnoreCase("Throw in Lisp")) {
                throw new LispThrow();
            }
            if (1 == invokeInLisp.length) {
                throw new InvokeException("Unexpected value: " + invokeInLisp[0].toString());
            }
            throw new InvokeException("Unexpected result: " + invokeInLisp.toString());
        }
    }
    
    static {
        JavaLinkDist.remoteServer = null;
        JavaLinkDist.localServer = null;
        JavaLinkDist.remoteReady = false;
        JavaLinkDist.connectIndex = -1;
        JavaLinkDist.topIndex = 536870912;
        structClass = TranStruct.CLASS;
        structName = JavaLinkDist.structClass.getName();
    }
    
    public static class JLinkerException extends Exception
    {
        JLinkerException(final String message) {
            super(message);
        }
    }
    
    public static class InvokeException extends JLinkerException
    {
        InvokeException(final String s) {
            super(s);
        }
    }
    
    public static class LispException extends JLinkerException
    {
        Object lispErr;
        
        LispException(final String s, final Object lispErr) {
            super(s);
            this.lispErr = lispErr;
        }
    }
    
    public static class LispThrow extends JLinkerException
    {
        LispThrow() {
            super("Throw in Lisp");
        }
    }
}
