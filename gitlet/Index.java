package gitlet;


import java.io.Serializable;
import java.util.Map;
import java.util.Set;

//* A class that represents staged area *//
public class Index implements Serializable {
    private Map<String, String> stagedfiles;
    private Map<String, String> unstagedfiles;
    private Set<String> workingTree;

    public Index(Map<String, String> stagedfiles, Map<String, String> unstagedfiles, Set<String> workingTree) {
        this.stagedfiles = stagedfiles;
        this.unstagedfiles = unstagedfiles;
        this.workingTree = workingTree;
    }

    public Set<String> getWorkingTree() {
        return workingTree;
    }

    public void setWorkingTree(Set<String> workingTree) {
        this.workingTree = workingTree;
    }

    public Map<String, String> getStagedfiles() {
        return stagedfiles;
    }

    public Map<String, String> getUnstagedfiles() {
        return unstagedfiles;
    }
}
