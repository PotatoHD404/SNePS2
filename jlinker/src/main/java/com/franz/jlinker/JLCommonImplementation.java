// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public abstract class JLCommonImplementation
{
    public abstract Object[] newGate();
    
    public abstract String testGate(final Object[] p0);
    
    public abstract Object[] lispValues(final Object p0, final String p1, final int p2, final int p3, final boolean p4);
    
    public abstract int callLisp(final String p0, final Object p1);
    
    public abstract int callLisp(final String p0, final Object p1, final String p2);
    
    public abstract int callLisp(final String p0, final Object p1, final String p2, final int[] p3);
    
    public abstract int callLisp(final String p0, final Object p1, final String[] p2, final int[] p3);
    
    public abstract void activate(final int p0, final String[] p1);
    
    public abstract int version();
}
