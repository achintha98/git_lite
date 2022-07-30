package gitlet;

import java.io.File;
import java.util.List;

import static gitlet.Utils.join;

public class BranchRepository extends Repository {

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    /**Head File*/
    private static final File HEAD_PATH_FILE = join(GITLET_DIR, "head");
    /**branch Files*/
    private static final File BRANCH_HEAD = join(GITLET_DIR,"refs", "heads");

    public void initBranch(String initCommitHashCode) {
        BRANCH_HEAD.mkdirs();
        File masterBranch = join(BRANCH_HEAD + "\\" + "master");
        Utils.writeContents(HEAD_PATH_FILE, masterBranch.getPath());
        Utils.writeContents(masterBranch, initCommitHashCode);
    }

    public void checkoutBranch(String branchPath) {
        Utils.writeContents(HEAD_PATH_FILE, BRANCH_HEAD + "\\" + branchPath);
    }

    public List<String> branchesList() {
        return Utils.plainFilenamesIn(BRANCH_HEAD);
    }

    public String activeBranchName() {
        return getActiveBranchHeadFile().getName();
    }

    public File getActiveBranchHeadFile() {
        String headPath = Utils.readContentsAsString(HEAD_PATH_FILE);
        return Utils.join(headPath);
    }

    public void createBranch(String branchName) {
        File branchFile = Utils.join(BRANCH_HEAD, branchName);
        String activeHeadHashCode = Utils.readContentsAsString(getActiveBranchHeadFile());
        Utils.writeContents(branchFile, activeHeadHashCode);
    }

    public boolean branchDirExists(String branchName) {
        File branchDir = Utils.join(BRANCH_HEAD, branchName);
        return branchDir.exists();
    }
}
