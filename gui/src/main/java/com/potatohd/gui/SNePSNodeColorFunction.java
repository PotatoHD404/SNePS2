package com.potatohd.gui;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import java.awt.Color;
import java.awt.Paint;
/*
 * com.potatohd.SNePSNodeColorFunction.java
 *
 * Created on September 27, 2006, 10:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Michael Kandefer
 */
public class SNePSNodeColorFunction implements VertexPaintFunction {
    
    /** Creates a new instance of com.potatohd.SNePSNodeColorFunction */
    public SNePSNodeColorFunction(){
        
    }

    public Paint getFillPaint(Vertex v) {
        SNePSGUINode sgn;
        if(v instanceof SNePSGUINode){
            sgn = (SNePSGUINode)v;
            switch (sgn.getNodeType()){
                case SNePSGUINode.BASE: return Color.cyan;
                case SNePSGUINode.MOL: return Color.red;
                case SNePSGUINode.PAT: return Color.YELLOW;
                case SNePSGUINode.VAR: return Color.green;
            }
        }
        return Color.RED;
    }

    public Paint getDrawPaint(Vertex v) {
        return Color.black;
    }
    
}
