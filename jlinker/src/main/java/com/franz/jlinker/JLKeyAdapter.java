// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.event.KeyAdapter;

public class JLKeyAdapter extends KeyAdapter
{
    public static synchronized void addTo(final Component component) {
        component.addKeyListener(new JLKeyAdapter());
    }
    
    private void caller(final String s, final KeyEvent keyEvent) {
        LispCall.dispatchEvent(s, keyEvent.getComponent(), new String[] { keyEvent.paramString() }, new int[] { keyEvent.getModifiers(), keyEvent.isActionKey() ? 1 : 0, keyEvent.getKeyCode() });
    }
    
    public void keyTyped(final KeyEvent keyEvent) {
        this.caller("keyTyped", keyEvent);
    }
    
    public void keyPressed(final KeyEvent keyEvent) {
        this.caller("keyPressed", keyEvent);
    }
    
    public void keyReleased(final KeyEvent keyEvent) {
        this.caller("keyReleased", keyEvent);
    }
}
