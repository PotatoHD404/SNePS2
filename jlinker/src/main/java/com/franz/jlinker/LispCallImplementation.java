// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

abstract class LispCallImplementation
{
    abstract Class getOpClass();
    
    abstract void assemble(final String p0);
    
    abstract void setOp(final String p0);
    
    abstract void setOp(final Object p0);
    
    abstract int addArg(final byte p0);
    
    abstract int addArg(final short p0);
    
    abstract int addArg(final int p0);
    
    abstract int addArg(final long p0);
    
    abstract int addArg(final boolean p0);
    
    abstract int addArg(final byte[] p0);
    
    abstract int addArg(final short[] p0);
    
    abstract int addArg(final int[] p0);
    
    abstract int addArg(final String p0);
    
    abstract int addArg(final String[] p0);
    
    abstract int addArg(final float p0);
    
    abstract int addArg(final double p0);
    
    abstract int addArg(final float[] p0);
    
    abstract int addArg(final double[] p0);
    
    abstract int addArg(final Object p0);
    
    abstract int addSymbol(final String p0);
    
    abstract int addSymbol(final String p0, final String p1);
    
    abstract int addSymbol(final String p0, final String p1, final int p2);
    
    abstract int call() throws JavaLinkDist.JLinkerException;
    
    abstract Object getValue(final int p0);
    
    abstract int typeOf(final Object p0);
    
    abstract int typeOf(final int p0);
    
    abstract int intValue(final int p0);
    
    abstract long longValue(final int p0);
    
    abstract double doubleValue(final int p0);
    
    abstract boolean booleanValue(final int p0);
    
    abstract String stringValue(final int p0);
    
    abstract int[] intArrayValue(final int p0);
    
    abstract String[] stringArrayValue(final int p0);
    
    abstract double[] doubleArrayValue(final int p0);
    
    abstract Object objectValue(final int p0);
    
    abstract String symbolName(final int p0);
    
    abstract String symbolPackage(final int p0);
    
    abstract String lispType(final int p0);
    
    abstract int query(final boolean p0, final boolean p1) throws JavaLinkDist.JLinkerException;
    
    abstract void setArg(final int p0, final Object p1);
    
    abstract void setArg(final int p0, final boolean p1);
    
    abstract void setArg(final int p0, final int p1);
    
    abstract void setArg(final int p0, final long p1);
    
    abstract void setArg(final int p0, final double p1);
    
    abstract void setArg(final int p0, final String p1);
    
    abstract void setArg(final int p0, final int[] p1);
    
    abstract void setArg(final int p0, final double[] p1);
    
    abstract void setArg(final int p0, final String[] p1);
    
    abstract void setSymbol(final int p0, final String p1);
    
    abstract void setSymbol(final int p0, final String p1, final String p2);
    
    abstract void setSymbol(final int p0, final String p1, final String p2, final int p3);
    
    abstract LispCallImplementation builder(final LispCall p0, final int p1, final boolean p2, final String p3);
    
    abstract void close();
    
    abstract void reset();
    
    abstract int mayCall();
}
