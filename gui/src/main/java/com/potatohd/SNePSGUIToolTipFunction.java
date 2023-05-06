package com.potatohd;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ToolTipFunction;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
/*
 * com.potatohd.SNePSGUIToolTipFunction.java
 *
 * Created on October 25, 2006, 1:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Michael Kandefer
 */
public class SNePSGUIToolTipFunction implements ToolTipFunction {
    
    /** Creates a new instance of com.potatohd.SNePSGUIToolTipFunction */
    public SNePSGUIToolTipFunction() {
    }
    
     /**
     * @param v the Vertex
     * @return toString on the passed Vertex
     */
    public String getToolTipText(Vertex v) {
        if(v instanceof SNePSGUINode){
            SNePSGUINode node = (SNePSGUINode)v;
            return node.detailedPrint();
        }
        return v.toString();
    }
    
    /**
     * @param e the Edge
     * @return toString on the passed Edge
     */
    public String getToolTipText(Edge e) {
        return e.toString();
    }
    
    public String getToolTipText(MouseEvent e) {
        return ((JComponent)e.getSource()).getToolTipText();
    }
}
