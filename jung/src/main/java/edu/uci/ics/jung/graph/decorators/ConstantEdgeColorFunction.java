/*
 * Created on Jul 16, 2004
 *
 * Copyright (c) 2004, the JUNG Project and the Regents of the University
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.graph.decorators;

import java.awt.Color;
import java.io.Serializable;

import edu.uci.ics.jung.graph.Edge;

/**
 * Returns the same <code>Color</code> for all specified edges.
 *
 * @author Joshua O'Madadhain
 * @deprecated Replaced by ConstantEdgePaintFunction.
 */
public class ConstantEdgeColorFunction implements EdgeColorFunction, Serializable {
    private Color color;

    public ConstantEdgeColorFunction(Color c) {
        this.color = c;
    }

    /*
     * @deprecated Should use getPaint
     */
    public Color getEdgeColor(Edge e) {
        return color;
    }
}