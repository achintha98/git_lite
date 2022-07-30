package gitlet;

import java.util.Date;
import java.util.List;

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
        Commit newCommit = new Commit.Builder().messageBuilder(msg)
                .dateBuilder(new Date())
                .parentBuilder(commitRepository.getHeadCommit())
                .parentIdBuilder(commitRepository.getHeadHashCode())
                .blobsMapBuilder(indexRepository.getIndexObj().getStagedfiles()).build();
        String hashCode = Utils.sha1((Object) Utils.serialize(newCommit));
        commitRepository.saveSerializedCommit(hashCode, newCommit);
        commitRepository.writeNewHeadHash(hashCode);
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

    public String checkoutCommittedFile(String commitId, String fileName) {
        Commit foundedCommit = commitRepository.getCommitByCommitId(commitId);
        String fileHashCode = foundedCommit.getBlobsMap().get(fileName);
        if(fileHashCode == null) {
            System.out.println("The file does not exists in the given commit");
            System.exit(0);
        }
        return fileHashCode;
    }

    public void find(String message) {
        List<String> commitFilesList = commitRepository.getAllCommitIds();
        for (String commitId: commitFilesList) {
            Commit commit = commitRepository.getCommitByCommitId(commitId);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitId);
            }
        }
    }
}
