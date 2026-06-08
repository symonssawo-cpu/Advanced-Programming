import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INITIALIZING BANK ACCOUNT ===");
        BankAccount account = new BankAccount(1000.00);
        Calendar today = Calendar.getInstance();
        System.out.println("Initial Account Balance: $" + account.getBalance());

        System.out.println("\n=== TEST 1: DEPOSIT TRANSACTION ===");
        DepositTransaction deposit = new DepositTransaction(500.00, today);
        try {
            deposit.apply(account);
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        System.out.println("\n[Testing Deposit Reversal]");
        deposit.reverse(); 

        System.out.println("\n=== TEST 2: STANDARD WITHDRAWAL & REVERSAL ===");
        WithdrawalTransaction withdrawal1 = new WithdrawalTransaction(200.00, today);
        try {
            withdrawal1.apply(account);
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n[Testing Withdrawal Reversal]");
        withdrawal1.reverse();
        
        System.out.println("\n[Testing Double Reversal]");
        withdrawal1.reverse();

        System.out.println("\n=== TEST 3: EXCEPTION HANDLING (OVERDRAFT) ===");
        WithdrawalTransaction massiveWithdrawal = new WithdrawalTransaction(50000.00, today);
        try {
            System.out.println("Attempting standard withdrawal of $50000...");
            massiveWithdrawal.apply(account);
        } catch (InsufficientFundsException e) {
            System.out.println("CAUGHT EXCEPTION: " + e.getMessage());
        }

        System.out.println("\n=== TEST 4: PARTIAL WITHDRAWAL (OVERLOADED APPLY) ===");
        WithdrawalTransaction partialWithdrawal = new WithdrawalTransaction(2000.00, today);
        partialWithdrawal.apply(account, true);

        System.out.println("=== TEST 5: POLYMORPHISM AND UPCASTING ===");
        DepositTransaction polyDeposit = new DepositTransaction(300.00, today);
        BaseTransaction baseRef = (BaseTransaction) polyDeposit;
        
        try {
            System.out.println("Applying transaction using BaseTransaction reference...");
            baseRef.apply(account); 
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n=== END OF TESTING ===");
        System.out.println("Final Account Balance: $" + account.getBalance());
    }
}