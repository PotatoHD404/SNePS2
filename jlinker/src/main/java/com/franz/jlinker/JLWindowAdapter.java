// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.WindowEvent;
import java.awt.Window;
import java.awt.event.WindowAdapter;

public class JLWindowAdapter extends WindowAdapter
{
    public static synchronized void addTo(final Window window) {
        window.addWindowListener(new JLWindowAdapter());
    }
    
    private void caller(final String s, final WindowEvent windowEvent) {
        LispCall.dispatchEvent(s, windowEvent.getWindow(), new String[0], new int[0]);
    }
    
    public void windowOpened(final WindowEvent windowEvent) {
        this.caller("windowOpened", windowEvent);
    }
    
    public void windowClosing(final WindowEvent windowEvent) {
        this.caller("windowClosing", windowEvent);
    }
    
    public void windowClosed(final WindowEvent windowEvent) {
        this.caller("windowClosed", windowEvent);
    }
    
    public void windowIconified(final WindowEvent windowEvent) {
        this.caller("windowIconified", windowEvent);
    }
    
    public void windowDeiconified(final WindowEvent windowEvent) {
        this.caller("windowDeiconified", windowEvent);
    }
    
    public void windowActivated(final WindowEvent windowEvent) {
        this.caller("windowActivated", windowEvent);
    }
    
    public void windowDeactivated(final WindowEvent windowEvent) {
        this.caller("windowDeactivated", windowEvent);
    }
}
