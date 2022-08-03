package gitlet;

import java.io.File;
import java.util.TreeMap;
import java.util.TreeSet;

public class IndexRepository extends Repository{

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));

    public void initIndexTree() {
        BLOBS_DIR.mkdirs();
        Index indexObj = new Index(new TreeMap<>(), new TreeMap<>(),new TreeSet<String>());
        Utils.writeObject(INDEX_FILE, indexObj);
    }

    public Index getIndexObj() {
        return Utils.readObject(INDEX_FILE, Index.class);
    }

    public void setIndexObj(Index indexObj) {
        Utils.writeObject(INDEX_FILE, indexObj);
    }

    public String getBlobFile(String hashCode) {
        if (fileExists(BLOBS_DIR, hashCode)) {
            File blobFile = Utils.join(BLOBS_DIR, hashCode);
            return Utils.readContentsAsString(blobFile);
        }
        return null;
    }

    public void saveBlob(String hashCode,  byte[] blob) {
        File blobFile = Utils.join(BLOBS_DIR, hashCode);
        Utils.writeContents(blobFile, blob);
    }

    public void restoreFileInCWD(String fileName, String fileContent) {
        File cwdFile = Utils.join(CWD, fileName);
        Utils.writeContents(cwdFile, fileContent);
    }
}

