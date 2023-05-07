// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class JavaLinkNotifier
{
    public void notifyLispShort(final String s, final int n, final int n2, final String s2) {
        this.notifyLispBoth(s, n, n2, new String[] { s2 }, new int[0]);
    }
    
    public void notifyLispLongs(final String s, final int n, final int n2, final String s2, final int[] array) {
        this.notifyLispBoth(s, n, n2, new String[] { s2 }, array);
    }
    
    public void notifyLispStrings(final String s, final int n, final int n2, final String[] array) {
        this.notifyLispBoth(s, n, n2, array, new int[0]);
    }
    
    public void notifyLispBoth(final String s, final int n, final int n2, final String[] array, final int[] array2) {
        final int[] array3 = new int[4 + array2.length];
        array3[0] = -1;
        array3[1] = 5;
        array3[2] = n;
        array3[3] = n2;
        for (int i = 0; i < array2.length; ++i) {
            array3[4 + i] = array2[i];
        }
        final String[] array4 = new String[1 + array.length];
        array4[0] = s;
        for (int j = 0; j < array.length; ++j) {
            array4[1 + j] = array[j];
        }
        final TranStruct tranStruct = new TranStruct("Self", 7680, array3, array4, new double[0]);
        if (0 == Transport.exData) {
            tranStruct.type |= 0xE000;
        }
        JavaLinkCommon.dsprint("JavaLinkNotifier invoke=> " + JLCommonSocket.client_object.invoke(new TranStruct[] { tranStruct }, true));
    }
}
