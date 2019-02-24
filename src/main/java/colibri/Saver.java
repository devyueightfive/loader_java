package colibri;

import java.util.ArrayList;
import java.util.Queue;


public class Saver extends Thread {
    private final int PAUSE_TIME = 5000;

    private String toDatabase;
    private Queue<String> buffer;
    private ArrayList<String> items = new ArrayList<>();

    Saver(String path, Queue<String> buffer) {
        toDatabase = path;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                /*
                    For non-empty buffer save data to database.
                 */
                for (int i = 0; i < buffer.size(); i++) {
                    items.add(buffer.poll());
                }
                if (!items.isEmpty()) {
                    save(items, toDatabase); //
                    items.clear();
                }
                /*
                    Every 1 sec (not overload hard disk) save to Database;
                 */
                Thread.sleep(PAUSE_TIME);
            } catch (InterruptedException intEx) {
                System.out.println(this.getName() + " got interruption. Closing.");
                System.exit(0);
            } catch (Exception ex) {
                /*Keep saving*/
                ex.printStackTrace();
                System.out.println(ex.toString() + "\n Keep to save data ...");
            }
        }

    }

    private void save(ArrayList<String> data, String path) {
        for (var item : items) System.out.println(item);
        System.out.println("Saved " + data.size() + " lines.");
    }


}
