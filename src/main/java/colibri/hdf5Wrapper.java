//https://bitbucket.hdfgroup.org/pages/HDFFV/hdf5doc/master/browse/html/javadoc/overview-summary.html


package colibri;

import java.lang.Exception;

import hdf.hdf5lib.H5;

import static hdf.hdf5lib.HDF5Constants.H5F_ACC_TRUNC;
import static hdf.hdf5lib.HDF5Constants.H5P_DEFAULT;


public class hdf5Wrapper {


    final static private String FILENAME = "nolast.h5";
    final static private String GROUPNAME = "binancecom";

    public static void run() {
        long fileId = -1;
        long groupId = -1;

        // Create a new file using default properties.
        try {
            //  https://portal.hdfgrou

            //  p.org/display/H5/H5F_CREATE
            fileId = H5.H5Fcreate(
                    FILENAME,
                    H5F_ACC_TRUNC,
                    H5P_DEFAULT,
                    H5P_DEFAULT);
        } catch (Exception e) {
            System.out.println("Boom: " + e.toString());
            e.printStackTrace();
        }


        // Create a group in the file.
        try {
            // https://portal.hdfgroup.org/display/H5/H5G_CREATE2
            // H5G_CREATE2(loc_id, name, lcpl_id, gcpl_id, gapl_id)
            groupId = H5.H5Gcreate(
                    fileId,
                    "/" + GROUPNAME,
                    H5P_DEFAULT,
                    H5P_DEFAULT,
                    H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Close the group. The handle "group" can no longer be used.
        try {
            if (groupId >= 0)
                H5.H5Gclose(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Re-open the group, obtaining a new handle.
        try {
            if (fileId >= 0)
                // https://portal.hdfgroup.org/display/H5/H5G_OPEN2
                // H5G_OPEN2(loc_id, name, gapl_id)
                groupId = H5.H5Gopen(
                        fileId,
                        "/" + GROUPNAME,
                        H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the group.
        try {
            if (groupId >= 0)
                H5.H5Gclose(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the file.
        try {
            if (fileId >= 0)
                H5.H5Fclose(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
