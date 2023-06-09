/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 * 
 * Created on Feb 17, 2004
 */
package edu.uci.ics.jung.visualization;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.graph.Vertex;

/**
 * This class translates mouse clicks into vertex clicks
 * 
 * @author danyelf
 */
public class MouseListenerTranslator extends MouseAdapter {

	private VisualizationViewer vv;
	private GraphMouseListener gel;

	/**
	 * @param gel
	 * @param vv
	 */
	public MouseListenerTranslator(GraphMouseListener gel, VisualizationViewer vv) {
		this.gel = gel;
		this.vv = vv;
	}
	
	/**
	 * Transform the point to the coordinate system in the
	 * VisualizationViewer, then use either PickSuuport
	 * (if available) or Layout to find a Vertex
	 * @param point
	 * @return
	 */
	private Vertex getVertex(Point2D point) {
	    // adjust for scale and offset in the VisualizationViewer
	    PickSupport pickSupport = vv.getPickSupport();
	    Vertex v = null;
	    if(pickSupport != null) {
		    Point2D p = vv.inverseViewTransform(point);
	        v = pickSupport.getVertex(p.getX(), p.getY());
	    } 
	    return v;
	}
	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	    Vertex v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphClicked(v, e );
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		Vertex v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphPressed(v, e );
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		Vertex v = getVertex(e.getPoint());
		if ( v != null ) {
			gel.graphReleased(v, e );
		}
	}
}
