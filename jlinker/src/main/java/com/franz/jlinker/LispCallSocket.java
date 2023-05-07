// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

class LispCallSocket extends LispCallImplementation
{
    static Class opclass;
    LispCall lci;
    
    Class getOpClass() {
        return LispCallSocket.opclass;
    }
    
    LispCallImplementation builder(final LispCall lci, final int n, final boolean b, final String s) {
        final LispCallSocket lispCallSocket = new LispCallSocket();
        lispCallSocket.lci = lci;
        if (!s.equals("")) {
            lci.lispOp = JavaLinkDist.newDistOb(s);
        }
        return lispCallSocket;
    }
    
    void setOp(final String s) {
        this.setOp(JavaLinkDist.newDistOb(s));
    }
    
    void setOp(final Object lispOp) {
        this.lci.lispOp = lispOp;
        if (this.lci.state == 0) {
            this.lci.state = 1;
        }
    }
    
    synchronized int addArgWrapped(final TranStruct tranStruct) {
        if (this.lci.state != 1) {
            throw new IllegalArgumentException("Wrong state");
        }
        return this.lci.newHolder(tranStruct);
    }
    
    synchronized int addArg(final int n) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(n));
    }
    
    synchronized int addArg(final short n) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(n));
    }
    
    synchronized int addArg(final byte b) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(b));
    }
    
    synchronized int addArg(final long n) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(n));
    }
    
    synchronized int addArg(final boolean b) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(b));
    }
    
    synchronized int addArg(final int[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final short[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final byte[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final String s) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(s));
    }
    
    synchronized int addArg(final String[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final double n) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(n));
    }
    
    synchronized int addArg(final float n) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(n));
    }
    
    synchronized int addArg(final double[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final float[] array) {
        return this.addArgWrapped(JavaLinkDist.newDistOb(array));
    }
    
    synchronized int addArg(final Object o) {
        if (LispCallSocket.opclass.isInstance(o)) {
            return this.addArgWrapped((TranStruct)o);
        }
        return this.addArgWrapped(JavaLinkDist.newDistOb(o));
    }
    
    synchronized int addSymbol(final String s) {
        return this.addArgWrapped(JavaLinkDist.newDistSym(s, "", 0));
    }
    
    synchronized int addSymbol(final String s, final String s2) {
        return this.addArgWrapped(JavaLinkDist.newDistSym(s, s2, 0));
    }
    
    synchronized int addSymbol(final String s, final String s2, final int n) {
        return this.addArgWrapped(JavaLinkDist.newDistSym(s, s2, n));
    }
    
    synchronized void assemble(final String s) {
        if (this.lci.state != 1) {
            this.lci.throwWrongState(s, "");
        }
        final TranStruct[] args = new TranStruct[this.lci.count];
        this.lci.args = args;
        for (int i = 0; i < this.lci.count; ++i) {
            args[this.lci.count - i - 1] = (TranStruct)this.lci.chain.arg;
            this.lci.chain = this.lci.chain.next;
        }
        this.lci.count = 0;
        this.lci.state = 2;
    }
    
    synchronized int call() throws JavaLinkDist.JLinkerException {
        if (this.lci.state == 1) {
            this.assemble("call");
        }
        if (this.lci.state != 2) {
            this.lci.throwWrongState("call", "");
        }
        final TranStruct[] invokeInLispEx = JavaLinkDist.invokeInLispEx(this.lci.callStyle, (TranStruct)this.lci.lispOp, (TranStruct[])this.lci.args);
        this.lci.res = invokeInLispEx;
        if (!this.lci.retain) {
            this.lci.args = null;
        }
        if (this.lci.callStyle >= 3) {
            this.lci.waitref = invokeInLispEx[1];
            this.lci.res = null;
            this.lci.state = 4;
            return 0;
        }
        this.lci.waitref = null;
        this.lci.state = 3;
        if (invokeInLispEx.length == 0) {
            return 0;
        }
        return JavaLinkDist.intValue(invokeInLispEx[0]);
    }
    
    synchronized Object getValue(final int n) {
        final TranStruct[] array = (TranStruct[])this.lci.res;
        final TranStruct tranStruct = array[n + 1];
        if (tranStruct == null) {
            this.lci.throwWrongState("getValue", "when value already retrieved once");
        }
        if (!this.lci.retain) {
            array[n + 1] = null;
        }
        return tranStruct;
    }
    
    int typeOf(final Object o) {
        final TranStruct tranStruct = (TranStruct)o;
        if (JavaLinkDist.nullP(tranStruct)) {
            return 0;
        }
        if (JavaLinkDist.booleanP(tranStruct)) {
            return 1;
        }
        if (JavaLinkDist.longP(tranStruct)) {
            return 3;
        }
        if (JavaLinkDist.integerP(tranStruct)) {
            return 2;
        }
        if (JavaLinkDist.realP(tranStruct)) {
            return 4;
        }
        if (JavaLinkDist.errorP(tranStruct)) {
            return 13;
        }
        if (JavaLinkDist.symbolP(tranStruct)) {
            return 11;
        }
        if (JavaLinkDist.stringP(tranStruct)) {
            return 5;
        }
        final Object pointerValue = JavaLinkDist.pointerValue(tranStruct);
        if (pointerValue == tranStruct) {
            return 12;
        }
        final String name = ((TranStruct)pointerValue).getClass().getName();
        if (name.equals("[I")) {
            return 21;
        }
        if (name.equals("[D")) {
            return 22;
        }
        if (name.equals("[Ljava.lang.String;")) {
            return 23;
        }
        return 24;
    }
    
    synchronized int typeOf(final int n) {
        if (n + 1 >= ((TranStruct[])this.lci.res).length) {
            return 92;
        }
        return this.typeOf(this.getValue(n));
    }
    
    TranStruct getValueTr(final int n) {
        return (TranStruct)this.getValue(n);
    }
    
    synchronized int intValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.integerP(valueTr)) {
            return JavaLinkDist.intValue(valueTr);
        }
        throw new UnsupportedOperationException("intValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized long longValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.longP(valueTr)) {
            return JavaLinkDist.longValue(valueTr);
        }
        if (JavaLinkDist.integerP(valueTr)) {
            return JavaLinkDist.intValue(valueTr);
        }
        throw new UnsupportedOperationException("longValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized double doubleValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.realP(valueTr)) {
            return JavaLinkDist.doubleValue(valueTr);
        }
        throw new UnsupportedOperationException("doubleValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized boolean booleanValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.booleanP(valueTr)) {
            return JavaLinkDist.boolValue(valueTr);
        }
        throw new UnsupportedOperationException("booleanValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized String stringValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.stringP(valueTr)) {
            return JavaLinkDist.stringValue(valueTr);
        }
        throw new UnsupportedOperationException("stringValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized int[] intArrayValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        final int type = this.typeOf(valueTr);
        if (type == 21) {
            return (int[])JavaLinkDist.pointerValue(valueTr);
        }
        throw new UnsupportedOperationException("intArrayValue of " + LispCall.nameOfType(type));
    }
    
    synchronized String[] stringArrayValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        final int type = this.typeOf(valueTr);
        if (type == 23) {
            return (String[])JavaLinkDist.pointerValue(valueTr);
        }
        throw new UnsupportedOperationException("stringArrayValue of " + LispCall.nameOfType(type));
    }
    
    synchronized double[] doubleArrayValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        final int type = this.typeOf(valueTr);
        if (type == 22) {
            return (double[])JavaLinkDist.pointerValue(valueTr);
        }
        throw new UnsupportedOperationException("doubleArrayValue of " + LispCall.nameOfType(type));
    }
    
    synchronized Object objectValue(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.pointerP(valueTr)) {
            final Object pointerValue = JavaLinkDist.pointerValue(valueTr);
            if (pointerValue != valueTr) {
                return pointerValue;
            }
        }
        throw new UnsupportedOperationException("objectValue of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized String symbolName(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.symbolP(valueTr)) {
            return JavaLinkDist.symbolName(valueTr);
        }
        throw new UnsupportedOperationException("symbolName of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized String symbolPackage(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.symbolP(valueTr)) {
            return JavaLinkDist.symbolPackage(valueTr);
        }
        throw new UnsupportedOperationException("symbolPackage of " + LispCall.nameOfType(this.typeOf(valueTr)));
    }
    
    synchronized String lispType(final int n) {
        final TranStruct valueTr = this.getValueTr(n);
        if (JavaLinkDist.pointerP(valueTr) && valueTr == JavaLinkDist.pointerValue(valueTr)) {
            return JavaLinkDist.stringValue(valueTr);
        }
        return LispCall.nameOfType(this.typeOf(valueTr));
    }
    
    synchronized int query(final boolean b, final boolean b2) throws JavaLinkDist.JLinkerException {
        TranStruct[] array = null;
        if (this.lci.res != null) {
            array = (TranStruct[])this.lci.res;
        }
        switch (this.lci.state) {
            case 0:
            case 1:
            case 2:
            case 6: {
                return -100;
            }
            case 3: {
                if (this.lci.res == null) {
                    return -99;
                }
                if (array.length == 0) {
                    return 0;
                }
                return JavaLinkDist.intValue(array[0]);
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
            final TranStruct[] invokeInLispEx = JavaLinkDist.invokeInLispEx(2, JavaLinkDist.newDistOb("cl:car"), new TranStruct[] { (TranStruct)this.lci.waitref });
            this.lci.res = invokeInLispEx;
            final int intValue = JavaLinkDist.intValue(invokeInLispEx[1]);
            if (intValue >= 0) {
                this.lci.waitres = intValue;
                this.lci.state = 5;
            }
            else if (intValue == -98) {
                this.lci.waitres = intValue;
                this.lci.res = null;
                this.lci.state = 5;
            }
            else if (intValue == -99) {
                this.lci.res = null;
                this.lci.state = 3;
                return -99;
            }
        }
        if (b2 && this.lci.state == 5) {
            final TranStruct[] invokeInLispEx2 = JavaLinkDist.invokeInLispEx(2, JavaLinkDist.newDistOb("javatools.jlinker::jl-async-results"), new TranStruct[] { (TranStruct)this.lci.waitref });
            this.lci.res = invokeInLispEx2;
            this.lci.state = 3;
            if (this.lci.res == null) {
                return 0;
            }
            if (((TranStruct[])this.lci.res).length == 0) {
                return 0;
            }
            return JavaLinkDist.intValue(invokeInLispEx2[0]);
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
    }
    
    synchronized void setArgWrapped(final int n, final JLWrapper jlWrapper) {
        final TranStruct tranStruct = (TranStruct)jlWrapper;
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
        ((TranStruct[])this.lci.args)[n] = tranStruct;
    }
    
    synchronized void setArg(final int n, final boolean b) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(b));
    }
    
    synchronized void setArg(final int n, final int n2) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(n2));
    }
    
    synchronized void setArg(final int n, final long n2) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(n2));
    }
    
    synchronized void setArg(final int n, final double n2) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(n2));
    }
    
    synchronized void setArg(final int n, final String s) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(s));
    }
    
    synchronized void setArg(final int n, final Object o) {
        if (LispCallSocket.opclass.isInstance(o)) {
            this.setArgWrapped(n, (JLWrapper)o);
        }
        else {
            this.setArgWrapped(n, JavaLinkDist.newDistOb(o));
        }
    }
    
    synchronized void setArg(final int n, final int[] array) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(array));
    }
    
    synchronized void setArg(final int n, final double[] array) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(array));
    }
    
    synchronized void setArg(final int n, final String[] array) {
        this.setArgWrapped(n, JavaLinkDist.newDistOb(array));
    }
    
    synchronized void setSymbol(final int n, final String s) {
        this.setArg(n, JavaLinkDist.newDistSym(s, "", 0));
    }
    
    synchronized void setSymbol(final int n, final String s, final String s2) {
        this.setArg(n, JavaLinkDist.newDistSym(s, s2, 0));
    }
    
    synchronized void setSymbol(final int n, final String s, final String s2, final int n2) {
        this.setArg(n, JavaLinkDist.newDistSym(s, s2, n2));
    }
    
    int mayCall() {
        return 2;
    }
    
    static {
        LispCallSocket.opclass = null;
        try {
            LispCallSocket.opclass = Class.forName("com.franz.jlinker.TranStruct");
        }
        catch (ClassNotFoundException ex) {}
    }
}
