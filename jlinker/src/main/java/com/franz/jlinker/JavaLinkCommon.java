// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;


import java.util.Hashtable;

public class JavaLinkCommon
{
    public static JLCommonImplementation ltoj_anchor;
    public static boolean sdebug;
    static Hashtable j_to_handle;
    static int new_handle;
    
    public static int version() {
        return JavaLinkCommon.ltoj_anchor.version();
    }
    
    public static Object[] newGate() {
        return JavaLinkCommon.ltoj_anchor.newGate();
    }
    
    public static String testGate(final Object[] array) {
        return JavaLinkCommon.ltoj_anchor.testGate(array);
    }
    
    public static Object[] lispValues(final Object o, final String s, final int n, final int n2, final boolean b) {
        return JavaLinkCommon.ltoj_anchor.lispValues(o, s, n, n2, b);
    }
    
    public static void dsprint(final String x) {
        if (JavaLinkCommon.sdebug) {
            System.out.println(x);
        }
    }
    
    public static int registerSOb(final Object o) {
        final Integer value = (Integer) JavaLinkCommon.j_to_handle.get(o);
        if (value == null) {
            ++JavaLinkCommon.new_handle;
            JavaLinkCommon.j_to_handle.put(o, new Integer(JavaLinkCommon.new_handle));
            return JavaLinkCommon.new_handle;
        }
        return value;
    }
    
    public static int getSObHandle(final Object key) {
        final Integer value = (Integer) JavaLinkCommon.j_to_handle.get(key);
        if (value == null) {
            return 0;
        }
        return value;
    }
    
    static {
        JavaLinkCommon.sdebug = false;
        JavaLinkCommon.j_to_handle = new Hashtable(1000);
        JavaLinkCommon.new_handle = 1;
    }
}
