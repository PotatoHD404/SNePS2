/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
/*
 * Created on Jul 7, 2004
 *
 */
package edu.uci.ics.jung.graph.decorators;

import edu.uci.ics.jung.graph.ArchetypeEdge;

/**
 * Returns a constructor-specified constant value for each edge.
 *  
 * @author Joshua O'Madadhain
 */
public class ConstantEdgeValue implements NumberEdgeValue
{
    protected Number value;

    public ConstantEdgeValue(double value)
    {
        this.value = new Double(value);
    }
    
    public ConstantEdgeValue(Number value) 
    {
        this.value = value;
    }

    /**
     * @see NumberEdgeValue#getNumber(ArchetypeEdge)
     */
    public Number getNumber(ArchetypeEdge arg0)
    {
        return value;
    }

    /**
     * @see NumberEdgeValue#setNumber(ArchetypeEdge, Number)
     */
    public void setNumber(ArchetypeEdge arg0, Number arg1)
    {
        throw new UnsupportedOperationException();
    }

}
