package colibri;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import colibri.Settings.Market;


public class App {

    public static void main(String[] args) {
        /*
            Get <settings>
        */
        var settings = new Settings();
        Database.makePreConditions(settings);


        LinkedList<String> queue = new LinkedList<>();
        //TODO: start thread that monitor <queue> for save data


        Market[] markets = settings.getMarkets();

        /*
            for each market print <trade pairs> to listen
        */
        for (Market market : markets) {
            for (String tp : market.tradePairs) {
                System.out.println(market.name + ":" + tp);
            }
        }

        /*
            Collect Threads to the array.
         */
        List<ThreadCollector> collectors = new ArrayList<>();
        /*
            for each market start thread to listen tradeURL end point
        */
        for (Market market : markets) {
            for (String tp : market.tradePairs) {
                String tradeURL = Market.getTradeURL(market.tradeURL_Template, tp);
                ThreadCollector t = new ThreadCollector(tradeURL, queue);
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
        for (ThreadCollector t : collectors) {
            try {
                t.join();
            } catch (Exception ex) {
                System.out.println(ex.toString());
                System.exit(0);
            }
        }
    }

}
