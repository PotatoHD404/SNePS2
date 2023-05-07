// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.ActionEvent;
import java.awt.TextField;
import java.awt.MenuItem;
import java.awt.List;
import java.awt.Button;
import java.awt.event.ActionListener;

public class JLActionListener implements ActionListener
{
    private Object handle;
    
    public static synchronized void addTo(final Button handle) {
        final JLActionListener l = new JLActionListener();
        ((Button)(l.handle = handle)).addActionListener(l);
    }
    
    public static synchronized void addTo(final List handle) {
        final JLActionListener l = new JLActionListener();
        ((List)(l.handle = handle)).addActionListener(l);
    }
    
    public static synchronized void addTo(final MenuItem handle) {
        final JLActionListener l = new JLActionListener();
        ((MenuItem)(l.handle = handle)).addActionListener(l);
    }
    
    public static synchronized void addTo(final TextField handle) {
        final JLActionListener l = new JLActionListener();
        ((TextField)(l.handle = handle)).addActionListener(l);
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        JavaLinkCommon.ltoj_anchor.callLisp("actionPerformed", this.handle, new String[] { actionEvent.paramString(), actionEvent.getActionCommand() }, new int[] { actionEvent.getModifiers() });
    }
}
