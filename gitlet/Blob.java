package gitlet;

import java.io.Serializable;

/** Class that stores files as byte streams */
public class Blob implements Serializable {
    private final String blobSha1Name;
    private final byte[] byteArray;

    public Blob(String blobSha1Name, byte[] byteArray) {
        this.blobSha1Name = blobSha1Name;
        this.byteArray = byteArray;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public String getBlobSha1Name() {
        return blobSha1Name;
    }

}
