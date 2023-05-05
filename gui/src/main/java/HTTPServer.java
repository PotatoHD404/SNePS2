import edu.uci.ics.jung.graph.impl.SparseGraph;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.google.gson.Gson;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class HTTPServer {
    public static SNePSGUIShow gui = new SNePSGUIShow(false);
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        gui.setVisible(true);
        Javalin app = Javalin.create().start(7000);
        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                app.close();
                System.exit(0);
            }
        });
        SparseGraph a = new SparseGraph();
        System.out.println(gson.toJson(a));

        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/api/transfer/object", HTTPServer::receiveSerializedObject);

    }

    private static void receiveSerializedObject(Context ctx) {
//        try {
        byte[] serializedData = ctx.bodyAsBytes();
        // use gson to convert the byte array to SparseGraph
        SparseGraph deserializedObject = gson.fromJson(new String(serializedData), SparseGraph.class);
//            SparseGraph deserializedObject = SparseGraph.deserializeObject(serializedData);
        System.out.println("Deserialized object:");
        System.out.println(serializedData.length);
        deserializedObject.getVertices().stream().forEach(v -> System.out.println(v));
        gui.displayNetwork1(deserializedObject);
        ctx.status(200).result("Object received successfully");
        System.out.println("Object received successfully");
//        } catch (IOException | ClassNotFoundException e) {
//            ctx.status(400).result("Failed to deserialize object");
//        }
    }

    // The deserializeObject method shown in the previous answer
}
