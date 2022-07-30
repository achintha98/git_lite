package gitlet;

public class Command {

    private final String[] arguments;
    private final CommitFileService commitFileService;
    private final BranchService branchService;
    private final IndexFileService indexFileService;
    private final BlobFileService blobFileService;
    private final DisplayPrinter printer;


    private Command(Builder builder) {
        this.arguments = builder.arguments;
        this.commitFileService = builder.commitFileService;
        this.branchService = builder.branchService;
        this.indexFileService = builder.indexFileService;
        this.blobFileService = builder.blobFileService;
        this.printer = builder.printer;
    }

    public static class Builder {
        private  String[] arguments;
        private  CommitFileService commitFileService;
        private  BranchService branchService;
        private  IndexFileService indexFileService;
        private  BlobFileService blobFileService;
        private DisplayPrinter printer;

        public Builder argumentsBuilder(String[] arguments) {
            this.arguments = arguments;
            return this;
        }
        public Builder commitFileServiceBuilder(CommitFileService commitFileService) {
            this.commitFileService = commitFileService;
            return this;
        }
        public Builder branchServiceBuilder(BranchService branchService) {
            this.branchService = branchService;
            return this;
        }
        public Builder indexFileServiceBuilder(IndexFileService indexFileService) {
            this.indexFileService = indexFileService;
            return this;
        }
        public Builder blobFileServiceBuilder(BlobFileService blobFileService) {
            this.blobFileService = blobFileService;
            return this;
        }
        public Builder displayPrinterBuilder(DisplayPrinter printer) {
            this.printer =  printer;
            return this;
        }
        public Command build() {
            return new Command(this);
        }
    }

    public void run() {
        String firstArg = arguments[0];
        switch (firstArg) {
            case "init" -> init();
            case "add" -> add(arguments[1]);
            case "commit" -> commit(arguments[1]);
            case "rm" -> remove(arguments[1]);
            case "log" -> log();
            case "global-log" -> globalLog();
            case "find" -> find(arguments[1]);
            case "status" -> status();
            case "branch" -> branch(arguments[1]);
            case "checkout" -> {
                checkout(arguments[1]);
                checkout(arguments[1], arguments[2]);
                checkout(arguments[1], arguments[2], arguments[3]);
            }
        }
    }

    public void init() {
        commitFileService.saveCommit();
    }

    public void add(String fileName) {
        indexFileService.stageFile(fileName);
    }

    public void commit(String msg) {
        commitFileService.saveCommit(msg);
    }

    public void remove(String fileName) {
        indexFileService.remove(fileName);
    }

    public void log() {
        printer.log();
    }

    public void globalLog() {
        printer.globalLog();
    }

    public void find(String msg) {
        commitFileService.find(msg);
    }

    public  void status() {
        printer.status();
    }

    public void checkout(String branchName) {
        branchService.checkoutBranch(branchName);
    }

    public void checkout(String dash, String fileName) {
    String dashString = "--";
        if (! dash.equals(dashString)) {
            System.out.println("Invalid command");
            System.exit(0);
        }
        indexFileService.checkoutStagedFile(fileName);
    }

    public void checkout(String dash, String commitId, String fileName) {
        String dashString = "--";
        if (! dash.equals(dashString)) {
            System.out.println("Invalid command");
            System.exit(0);
        }
        commitFileService.checkoutCommittedFile(commitId, fileName);
    }

    public void branch(String branchName) {
        branchService.createBranch(branchName);
    }
}
