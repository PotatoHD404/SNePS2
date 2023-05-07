// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class JLCommonJNI extends JLCommonImplementation
{
    final int version = 8001001;
    
    public int version() {
        return 8001001;
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
    
    public Object[] lispValues(final Object o, final String s, final int n, final int n2, final boolean b) {
        return new Object[0];
    }
    
    public int callLisp(final String s, final Object o) {
        return this.callLisp(s, o, new String[0], null);
    }
    
    public int callLisp(final String s, final Object o, final String s2) {
        return this.callLisp(s, o, new String[] { s2 }, null);
    }
    
    public int callLisp(final String s, final Object o, final String s2, final int[] array) {
        return this.callLisp(s, o, new String[] { s2 }, array);
    }
    
    public int callLisp(final String s, final Object o, final String[] array, final int[] array2) {
        final int sObHandle = JavaLinkCommon.getSObHandle(o);
        if (sObHandle == 0) {
            return 0;
        }
        return LispCallJNI.callLispWithEvent(s, sObHandle, array, array2);
    }
    
    public void activate(final int n, final String[] array) {
    }
    
    public static Thread sleeper() {
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                final Object o = new Object();
                try {
                    o.wait();
                }
                catch (InterruptedException ex) {}
            }
        });
        thread.setDaemon(false);
        thread.start();
        return thread;
    }
}
