package colibri;

import hdf.hdf5lib.H5;
import hdf.hdf5lib.exceptions.HDF5LibraryException;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static hdf.hdf5lib.HDF5Constants.*;

public class Database {

    public static void makePreConditions(Settings settings)
            throws ArrayIndexOutOfBoundsException, InvalidPathException {

        String pathToDatabase = settings.getPathToDatabase();
        /*
            Check <pathToDatabase>
        */
        try {
            pathToDatabase = Paths
                    .get(pathToDatabase)
                    .toAbsolutePath()
                    .normalize()
                    .toString();
        } catch (InvalidPathException ex) {
            throw new InvalidPathException(pathToDatabase,
                    "Settings.pathToDatabase is not valid.");
        }

        /*
            Is path a normal File?
         */
        boolean isFile = new File(pathToDatabase).isFile();
        long fileId = -1;
        if (!isFile) {
            /*
                Create a new .h5 file using default properties.
            */
            try {
                /*
                    https://portal.hdfgroup.org/display/H5/H5F_CREATE
                */
                fileId = H5.H5Fcreate(
                        pathToDatabase, H5F_ACC_TRUNC,
                        H5P_DEFAULT, H5P_DEFAULT);
            } catch (Exception e) {
                System.out.println("\nH5Fcreate: " + e.toString());
                e.printStackTrace();
                System.exit(1);
            }

        }

        /*
            Close the file.
        */
        try {
            if (fileId >= 0)
                H5.H5Fclose(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
            Path is valid.
            Open/Create .h5 file (pathToDatabase).
         */

        long groupId = -1;
        try {
            fileId = H5.H5Fopen(pathToDatabase, H5F_ACC_RDWR, H5P_DEFAULT);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("\nH5Fopen: " + ex.toString());
            System.exit(1);
        }

        /*
            Open/Create groups in .h5 file.
         */
        try {
            Settings.Market[] markets = settings.getMarkets();
            for (Settings.Market market : markets) {
                String mName = market.name.replace(".", "");
            /*
                for each market create base group eith name "/"+{@code mName}"
             */
//                System.out.println(mName + " :");

                Database.makePreConditionsWithGroup(mName, fileId);

                for (String tp : market.tradePairs) {
                    String gName = "/" + mName + "/" + tp.replace("_", "");
//                    System.out.println(gName + " :");
                    Database.makePreConditionsWithGroup(gName, fileId);
                    /*
                        TODO: create Tables
                     */
                }
            }
            /*
                save data
             */
            H5.H5Fflush(fileId, H5F_SCOPE_LOCAL);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Database [H5Gopen]: " + ex.toString());
            System.exit(1);


        } finally {
            /*
                 Close the file.
             */
            try {
                if (fileId >= 0)
                    H5.H5Fclose(fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void makePreConditionsWithGroup(String group, long fileId) {
        try {
            long groupId = -1;
            try {
                groupId = H5.H5Gopen(fileId, group, H5P_DEFAULT);
            } catch (HDF5LibraryException ex) {
                groupId = H5.H5Gcreate(fileId, group, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
                System.out.println("Group " + group + " is created.");
            }
            if (groupId >= 0)
                H5.H5Gclose(groupId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("makePreConditionsWithGroup :" + e.toString());
            System.exit(1);
        }

    }

}
