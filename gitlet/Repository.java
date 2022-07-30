package gitlet;

import java.io.File;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Achintha Kalunayaka
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    /** Blobs directory. */
    public static final File BLOBS_DIR = join(GITLET_DIR, "blobs");
    /** Commit directory. */
    private static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    /**Index File*/
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     */

    public boolean gitletDirExists() {
        return GITLET_DIR.exists() ;
    }

    public File getCOMMIT_DIR() {
        return COMMIT_DIR;
    }

    public boolean removeFile(String fileName) {
        return Utils.restrictedDelete(fileName);
    }

    public void writeToFileAsString(File file, String contents) {
        Utils.writeContents(file, contents);
    }
    public boolean fileExists(String directory, String fileName) {
        File file = Utils.join(directory, fileName);
        return file.exists();
    }

    public File getCWDFile(String fileName) {
        return Utils.join( fileName);
    }
}
