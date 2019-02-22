package colibri;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import colibri.Settings.Market;


public class App {

    public static void main(String[] args) {
        /*
            Get <settings> & make preconditions
        */
        var settings = new Settings();
        Database.makePreConditions(settings);

        var queue = new Vector<String>();
        //TODO: start thread that monitor <queue> for save data

        Saver saver = new Saver("", queue);
        saver.start();

        /*
            Collect Threads to the array.
         */
        Market[] markets = settings.getMarkets();
        List<Receiver> collectors = new ArrayList<>();
        /*
            for each market start thread to listen tradeURL end point
        */
        for (Market market : markets) {
            for (String tp : market.tradePairs) {
                String tradeURL = Market.getTradeURL(market.tradeURL_Template, tp);
                Receiver t = new Receiver(tradeURL, queue);
                collectors.add(t);
                t.start();
            }
        }
        /*
            End of program.
         */
        /*
           Waiting the threads (WebSocket listeners) endings.
        */
        for (Receiver t : collectors) {
            try {
                t.join();
            } catch (Exception ex) {
                System.out.println(ex.toString());
                System.exit(1);
            }
        }
        try {
            saver.join();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            System.exit(1);
        }
    }

}
