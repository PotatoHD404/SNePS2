// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.lang.reflect.Field;

public class LispCallJNI extends LispCallImplementation
{
    static Class opclass;
    LispCall lci;
    String argtypes;
    int rescount;
    public String restypes;
    public Object res0;
    public Object res1;
    public Object res2;
    public Object res3;
    public Object res4;
    public int int0;
    public float float0;
    public double double0;
    static Object[] eventQueue;
    static int eventCount;
    static Thread lispThread;
    static int callFlag;
    
    Class getOpClass() {
        return LispCallJNI.opclass;
    }
    
    public LispCallJNI() {
        this.argtypes = "";
        this.rescount = -1;
        this.restypes = null;
        this.res0 = null;
        this.res1 = null;
        this.res2 = null;
        this.res3 = null;
        this.res4 = null;
        this.int0 = 0;
        this.float0 = 0.0f;
        this.double0 = 0.0;
    }
    
    LispCallImplementation builder(final LispCall lci, final int n, final boolean b, final String lispOp) {
        final LispCallJNI lispCallJNI = new LispCallJNI();
        lispCallJNI.lci = lci;
        if (!lispOp.equals("")) {
            lci.lispOp = lispOp;
        }
        return lispCallJNI;
    }
    
    void setOp(final String op) {
        this.setOp((Object)op);
    }
    
    void setOp(final Object lispOp) {
        this.lci.lispOp = lispOp;
        if (this.lci.state == 0) {
            this.lci.state = 1;
        }
    }
    
    synchronized int addArgWrapped(final String s, final Object o) {
        if (this.lci.state != 1) {
            throw new IllegalArgumentException("Wrong state");
        }
        final int holder = this.lci.newHolder(o);
        if (holder == 1) {
            this.argtypes = s;
        }
        else {
            this.argtypes += s;
        }
        return holder;
    }
    
    synchronized int addArg(final int value) {
        return this.addArgWrapped("I", new Integer(value));
    }
    
    synchronized int addArg(final short value) {
        return this.addArgWrapped("S", new Integer(value));
    }
    
    synchronized int addArg(final byte value) {
        return this.addArgWrapped("B", new Integer(value));
    }
    
    synchronized int addArg(final long value) {
        return this.addArgWrapped("J", new Long(value));
    }
    
    synchronized int addArg(final boolean b) {
        boolean value = false;
        if (b) {
            value = true;
        }
        return this.addArgWrapped("Z", new Integer((int)(value ? 1 : 0)));
    }
    
    synchronized int addArg(final int[] array) {
        return this.addArgWrapped("N", array);
    }
    
    synchronized int addArg(final short[] array) {
        return this.addArgWrapped("M", array);
    }
    
    synchronized int addArg(final byte[] array) {
        return this.addArgWrapped("K", array);
    }
    
    synchronized int addArg(final String s) {
        return this.addArgWrapped("T", s);
    }
    
    synchronized int addArg(final String[] array) {
        return this.addArgWrapped("U", array);
    }
    
    synchronized int addArg(final double value) {
        return this.addArgWrapped("D", new Double(value));
    }
    
    synchronized int addArg(final float value) {
        return this.addArgWrapped("F", new Float(value));
    }
    
    synchronized int addArg(final double[] array) {
        return this.addArgWrapped("E", array);
    }
    
    synchronized int addArg(final float[] array) {
        return this.addArgWrapped("G", array);
    }
    
    synchronized int addArg(final Object o) {
        return this.addArgWrapped("X", o);
    }
    
    synchronized int addSymbol(final String s) {
        return this.addArgWrapped("Y", new JNIWrapper(s, "", 0));
    }
    
    synchronized int addSymbol(final String s, final String s2) {
        return this.addArgWrapped("Y", new JNIWrapper(s, s2, 0));
    }
    
    synchronized int addSymbol(final String s, final String s2, final int n) {
        return this.addArgWrapped("Y", new JNIWrapper(s, s2, n));
    }
    
    synchronized void assemble(final String s) {
        if (this.lci.state != 1) {
            this.lci.throwWrongState(s, "");
        }
        final Object[] args = new Object[this.lci.count];
        this.lci.args = args;
        for (int i = 0; i < this.lci.count; ++i) {
            args[this.lci.count - i - 1] = this.lci.chain.arg;
            this.lci.chain = this.lci.chain.next;
        }
        this.lci.count = 0;
        this.lci.state = 2;
    }
    
    native int callInLisp(final int p0, final Object p1, final int p2, final String p3, final Object[] p4);
    
    native int nthIntValue(final int p0, final JNIWrapper p1);
    
    native Object nthObjectValue(final int p0, final JNIWrapper p1);
    
    static synchronized int callLispWithEvent(final String s, final int value, final String[] array, final int[] array2) {
        if (LispCallJNI.callFlag == 2) {
            return callLispHandler(s, value, array, array2);
        }
        LispCallJNI.eventQueue = new Object[] { s, new Integer(value), array, array2, LispCallJNI.eventQueue };
        ++LispCallJNI.eventCount;
        return 0;
    }
    
    public static synchronized int pollEventQueue(final int n) {
        if (LispCallJNI.eventQueue == null) {
            return 0;
        }
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            final Object[] eventQueue = LispCallJNI.eventQueue;
            LispCallJNI.eventQueue = (Object[])LispCallJNI.eventQueue[4];
            --LispCallJNI.eventCount;
            --n2;
            callLispHandler((String)eventQueue[0], (int)eventQueue[1], (String[])eventQueue[2], (int[])eventQueue[3]);
            if (LispCallJNI.eventQueue == null) {
                return n2;
            }
        }
        return LispCallJNI.eventCount;
    }
    
    static native int callLispHandler(final String p0, final int p1, final String[] p2, final int[] p3);
    
    int setLispError(final String res1) {
        this.rescount = -1;
        this.res0 = null;
        this.restypes = "R";
        this.res1 = res1;
        return 0;
    }
    
    synchronized int call() throws JavaLinkDist.JLinkerException {
        if (this.lci.state == 1) {
            this.assemble("call");
        }
        if (this.lci.state != 2) {
            this.lci.throwWrongState("call", "");
        }
        this.rescount = this.callInLisp(this.lci.callStyle, this.lci.lispOp, this.lci.lispOpKind, this.argtypes, (Object[])this.lci.args);
        if (this.rescount < 0) {
            throw new JavaLinkDist.JLinkerException("Lisp error " + this.res1.toString());
        }
        if (!this.lci.retain) {
            this.lci.args = null;
        }
        if (this.lci.callStyle >= 3) {
            this.lci.waitref = this.res0;
            this.lci.state = 4;
            return 0;
        }
        this.lci.waitref = null;
        this.lci.state = 3;
        this.lci.res = this.lci;
        return this.rescount;
    }
    
    synchronized Object getValue(final int n) {
        Object o = null;
        if (n < this.rescount) {
            final String substring = this.restypes.substring(n, n + 1);
            if (substring.equals("V")) {
                this.lci.throwWrongState("getValue", " when value already retrieved once");
            }
            else if (substring.equals("W")) {
                o = null;
            }
            else if (substring.equals("Z")) {
                o = new Boolean(this.int0 == 1);
            }
            else if (substring.equals("C")) {
                o = new Character((char)this.int0);
            }
            else if (substring.equals("B")) {
                o = new Byte((byte)this.int0);
            }
            else if (substring.equals("S")) {
                o = new Short((short)this.int0);
            }
            else if (substring.equals("I")) {
                o = new Integer(this.int0);
            }
            else if (substring.equals("F")) {
                o = new Float(this.float0);
            }
            else if (substring.equals("D")) {
                o = new Double(this.double0);
            }
            else if (n == 0) {
                o = this.res0;
            }
            else if (n == 1) {
                o = this.res1;
            }
            else if (n == 2) {
                o = this.res2;
            }
            else if (n == 3) {
                o = this.res3;
            }
            else if (this.rescount == 5) {
                o = this.res4;
            }
            else {
                o = this.nthObjectValue(n - 4, (JNIWrapper)this.res4);
            }
            if (!this.lci.retain) {
                switch (n) {
                    case 0: {
                        this.res0 = null;
                    }
                    case 1: {
                        this.res1 = null;
                    }
                    case 2: {
                        this.res2 = null;
                    }
                    case 3: {
                        this.res3 = null;
                    }
                    case 4: {
                        if (this.rescount == 5) {
                            this.res4 = null;
                            break;
                        }
                        break;
                    }
                }
                this.restypes = this.restypes.substring(0, n) + "V" + this.restypes.substring(n + 1);
            }
        }
        else {
            this.lci.throwWrongState("getValue", " when index is out of range.");
        }
        return o;
    }
    
    int typeOf(final Object o) {
        return this.translateType(((JNIWrapper)o).type);
    }
    
    int translateType(final String s) {
        return JNIWrapper.translateType(s);
    }
    
    String getType(final int beginIndex) {
        if (beginIndex < this.restypes.length()) {
            String s = this.restypes.substring(beginIndex, beginIndex + 1);
            try {
                if (s.equals("A")) {
                    s = ((JNIWrapper)this.getValue(beginIndex)).type;
                }
            }
            catch (ClassCastException ex) {
                throw new ClassCastException("Casting " + this.getValue(beginIndex).getClass().getName() + " to JNIWrapper");
            }
            return s;
        }
        return "H";
    }
    
    synchronized int typeOf(final int n) {
        return this.translateType(this.getType(n));
    }
    
    synchronized int intValue(final int n) {
        final String type = this.getType(n);
        if (n == 0) {
            if (type.equals("I") || type.equals("S") || type.equals("B")) {
                return this.int0;
            }
        }
        else {
            if (type.equals("I")) {
                return (int)this.getValue(n);
            }
            if (type.equals("S")) {
                return (short)this.getValue(n);
            }
            if (type.equals("B")) {
                return (byte)this.getValue(n);
            }
        }
        throw new UnsupportedOperationException("intValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized long longValue(final int n) {
        final String type = this.getType(n);
        if (n == 0) {
            if (type.equals("I") || type.equals("S") || type.equals("B")) {
                return this.int0;
            }
            if (type.equals("J")) {
                return (long)this.res0;
            }
        }
        else {
            if (type.equals("J")) {
                return (long)this.getValue(n);
            }
            if (type.equals("I")) {
                return (int)this.getValue(n);
            }
            if (type.equals("S")) {
                return (short)this.getValue(n);
            }
            if (type.equals("B")) {
                return (byte)this.getValue(n);
            }
        }
        throw new UnsupportedOperationException("longValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized double doubleValue(final int n) {
        final String type = this.getType(n);
        if (n == 0) {
            if (type.equals("D")) {
                return this.double0;
            }
            if (type.equals("F")) {
                return this.float0;
            }
        }
        else {
            if (type.equals("D")) {
                return (double)this.getValue(n);
            }
            if (type.equals("F")) {
                return (float)this.getValue(n);
            }
        }
        throw new UnsupportedOperationException("doubleValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized float floatValue(final int n) {
        final String type = this.getType(n);
        if (n == 0) {
            if (type.equals("D")) {
                return (float)this.double0;
            }
            if (type.equals("F")) {
                return this.float0;
            }
        }
        else {
            if (type.equals("D")) {
                return (float)(double)this.getValue(n);
            }
            if (type.equals("F")) {
                return (float)this.getValue(n);
            }
        }
        throw new UnsupportedOperationException("floatValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized boolean booleanValue(final int n) {
        final String type = this.getType(n);
        if (n == 0) {
            if (type.equals("Z")) {
                return 1 == this.int0;
            }
        }
        else if (type.equals("Z")) {
            return (boolean)this.getValue(n);
        }
        throw new UnsupportedOperationException("booleanValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized String stringValue(final int n) {
        final String type = this.getType(n);
        if (type.equals("T")) {
            return (String)this.getValue(n);
        }
        throw new UnsupportedOperationException("stringValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized int[] intArrayValue(final int n) {
        final String type = this.getType(n);
        if (type.equals("N")) {
            return (int[])this.getValue(n);
        }
        throw new UnsupportedOperationException("intArrayValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized String[] stringArrayValue(final int n) {
        final String type = this.getType(n);
        if (type.equals("U")) {
            return (String[])this.getValue(n);
        }
        throw new UnsupportedOperationException("stringArrayValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized double[] doubleArrayValue(final int n) {
        final String type = this.getType(n);
        if (type.equals("E")) {
            return (double[])this.getValue(n);
        }
        throw new UnsupportedOperationException("doubleArrayValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized float[] floatArrayValue(final int n) {
        final String type = this.getType(n);
        if (type.equals("G")) {
            return (float[])this.getValue(n);
        }
        throw new UnsupportedOperationException("floatArrayValue of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized Object objectValue(final int n) {
        return this.getValue(n);
    }
    
    synchronized String symbolName(final int n) {
        final String type = this.getType(n);
        if (type.equals("Y")) {
            return (String)((JNIWrapper)this.getValue(n)).data;
        }
        throw new UnsupportedOperationException("symbolName of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized String symbolPackage(final int n) {
        final String type = this.getType(n);
        if (type.equals("Y")) {
            return ((JNIWrapper)this.getValue(n)).packageName;
        }
        throw new UnsupportedOperationException("symbolPackage of " + LispCall.nameOfType(this.translateType(type)));
    }
    
    synchronized String lispType(final int n) {
        final String type = this.getType(n);
        if (type.equals("P")) {
            return ((JNIWrapper)this.getValue(n)).getLispType();
        }
        if (type.equals("Y")) {
            return ((JNIWrapper)this.getValue(n)).getLispType();
        }
        return LispCall.nameOfType(this.typeOf(n));
    }
    
    synchronized int query(final boolean b, final boolean b2) throws JavaLinkDist.JLinkerException {
        switch (this.lci.state) {
            case 0:
            case 1:
            case 2:
            case 6: {
                return -100;
            }
            case 3: {
                if (this.rescount == -1) {
                    return -99;
                }
                return this.rescount;
            }
            case 5: {
                break;
            }
            case 4: {
                break;
            }
            default: {
                return -100;
            }
        }
        if (this.lci.waitref == null) {
            return -100;
        }
        if (b && this.lci.state == 4) {
            final int nthIntValue = this.nthIntValue(0, (JNIWrapper)this.lci.waitref);
            if (nthIntValue >= 0) {
                this.lci.waitres = nthIntValue;
                this.lci.state = 5;
            }
            else if (nthIntValue == -98) {
                this.lci.waitres = nthIntValue;
                this.lci.state = 5;
            }
            else if (nthIntValue == -99) {
                this.lci.state = 3;
                this.lci.res = this.lci;
                return -99;
            }
        }
        if (b2 && this.lci.state == 5) {
            this.rescount = this.callInLisp(2, "javatools.jlinker::jl-async-results", 0, "P", new Object[] { this.lci.waitref });
            this.lci.state = 3;
            this.lci.res = this.lci;
            if (this.rescount == -1) {
                return 0;
            }
            return this.rescount;
        }
        else {
            if (this.lci.state != 5) {
                return -10;
            }
            if (b) {
                return this.lci.waitres;
            }
            return -11;
        }
    }
    
    synchronized void close() {
    }
    
    synchronized void reset() {
        this.rescount = -1;
    }
    
    synchronized void setArgWrapped(final int endIndex, final String str, final Object o) {
        switch (this.lci.state) {
            case 1: {
                this.assemble("setArg");
                break;
            }
            case 2: {
                break;
            }
            default: {
                this.lci.throwWrongState("setArg", "");
                break;
            }
        }
        this.argtypes = this.argtypes.substring(0, endIndex) + str + this.argtypes.substring(endIndex + 1);
        ((Object[])this.lci.args)[endIndex] = o;
    }
    
    synchronized void setArg(final int n, final boolean b) {
        boolean value = false;
        if (b) {
            value = true;
        }
        this.setArgWrapped(n, "Z", new Integer((int)(value ? 1 : 0)));
    }
    
    synchronized void setArg(final int n, final int value) {
        this.setArgWrapped(n, "I", new Integer(value));
    }
    
    synchronized void setArg(final int n, final long value) {
        this.setArgWrapped(n, "J", new Long(value));
    }
    
    synchronized void setArg(final int n, final double value) {
        this.setArgWrapped(n, "D", new Double(value));
    }
    
    synchronized void setArg(final int n, final String s) {
        this.setArgWrapped(n, "T", s);
    }
    
    synchronized void setArg(final int n, final Object o) {
        this.setArgWrapped(n, "P", o);
    }
    
    synchronized void setArg(final int n, final int[] array) {
        this.setArgWrapped(n, "N", array);
    }
    
    synchronized void setArg(final int n, final double[] array) {
        this.setArgWrapped(n, "E", array);
    }
    
    synchronized void setArg(final int n, final String[] array) {
        this.setArgWrapped(n, "U", array);
    }
    
    synchronized void setSymbol(final int n, final String s) {
        this.setArgWrapped(n, "Y", new JNIWrapper(s, "", 0));
    }
    
    synchronized void setSymbol(final int n, final String s, final String s2) {
        this.setArgWrapped(n, "Y", new JNIWrapper(s, s2, 0));
    }
    
    synchronized void setSymbol(final int n, final String s, final String s2, final int n2) {
        this.setArgWrapped(n, "Y", new JNIWrapper(s, s2, n2));
    }
    
    int mayCall() {
        switch (LispCallJNI.callFlag) {
            case 0: {
                return 0;
            }
            case 2: {
                return 2;
            }
            default: {
                if (LispCallJNI.lispThread.equals(Thread.currentThread())) {
                    return 1;
                }
                return -1;
            }
        }
    }
    
    public static void setLispThread(final int n) {
        if (n == 2) {
            LispCallJNI.callFlag = 2;
        }
        else {
            LispCallJNI.lispThread = Thread.currentThread();
            LispCallJNI.callFlag = 1;
        }
    }
    
    public static byte[] getLongFieldBytes(final Field field, final Object obj) throws IllegalArgumentException, IllegalAccessException {
        final byte[] array = new byte[9];
        long long1 = field.getLong(obj);
        array[0] = 1;
        if (long1 < 0L) {
            array[0] = -1;
            long1 = -(long1 + 1L);
        }
        for (int i = 8; i > 0; --i) {
            array[i] = (byte)(long1 & 0xFFL);
            long1 >>= 8;
        }
        return array;
    }
    
    public static byte[] getLongValueBytes(final Long n) throws IllegalArgumentException, IllegalAccessException {
        final byte[] array = new byte[9];
        long longValue = n;
        array[0] = 1;
        if (longValue < 0L) {
            array[0] = -1;
            longValue = -(longValue + 1L);
        }
        for (int i = 8; i > 0; --i) {
            array[i] = (byte)(longValue & 0xFFL);
            longValue >>= 8;
        }
        return array;
    }
    
    public static void setLongFieldBytes(final Field field, final Object obj, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9) throws IllegalArgumentException, IllegalAccessException {
        long l = (long)(n2 & 0xFF) | (long)(n3 & 0xFF) << 8 | (long)(n4 & 0xFF) << 16 | (long)(n5 & 0xFF) << 24 | (long)(n6 & 0xFF) << 32 | (long)(n7 & 0xFF) << 40 | (long)(n8 & 0xFF) << 48 | (long)(n9 & 0xFF) << 56;
        if (n < 0) {
            l = -l - 1L;
        }
        field.setLong(obj, l);
    }
    
    static {
        LispCallJNI.opclass = null;
        try {
            LispCallJNI.opclass = Class.forName("java.lang.Object");
        }
        catch (ClassNotFoundException ex) {}
        LispCallJNI.eventQueue = null;
        LispCallJNI.eventCount = 0;
        LispCallJNI.callFlag = 0;
    }
}
