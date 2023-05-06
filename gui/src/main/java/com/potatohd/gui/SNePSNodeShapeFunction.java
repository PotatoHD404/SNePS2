package com.potatohd.gui;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction; 
import java.awt.Polygon; 
import java.awt.Rectangle; 
import java.awt.Shape; 
import java.awt.geom.Ellipse2D; 
import java.awt.geom.RoundRectangle2D; 
/* 
 * com.potatohd.SNePSNodeShapeFunction.java
 * 
 * Created on September 27, 2006, 10:02 PM 
 * 
 * To change this template, choose Tools | Template Manager 
 * and open the template in the editor. 
 */ 
 
/** 
 * 
 * @author Michael Kandefer 
 */ 
public class SNePSNodeShapeFunction implements VertexShapeFunction { 
     
    /** Creates a new instance of com.potatohd.SNePSNodeShapeFunction */
    public SNePSNodeShapeFunction() { 
    } 
 
    public Shape getShape(Vertex v) { 
        SNePSGUINode sgn; 
        int [] x = {-15, 0, 15, 0}; 
        int [] y = {0, -15, 0, 15};  
        if(v instanceof SNePSGUINode){ 
            sgn = (SNePSGUINode)v; 
            switch (sgn.getNodeType()){ 
	    case SNePSGUINode.BASE:
		String na = sgn.getNodeAccess();
		return new RoundRectangle2D.Double(-6*na.length(),-15,na.length()*12,30, 10 ,10); 
	    case SNePSGUINode.MOL: return new Ellipse2D.Double(-15,-15,30,30); 
	    case SNePSGUINode.PAT: return new Rectangle(-15,-15,30,30); 
	    case SNePSGUINode.VAR: return new Polygon(x,y,4); 
            } 
        } 
        return new Ellipse2D.Double(-50,-50,100,100); 
    } 
    
}            
