import java.util.InputMismatchException;
import java.util.Scanner;

public class trymoto {
    // Initial balance for all accounts
    private static final double INITIAL_BALANCE = 10000.00;

    // Number of accounts
    private static int numAccounts;
    // Array to store account information
    private static BankAccount[] accounts;

    public static void main(String[] args) {
        Scanner inputRecord = new Scanner(System.in);
        Scanner input = new Scanner(System.in);
        Scanner inputPass = new Scanner(System.in);

        boolean isMismatch = false;

        // Ask the user how many records to create
        System.out.print("Number of Records: ");
        try {
            numAccounts = input.nextInt();
        } catch (InputMismatchException e) {
            isMismatch = true;
        }

        if (isMismatch == true) {
            System.out.println("Input Mismatch!");
            // Ask the user if they want to continue or stop the app
            System.out.print("[C] Continue or [S] Stop the App: ");
            String choice = inputRecord.nextLine();
            if (choice.equalsIgnoreCase("C")) {
                // Automatically assign 3 records
                numAccounts = 3;
            } else if (choice.equalsIgnoreCase("S")) {
                System.out.print("End of Program");
            } else {
                System.out.print("End of Program");
            }
        }

        if (numAccounts == 3 || isMismatch == false) {
            accounts = new BankAccount[numAccounts];
            System.out.println("Adding Records ");

            // Create the accounts
            for (int i = 0; i < numAccounts; i++) {
                System.out.println("-------------------------------------------");
                System.out.println("Record No     : " + (i + 1));
                System.out.print("Client Name   : ");
                String name = input.next();
                System.out.println("Account No     : ACC-000-" + (i + 1));
                System.out.print("Password      : ");
                String password = input.next();
                System.out.print("PIN Code      : ");
                int pinCode = input.nextInt();
                // Initialize the account with the given information
                String acctNum = "ACC-000-" + (i + 1);
                accounts[i] = new BankAccount(name, acctNum, password, pinCode, INITIAL_BALANCE);

            }

            // Main menu
            boolean running = true;
            while (running) {
                System.out.println("-------------------------------------------");
                System.out.print("\tMyBank Main Menu");
                System.out.println("\n-------------------------------------------");
                System.out.print("[L]og [E]xit Select Transaction: ");
                String choice = input.next();
                if (choice.equalsIgnoreCase("L")) {
                    // Ask for account number and password
                    System.out.println("-------------------------------------------");
                    int attempts = 0;
                    int attemptsCount = 2;
                    while (attempts < 3) {
                        System.out.print("Account No  : ");
                        String accountNumber = input.next();
                        System.out.print("Password    : ");
                        String password = inputPass.nextLine();
                        // Try to login
                        BankAccount account = login(accountNumber, password);
                        if (account != null && account.checkPassword(password)) {
                            // If login is successful, show account menu
                            accountMenu(account, input, inputPass);
                            break;
                        } else {
                            int count = attemptsCount - attempts;
                            System.out.println(count + " Attempts left!");
                            attempts++;
                        }
                    }
                    if (attempts == 3) {
                        System.out.println(">> Transaction Reported!");
                    }

                } else if (choice.equalsIgnoreCase("E")) {
                    // Exit the program
                    System.out.println(">> End of Program");
                    running = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            }
        }

    }

    // Attempts to login with the given account number and password
    private static BankAccount login(String accountNumber, String password) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().endsWith(String.valueOf(accountNumber)) && account.checkPassword(password)) {
                return account;
            }
        }
        return null;
    }

    // Shows the account menu for the given account
    private static void accountMenu(BankAccount account, Scanner input, Scanner inputPass) {
        boolean running = true;
        while (running) {
            System.out.println("-------------------------------------------");
            System.out.print("    MyBank Automated Teller Machine");
            System.out.print("\n-------------------------------------------");
            System.out.println("\nHi " + account.getName() + "!");
            System.out.println("Current Balance: " + account.getBalance());
            System.out.print("[W]ithdraw [D]eposit [T]ransfer [E]nd");
            System.out.println("\n-------------------------------------------");
            System.out.print("Select Transaction: ");
            String choice = input.next();
            if (choice.equalsIgnoreCase("W")) {
                // Withdraw
                System.out.print("Enter amount to withdraw: ");
                double amount = input.nextDouble();
                System.out.print("Enter your PIN Code: ");
                int pinCode = input.nextInt();
                if (account.checkPinCode(pinCode)) {
                    if (account.withdraw(amount)) {
                        System.out.println(">> Withdraw successful.");
                    } else {
                        System.out.println(">> Insufficient fund!");
                    }
                } else {
                    System.out.println(">> Wrong PIN Code.");
                }
            } else if (choice.equalsIgnoreCase("D")) {
                // Deposit
                System.out.print("Enter amount to deposit: ");
                double amount = input.nextDouble();
                account.deposit(amount);
                System.out.println(">> Deposit successful.");
            } else if (choice.equalsIgnoreCase("T")) {
                // Transfer
                System.out.print("Enter account number: ");
                String destAccountNumber = inputPass.nextLine();
                System.out.print("Amount to transfer: ");
                double amount = input.nextDouble();
                System.out.print("Enter PIN Code: ");
                int pinCode = input.nextInt();
                if (account.checkPinCode(pinCode)) {
                    BankAccount destAccount = getAccount(destAccountNumber);
                    if (destAccount != null) {
                        if (account.transfer(destAccount, amount)) {
                            System.out.println(">> Money Transferred!");
                        } else {
                            System.out.println(">> Insufficient fund!");
                        }
                    } else {
                        System.out.println(">> Rejected! Account does not exist");
                    }
                } else {
                    System.out.println(">> Wrong PIN Code!");
                }
            } else if (choice.equalsIgnoreCase("E")) {
                // End
                running = false;
            } else {
                System.out.println("<< Invalid Transaction!");
            }
        }
    }

    // Returns the account with the given account number, or null if not found
    private static BankAccount getAccount(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().endsWith(String.valueOf(accountNumber))) {
                return account;
            }
        }
        return null;
    }
}

class BankAccount {
    // Account information
    private String name;
    private String accountNumber;
    private String password;
    private int pinCode;
    private double balance;

    public BankAccount(String name, String accountNumber, String password, int pinCode, double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.password = password;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public int getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    // Setter methods
    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Withdraws the given amount from the account
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    // Deposits the given amount into the account
    public void deposit(double amount) {
        balance += amount;
    }

    // Transfers the given amount to the destination account
    public boolean transfer(BankAccount dest, double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        dest.deposit(amount);
        return true;
    }

    // Checks if the given password is correct
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // Checks if the given PIN code is correct
    public boolean checkPinCode(int pinCode) {
        return this.pinCode == pinCode;
    }
}