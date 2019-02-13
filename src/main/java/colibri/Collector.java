package colibri;

import java.util.Queue;

public class Collector extends Thread {
    private String urlEndPoint;
    private Queue<String> someQueue;

    public Collector(String urlEndPointToListen, Queue<String> someQueue) {
        super();
        this.urlEndPoint = urlEndPointToListen;
    }

    public void run() {
        try {
            while (true) {
                this.listenEndPoint(urlEndPoint);
            }
        }   //try
        catch (Exception exceptionObject) {
            StringBuilder report = new StringBuilder("Listener of [").append(urlEndPoint).append("] is closed");
            System.out.println(report);
        }
    }

    private void listenEndPoint(String url) {

    }
}
