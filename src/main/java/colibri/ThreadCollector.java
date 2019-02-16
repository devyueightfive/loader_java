package colibri;

import java.lang.Exception;
import java.util.LinkedList;
import org.bytedeco.javacpp.*;
import static org.bytedeco.javacpp.hdf5.*;

public class ThreadCollector extends Thread {
    private String urlEndPoint;
    private LinkedList<String> queue;

    ThreadCollector(String urlEndPointToListen, LinkedList<String> someQueue) {
        super();
        this.urlEndPoint = urlEndPointToListen;
        this.queue = someQueue;
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
        //TODO: putDataTo(this.queue);
    }
}
