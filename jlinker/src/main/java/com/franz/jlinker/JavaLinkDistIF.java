// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class JavaLinkDistIF
{
    public void discard(final int[] array) {
        final int[] array2 = new int[2 + array.length];
        array2[0] = -1;
        array2[1] = 3;
        for (int i = 0; i < array.length; ++i) {
            array2[2 + i] = array[i];
        }
        final TranStruct tranStruct = new TranStruct("Self", 7680, array2, new String[0], new double[0]);
        if (0 == Transport.exData) {
            tranStruct.type |= 0xE000;
        }
        JLCommonSocket.client_object.invoke(new TranStruct[] { tranStruct }, true);
    }
    
    public void invokeOneWay(final TranStruct tranStruct, final TranStruct[] array) {
        this.invoke(-1, tranStruct, array);
    }
    
    public TranStruct[] invoke(final int n, final TranStruct tranStruct, final TranStruct[] array) {
        final TranStruct[] array2 = new TranStruct[2 + array.length];
        final TranStruct tranStruct2 = new TranStruct("Self", 7680, new int[] { n, 4 }, new String[0], new double[0]);
        if (0 == Transport.exData) {
            tranStruct2.type |= 0xE000;
        }
        array2[0] = tranStruct2;
        array2[1] = tranStruct;
        for (int i = 0; i < array.length; ++i) {
            array2[2 + i] = array[i];
        }
        switch (n) {
            case -1: {
                JLCommonSocket.client_object.invoke(array2, true);
                return new TranStruct[0];
            }
            case 0: {
                JLCommonSocket.client_object.invoke(array2);
                return new TranStruct[0];
            }
            default: {
                final Object invoke = JLCommonSocket.client_object.invoke(array2);
                try {
                    if (0 <= TranStruct.isArrayLen(invoke)) {
                        return (TranStruct[])invoke;
                    }
                    if (0 <= TranStruct.isArrayOfLen(invoke)) {
                        return TranStruct.toArray(invoke);
                    }
                    return new TranStruct[] { JavaLinkDist.newDistOb(invoke) };
                }
                catch (Exception ex) {
                    return new TranStruct[] { JavaLinkDist.newDistErr(ex), JavaLinkDist.newDistOb(invoke) };
                }
            }
        }
    }
    
    public JavaLinkDistIF openDist() {
        return this;
    }
}
