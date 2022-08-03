package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Achintha Kalunayaka
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        if (firstArg == null) {
            System.out.println("No command found in gitlet");
            System.exit(0);
        }

        BranchRepository branchRepository = new BranchRepository();
        IndexRepository indexRepository = new IndexRepository();
        CommitRepository commitRepository = new CommitRepository();

        BranchService branchService = new BranchService(branchRepository);
        IndexFileService indexFileService = new IndexFileService(indexRepository, commitRepository, branchRepository);
        CommitFileService commitFileService = new CommitFileService(commitRepository, indexRepository, branchRepository);
        DisplayPrinter displayPrinter = new DisplayPrinter(commitRepository, branchRepository, indexRepository);

        Command command = new Command.Builder().argumentsBuilder(args).commitFileServiceBuilder(commitFileService)
                .branchServiceBuilder(branchService).
                indexFileServiceBuilder(indexFileService).displayPrinterBuilder(displayPrinter).build();
        command.run();
    }
}
