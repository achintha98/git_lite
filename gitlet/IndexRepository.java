package gitlet;

import java.io.File;
import java.util.TreeMap;
import java.util.TreeSet;

import static gitlet.Utils.join;

public class IndexRepository extends Repository{

    public Index getIndexObj() {
        return Utils.readObject(INDEX_FILE, Index.class);
    }

    public void setIndexObj(Index indexObj) {
        Utils.writeObject(INDEX_FILE, indexObj);
    }

    public void initIndexTree() {
        BLOBS_DIR.mkdirs();
        Index indexObj = new Index(new TreeMap<>(), new TreeSet<>());
        Utils.writeObject(INDEX_FILE, indexObj);
    }

    public String getBlobFile(String hashCode) {
        if (fileExists(BLOBS_DIR.getName(), hashCode)) {
            File blobFile = Utils.join(BLOBS_DIR.getName(), hashCode);
            return Utils.readContentsAsString(blobFile);
        }
        return null;
    }

    public void saveBlob(String hashCode,  byte[] blob) {
        File blobFile = Utils.join(BLOBS_DIR, hashCode);
        Utils.writeContents(blobFile, blob);
    }
}

