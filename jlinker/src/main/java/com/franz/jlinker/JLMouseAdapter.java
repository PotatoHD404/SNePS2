// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.event.MouseAdapter;

public class JLMouseAdapter extends MouseAdapter
{
    public static synchronized void addTo(final Component component) {
        component.addMouseListener(new JLMouseAdapter());
    }
    
    private void caller(final String s, final MouseEvent mouseEvent) {
        LispCall.dispatchEvent(s, mouseEvent.getComponent(), new String[] { mouseEvent.paramString() }, new int[] { mouseEvent.getModifiers(), mouseEvent.isPopupTrigger() ? 1 : 0, mouseEvent.getClickCount(), mouseEvent.getX(), mouseEvent.getY() });
    }
    
    public void mouseClicked(final MouseEvent mouseEvent) {
        this.caller("mouseClicked", mouseEvent);
    }
    
    public void mousePressed(final MouseEvent mouseEvent) {
        this.caller("mousePressed", mouseEvent);
    }
    
    public void mouseReleased(final MouseEvent mouseEvent) {
        this.caller("mouseReleased", mouseEvent);
    }
    
    public void mouseEntered(final MouseEvent mouseEvent) {
        this.caller("mouseEntered", mouseEvent);
    }
    
    public void mouseExited(final MouseEvent mouseEvent) {
        this.caller("mouseExited", mouseEvent);
    }
}
