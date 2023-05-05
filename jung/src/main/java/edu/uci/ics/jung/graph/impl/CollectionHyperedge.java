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

/**
 * An implementation of <code>Hyperedge</code> that stores its
 * collection of incident vertices internally as a <code>Collection</code>.
 * 
 * @author Joshua O'Madadhain
 */
public abstract class CollectionHyperedge extends AbstractHyperedge implements Serializable {
    protected Collection vertices;
    
    /**
     * @see AbstractHyperedge#getVertices_internal()
     */
    protected Collection getVertices_internal()
    {
        return vertices;
    }

}
