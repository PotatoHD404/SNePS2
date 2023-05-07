// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.event.MouseMotionAdapter;

public class JLMouseMotionAdapter extends MouseMotionAdapter
{
    public static synchronized void addTo(final Component component) {
        component.addMouseMotionListener(new JLMouseMotionAdapter());
    }
    
    private void caller(final String s, final MouseEvent mouseEvent) {
        LispCall.dispatchEvent(s, mouseEvent.getComponent(), new String[] { mouseEvent.paramString() }, new int[] { mouseEvent.getModifiers(), mouseEvent.isPopupTrigger() ? 1 : 0, mouseEvent.getClickCount(), mouseEvent.getX(), mouseEvent.getY() });
    }
    
    public void mouseDragged(final MouseEvent mouseEvent) {
        this.caller("mouseDragged", mouseEvent);
    }
    
    public void mouseMoved(final MouseEvent mouseEvent) {
        this.caller("mouseMoved", mouseEvent);
    }
}
