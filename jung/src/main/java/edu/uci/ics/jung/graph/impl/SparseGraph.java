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
package edu.uci.ics.jung.graph.impl;

import java.io.*;
import java.util.Collection;


/**
 * An implementation of <code>AbstractSparseGraph</code> that, by default, imposes no
 * constraints on the edges and vertices that may be added to it.  As a result,
 * this class supports mixed-mode graphs (graphs with both directed and 
 * undirected edges) and graphs with parallel edges.
 * <code>SparseVertex</code> is an appropriate vertex type for this graph.
 * 
 * @see SparseVertex
 * @author Joshua O'Madadhain
 */
public class SparseGraph extends AbstractSparseGraph implements Serializable {

//    private static final long serialVersionUID = 1L;

    public SparseGraph() {
    }

    public SparseGraph(Collection edge_constraints) {
        this.getEdgeConstraints().addAll(edge_constraints);
    }


    public byte[] serializeObject() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(this);
            return baos.toByteArray();
        }
    }

    public static SparseGraph deserializeObject(byte[] serializedData) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (SparseGraph) ois.readObject();
        }
    }
}
