package colibri;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
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

    String getPathToDatabase() {
        return settingsTemplate.pathToDatabase;
    }

    Market[] getMarkets() {
        return this.settingsTemplate.markets;
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
            System.out.println("Settings [getSettingsString:FileReader] error : " + ex.toString());
            System.exit(1);
        }
        return read.toString();
    }

    private String getPathToSettings() {
        String relativePathToSettings = "./settings/settings.json";
        String pathToProject = Paths.get(".").toAbsolutePath().normalize().toString();
        try {
            String path = Paths.get(pathToProject, relativePathToSettings)
                    .toAbsolutePath().normalize().toString();
            if (new File(path).isFile()) {
                return path;
            } else {
                System.out.println("There is no setting file " + relativePathToSettings + ".");
                System.exit(1);
            }
        } catch (InvalidPathException ex) {
            System.out.println("<SettingsTemplate> invalid path : " + ex.toString());
            System.exit(1);
        }
        return "";
    }
}
