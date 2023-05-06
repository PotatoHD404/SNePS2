package com.potatohd.gui; /**
 *
 * @author Michael Kandefer
 */

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.UserDataContainer;
import java.util.Iterator;
import java.util.Set;
public class SNePSGUINode extends DirectedSparseVertex {
     
    private String node_access;
    private String node_description;    
    private String node_snepslog;
    private int node_type;
    
    /** Creates a new instance of com.potatohd.SNePSGUINode */
    public SNePSGUINode() {
        super();
        node_access = "";
        node_description = "";
        node_snepslog = "";  
    }
    
    public SNePSGUINode (String na, String desc, String snepslog, int nt) {
        super();
        node_access = na;
        node_description = desc;
        node_snepslog = snepslog;
        node_type = nt;
    }
    
    public String getNodeAccess() {
        return node_access;
    }
    
    public String getNodeDescription(){
        return node_description;
    }
    
    public String getNodeSnepslog(){
        return node_snepslog;
    }
    
    public int getNodeType(){
        return node_type;
    }
    
    public String detailedPrint(){
        return "Node ID: " + node_access + " --- FOL Representation: " + node_snepslog + " --- Description: " + node_description;
    }
    
    public String toString(){
        return node_access;
    }
    
    public static final int MOL  = 0;
    public static final int VAR  = 1;
    public static final int PAT  = 2;
    public static final int BASE = 3;
    
}
