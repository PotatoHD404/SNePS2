/*
 * Created on Apr 28, 2005
 *
 * Copyright (c) 2005, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.graph.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.jung.graph.Hyperedge;

/**
 * 
 * @author Joshua O'Madadhain
 */
public abstract class CollectionHypervertex extends AbstractHypervertex implements Serializable {
    protected Collection incident_edges;
    
    /**
     * @see AbstractArchetypeVertex#getNeighbors_internal()
     */
    protected Collection getNeighbors_internal()
    {
        Set neighbors = new HashSet();
        for (Iterator iter = incident_edges.iterator(); iter.hasNext(); )
        {
            Hyperedge e = (Hyperedge)iter.next();
            neighbors.addAll(e.getIncidentVertices());
        }
        return neighbors;
    }

    /**
     * @see AbstractArchetypeVertex#getEdges_internal()
     */
    protected Collection getEdges_internal()
    {
        return incident_edges;
    }
    
}
