import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
/*
 * SNePSGUIArc.java
 *
 * Created on September 9, 2006, 9:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Michael Kandefer
 */
public class SNePSGUIArc extends DirectedSparseEdge {
    
    public String arc_label;
    
    /** Creates a new instance of SNePSGUIArc */
    public SNePSGUIArc(SNePSGUINode from, SNePSGUINode to) {
        super(from,to);
        arc_label = "";
    }
    
    public SNePSGUIArc (String l, SNePSGUINode from, SNePSGUINode to){
        super(from,to);
        arc_label = l;
    }
    
    public String getLabel(){
        return arc_label;
    }
    
    public String toString (){
        return arc_label;
    }
}
