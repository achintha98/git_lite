package gitlet;

import java.io.File;
import java.util.Map;

public class IndexFileService {

    private final IndexRepository indexRepository;
    private final CommitRepository commitRepository;

    public IndexFileService(IndexRepository indexRepository, CommitRepository commitRepository) {
        this.indexRepository = indexRepository;
        this.commitRepository = commitRepository;
    }

    public void stageFile(String fileName) {
        Blob blob = cwdFileToBlob(fileName);
        Index indexObj = indexRepository.getIndexObj();
        indexObj.getStagedfiles().put(fileName, blob.getBlobSha1Name());
        indexRepository.saveBlob(blob.getBlobSha1Name(), blob.getByteArray());
    }

    public boolean isFileStaged(String fileName) {
        Index indexObj = indexRepository.getIndexObj();
        return indexObj.getStagedfiles().get(fileName) != null;
    }

    public void unStageFile(Index indexObj, String fileName) {
        indexObj.getUnstagedfiles().add(fileName);
    }

    public void remove(String fileName) {
        if (isFileStaged(fileName)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        Index indexObj = indexRepository.getIndexObj();
        indexObj.getStagedfiles().remove(fileName);
        if (commitRepository.isFileInHead(fileName)) {
            unStageFile(indexObj, fileName);
            indexRepository.removeFile(fileName);
        }
        indexRepository.setIndexObj(indexObj);
    }

    public Blob cwdFileToBlob(String fileName) {
        File stagedFile = indexRepository.getCWDFile(fileName);
        byte[] addedFile = Utils.readContents(stagedFile);
        String hashCode = Utils.sha1((Object) addedFile);
        return new Blob(hashCode, addedFile);
    }

    public void checkoutStagedFile(String fileName) {
        Map<String, String> stagedFiles= indexRepository.getIndexObj().getStagedfiles();
        String blobContent = indexRepository.getBlobFile(stagedFiles.get(fileName));
        if(stagedFiles.get(fileName) == null || blobContent == null) {
            System.out.println("The file does not exists");
            System.exit(0);
        }
        File fileInCWD = indexRepository.getCWDFile(fileName);
        indexRepository.writeToFileAsString(fileInCWD,blobContent);
    }

}
