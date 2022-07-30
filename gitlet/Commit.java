package gitlet;
import java.io.Serializable;
import java.util.*;
/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Achintha Kalunayaka
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    /** The message of this Commit. */
    private final String message;
    private final Date date;
    private final transient Commit parent;
    private final String parentId;
    private final Map<String, String> blobsMap;

    // TODO : look at factory method
    private Commit(Builder commitBuilder) {
        this.message = commitBuilder.message;
        this.date = commitBuilder.date;
        this.parent = commitBuilder.parent;
        this.parentId = commitBuilder.parentId;
        this.blobsMap = commitBuilder.blobsMap;
    }
    public static class Builder {
        private String message;
        private Date date;
        private transient Commit parent;
        private String parentId;
        private Map<String, String> blobsMap;

        public Builder messageBuilder(String message) {
            this.message = message;
            return this;
        }
        public Builder dateBuilder(Date date) {
            this.date = date;
            return this;
        }
        public Builder parentBuilder(Commit parent) {
            this.parent = parent;
            return this;
        }
        public Builder parentIdBuilder(String parentId) {
            this.parentId = parentId;
            return this;
        }
        public Builder blobsMapBuilder(Map<String, String> blobsMap) {
            this.blobsMap = blobsMap;
            return this;
        }
        public Commit build() {
            return new Commit(this);
        }
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public Commit getParent() {
        return parent;
    }

    public String getParentId() {
        return parentId;
    }

    public Map<String, String> getBlobsMap() {
        return blobsMap;
    }
}
