package potatohd.sneps.server;

import io.javalin.Javalin;
import io.javalin.http.Context;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

public class HTTPServer {


    private static CountDownLatch objectReceivedLatch;
    private static final AtomicReference<byte[]> receivedBytes = new AtomicReference<>();
    private static final Semaphore snepslogSemaphore = new Semaphore(1);

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);



        app.get("/", ctx -> ctx.result("Hello World"));
        app.post("/snepslog", HTTPServer::handleRequestSnepslog);
        app.post("/api/transfer/object", HTTPServer::receiveSerializedObject);
    }

    private static String getSnepslogHelper(byte[] data) {
        // get string lines from data
        String[] lines = new String(data).split("\n");
        // map lines, prefixing every line with
        // "snepslog:tell \""
        // and suffixing every line with
        // "\n"
        String[] prefixedLines = Arrays.stream(lines).map(line -> "(snepslog:tell \"" + line + "\")\n").toArray(String[]::new);
        // join the prefixed lines into a single string
        String prefixedLinesString = String.join("", prefixedLines);

        return "(defpackage :snepslog-helper\n" +
                "  (:use :common-lisp :excl)\n" +
                "  (:export :run-demo))\n" +
                "\n" +
                "(in-package :snepslog-helper)\n" +
                "\n" +
                prefixedLinesString +
                "\n" +
                "    (sneps:show))\n" +
                "\n" +
                "(defun run-demo ()\n" +
                "  (run-snepslog-demo))";
    }

    private static void handleRequestSnepslog(Context ctx) throws InterruptedException, IOException {
        // Acquire the semaphore to handle only one /snepslog call at a time
        snepslogSemaphore.acquire();
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
//        Sneps-2.7.0 -- -config-file sneps_config.lisp
        ProcessBuilder pb = new ProcessBuilder("/app/Sneps-2.7.0", "--", "-config-file", "/app/sneps_config.lisp");
        pb.redirectErrorStream(true);
        Process p = pb.start();
        p.waitFor();
        System.out.println("Snepslog finished");

        // Initialize the CountDownLatch and wait for the object to be received
        objectReceivedLatch = new CountDownLatch(1);
        objectReceivedLatch.await();

        // Return the received bytes in the response
        byte[] responseBytes = receivedBytes.get();
        if (responseBytes != null) {
            ctx.status(200).result(responseBytes);
        } else {
            ctx.status(400).result("Failed to receive object");
        }

        // Release the semaphore to allow handling the next /snepslog call
        snepslogSemaphore.release();
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