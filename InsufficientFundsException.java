import java.util.Calendar;
import java.util.UUID;

// 1. The Custom Exception Class
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// 2. Updated BankAccount (Throws exception)
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    // Modified to throw the custom exception
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > this.balance) {
            throw new InsufficientFundsException("Withdrawal of $" + amount + " failed. Available balance: $" + this.balance);
        }
        this.balance -= amount;
    }

    public double getBalance() {
        return this.balance;
    }
}

// 3. The Interface (Updated signatures to allow throws)
public interface TransactionInterface {
    double getAmount();
    Calendar getDate();
    String getTransactionID();
}

// 4. Base Transaction
public class BaseTransaction implements TransactionInterface {
    protected double amount;
    protected Calendar date;
    protected String transactionID;

    public BaseTransaction(double amount, Calendar date) {
        this.amount = amount;
        this.date = date;
        this.transactionID = UUID.randomUUID().toString(); 
    }

    @Override
    public double getAmount() { return this.amount; }
    @Override
    public Calendar getDate() { return this.date; }
    @Override
    public String getTransactionID() { return this.transactionID; }

    public void apply(BankAccount ba) throws InsufficientFundsException {
        System.out.println("Generic base transaction logged.");
    }
    
    public boolean reverse() { return false; }
}

// 5. Updated WithdrawalTransaction (Try-Catch-Finally and Overloading)
public class WithdrawalTransaction extends BaseTransaction {
    private BankAccount accountAppliedTo;
    private boolean isReversed;
    private double shortfallAmount; 

    public WithdrawalTransaction(double amount, Calendar date) {
        super(amount, date);
        this.accountAppliedTo = null;
        this.isReversed = false;
        this.shortfallAmount = 0.0;
    }

    // Standard apply method with 'throws' keyword
    @Override
    public void apply(BankAccount ba) throws InsufficientFundsException {
        ba.withdraw(this.getAmount()); 
        this.accountAppliedTo = ba;
        System.out.println("Withdrawal of $" + this.getAmount() + " successfully applied.");
    }

    // Overloaded apply method implementing try...catch...finally
    public void apply(BankAccount ba, boolean allowPartialWithdrawal) {
        try {
            System.out.println("Attempting to withdraw $" + this.getAmount() + "...");
            this.apply(ba); 
        } catch (InsufficientFundsException e) {
            System.out.println("Transaction Error: " + e.getMessage());
            
            double currentBalance = ba.getBalance();
            
            // Checks if balance > 0 AND 0 < balance < withdrawal amount
            if (allowPartialWithdrawal && currentBalance > 0 && currentBalance < this.getAmount()) {
                System.out.println("Initiating partial withdrawal of available funds...");
                try {
                    ba.withdraw(currentBalance);
                    this.shortfallAmount = this.getAmount() - currentBalance;
                    this.accountAppliedTo = ba; 
                    
                    System.out.println("Partial withdrawal of $" + currentBalance + " successful.");
                } catch (InsufficientFundsException fatalError) {
                    System.out.println("Critical error processing partial withdrawal.");
                }
            } else {
                System.out.println("Partial withdrawal not executed.");
            }
        } finally {
            System.out.println("--- Transaction process finalized ---");
            System.out.println("Transaction ID: " + this.getTransactionID());
            System.out.println("Recorded Shortfall: $" + this.shortfallAmount + "\n");
        }
    }

    @Override
    public boolean reverse() {
        if (this.accountAppliedTo != null && !this.isReversed) {
            double amountToRefund = this.getAmount() - this.shortfallAmount;
            this.accountAppliedTo.deposit(amountToRefund);
            this.isReversed = true;
            System.out.println("Withdrawal transaction reversed. Refunded: $" + amountToRefund);
            return true;
        }
        return false;
    }
}