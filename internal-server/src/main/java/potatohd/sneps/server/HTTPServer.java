package potatohd.sneps.server;

import io.javalin.Javalin;
import io.javalin.http.Context;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPServer {


    private static CountDownLatch objectReceivedLatch;
    private static final AtomicReference<byte[]> receivedBytes = new AtomicReference<>();
    private static final Semaphore snepslogSemaphore = new Semaphore(1);

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);
        objectReceivedLatch = new CountDownLatch(1);


        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/snepslog", HTTPServer::handleRequestSnepslog);
        app.post("/api/transfer/object", HTTPServer::receiveSerializedObject);
    }

    private static String getSnepslogHelper(byte[] data) {
        // get string lines from data
        String[] lines = new String(data).split("\n");
        if (lines.length == 0) {
            return "";
        }
        // map lines, prefixing every line with
        // "snepslog:tell \""
        // and suffixing every line with
        // "\n"
        String[] prefixedLines = Arrays.stream(lines).filter(line -> !Objects.equals(line, "")).map(line -> "(snepslog:tell \"" + line + "\")\n").toArray(String[]::new);
        // join the prefixed lines into a single string
        String prefixedLinesString = String.join("", prefixedLines);

        return "(defpackage :snepslog-helper\n" +
                "  (:use :common-lisp :excl)\n" +
                "  (:export :run-demo))\n" +
                "\n" +
                "(in-package :snepslog-helper)\n" +
                "(defun run-demo ()\n" +
                prefixedLinesString +
                "\n" +
                "    (sneps:show))\n";
    }

    private static void handleRequestSnepslog(Context ctx) throws InterruptedException, IOException {
        // Acquire the semaphore to handle only one /snepslog call at a time
        System.out.println("Acquiring snepslog semaphore");
        snepslogSemaphore.acquire();
        System.out.println("Acquired snepslog semaphore");
        String helper = getSnepslogHelper(ctx.bodyAsBytes());

        // write helper to file
        System.out.println("Writing helper to file");
        File file = new File("/app/sneps/snepslog-helper.lisp");
        file.createNewFile();
        System.out.println("Created file");
        java.io.FileWriter writer = new java.io.FileWriter(file);
        writer.write(helper);
        writer.close();
        System.out.println("Wrote helper to file");
        // run command that starts snepslog
        System.out.println("Running snepslog");
//        alisp -q  -L /app/sneps/load-sneps.lisp

        objectReceivedLatch = new CountDownLatch(1);


        String[] command = {"alisp", "-q", "-L", "/app/sneps/load-sneps.lisp"};

        // Create a ProcessBuilder instance with the command
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            // Start the process
            Process process = processBuilder.start();

            // Read the output of the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish and check the exit code
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Initialize the CountDownLatch and wait for the object to be received
        System.out.println("Waiting for object to be received");
        objectReceivedLatch.await();


        // Return the received bytes in the response
        byte[] responseBytes = receivedBytes.get();
        System.out.println("Received bytes: " + responseBytes.length);
        System.out.println("Sending response");
        ctx.status(200).result(responseBytes);

        // Release the semaphore to allow handling the next /snepslog call
        System.out.println("Releasing snepslog semaphore");
        snepslogSemaphore.release();
        System.out.println("Completed snepslog");
    }

    private static void receiveSerializedObject(Context ctx) {
        System.out.println("Received object");
        byte[] serializedData = ctx.bodyAsBytes();
        System.out.println("Serialized data received");

        // Set the received bytes and count down the latch
        receivedBytes.set(serializedData);
        objectReceivedLatch.countDown();

        ctx.status(200).result("Object received successfully");
        System.out.println("Object received successfully");
    }
}