import java.util.Calendar;
import java.util.UUID;

// 1. The Interface
public interface TransactionInterface {
    double getAmount();
    Calendar getDate();
    String getTransactionID();
}

// A simple BankAccount class
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

// BaseTransaction Class
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
    public double getAmount() {
        return this.amount;
    }

    @Override
    public Calendar getDate() {
        return this.date;
    }

    @Override
    public String getTransactionID() {
        return this.transactionID;
    }

    public void printTransactionDetails() {
        System.out.println("Transaction ID: " + getTransactionID());
        System.out.println("Date: " + getDate().getTime());
        System.out.println("Amount: $" + getAmount());
    }

    public void apply(BankAccount ba) {
        System.out.println("Generic base transaction logged for account.");
        System.out.println("Current Balance remains: $" + ba.getBalance());
    }
}

//  Derived Class: DepositTransaction
public class DepositTransaction extends BaseTransaction {
    public DepositTransaction(double amount, Calendar date) {
        super(amount, date);
    }

    @Override
    public void apply(BankAccount ba) {
        ba.deposit(this.getAmount());
        System.out.println("Deposit of $" + this.getAmount() + " successfully applied.");
        System.out.println("New Balance: $" + ba.getBalance());
    }
}

// 5. Derived Class: WithdrawalTransaction
public class WithdrawalTransaction extends BaseTransaction {
    public WithdrawalTransaction(double amount, Calendar date) {
        super(amount, date);
    }

    @Override
    public void apply(BankAccount ba) {
        ba.withdraw(this.getAmount());
        System.out.println("Withdrawal of $" + this.getAmount() + " successfully applied.");
        System.out.println("New Balance: $" + ba.getBalance());
    }
}