package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.os.Environment;

import java.io.File;

/**
 * Created by Zikey on 19/02/2018.
 */

public class FileAddressGenerator {

    private File file;
    private String fileName_Extension;
    private String folderName;



    public String getAddress(String folderName, String fileName_Extension) {
        try {
            File path = null;
            path = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/");
            if (!path.isDirectory())
                return null;

            file = new File(path.getAbsolutePath() + File.separator + fileName_Extension);
            if (!file.isFile())
                return null;

            return file.getAbsolutePath();

        } catch (Exception ex) {
            LogWrapper.loge("FileAddressGenerator_getAddress_Exception: ", ex);
        }

        return null;

    }


    public FileAddressGenerator setFileName_Extension(String name_extension) {

        this.fileName_Extension = name_extension;

        return this;
    }


    public FileAddressGenerator setFolderName(String folderName) {

        this.folderName = folderName;
        return this;

    }

    public FileAddressGenerator generateDirectory() {


        File storagePath = new File(Environment.getExternalStorageDirectory() + "/" + folderName + "/");
        storagePath.mkdirs();

        file = new File(storagePath.getAbsolutePath() + File.separator + fileName_Extension);

        return this;

    }

    public File getFile() {
        return file;
    }
}
