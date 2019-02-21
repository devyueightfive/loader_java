package colibri;

import java.lang.Exception;
import java.util.LinkedList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class ThreadCollector extends Thread {
    private String urlEndPoint;
    private LinkedList<String> queue;

    ThreadCollector(String urlEndPointToListen, LinkedList<String> someQueue) {
        super();
        urlEndPoint = urlEndPointToListen;
        queue = someQueue;

        /*
            To show URL
        */
        System.out.println(this.getName() + " Listening ..." + this.urlEndPoint);
    }

    public void run() {
        this.listenEndPoint(urlEndPoint);
    }

    private void listenEndPoint(String url) {
        String myName = this.getName();
        WebSocket.Listener listener = new WebSocket.Listener() {
            List<CharSequence> parts = new ArrayList<>();
            CompletableFuture<?> accumulatedMessage = new CompletableFuture<>();

            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println(myName + " CONNECTED.");
                WebSocket.Listener.super.onOpen(webSocket);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                parts.add(data);
                webSocket.request(1);
                if (last) {
                    processWholeParts(parts);
                    parts.clear();
                    accumulatedMessage.complete(null);
                    CompletionStage<?> cf = accumulatedMessage;
                    accumulatedMessage = new CompletableFuture<>();
                    return cf;
                }
                return accumulatedMessage;
            }

            void processWholeParts(List parts) {
                System.out.println(parts.toString());
            }
        };

        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), listener);

        while (true) {
            try {
                ws.join();
            } catch (Exception ex) {
                System.out.println("My error: " + ex.toString());
                System.exit(1);
            }
        }

        //TODO: putDataTo(this.queue);
    }
}
