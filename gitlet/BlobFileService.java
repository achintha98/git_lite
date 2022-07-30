package gitlet;

import java.io.File;

public class BlobFileService {

    private final BlobRepository repository;

    public BlobFileService(BlobRepository repository) {
        this.repository = repository;
    }
}
