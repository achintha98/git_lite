package gitlet;

import java.util.*;

public class CommitFileService {
    private final CommitRepository commitRepository;
    private final IndexRepository indexRepository;
    private final BranchRepository branchRepository;

    public CommitFileService(CommitRepository commitRepository, IndexRepository indexRepository, BranchRepository branchRepository) {
        this.commitRepository = commitRepository;
        this.indexRepository = indexRepository;
        this.branchRepository = branchRepository;
    }

    public void saveCommit(String msg) {
        Index indexObj = indexRepository.getIndexObj();
        Map<String ,String> stagedFiles = indexObj.getStagedfiles();
        if (indexObj.getWorkingTree().isEmpty()) {
            System.out.println("Nothing to commit");
            System.exit(0);
        }
        Commit newCommit = new Commit.Builder().messageBuilder(msg)
                .dateBuilder(new Date())
                .parentBuilder(commitRepository.getHeadCommit())
                .parentIdBuilder(commitRepository.getHeadHashCode())
                .blobsMapBuilder(stagedFiles).build();
        String hashCode = Utils.sha1((Object) Utils.serialize(newCommit));
        commitRepository.saveSerializedCommit(hashCode, newCommit);
        commitRepository.writeNewHeadHash(hashCode);
        indexObj.setWorkingTree(new TreeSet<>());
        indexRepository.setIndexObj(indexObj);
    }

    public void saveCommit() {
        if (commitRepository.gitletDirExists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory");
            System.exit(0);
        }
        indexRepository.initIndexTree();
        Commit newCommit = new Commit.Builder().messageBuilder("initial commit")
                .dateBuilder(new Date(0))
                .parentBuilder(null).
                blobsMapBuilder(indexRepository.getIndexObj().getStagedfiles())
                .build();
        String hashCode = Utils.sha1((Object) Utils.serialize(newCommit));
        branchRepository.initBranch(hashCode);
        commitRepository.initCommit(hashCode, newCommit);
    }

    public void checkoutCommittedFile(String commitId, String fileName) {
        Commit commit = commitRepository.getCommitByCommitId(commitId);
        String fileHashCode = commit.getBlobsMap().get(fileName);
        if(fileHashCode == null) {
            System.out.println("The file does not exists in the given commit");
            System.exit(0);
        }
        String blobContent = indexRepository.getBlobFile(fileHashCode);
        indexRepository.restoreFileInCWD(fileName, blobContent);
    }

    public void findByCommitMessage(String message) {
        List<String> commitFilesList = commitRepository.getAllCommitIds();
        for (String commitId: commitFilesList) {
            Commit commit = commitRepository.getCommitByCommitId(commitId);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitId);
            }
        }
    }
    public void log() {
        logHelper(commitRepository.getHeadCommit(), commitRepository.getHeadHashCode());
    }

    public void globalLog() {
        List<String> commitFilesList = commitRepository.getAllCommitIds();
        for (String commitId: commitFilesList) {
            Commit commit = commitRepository.getCommitByCommitId(commitId);
            StringBuilder logString = logString(commit, commitId);
            System.out.println(logString);
        }
    }

    private void logHelper(Commit commit, String commitHashCode) {
        StringBuilder logString = logString(commit, commitHashCode);
        if (commit.getParentId() == null) {
            System.out.println(logString);
            return;
        }
        System.out.println(logString);
        logHelper(commitRepository.getParentCommit(commit), commitRepository.getHeadHashCode());
    }

    private StringBuilder logString(Commit commit, String commitHash) {
        StringBuilder logString = new StringBuilder();
        Date date = commit.getDate();
        Formatter dateFormatter = new Formatter();
//        Display standard 12-hour time format.
        String dateStr = dateFormatter.format("%ta %tb %td %tT %tz %tY",
                date, date, date, date, date, date).toString();
        logString.append("===" + "\n");
        logString.append(commitHash).append("\n");
        logString.append("Date:").append(dateStr).append("\n");
        logString.append(commit.getMessage()).append("\n");
        return logString;
    }
}
