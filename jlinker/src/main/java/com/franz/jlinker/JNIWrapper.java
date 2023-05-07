// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

class JNIWrapper extends JLWrapper
{
    String type;
    Object data;
    String packageName;
    int action;
    int lispIndex;
    String lispType;
    String lispDesc;
    
    public String getWrapperType() {
        return this.type;
    }
    
    public String getLispType() {
        if (this.type.equals("P")) {
            return this.lispType;
        }
        if (this.type.equals("Y")) {
            return "symbol";
        }
        return "";
    }
    
    public int getLispIndex() {
        return this.lispIndex;
    }
    
    public String getSymbolName() {
        if (this.type.equals("Y")) {
            return (String)this.data;
        }
        return "";
    }
    
    public String getSymbolPackage() {
        return this.packageName;
    }
    
    public int getSymbolAction() {
        return this.action;
    }
    
    JNIWrapper(final String type, final Object data) {
        this.type = "";
        this.data = null;
        this.packageName = "";
        this.action = 0;
        this.lispIndex = 0;
        this.lispType = "";
        this.lispDesc = "";
        this.type = type;
        this.data = data;
        this.packageName = "";
        this.action = 0;
    }
    
    JNIWrapper(final String type, final int lispIndex, final String lispType, final String lispDesc) {
        this.type = "";
        this.data = null;
        this.packageName = "";
        this.action = 0;
        this.lispIndex = 0;
        this.lispType = "";
        this.lispDesc = "";
        this.type = type;
        this.lispType = lispType;
        this.lispDesc = lispDesc;
        this.lispIndex = lispIndex;
    }
    
    JNIWrapper(final String data, final String packageName, final int action) {
        this.type = "";
        this.data = null;
        this.packageName = "";
        this.action = 0;
        this.lispIndex = 0;
        this.lispType = "";
        this.lispDesc = "";
        this.type = "Y";
        this.data = data;
        this.packageName = packageName;
        this.action = action;
    }
    
    public static int translateType(final String s) {
        if (s.equals("W")) {
            return 0;
        }
        if (s.equals("Z")) {
            return 1;
        }
        if (s.equals("C")) {
            return 2;
        }
        if (s.equals("B")) {
            return 2;
        }
        if (s.equals("S")) {
            return 2;
        }
        if (s.equals("I")) {
            return 2;
        }
        if (s.equals("J")) {
            return 3;
        }
        if (s.equals("F")) {
            return 4;
        }
        if (s.equals("D")) {
            return 4;
        }
        if (s.equals("T")) {
            return 5;
        }
        if (s.equals("K")) {
            return 21;
        }
        if (s.equals("M")) {
            return 21;
        }
        if (s.equals("N")) {
            return 21;
        }
        if (s.equals("G")) {
            return 22;
        }
        if (s.equals("E")) {
            return 22;
        }
        if (s.equals("U")) {
            return 23;
        }
        if (s.equals("P")) {
            return 12;
        }
        if (s.equals("R")) {
            return 13;
        }
        if (s.equals("Y")) {
            return 11;
        }
        if (s.equals("X")) {
            return 24;
        }
        if (s.equals("H")) {
            return 92;
        }
        if (s.equals("A")) {
            return 90;
        }
        return 99;
    }
    
    public static int decodeType(final String s) {
        if (s.equals("W")) {
            return 0;
        }
        if (s.equals("Z")) {
            return 1;
        }
        if (s.equals("C")) {
            return 2;
        }
        if (s.equals("B")) {
            return 3;
        }
        if (s.equals("S")) {
            return 4;
        }
        if (s.equals("I")) {
            return 5;
        }
        if (s.equals("J")) {
            return 6;
        }
        if (s.equals("F")) {
            return 7;
        }
        if (s.equals("D")) {
            return 8;
        }
        if (s.equals("T")) {
            return 9;
        }
        if (s.equals("K")) {
            return 19;
        }
        if (s.equals("M")) {
            return 20;
        }
        if (s.equals("N")) {
            return 21;
        }
        if (s.equals("G")) {
            return 23;
        }
        if (s.equals("E")) {
            return 24;
        }
        if (s.equals("U")) {
            return 25;
        }
        if (s.equals("P")) {
            return 64;
        }
        if (s.equals("R")) {
            return 65;
        }
        if (s.equals("Y")) {
            return 66;
        }
        if (s.equals("X")) {
            return 67;
        }
        if (s.equals("A")) {
            return 68;
        }
        if (s.equals("H")) {
            return 98;
        }
        return 99;
    }
}
