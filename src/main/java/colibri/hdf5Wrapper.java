package colibri;

import java.io.*;
import java.lang.Exception;

import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.hdf5.*;

public class hdf5Wrapper {
    final static private String FILENAME = "nolast.h5";
    final static private String GROUPNAME = "binancecom";

    public static void run() {
        long fileId = -1;
        long groupId = -1;

        // Create a new file using default properties.
        try {
            /*

            Parameters:
                const char *name    IN: Name of the file to access.
                unsigned flags 	    IN: File access flags. Allowable values are:

                    H5F_ACC_TRUNC
                        Truncate file, if it already exists, erasing all data previously stored in the file.
                    H5F_ACC_EXCL
                        Fail if file already exists.

                H5F_ACC_TRUNC and H5F_ACC_EXCL are mutually exclusive; use exactly one.
                An additional flag, H5F_ACC_DEBUG, prints debug information.

                hid_t fcpl_id 	IN: File creation property list identifier, used when modifying default file meta-data.
                    Use H5P_DEFAULT to specify default file creation properties.

                hid_t fapl_id 	IN: File access property list identifier.
                    If parallel file access is desired, this is a collective call according to the communicator
                    stored in the fapl_id.
                    Use H5P_DEFAULT for default file access properties.

             Returns:
                Returns a file identifier if successful; otherwise returns a negative value.

            */

            String fileName;
            int flags;
            long create_plist;
            long access_plist;
            fileId = H5Fcreate(
                    fileName = FILENAME,
                    flags = H5F_ACC_TRUNC,
                    create_plist = H5P_DEFAULT,
                    access_plist = H5P_DEFAULT);
        } catch (Exception e) {
            System.out.println("Boom: "+e.toString());
            e.printStackTrace();
        }


        // Create a group in the file.
        try {
            /*

            Parameters:
                hid_t loc_id 	IN: File or group identifier
                const char *name     	IN: Absolute or relative name of the link to the new group
                hid_t lcpl_id 	IN: Link creation property list identifier
                hid_t gcpl_id 	IN: Group creation property list identifier
                hid_t gapl_id 	IN: Group access property list identifier
                (No group access properties have been implemented at this time; use H5P_DEFAULT.)

            Returns:
                Returns a group identifier if successful; otherwise returns a negative value.

            */
            long loc_id;
            String name;
            long lcpl_id;
            long gcpl_id;
            long gapi_id;
            groupId = H5Gcreate2(
                    loc_id = fileId,
                    name = "/" + GROUPNAME,
                    lcpl_id = H5P_DEFAULT,
                    gcpl_id = H5P_DEFAULT,
                    gapi_id = H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Close the group. The handle "group" can no longer be used.
        try {
            if (groupId >= 0)
                H5Gclose(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Re-open the group, obtaining a new handle.
        try {
            long loc_id;
            String name;
            long gapi_id;
            if (fileId >= 0)
                groupId = H5Gopen2(
                        loc_id = fileId,
                        name = "/" + GROUPNAME,
                        gapi_id = H5P_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the group.
        try {
            if (groupId >= 0)
                H5Gclose(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the file.
        try {
            if (fileId >= 0)
                H5Fclose(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
