/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 * Created on Sep 13, 2004
 */
package edu.uci.ics.jung.graph.predicates;

import edu.uci.ics.jung.graph.ArchetypeVertex;

import java.io.Serializable;

/**
 * Returns true iff the vertex contains the specified user data key.
 *  
 * @author Joshua O'Madadhain
 */
public class ContainsUserDataKeyVertexPredicate extends VertexPredicate implements Serializable {
    protected Object key;
    
    public ContainsUserDataKeyVertexPredicate(Object key)
    {
        this.key = key;
    }
    
    /**
     * @see VertexPredicate#evaluateVertex(ArchetypeVertex)
     */
    public boolean evaluateVertex(ArchetypeVertex v)
    {
        return v.containsUserDatumKey(key);
    }
}
