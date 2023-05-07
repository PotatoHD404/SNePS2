// 
// Decompiled by Procyon v0.5.36
// 

package com.franz.jlinker;

import java.awt.event.ItemEvent;
import java.awt.List;
import java.awt.ItemSelectable;
import java.awt.Choice;
import java.awt.CheckboxMenuItem;
import java.awt.Checkbox;
import java.awt.event.ItemListener;

public class JLItemListener implements ItemListener
{
    private Object handle;
    
    public static synchronized void addTo(final Checkbox handle) {
        final JLItemListener l = new JLItemListener();
        ((Checkbox)(l.handle = handle)).addItemListener(l);
    }
    
    public static synchronized void addTo(final CheckboxMenuItem handle) {
        final JLItemListener l = new JLItemListener();
        ((CheckboxMenuItem)(l.handle = handle)).addItemListener(l);
    }
    
    public static synchronized void addTo(final Choice handle) {
        final JLItemListener l = new JLItemListener();
        ((Choice)(l.handle = handle)).addItemListener(l);
    }
    
    public static synchronized void addTo(final ItemSelectable handle) {
        final JLItemListener jlItemListener = new JLItemListener();
        ((ItemSelectable)(jlItemListener.handle = handle)).addItemListener(jlItemListener);
    }
    
    public static synchronized void addTo(final List handle) {
        final JLItemListener l = new JLItemListener();
        ((List)(l.handle = handle)).addItemListener(l);
    }
    
    public void itemStateChanged(final ItemEvent itemEvent) {
        LispCall.dispatchEvent("itemStateChanged", this.handle, new String[] { itemEvent.paramString(), itemEvent.getItem().toString() }, new int[] { (itemEvent.getStateChange() == 1) ? 1 : 0 });
    }
}
