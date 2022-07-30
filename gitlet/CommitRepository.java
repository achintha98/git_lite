package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static gitlet.Utils.join;

public class CommitRepository extends Repository {

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    /**Head File*/
    private static final File HEAD_PATH_FILE = join(GITLET_DIR, "head");
    /** Commit directory. */
    private static final File COMMIT_DIR = join(GITLET_DIR, "commits");

    public void initCommit(String hashCode, Commit initCommit) {
        COMMIT_DIR.mkdir();
        saveSerializedCommit(hashCode, initCommit);
    }

    public String getHeadHashFilePath() {
        File headFile = Utils.join(HEAD_PATH_FILE);
        return Utils.readContentsAsString(headFile);
    }

    public String getHeadHashCode() {
        File headHashFile = Utils.join(getHeadHashFilePath());
        return Utils.readContentsAsString(headHashFile);
    }

    public Commit getHeadCommit() {
        File headCommitFile = Utils.join(COMMIT_DIR, getHeadHashCode());
        return Utils.readObject(headCommitFile, Commit.class);
    }

    public Commit getParentCommit(Commit currentCommit) {
        File commitFile = Utils.join(COMMIT_DIR, currentCommit.getParentId());
        return Utils.readObject(commitFile, Commit.class);
    }

    public Commit getCommitByCommitId(String CommitId) {
        File commitFile = Utils.join(COMMIT_DIR, CommitId);
        return Utils.readObject(commitFile, Commit.class);
    }

    public void saveSerializedCommit(String hashCode, Serializable serializable) {
        File commitFile = Utils.join(COMMIT_DIR, hashCode);
        Utils.writeObject(commitFile, serializable);
    }

    public List<String> getAllCommitIds() {
        List<String> commitFilesList = Utils.plainFilenamesIn(COMMIT_DIR);
        assert commitFilesList != null;
        return commitFilesList;
    }

    public boolean isFileInHead(String fileName) {
        return getHeadCommit().getBlobsMap().get(fileName) != null;
    }

    public void writeNewHeadHash(String hashCode) {
        String headPath = Utils.readContentsAsString(HEAD_PATH_FILE);
        File headFile = Utils.join(headPath);
        Utils.writeContents(headFile, hashCode);
    }
}
