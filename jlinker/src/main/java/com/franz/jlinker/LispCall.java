// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class LispCall
{
    public static final int STYLE_ONEWAY = -1;
    public static final int STYLE_IGNORE = 0;
    public static final int STYLE_REF = 1;
    public static final int STYLE_COPY = 2;
    public static final int STYLE_ASYNC_BR = 3;
    static final int STYLE_ASYNC = 3;
    public static final int STYLE_ASYNC_GO = 4;
    static final int STYLE_TOP = 4;
    public static final int STATE_NEW = 0;
    public static final int STATE_COLLECTING = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_DONE = 3;
    public static final int STATE_WAITING = 4;
    public static final int STATE_WAITDONE = 5;
    public static final int STATE_CLOSED = 6;
    public static final int RES_NULL = 0;
    public static final int RES_BOOLEAN = 1;
    public static final int RES_INTEGER = 2;
    public static final int RES_LONG = 3;
    public static final int RES_REAL = 4;
    public static final int RES_STRING = 5;
    public static final int RES_SYMBOL = 11;
    public static final int RES_LISP_POINTER = 12;
    public static final int RES_ERROR = 13;
    public static final int RES_INT_ARRAY = 21;
    public static final int RES_DOUBLE_ARRAY = 22;
    public static final int RES_STRING_ARRAY = 23;
    public static final int RES_JAVA_POINTER = 24;
    public static final int RES_WRONG_STATE = 91;
    public static final int RES_MISSING = 92;
    public static final int RES_WRAPPER = 90;
    public static final int RES_UNKNOWN = 99;
    int state;
    boolean retain;
    int callStyle;
    Object lispOp;
    int lispOpKind;
    Object args;
    Object res;
    Holder chain;
    int count;
    Object waitref;
    int waitres;
    public LispCallImplementation implementation;
    public static LispCallImplementation prototype;
    
    int newHolder(final Object o) {
        this.chain = new Holder(o, this.chain);
        return ++this.count;
    }
    
    public LispCall() {
        this.retain = true;
        this.callStyle = 2;
        this.lispOp = null;
        this.lispOpKind = 0;
        this.args = null;
        this.res = null;
        this.chain = null;
        this.count = 0;
        this.waitref = null;
        this.waitres = -99;
        this.implementation = LispCall.prototype.builder(this, 2, true, "");
        this.state = 0;
    }
    
    public LispCall(final boolean retain) {
        this.retain = true;
        this.callStyle = 2;
        this.lispOp = null;
        this.lispOpKind = 0;
        this.args = null;
        this.res = null;
        this.chain = null;
        this.count = 0;
        this.waitref = null;
        this.waitres = -99;
        this.implementation = LispCall.prototype.builder(this, 2, retain, "");
        this.retain = retain;
        this.state = 0;
    }
    
    public LispCall(final int style) {
        this.retain = true;
        this.callStyle = 2;
        this.lispOp = null;
        this.lispOpKind = 0;
        this.args = null;
        this.res = null;
        this.chain = null;
        this.count = 0;
        this.waitref = null;
        this.waitres = -99;
        this.implementation = LispCall.prototype.builder(this, style, true, "");
        this.setStyle(style);
        this.state = 0;
    }
    
    public LispCall(final String s) {
        this.retain = true;
        this.callStyle = 2;
        this.lispOp = null;
        this.lispOpKind = 0;
        this.args = null;
        this.res = null;
        this.chain = null;
        this.count = 0;
        this.waitref = null;
        this.waitres = -99;
        this.implementation = LispCall.prototype.builder(this, 2, true, s);
        this.state = 1;
    }
    
    public int getState() {
        return this.state;
    }
    
    public Object getOp() {
        return this.lispOp;
    }
    
    public int getStyle() {
        return this.callStyle;
    }
    
    public void setOp(final String op) {
        this.lispOpKind = 0;
        this.implementation.setOp(op);
    }
    
    public void setOp(final Object op) {
        this.castObject(op);
        this.lispOpKind = 1;
        this.implementation.setOp(op);
    }
    
    void castObject(final Object o) {
        if (!this.implementation.getOpClass().isInstance(o)) {
            throw new ClassCastException("Cannot cast object of class " + o.getClass().getName() + " to " + this.implementation.getOpClass().getName());
        }
    }
    
    public void setStyle(final int n) {
        if (-1 <= n && n <= 4) {
            this.callStyle = n;
            return;
        }
        throw new IllegalArgumentException("Unknown call style " + n + ".");
    }
    
    public synchronized boolean getRetain() {
        return this.retain;
    }
    
    public synchronized void setRetain(final boolean retain) {
        this.retain = retain;
    }
    
    public synchronized int addArg(final byte b) {
        return this.implementation.addArg(b);
    }
    
    public synchronized int addArg(final short n) {
        return this.implementation.addArg(n);
    }
    
    public synchronized int addArg(final int n) {
        return this.implementation.addArg(n);
    }
    
    public synchronized int addArg(final long n) {
        return this.implementation.addArg(n);
    }
    
    public synchronized int addArg(final boolean b) {
        return this.implementation.addArg(b);
    }
    
    public synchronized int addArg(final byte[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final short[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final int[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final String s) {
        return this.implementation.addArg(s);
    }
    
    public synchronized int addArg(final String[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final float n) {
        return this.implementation.addArg(n);
    }
    
    public synchronized int addArg(final double n) {
        return this.implementation.addArg(n);
    }
    
    public synchronized int addArg(final float[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final double[] array) {
        return this.implementation.addArg(array);
    }
    
    public synchronized int addArg(final Object o) {
        return this.implementation.addArg(o);
    }
    
    public synchronized int addSymbol(final String s) {
        return this.implementation.addSymbol(s);
    }
    
    public synchronized int addSymbol(final String s, final String s2) {
        return this.implementation.addSymbol(s, s2);
    }
    
    public synchronized int addSymbol(final String s, final String s2, final int n) {
        return this.implementation.addSymbol(s, s2, n);
    }
    
    public synchronized int call() throws JavaLinkDist.JLinkerException {
        switch (this.implementation.mayCall()) {
            case 0: {
                throw new IllegalStateException("Lisp is not ready for a call.");
            }
            case -1: {
                throw new IllegalStateException("May not call Lisp in this thread.");
            }
            default: {
                return this.implementation.call();
            }
        }
    }
    
    public synchronized int callOneWay() throws JavaLinkDist.JLinkerException {
        this.callStyle = -1;
        return this.call();
    }
    
    public synchronized int callIgnore() throws JavaLinkDist.JLinkerException {
        this.callStyle = 0;
        return this.call();
    }
    
    public synchronized int callRef() throws JavaLinkDist.JLinkerException {
        this.callStyle = 1;
        return this.call();
    }
    
    public synchronized int callCopy() throws JavaLinkDist.JLinkerException {
        this.callStyle = 2;
        return this.call();
    }
    
    public synchronized int callAsyncBr() throws JavaLinkDist.JLinkerException {
        this.callStyle = 3;
        return this.call();
    }
    
    public synchronized int callAsyncGo() throws JavaLinkDist.JLinkerException {
        this.callStyle = 4;
        return this.call();
    }
    
    public synchronized Object getValue() {
        return this.getValue(0);
    }
    
    public synchronized Object getValue(final int n) {
        if (this.state != 3) {
            this.throwWrongState("getValue", "");
        }
        if (this.res == null) {
            this.throwWrongState("getValue", " when result from Lisp is null");
        }
        return this.implementation.getValue(n);
    }
    
    int typeOf(final Object o) {
        this.castObject(o);
        return this.implementation.typeOf(o);
    }
    
    public static String nameOfType(final int i) {
        switch (i) {
            case 0: {
                return "Null";
            }
            case 1: {
                return "Boolean";
            }
            case 2: {
                return "Integer";
            }
            case 3: {
                return "Long";
            }
            case 4: {
                return "Real";
            }
            case 5: {
                return "String";
            }
            case 13: {
                return "Error";
            }
            case 11: {
                return "Symbol";
            }
            case 12: {
                return "Lisp-Pointer";
            }
            case 21: {
                return "Int-Array";
            }
            case 22: {
                return "Double-Array";
            }
            case 24: {
                return "Java-Pointer";
            }
            case 23: {
                return "String-Array";
            }
            case 91: {
                return "Wrong-State";
            }
            case 92: {
                return "Missing";
            }
            case 99: {
                return "Unknown";
            }
            case 90: {
                return "Wrapper";
            }
            default: {
                return "Unknown-" + i;
            }
        }
    }
    
    public synchronized int typeOf() {
        return this.typeOf(0);
    }
    
    public synchronized int typeOf(final int n) {
        if (this.state != 3) {
            return 91;
        }
        if (this.res == null) {
            return 92;
        }
        return this.implementation.typeOf(n);
    }
    
    public synchronized int intValue() {
        return this.intValue(0);
    }
    
    public synchronized int intValue(final int n) {
        return this.implementation.intValue(n);
    }
    
    public synchronized long longValue() {
        return this.longValue(0);
    }
    
    public synchronized long longValue(final int n) {
        return this.implementation.longValue(n);
    }
    
    public synchronized double doubleValue() {
        return this.doubleValue(0);
    }
    
    public synchronized double doubleValue(final int n) {
        return this.implementation.doubleValue(n);
    }
    
    public synchronized boolean booleanValue() {
        return this.booleanValue(0);
    }
    
    public synchronized boolean booleanValue(final int n) {
        return this.implementation.booleanValue(n);
    }
    
    public synchronized String stringValue() {
        return this.stringValue(0);
    }
    
    public synchronized String stringValue(final int n) {
        return this.implementation.stringValue(n);
    }
    
    public synchronized int[] intArrayValue() {
        return this.intArrayValue(0);
    }
    
    public synchronized int[] intArrayValue(final int n) {
        return this.implementation.intArrayValue(n);
    }
    
    public synchronized String[] stringArrayValue() {
        return this.stringArrayValue(0);
    }
    
    public synchronized String[] stringArrayValue(final int n) {
        return this.implementation.stringArrayValue(n);
    }
    
    public synchronized double[] doubleArrayValue() {
        return this.doubleArrayValue(0);
    }
    
    public synchronized double[] doubleArrayValue(final int n) {
        return this.implementation.doubleArrayValue(n);
    }
    
    public synchronized Object objectValue() {
        return this.objectValue(0);
    }
    
    public synchronized Object objectValue(final int n) {
        return this.implementation.objectValue(n);
    }
    
    public synchronized String symbolName() {
        return this.symbolName(0);
    }
    
    public synchronized String symbolName(final int n) {
        return this.implementation.symbolName(n);
    }
    
    public synchronized String symbolPackage() {
        return this.symbolPackage(0);
    }
    
    public synchronized String symbolPackage(final int n) {
        return this.implementation.symbolPackage(n);
    }
    
    public synchronized String lispType() {
        return this.lispType(0);
    }
    
    public synchronized String lispType(final int n) {
        return this.implementation.lispType(n);
    }
    
    public synchronized int query() {
        int query = -99;
        try {
            query = this.query(false, false);
        }
        catch (JavaLinkDist.JLinkerException ex) {}
        return query;
    }
    
    public synchronized int query(final boolean b, final boolean b2) throws JavaLinkDist.JLinkerException {
        return this.implementation.query(b, b2);
    }
    
    public synchronized void close() {
        this.waitref = null;
        this.waitres = -99;
        this.res = null;
        this.args = null;
        this.count = 0;
        this.chain = null;
        if (this.state != 0) {
            this.state = 6;
        }
        this.implementation.close();
    }
    
    public synchronized boolean reset() {
        this.waitref = null;
        this.waitres = -99;
        this.res = null;
        if (this.state == 1) {
            this.implementation.assemble("reset");
        }
        this.count = 0;
        this.chain = null;
        switch (this.state) {
            case 0:
            case 1:
            case 2: {
                break;
            }
            case 3:
            case 4:
            case 5: {
                if (this.args == null) {
                    this.state = 1;
                    break;
                }
                this.state = 2;
                break;
            }
            case 6: {
                if (this.lispOp == null) {
                    this.state = 0;
                    break;
                }
                this.state = 1;
                break;
            }
            default: {
                this.throwWrongState("reset", "");
                break;
            }
        }
        this.implementation.reset();
        return this.state == 2;
    }
    
    public synchronized void setArg(final int n, final boolean b) {
        this.implementation.setArg(n, b);
    }
    
    public synchronized void setArg(final int n, final int n2) {
        this.implementation.setArg(n, n2);
    }
    
    public synchronized void setArg(final int n, final long n2) {
        this.implementation.setArg(n, n2);
    }
    
    public synchronized void setArg(final int n, final double n2) {
        this.implementation.setArg(n, n2);
    }
    
    public synchronized void setArg(final int n, final String s) {
        this.implementation.setArg(n, s);
    }
    
    public synchronized void setArg(final int n, final Object o) {
        this.implementation.setArg(n, o);
    }
    
    public synchronized void setArg(final int n, final int[] array) {
        this.implementation.setArg(n, array);
    }
    
    public synchronized void setArg(final int n, final double[] array) {
        this.implementation.setArg(n, array);
    }
    
    public synchronized void setArg(final int n, final String[] array) {
        this.implementation.setArg(n, array);
    }
    
    public synchronized void setSymbol(final int n, final String s) {
        this.implementation.setSymbol(n, s);
    }
    
    void throwWrongState(final String str, final String str2) {
        String str3 = null;
        switch (this.state) {
            case 0: {
                str3 = "STATE_NEW";
                break;
            }
            case 1: {
                str3 = "STATE_COLLECTING";
                break;
            }
            case 2: {
                str3 = "STATE_READY";
                break;
            }
            case 3: {
                str3 = "STATE_DONE";
                break;
            }
            case 4: {
                str3 = "STATE_WAITING";
                break;
            }
            case 5: {
                str3 = "STATE_WAITDONE";
                break;
            }
            case 6: {
                str3 = "STATE_CLOSED";
                break;
            }
            default: {
                str3 = "STATE_???";
                break;
            }
        }
        throw new IllegalStateException(str + " cannot be called in " + str3 + " state" + str2 + ".");
    }
    
    public int mayCall() {
        return this.implementation.mayCall();
    }
    
    public static int dispatchEvent(final String s, final Object o, final String[] array, final int[] array2) {
        return JavaLinkCommon.ltoj_anchor.callLisp(s, o, array, array2);
    }
    
    static {
        LispCall.prototype = new LispCallSocket();
    }
    
    class Holder
    {
        Holder next;
        Object arg;
        
        Holder(final Object arg, final Holder next) {
            this.next = next;
            this.arg = arg;
        }
    }
}
