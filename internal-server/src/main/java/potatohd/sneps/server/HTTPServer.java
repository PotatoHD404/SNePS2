package potatohd.sneps.server;

import io.javalin.Javalin;
import io.javalin.http.Context;


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

    private static void handleRequestSnepslog(Context ctx) throws InterruptedException {
        // Acquire the semaphore to handle only one /snepslog call at a time
        snepslogSemaphore.acquire();

        // Start the process here
        // ...

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