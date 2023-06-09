/*
* Copyright (c) 2003, the JUNG Project and the Regents of the University 
* of California
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
*/
package edu.uci.ics.jung.graph.filters;

import edu.uci.ics.jung.graph.Graph;

import java.io.Serializable;

/**
 * Contains an audit trail of a graph filtering step. Maintains the name
 * of the filters used, a history of previous filters, and links to
 * the original graph that was used to generate the graph.
 * 
 * @author danyelf
 */
public class GraphAssemblyRecord implements Serializable {

	/**
	 * The key that identifes this <tt>GraphAssemblyRecord</tt> in the 
	 * {@link edu.uci.ics.jung.utils.UserData UserData}.
	 */
	public static final Object FILTER_GRAPH_KEY = "edu.uci.ics.jung.filter.UnassembledGraph_FilterData";

	private String name;
	private Graph originalGraph;

	/**
	 * Creates a <tt>GraphAssemblyRecord<tt>, recording the original 
	 * <tt>Graph</tt> and the name of the filter that produced it.
	 * <p>
	 * This should be done automatically during the <tt>assemble()</tt> 
	 * call to the <tt>UnassembledGraph</tt>.
	 * @param graph
	 */
	GraphAssemblyRecord(UnassembledGraph graph) {
		this.originalGraph = graph.getOriginalGraph();	
		this.name = graph.getFilterName();
	}
	
	/**
	 * Returns the original graph that created this subset. Warning:
	 * because this stays around, the original Graph will NOT be
	 * garbage collected! Be sure to delete this user data if you
	 * want to allow the original graph's memory to be reallocated.
	 * @return a pointer to the original Graph.
	 */
	public Graph getOriginalGraph() {
		return originalGraph;
	}
	
	/**
	 * Returns the name of the filter that generated this Graph. If a series
	 * of filters were used (as in the second example at 
	 * <tt>{@link EfficientFilter EfficientFilter}</tt>,
	 * then they are all returned, collated together.
	 * @return the name of the filter
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the GraphAssemblyRecord, if it exists, of the original
	 * graph. If the original graph was generated by a different filter 
	 * (as in the first example at 
	 * <tt>{@link EfficientFilter EfficientFilter}</tt>,
	 * then there is a "previous" graph--and thus a previous <tt>GraphAssemblyRecord</tt>.
	 */
	public GraphAssemblyRecord getPreviousAssemblyRecord() {
		GraphAssemblyRecord gar = (GraphAssemblyRecord) originalGraph.getUserDatum( FILTER_GRAPH_KEY );
		return gar;
	}
	

	/**
	 * Returns the collated name of the sequence of filters. Names are 
	 * returned in reverse order, separated by ":::".
	 * If the original graph was generated by a different filter (as in the
	 * first example at 
	 * <tt>{@link EfficientFilter EfficientFilter}</tt>,
	 * then there is a "previous" graph--and thus a previous <tt>GraphAssemblyRecord</tt>.
	 * This follows the chain back and returns the collated set of names.
	 */
	public String getNameExtended() {
		if (getPreviousAssemblyRecord() != null) {
			return (name + ":::" + getPreviousAssemblyRecord().getNameExtended() );
		} else {
			return name;
		}
	}

	/**
	 * Returns the first original graph
	 * If the original graph was generated by a different filter (as in the
	 * first example at 
	 * <tt>{@link EfficientFilter EfficientFilter}</tt>,
	 * then there is a "previous" graph--and thus a previous <tt>GraphAssemblyRecord</tt>.
	 * This follows the chain back and returns the first Graph.
	 */
	public Graph getOriginalExtended() {
		if (getPreviousAssemblyRecord() != null) {
			return (getPreviousAssemblyRecord().getOriginalExtended() );
		} else {
			return originalGraph;
		}
	}

	/**
	 * Returns the <tt>GraphAssemblyRecord</tt> for a particular graph. Expects the
	 * data to be stored in the user data.
	 * @param g		A <tt>Graph</tt> that may be a filtered version of some other graph.
	 * @return		The <tt>GraphAssemblyRecord</tt> associated with the graph, or
	 * <tt>null</tt>, if there isn't one.
	 * 
	 * @see #FILTER_GRAPH_KEY
	 * @see Graph#getUserDatum
	 */
	public static GraphAssemblyRecord getAssemblyRecord( Graph g) {
		GraphAssemblyRecord gar = (GraphAssemblyRecord) g.getUserDatum( FILTER_GRAPH_KEY );
		return gar;		
	}

}
