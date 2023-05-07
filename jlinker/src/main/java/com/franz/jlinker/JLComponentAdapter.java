// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.ComponentEvent;
import java.awt.Component;
import java.awt.event.ComponentAdapter;

public class JLComponentAdapter extends ComponentAdapter
{
    public static synchronized void addTo(final Component component) {
        component.addComponentListener(new JLComponentAdapter());
    }
    
    private void caller(final String s, final ComponentEvent componentEvent) {
        LispCall.dispatchEvent(s, componentEvent.getComponent(), new String[] { componentEvent.paramString() }, new int[0]);
    }
    
    public void componentResized(final ComponentEvent componentEvent) {
        this.caller("componentResized", componentEvent);
    }
    
    public void componentMoved(final ComponentEvent componentEvent) {
        this.caller("componentMoved", componentEvent);
    }
    
    public void componentShown(final ComponentEvent componentEvent) {
        this.caller("componentShown", componentEvent);
    }
    
    public void componentHidden(final ComponentEvent componentEvent) {
        this.caller("componentHidden", componentEvent);
    }
}
