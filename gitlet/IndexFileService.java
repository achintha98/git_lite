package gitlet;

import java.io.File;
import java.util.*;

public class IndexFileService {

    private final IndexRepository indexRepository;
    private final CommitRepository commitRepository;
    BranchRepository branchRepository;

    public IndexFileService(IndexRepository indexRepository, CommitRepository commitRepository, BranchRepository branchRepository) {
        this.indexRepository = indexRepository;
        this.commitRepository = commitRepository;
        this.branchRepository = branchRepository;
    }

    public void stageFile(String fileName) {
        Blob blob = cwdFileToBlob(fileName);
        boolean isFileStaged = isFileStaged(fileName, blob.getBlobSha1Name());
        if (isFileStaged) {
            System.out.println("File is already added.");
            System.exit(0);
        }
        Index indexObj = indexRepository.getIndexObj();
        Map<String, String> stagedFiles= indexObj.getStagedfiles();
        Set<String> workingTree = indexObj.getWorkingTree();
        if (isFileInHead(fileName, blob.getBlobSha1Name())) {
            if (workingTree.remove(fileName)) {
                indexRepository.setIndexObj(indexObj);
                return;
            }
        }
        stagedFiles.put(fileName, blob.getBlobSha1Name());
        indexObj.getWorkingTree().add(fileName);
        indexRepository.setIndexObj(indexObj);
        indexRepository.saveBlob(blob.getBlobSha1Name(), blob.getByteArray());
    }

    public void unStageFile(String fileName) {
        Index indexObj = indexRepository.getIndexObj();
        String hashCode = indexObj.getStagedfiles().remove(fileName);
        indexObj.getUnstagedfiles().put(fileName, hashCode);
        indexRepository.setIndexObj(indexObj);
    }

    public void removeStagedFile(String fileName) {
        Blob blob = cwdFileToBlob(fileName);
        boolean isFileInHead = isFileInHead(fileName, blob.getBlobSha1Name());
        boolean isFileStaged  = isFileStaged(fileName, blob.getBlobSha1Name());
        if ((!isFileStaged)) {
            System.out.println("No reason to remove the file");
            System.exit(0);
        }
        Index indexObj = indexRepository.getIndexObj();
        indexObj.getStagedfiles().remove(fileName);
        if (isFileInHead) {
            unStageFile(fileName);
            indexRepository.removeFile(fileName);
        }
        indexRepository.setIndexObj(indexObj);
    }

    public void checkoutHeadFile(String fileName) {
        Map<String, String> filesInCommit = commitRepository.getHeadCommit().getBlobsMap();
        String fileHashCode = filesInCommit.get(fileName);
        if(fileHashCode == null) {
            System.out.println("The file does not exists");
            System.exit(0);
        }
        String blobContent = indexRepository.getBlobFile(fileHashCode);
        indexRepository.restoreFileInCWD(fileName, blobContent);
    }

    public StringBuilder statusString() {
        StringBuilder statusString = new StringBuilder();
        statusString.append("=== Branches ===" + "\n");
        List<String> branchesList = branchRepository.branchesList();
        Index indexObj = indexRepository.getIndexObj();
        for (String branchName : branchesList) {
            if (branchRepository.activeBranchName().equals(branchName)) {
                branchName  = "*" + branchName;
            }
            statusString.append(branchName).append("\n");
        }
        StringBuilder stagedFilesString = new StringBuilder();
        stagedFilesString.append("=== Staged Files ===" + "\n");
        StringBuilder modifiedFilesString = new StringBuilder();
        modifiedFilesString.append("=== Modifications Not Staged For Commit ===" + "\n");
        StringBuilder untrackedFiles = new StringBuilder();
        List<String> cwdFilesList = indexRepository.filesInCWD();
        Set<String> workingTree = indexObj.getWorkingTree();
        for (String fileName: cwdFilesList) {
            Blob blobOfFile = cwdFileToBlob(fileName);
            String StagedFileHashCode = indexObj.getStagedfiles().get(fileName);
            if (StagedFileHashCode != null) {
                if (!(StagedFileHashCode.equals(blobOfFile.getBlobSha1Name()))) {
                    modifiedFilesString.append(fileName).append("\n");
                    continue;
                }
                if (workingTree.contains(fileName)) {
                    stagedFilesString.append(fileName).append("\n");
                    continue;
                }
            }
            untrackedFiles.append(fileName).append("\n");
        }
        statusString.append(stagedFilesString);
        statusString.append("=== Removed Files ===" + "\n");
        for (String stagedFile: indexObj.getUnstagedfiles().keySet()) {
            statusString.append(stagedFile).
                    append("\n");
        }
        statusString.append(modifiedFilesString);
        statusString.append("=== Untracked Files ===" + "\n");
        statusString.append(untrackedFiles);
        return statusString;
    }

    public void status() {
        StringBuilder statusString = statusString();
        System.out.println(statusString);
    }

    private boolean isFileStaged(String fileName, String hashCode) {
        Index indexObj = indexRepository.getIndexObj();
        String existingFileHashCode = indexObj.getStagedfiles().get(fileName);
        if (existingFileHashCode == null) {
            return false;
        }
        return existingFileHashCode.equals(hashCode);
    }

    private boolean isFileInHead(String fileName, String hashCode) {
        String headCommitHashCode = commitRepository.getHeadCommit().getBlobsMap().get(fileName);
        if (headCommitHashCode == null) {
            return false;
        }
        return headCommitHashCode.equals(hashCode);
    }

    private Blob cwdFileToBlob(String fileName) {
        File stagedFile = indexRepository.getCWDFile(fileName);
        byte[] addedFile = null;
        try {
            addedFile = Utils.readContents(stagedFile);
        }
        catch (IllegalArgumentException e) {
            System.out.println("No file is found.");
            System.exit(0);
        }
        String hashCode = Utils.sha1((Object) addedFile);
        return new Blob(hashCode, addedFile);
    }
}
