/*
* Copyright (c) 2003, the JUNG Project and the Regents of the University 
* of California
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
* 
* Created on Mar 5, 2004
*/
package edu.uci.ics.jung.graph.predicates;

import org.apache.commons.collections.Predicate;

import edu.uci.ics.jung.graph.ArchetypeGraph;

import java.io.Serializable;

/**
 * 
 * @author Joshua O'Madadhain
 */
public abstract class GraphPredicate implements Predicate, Serializable
{
    /**
     * @see org.apache.commons.collections.Predicate#evaluate(Object)
     */
    public final boolean evaluate(Object arg0)
    {
        // TODO Auto-generated method stub
        return evaluateGraph((ArchetypeGraph)arg0);
    }

    public abstract boolean evaluateGraph(ArchetypeGraph g);
}
