package gitlet;

import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

public class DisplayPrinter {

    private final CommitRepository commitRepository;
    private final BranchRepository branchRepository;
    private final IndexRepository indexRepository;

    public DisplayPrinter(CommitRepository commitRepository, BranchRepository branchRepository, IndexRepository indexRepository) {
        this.commitRepository = commitRepository;
        this.branchRepository = branchRepository;
        this.indexRepository = indexRepository;
    }

    public StringBuilder logString(Commit commit) {
        StringBuilder logString = new StringBuilder();
        Date date = commit.getDate();
        Formatter dateFormatter = new Formatter();
//        Display standard 12-hour time format.
        String dateStr = dateFormatter.format("%ta %tb %td %tT %tz %tY",
                date, date, date, date, date, date).toString();
        logString.append("===" + "\n");
        logString.append("Date:").append(dateStr).append("\n");
        logString.append(commit.getMessage()).append("\n");
        return logString;
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
        statusString.append("=== Staged Files ===" + "\n");
        Set<String> stagedFiles = indexObj.getStagedfiles().keySet();
        for (String stagedFile: stagedFiles) {
            statusString.append(stagedFile).
                    append("\n");
        }
        statusString.append("\n");
        statusString.append("=== Removed Files ===" + "\n");
        for (String stagedFile: indexObj.getUnstagedfiles()) {
            statusString.append(stagedFile).
                    append("\n");
        }
        statusString.append("=== Modifications Not Staged For Commit ===" + "\n");
        statusString.append("=== Untracked Files ===" + "\n");
        return statusString;
    }

    public void status() {
        StringBuilder statusString = statusString();
        System.out.println(statusString);
    }

    public void log() {
        logHelper(commitRepository.getHeadCommit());
    }

    private void logHelper(Commit commit) {
        StringBuilder logString = logString(commit);
        if (commit.getParentId() == null) {
            System.out.println(logString);
            return;
        }
        System.out.println(logString);
        logHelper(commitRepository.getParentCommit(commit));
    }

    public void globalLog() {
        List<String> commitFilesList = commitRepository.getAllCommitIds();
        for (String commitId: commitFilesList) {
            Commit commit = commitRepository.getCommitByCommitId(commitId);
            StringBuilder logString = logString(commit);
            System.out.println(logString);
        }
    }
}
