// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class JavaLinkRoot extends Thread
{
    String advertiseInFile;
    int advertiseOnPort;
    String advertisedHost;
    Object[] adFactory;
    public Object serverState;
    public int linkPort;
    public String linkHost;
    
    public JavaLinkRoot(final String advertiseInFile, final String advertisedHost, final int advertiseOnPort) {
        super("JavaLinkServer");
        this.advertiseInFile = "";
        this.advertiseOnPort = 0;
        this.advertisedHost = "";
        this.adFactory = new Object[] { new Integer(0), "" };
        this.serverState = "Closed";
        this.linkPort = 0;
        this.linkHost = "";
        this.advertiseOnPort = advertiseOnPort;
        this.advertiseInFile = advertiseInFile;
        this.advertisedHost = advertisedHost;
        this.serverState = "Closed";
        if (this.advertiseOnPort < 0) {
            JavaLinkCommon.dsprint("JavaLinkRoot: will connect to port " + this.advertisedHost + ":" + this.advertiseOnPort);
        }
        else if (this.advertiseInFile.equals("")) {
            JavaLinkCommon.dsprint("JavaLinkRoot: Allocating server port");
            this.adFactory = Transport.makeServerDataPort(this.advertiseOnPort);
            JavaLinkCommon.dsprint("JavaLinkRoot: Allocated server port " + this.advertisingOnPort());
        }
        JavaLinkCommon.dsprint("JavaLinkRoot: making JavaLinkDist.");
        new JavaLinkDist();
    }
    
    public int advertisingOnPort() {
        return (int)this.adFactory[0];
    }
    
    public void run() {
        Object obj;
        if (this.advertiseOnPort < 0) {
            JavaLinkCommon.dsprint("JavaLinkRoot: connecting to port.");
            obj = Transport.makeServerDataPort(-this.advertiseOnPort, this.advertisedHost);
        }
        else if (this.advertiseInFile.equals("")) {
            JavaLinkCommon.dsprint("JavaLinkRoot: listening at server port.");
            obj = Transport.makeServerDataPort(this.adFactory);
        }
        else {
            if (this.advertisedHost.equals("")) {
                this.advertisedHost = "localhost";
            }
            JavaLinkCommon.dsprint("JavaLinkRoot: advertising in " + this.advertiseInFile + " as " + this.advertisedHost + ":" + this.advertiseOnPort);
            obj = Transport.makeServerDataPort(this.advertiseInFile, this.advertisedHost, this.advertiseOnPort);
        }
        if (Transport.isP(obj)) {
            final Transport coerce = Transport.coerce(obj);
            this.serverState = "Running";
            JavaLinkCommon.dsprint("JavaLinkRoot: server started.");
            this.serverState = coerce.doServer(this);
        }
        else {
            JavaLinkCommon.dsprint("JavaLinkRoot: makeServerDataPort failed " + obj);
        }
        JavaLinkCommon.dsprint("JavaLinkRoot: server ending status: " + this.serverState);
    }
    
    JLCommonSocket getAnchor() {
        return (JLCommonSocket)JavaLinkCommon.ltoj_anchor;
    }
    
    public Object doRequest(final String str, final int n, final int[] array, final String[] array2, final double[] array3) {
        if (str.equals("LispServerPort")) {
            this.linkPort = n;
            this.linkHost = array2[0];
            synchronized (this) {
                this.notify();
            }
            return new Integer(n);
        }
        return "Unknown request: " + str;
    }
    
    public TranStruct[] doInvoke(final TranStruct[] array) {
        int i = 2;
        final int length = array.length;
        TranStruct[] invoke = new TranStruct[0];
        if (0 < length) {
            final TranStruct tranStruct = array[0];
            if ((tranStruct.type & 0x1F00) == 0x1E00 && tranStruct.nums.length > 1) {
                i = tranStruct.nums[0];
                final int j = tranStruct.nums[1];
                switch (j) {
                    case 4: {
                        if (1 < length) {
                            final TranStruct[] array2 = new TranStruct[length - 2];
                            for (int k = 0; k < array2.length; ++k) {
                                array2[k] = array[k + 2];
                            }
                            invoke = JavaLinkDist.defaultLocal().invoke(i, array[1], array2);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        final int[] array3 = new int[tranStruct.nums.length - 2];
                        for (int l = 0; l < array3.length; ++l) {
                            array3[l] = tranStruct.nums[l + 2];
                        }
                        JavaLinkDist.defaultLocal().discard(array3);
                        break;
                    }
                    case 2: {
                        invoke = new TranStruct[] { JavaLinkDist.newDistOb(this.getAnchor().message(tranStruct.nums[2], tranStruct.strings)) };
                        break;
                    }
                    case 1: {
                        this.getAnchor().activate(tranStruct.nums[2], tranStruct.strings);
                        break;
                    }
                    default: {
                        invoke = new TranStruct[] { JavaLinkDist.newDistOb("Unknown message: style=" + i + " kind=" + j + " nums.length=" + tranStruct.nums.length) };
                        break;
                    }
                }
            }
            else {
                invoke = new TranStruct[] { JavaLinkDist.newDistOb("Unknown message type: " + tranStruct.type) };
            }
        }
        else {
            invoke = new TranStruct[] { JavaLinkDist.newDistOb("Zero-length arg list") };
        }
        switch (i) {
            case -1:
            case 0: {
                return invoke;
            }
            default: {
                return invoke;
            }
        }
    }
}
