// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class TranStruct extends JLWrapper
{
    public static final Class CLASS;
    public String home;
    public int type;
    public int[] nums;
    public String[] strings;
    public double[] reals;
    public Object exdata;
    
    public Object getExdata() {
        return this.exdata;
    }
    
    public void setExdata(final Object exdata) {
        this.exdata = exdata;
    }
    
    public TranStruct(final String home, final int type, final int[] nums, final String[] strings, final double[] reals) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = nums;
        this.strings = strings;
        this.reals = reals;
    }
    
    public TranStruct(final String home, final int type, final int[] nums, final String s, final double[] reals) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = nums;
        this.strings = new String[] { s };
        this.reals = reals;
    }
    
    public TranStruct(final String home, final int type, final int[] nums, final String[] strings) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = nums;
        this.strings = strings;
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type, final int[] nums, final String s) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = nums;
        this.strings = new String[] { s };
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type, final int[] nums) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = nums;
        this.strings = new String[0];
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type, final int n) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = new int[] { n };
        this.strings = new String[0];
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type, final int n, final int n2) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = new int[] { n, n2 };
        this.strings = new String[0];
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type, final int n, final int n2, final int n3) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = new int[] { n, n2, n3 };
        this.strings = new String[0];
        this.reals = new double[0];
    }
    
    public TranStruct(final String home, final int type) {
        this.exdata = null;
        this.home = home;
        this.type = type;
        this.nums = new int[0];
        this.strings = new String[0];
        this.reals = new double[0];
    }
    
    public static boolean isP(final Object o) {
        return TranStruct.CLASS == o.getClass();
    }
    
    public static int isArrayLen(final Object o) {
        TranStruct[] array;
        try {
            array = (TranStruct[])o;
        }
        catch (Exception ex) {
            return -1;
        }
        return array.length;
    }
    
    public static int isArrayOfLen(final Object o) {
        Object[] array;
        try {
            array = (Object[])o;
        }
        catch (Exception ex) {
            return -1;
        }
        for (int i = 0; i < array.length; ++i) {
            if (!isP(array[i])) {
                return -i - 1;
            }
        }
        return array.length;
    }
    
    public static TranStruct[] toArray(final Object o) {
        TranStruct[] array = new TranStruct[0];
        try {
            if (isArrayLen(o) >= 0) {
                return (TranStruct[])o;
            }
            if (isArrayOfLen(o) < 0) {
                return array;
            }
            final Object[] array2 = (Object[])o;
            array = new TranStruct[array2.length];
            for (int i = 0; i < array2.length; ++i) {
                array[i] = (TranStruct)array2[i];
            }
            return array;
        }
        catch (Exception ex) {
            return array;
        }
    }
    
    static {
        CLASS = new TranStruct("", 0).getClass();
    }
}
