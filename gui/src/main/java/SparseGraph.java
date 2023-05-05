import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.AbstractSparseGraph;

import java.io.*;
import java.util.Collection;

public class SparseGraph extends AbstractSparseGraph implements Serializable {

    private static final long serialVersionUID = 1L;

    public SparseGraph() {
    }

    public SparseGraph(Collection edge_constraints) {
        this.getEdgeConstraints().addAll(edge_constraints);
    }

//    public MySparseGraph(SparseGraph dsg) {
//        this(dsg.getEdgeConstraints());
//    }

    public SparseGraph(SparseGraph dsg) {

//        this.setDirected(dsg.isDirected());
        dsg.getVertices().forEach(el -> this.addVertex((Vertex) el));
//        dsg.getEdgeConstraints().forEach(el -> this.addVertex((Vertex) el));
        dsg.getEdges().forEach(el -> this.addEdge((Edge) el));
//        dsg.getVertices().forEach(el -> this.addVertex((Vertex) el));
//        this.getEdgeConstraints().addAll(dsg.getEdgeConstraints());
//        this.getVertices().addAll(dsg.getVertices());
//        this.getEdges().addAll(dsg.getEdges());
//        this
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
