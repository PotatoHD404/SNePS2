// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

public class LispConnector
{
    public static boolean lispAdvertises;
    public static boolean advertInFile;
    public static String lispFile;
    public static String lispHost;
    public static int lispPort;
    public static int pollInterval;
    public static int pollCount;
    public static int javaTimeout;
    public static String javaFile;
    public static String javaHost;
    public static int javaPort;
    public static boolean debug;
    
    public static synchronized boolean go() {
        return go(false, null);
    }
    
    public static synchronized boolean go(final boolean b) {
        return go(b, null);
    }
    
    public static synchronized boolean go(final boolean b, final String[] array) {
        String str = "";
        JavaLinkCommon.sdebug = LispConnector.debug;
        if (JavaLinkDist.query(b)) {
            return true;
        }
        if (LispConnector.lispAdvertises) {
            if (LispConnector.advertInFile) {
                if (!JavaLinkDist.connect(LispConnector.lispFile, LispConnector.javaHost, LispConnector.javaPort, LispConnector.pollInterval, LispConnector.pollCount)) {
                    str = "Connect to Lisp file failed.";
                }
            }
            else if (!JavaLinkDist.connect(LispConnector.lispHost, LispConnector.lispPort, LispConnector.javaHost, LispConnector.javaPort, LispConnector.pollInterval, LispConnector.pollCount)) {
                str = "Connect to Lisp port failed.";
            }
        }
        else if (LispConnector.advertInFile) {
            if (!JavaLinkDist.advertise(LispConnector.javaFile, LispConnector.javaHost, LispConnector.javaPort, LispConnector.javaTimeout)) {
                str = "Lisp did not connect to file.";
            }
        }
        else if (!JavaLinkDist.advertise(LispConnector.javaPort, LispConnector.javaTimeout)) {
            str = "Lisp did not connect to port.";
        }
        if (str.length() == 0) {
            return true;
        }
        final String string = "LispConnector.go: " + str;
        if (array == null) {
            throw new IllegalArgumentException(string);
        }
        if (0 < array.length) {
            array[0] = string;
        }
        return false;
    }
    
    static {
        LispConnector.lispAdvertises = true;
        LispConnector.advertInFile = false;
        LispConnector.lispFile = "";
        LispConnector.lispHost = "";
        LispConnector.lispPort = 4321;
        LispConnector.pollInterval = 1000;
        LispConnector.pollCount = 300;
        LispConnector.javaTimeout = -1;
        LispConnector.javaFile = "";
        LispConnector.javaHost = "";
        LispConnector.javaPort = 0;
        LispConnector.debug = false;
    }
}
