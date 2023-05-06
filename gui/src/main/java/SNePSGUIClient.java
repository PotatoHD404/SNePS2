import edu.uci.ics.jung.graph.impl.SparseGraph;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SNePSGUIClient {
    public static SNePSGUIShow gui = new SNePSGUIShow(false);

    public static void main(String[] args) {
        gui.setVisible(true);
        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        try {
            String url = "http://localhost:7000/snepslog";
            String filePath = "main.snlog";

            // Read file content
            Path path = Paths.get(filePath);
            byte[] fileContent = Files.readAllBytes(path);

            // Create HttpURLConnection
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain");

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(fileContent);
                os.flush();
            }

            // Get response
            int responseCode = connection.getResponseCode();

            // Check if the response has a successful status
            if (responseCode == 200) {
                // Deserialize response into SparseGraph
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                byte[] responseBytes = output.toByteArray();
                SparseGraph deserializedObject = SparseGraph.deserializeObject(responseBytes);
                gui.displayNetwork1(deserializedObject);
                System.out.println("Deserialized object: " + deserializedObject);
            } else {
                System.err.println("Request failed with status code: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}