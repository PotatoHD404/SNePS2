/*
 * Created on Nov 7, 2004
 *
 * Copyright (c) 2004, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
package edu.uci.ics.jung.utils;

import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.jung.graph.ArchetypeVertex;

/**
 * Uses a <code>HashMap</code> to store mappings between vertices.
 * Places no constraints on mappings (mappings need not be 1-1,
 * onto, etc.).
 * 
 * @author Joshua O'Madadhain
 */
public class HashSettableVertexMapper implements SettableVertexMapper
{
    protected Map m;
    
    public HashSettableVertexMapper()
    {
        m = new HashMap();
    }
    
    /**
     * @see SettableVertexMapper#map(ArchetypeVertex, ArchetypeVertex)
     */
    public void map(ArchetypeVertex v1, ArchetypeVertex v2)
    {
        m.put(v1, v2);
    }

    /**
     * @see VertexMapper#getMappedVertex(ArchetypeVertex)
     */
    public ArchetypeVertex getMappedVertex(ArchetypeVertex v)
    {
        return (ArchetypeVertex)m.get(v);
    }

}
