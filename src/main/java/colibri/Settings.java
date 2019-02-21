package colibri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

class Settings {
    private SettingsTemplate settingsTemplate;

    /*
        Constructor
     */
    Settings() {
        //Get <settingsTemplate> as String
        String settingsAsString = getSettingsString();
        //<settingsTemplate> initialization
        try {// using gson from google
            Gson gson = new Gson();
            settingsTemplate = gson.fromJson(settingsAsString, SettingsTemplate.class);
        } catch (JsonSyntaxException ex) {
            System.out.println("<SettingsTemplate> invalid Json syntax : " + ex.toString());
            System.exit(1);
        }
    }

    //Static class to parse <settingsTemplate.json>
    static class SettingsTemplate {
        String pathToDatabase = "";
        String defaultPathToDatabase = "";
        Market[] markets = null;
    }

    //Static class to parse <settingsTemplate.json>
    static class Market {
        String name = "";
        String[] tradePairs = null;
        String tradeURL_Template = "";

        static String getTradeURL(String URL_Template, String tp) {
            return URL_Template.replace("<symbol>", tp.replace("_", ""));
        }
    }


    Market[] getMarkets() {
        return this.settingsTemplate.markets;
    }

    String getPathToDatabase() {
        String result;
        String currentPath = "";
        String defaultPath = "";
        //Check correct path to database
        try {
            currentPath = Paths.get(this.settingsTemplate.pathToDatabase).toAbsolutePath().normalize().toString();
        } catch (InvalidPathException ex) {
            System.out.format("<Database path> is incorrect: %s\n", ex.toString());
        }
        try {
            defaultPath = Paths.get(this.settingsTemplate.defaultPathToDatabase).toAbsolutePath().normalize().toString();
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
        //Get path to <settingsTemplate>
        String pathToSettings = getPathToSettings();
        //Get <settingsTemplate> from the file
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

    private String getPathToSettings() {
        String relativePathToSettings = "./settingsTemplate/settingsTemplate.json";
        String pathToProject = Paths.get(".").toAbsolutePath().normalize().toString();
        try {
            return Paths.get(pathToProject, relativePathToSettings).toAbsolutePath().normalize().toString();
        } catch (InvalidPathException ex) {
            System.out.println("<SettingsTemplate> invalid path : " + ex.toString());
            System.exit(-1);
        }
        return "";//not reachable
    }
}
