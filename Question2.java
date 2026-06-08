import java.util.Calendar;
import java.util.UUID;

public interface TransactionInterface {
    double getAmount();
    Calendar getDate();
    String getTransactionID();
}

// 2. BankAccount class (Remains the same)
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public double getBalance() {
        return this.balance;
    }
}

// 3. Updated BaseTransaction (Added reverse method)
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

    public void printTransactionDetails() {
        System.out.println("Transaction ID: " + getTransactionID());
        System.out.println("Amount: $" + getAmount());
    }

    public void apply(BankAccount ba) {
        System.out.println("Generic base transaction logged.");
    }

    // NEW: Default behavior is irreversible
    public boolean reverse() {
        System.out.println("This transaction type is irreversible.");
        return false;
    }
}

// 4. Updated DepositTransaction (Explicitly irreversible)
public class DepositTransaction extends BaseTransaction {
    public DepositTransaction(double amount, Calendar date) {
        super(amount, date);
    }

    @Override
    public void apply(BankAccount ba) {
        ba.deposit(this.getAmount());
        System.out.println("Deposit of $" + this.getAmount() + " applied.");
    }

    @Override
    public boolean reverse() {
        System.out.println("Failed: Deposit transactions are strictly irreversible.");
        return false;
    }
}

// 5. Updated WithdrawalTransaction (Reversible logic added)
public class WithdrawalTransaction extends BaseTransaction {
    private BankAccount accountAppliedTo;
    private boolean isReversed;

    public WithdrawalTransaction(double amount, Calendar date) {
        super(amount, date);
        this.accountAppliedTo = null;
        this.isReversed = false;
    }

    @Override
    public void apply(BankAccount ba) {
        ba.withdraw(this.getAmount());
        this.accountAppliedTo = ba; // Save reference for reversal
        System.out.println("Withdrawal of $" + this.getAmount() + " applied.");
    }

    @Override
    public boolean reverse() {
        if (this.accountAppliedTo != null && !this.isReversed) {
            this.accountAppliedTo.deposit(this.getAmount());
            this.isReversed = true;
            System.out.println("Withdrawal " + this.getTransactionID() + " reversed.");
            System.out.println("Restored Balance: $" + this.accountAppliedTo.getBalance());
            return true;
        } else if (this.isReversed) {
            System.out.println("Failed: Transaction already reversed.");
            return false;
        } else {
            System.out.println("Failed: Transaction not applied to an account yet.");
            return false;
        }
    }
}