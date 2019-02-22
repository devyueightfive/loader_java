package colibri;

import java.util.ArrayList;
import java.util.Vector;

public class Saver extends Thread {
    private Vector<String> queue;
    private ArrayList<String> items = new ArrayList<>();

    public Saver(String path, Vector<String> someQueue) {
        queue = someQueue;
    }

    @Override
    public void run() {
        int n;
        while (true) {
            if ((n = queue.size()) > 0) {
                for (int i = n; i > 0; i--) {
                    items.add(queue.remove(0));
                }

                save(items);
                items.clear();
                n = 0;
            } else {
                try {
                    Thread.sleep(10);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        }
    }

    private void save(ArrayList items) {
        System.out.println(items.size());
        for (var item : items) System.out.println(item.toString());
    }


}
