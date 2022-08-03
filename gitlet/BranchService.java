package gitlet;

public class BranchService {
    private final BranchRepository repository;

    public BranchService(BranchRepository repository) {
        this.repository = repository;
    }

    public void createBranch(String branchName) {
        if (repository.branchDirExists(branchName)) {
            System.out.println("The branch already exists");
            System.exit(0);
        }
        repository.createBranch(branchName);
    }

    public void checkoutBranch(String branchName) {
        if (repository.activeBranchName().equals(branchName)) {
            System.out.println("You are already on the same branch");
            System.exit(0);
        }
        if (repository.branchDirExists(branchName)) {
            repository.checkoutBranch(branchName);
            return;
        }
        System.out.println("The branch does not exists");
        System.exit(0);
    }
}
