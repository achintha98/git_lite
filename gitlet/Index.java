package gitlet;


import java.io.Serializable;
import java.util.Map;
import java.util.Set;

//* A class that represents staged area *//
public class Index implements Serializable {
    private Map<String, String> stagedfiles;
    private Set<String> unstagedfiles;

    public Index(Map<String, String> stagedfiles, Set<String> unstagedfiles) {
        this.stagedfiles = stagedfiles;
        this.unstagedfiles = unstagedfiles;
    }

    public Map<String, String> getStagedfiles() {
        return stagedfiles;
    }

    public Set<String> getUnstagedfiles() {
        return unstagedfiles;
    }
}
