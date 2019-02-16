/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package colibri;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


public class App {
    public static void main(String[] args) {
        //Initialize application with <settings> (in constructor)
        App application = new App();

        String pathToDatabase = application.getPathToDatabase();
        System.out.format("Path to database is : %s\n", pathToDatabase);
        if (pathToDatabase == null) System.exit(-1);

        LinkedList<String> queue = new LinkedList<>();
        //get settings from'settings.json'
        //TODO : from settings get path to database to save market data
        //TODO: start thread that monitor <queue>


        Market[] markets = application.getMarkets();
        //for each market start thread to listen tradeURL end point
        for (Market market : markets) {
            for (String tp : market.tradePairs) {
                System.out.println(market.name + ":" + tp);
            }
        }


//        ArrayList<ThreadCollector> collectors = new ArrayList<>();
//        //get market properties from settings
//        Market[] markets = application.getMarkets();
        //for each market start thread to listen tradeURL end point
//        for (Market market : markets) {
//            for (String tp : market.getTradePairs()) {
//                ThreadCollector t = new ThreadCollector(market.getTradeURL(tp), queue);
//                collectors.add(t);
//                t.start();
//            }
//        }
//        for (ThreadCollector t : collectors) {
//            try {
//                t.join();
//            } catch (Exception ex) {
//                System.out.println(ex);
//            }
//        }
    }

    private Settings settings;


    private App() {
        //Get <settings> as String
        String settingsString = getSettingsString();
        //<settings> initialization
        try {// using gson from google
            Gson gson = new Gson();
            this.settings = gson.fromJson(settingsString, Settings.class);
        } catch (JsonSyntaxException ex) {
            System.out.println("<Settings> invalid Json syntax : " + ex.toString());
            System.exit(-1);
        }
    }

    private String getPathToSettings() {
        String relativePathToSettings = "./settings/settings.json";
        String pathToProject = Paths.get(".").toAbsolutePath().normalize().toString();
        try {
            return Paths.get(pathToProject, relativePathToSettings).toAbsolutePath().normalize().toString();
        } catch (InvalidPathException ex) {
            System.out.println("<Settings> invalid path : " + ex.toString());
            System.exit(-1);
        }
        return "";//not reachable
    }


    //Static class to parse <settings.json>
    private static class Settings {
        String pathToDatabase = "";
        String defaultPathToDatabase = "";
        Market[] markets = null;
    }

    //Static class to parse <settings.json>
    private static class Market {
        String name = "";
        String[] tradePairs = null;
        String tradeURL = "";
    }


    private Market[] getMarkets() {
        return this.settings.markets;
    }

    private String getPathToDatabase() {
        String result = null;
        String currentPath = "";
        String defaultPath = "";
        //Check correct path to database
        try {
            currentPath = Paths.get(this.settings.pathToDatabase).toAbsolutePath().normalize().toString();
        } catch (InvalidPathException ex) {
            System.out.format("<Database path> is incorrect: %s\n", ex.toString());
        }
        try {
            defaultPath = Paths.get(this.settings.defaultPathToDatabase).toAbsolutePath().normalize().toString();
        } catch (Exception ex) {
            System.out.format("<Default database path> is incorrect: %s\n", ex.toString());

        }

        //Check does database path be a file.
        result = new File(currentPath).isFile() ? currentPath : (new File(defaultPath).isFile() ? defaultPath : null);
        try {
            if (result == null) {
                throw new IOException("Database path is not defined.");
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return result;
    }


    private String getSettingsString() {
        //Get path to <settings>
        String pathToSettings = getPathToSettings();
        //Get <settings> from the file
        //Read file char by char using FileReader
        int c;
        StringBuilder read = new StringBuilder();
        try (FileReader fr = new FileReader(pathToSettings)) {
            while ((c = fr.read()) != -1) {
                read.append((char) c);
            }
        } catch (Exception ex) {
            System.out.println("FileReader error : " + ex.toString());
            System.exit(-1);
        }
        return read.toString();
    }


}
