enum ATMOperation {
    CASH_WITHDRAW,
    CHECK_BALANCE
}

abstract class ATMState {
    
    public void insertCard(ATM atm, Card card) {
        System.out.println("Wrong state: Cannot insert card in the current state.");
    }
    
    public void authenticateCard(ATM atm, Card card, int pincode) {
        System.out.println("Wrong state: Cannot authenticate card in the current state.");
    }
    
    public void selectOperation(ATM atm, ATMOperation operation) {
        System.out.println("Wrong state: Cannot select operation in the current state.");
    }
    
    public void showBalance(ATM atm, Card card) {
        System.out.println("Wrong state: Cannot show balance in the current state.");
    }
    
    public void withdrawAmount(ATM atm, Card card, int amount) {
        System.out.println("Wrong state: Cannot withdraw amount in the current state.");
    }
    
    public void exit(ATM atm) {
        System.out.println("Wrong state: Cannot exit in the current state.");
    }
}

class IdleState extends ATMState {
    
    public void insertCard(ATM atm, Card card) {
        System.out.println("Card inserted. Moving to CardState.");
        atm.setState(new CardState());
    }
}

class CardState extends ATMState {
    
    public void authenticateCard(ATM atm, Card card, int pincode) {
        if (validatePin(card, pincode)) {
            System.out.println("PIN validated. Moving to SelectOperationState.");
            atm.setState(new SelectOperationState());
        } else {
            System.out.println("Invalid PIN. Exiting.");
            exit(atm);
        }
    }
    
    public boolean validatePin(Card card, int pincode) {
        System.out.println("Validating PIN...");
        return card.getBank().checkPinCode(pincode);
    }
    
    public void exit(ATM atm) {
        System.out.println("Exiting. Returning card.");
        returnCard();
        atm.setState(new IdleState());
    }
    
    public void returnCard() {
        System.out.println("Card returned to the user.");
    }
}

class SelectOperationState extends ATMState {
    
    public void selectOperation(ATM atm, ATMOperation operation) {
        System.out.println("Operation selected: " + operation);
        
        switch(operation) {
            case CASH_WITHDRAW:
                System.out.println("Moving to CashWithdrawState.");
                atm.setState(new CashWithdrawState());
                break;
            case CHECK_BALANCE:
                System.out.println("Moving to CheckBalanceState.");
                atm.setState(new CheckBalanceState());
                break;
            default:
                System.out.println("Invalid operation. Exiting.");
                exit(atm);
        }
    }
    
    public void exit(ATM atm) {
        System.out.println("Exiting. Returning card.");
        returnCard();
        atm.setState(new IdleState());
    }
    
    public void returnCard() {
        System.out.println("Card returned to the user.");
    }
}

class CashWithdrawState extends ATMState {
    
    NotesProcessor twoThousandNotesProcessor;
    NotesProcessor fiveHundredNotesProcessor;
    NotesProcessor oneHundredNotesProcessor;
    
    NotesProcessor currentNotesProcessor;
    
    public CashWithdrawState() {
        oneHundredNotesProcessor = new OneHundredNotesProcessor(null);
        fiveHundredNotesProcessor = new FiveHundredNotesProcessor(oneHundredNotesProcessor);
        twoThousandNotesProcessor = new TwoThousandNotesProcessor(fiveHundredNotesProcessor);
        currentNotesProcessor = twoThousandNotesProcessor;
    }
    
    public boolean validateWithdrawAmount(ATM atm, Card card, int amount) {
        System.out.println("Validating withdrawal amount: " + amount);
        boolean hundredMultiple = true;
        boolean sufficientAtmBalance = true;
        boolean sufficientBankBalance = true;
        
        if(amount % 100 != 0) {
            hundredMultiple = false;
            System.out.println("Amount is not a multiple of 100.");
        }
        
        if(atm.getAtmBalance() < amount) {
            sufficientAtmBalance = false;
            System.out.println("Insufficient ATM balance.");
        }
        
        if(card.getBank().getCurrentBalance() < amount) {
            sufficientBankBalance = false;
            System.out.println("Insufficient bank balance.");
        }
        
        return hundredMultiple && sufficientBankBalance && sufficientAtmBalance;
    }
    
    public void withdrawAmount(ATM atm, Card card, int amount) {
        System.out.println("Withdrawing amount: " + amount);
        
        if(validateWithdrawAmount(atm, card, amount)) {
            currentNotesProcessor.withdrawAmount(atm, card, amount);
            atm.deductAtmBalance(amount);
            card.getBank().deductBankBalance(amount);
            System.out.println("Withdrawal successful.");
            exit(atm);
        } else {
            System.out.println("Withdrawal failed due to validation errors.");
            exit(atm);
        }
    }
    
    public void exit(ATM atm) {
        System.out.println("Exiting. Returning card.");
        returnCard();
        atm.setState(new IdleState());
    }
    
    public void returnCard() {
        System.out.println("Card returned to the user.");
    }
}

class CheckBalanceState extends ATMState {
    
    public void showBalance(ATM atm, Card card) {
        double balance = card.getBank().getCurrentBalance();
        System.out.println("Your bank balance: " + balance);
        exit(atm);
    }
    
    public void exit(ATM atm) {
        System.out.println("Exiting. Returning card.");
        returnCard();
        atm.setState(new IdleState());
    }
    
    public void returnCard() {
        System.out.println("Card returned to the user.");
    }
}

class Card {
    int cvv;
    int cardNumber;
    Bank bank;
    String cardHolderName;
    
    public Card(String cardHolderName, int cardNumber, int cvv, Bank bank) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.bank = bank;
    }
    
    public Bank getBank() {
        return bank;
    }
}

class Bank {
    String bankName;
    String accountHolderName;
    int accountNumber;
    private int pincode;
    double balance;
    
    public Bank(String bankName, String accountHolderName, int accountNumber, int pincode, double balance) {
        this.bankName = bankName;
        this.accountHolderName = accountHolderName;
        this.accountNumber = accountNumber;
        this.pincode = pincode;
        this.balance = balance;
    }

    public int getPincode() {
        return pincode;
    }
    
    public boolean checkPinCode(int pincode) {
        System.out.println("Checking PIN...");
        return this.pincode == pincode;
    }
    
    public void deductBankBalance(int withdrawAmount) {
        balance = balance - withdrawAmount;
        System.out.println("Deducted from bank balance: " + withdrawAmount + ". New balance: " + balance);
    }
    
    public double getCurrentBalance() {
        return balance;
    }
}

interface NotesProcessor {
    
    public void withdrawAmount(ATM atm, Card card, int amount);
}

class TwoThousandNotesProcessor implements NotesProcessor {
    
    NotesProcessor nextNotesProcessor;
    
    public TwoThousandNotesProcessor(NotesProcessor nextNotesProcessor) {
        this.nextNotesProcessor = nextNotesProcessor;
    }
    
    public void withdrawAmount(ATM atm, Card card, int amount) {
        int twoThousandNotes = atm.getTwoThousandNotes();
        int notesNeeded = amount / 2000;
        int leftAmount = amount % 2000;
        
        if(notesNeeded <= twoThousandNotes) {
            atm.deductTwoThousandNotes(notesNeeded);
            System.out.println("Dispensed " + notesNeeded + " x 2000 notes.");
        } else {
            atm.deductTwoThousandNotes(twoThousandNotes);
            System.out.println("Dispensed " + twoThousandNotes + " x 2000 notes.");
            leftAmount = leftAmount + ((notesNeeded - twoThousandNotes) * 2000);
        }
        
        if(leftAmount != 0 && nextNotesProcessor != null) {
            nextNotesProcessor.withdrawAmount(atm, card, leftAmount);
        }
    }
}

class FiveHundredNotesProcessor implements NotesProcessor {
    
    NotesProcessor nextNotesProcessor;
    
    public FiveHundredNotesProcessor(NotesProcessor nextNotesProcessor) {
        this.nextNotesProcessor = nextNotesProcessor;
    }
    
    public void withdrawAmount(ATM atm, Card card, int amount) {
        int fiveHundredNotes = atm.getFiveHundredNotes();
        int notesNeeded = amount / 500;
        int leftAmount = amount % 500;
        
        if(notesNeeded <= fiveHundredNotes) {
            atm.deductFiveHundredNotes(notesNeeded);
            System.out.println("Dispensed " + notesNeeded + " x 500 notes.");
        } else {
            atm.deductFiveHundredNotes(fiveHundredNotes);
            System.out.println("Dispensed " + fiveHundredNotes + " x 500 notes.");
            leftAmount = leftAmount + ((notesNeeded - fiveHundredNotes) * 500);
        }
        
        if(leftAmount != 0 && nextNotesProcessor != null) {
            nextNotesProcessor.withdrawAmount(atm, card, leftAmount);
        }
    }
}

class OneHundredNotesProcessor implements NotesProcessor {
    
    NotesProcessor nextNotesProcessor;
    
    public OneHundredNotesProcessor(NotesProcessor nextNotesProcessor) {
        this.nextNotesProcessor = nextNotesProcessor;
    }
    
    public void withdrawAmount(ATM atm, Card card, int amount) {
        int oneHundredNotes = atm.getOneHundredNotes();
        int notesNeeded = amount / 100;
        int leftAmount = amount % 100;
        
        if(notesNeeded <= oneHundredNotes) {
            atm.deductOneHundredNotes(notesNeeded);
            System.out.println("Dispensed " + notesNeeded + " x 100 notes.");
        } else {
            atm.deductOneHundredNotes(oneHundredNotes);
            System.out.println("Dispensed " + oneHundredNotes + " x 100 notes.");
            leftAmount = leftAmount + ((notesNeeded - oneHundredNotes) * 100);
        }
        
        if(leftAmount != 0 && nextNotesProcessor != null) {
            nextNotesProcessor.withdrawAmount(atm, card, leftAmount);
        }
    }
}

class ATM {
    
    private ATMState atmState;
    private int atmBalance;
    private int noOfTwoThousandNotes;
    private int noOfFiveHundredNotes;
    private int noOfOneHundredNotes;
    
    public ATM() {
        atmState = new IdleState();
    }
    
    public void setATMBalance(int atmBalance) {
        this.atmBalance = atmBalance;
    }
    
    public void setState(ATMState atmState) {
        this.atmState = atmState;
    }
    
    public ATMState getAtmState() {
        return atmState;
    }
    
    public void setTwoThousandNotes(int notes) {
        this.noOfTwoThousandNotes = notes;
    }
    
    public void setFiveHundredNotes(int notes) {
        this.noOfFiveHundredNotes = notes;
    }
    
    public void setOneHundredNotes(int notes) {
        this.noOfOneHundredNotes = notes;
    }
    
    public int getTwoThousandNotes() {
        return this.noOfTwoThousandNotes;
    }
    
    public int getFiveHundredNotes() {
        return this.noOfFiveHundredNotes;
    }
    
    public int getOneHundredNotes() {
        return this.noOfOneHundredNotes;
    }
    
    public void deductTwoThousandNotes(int notes) {
        this.noOfTwoThousandNotes -= notes;
    }
    
    public void deductFiveHundredNotes(int notes) {
        this.noOfFiveHundredNotes -= notes;
    }
    
    public void deductOneHundredNotes(int notes) {
        this.noOfOneHundredNotes -= notes;
    }
    
    public void deductAtmBalance(int balance) {
        atmBalance -= balance;
    }
    
    public int getAtmBalance() {
        return atmBalance;
    }
}

public class ATMSystem {
    
	public static void main(String[] args) {
	    
		System.out.println("welcome to atm low level design\n");
		
		ATMSystem AtmSystem = new ATMSystem();
		
		// 1. create bank
		int bankAccountNumber = (int)(Math.random() * 9000) + 1000;
		Bank bank = new Bank("lldBank", "phill", bankAccountNumber, 1234, 450000);
		
		// 2. create card
		int cardNumber = (int)(Math.random() * 9000) + 1000;
		Card card = new Card("phill", cardNumber, 445, bank);
		
		// 3. intialize ATM
		ATM atm = new ATM();
		atm.setATMBalance(200000);
		atm.setTwoThousandNotes(50);
		atm.setFiveHundredNotes(100);
		atm.setOneHundredNotes(500);
		
		// 4. insert card
		atm.getAtmState().insertCard(atm, card);
		
		// 5. authenticate card
		atm.getAtmState().authenticateCard(atm, card, 1234);
		
		// 6. select operation
		atm.getAtmState().selectOperation(atm, ATMOperation.CASH_WITHDRAW);
		
		// 7. withdraw amount
		atm.getAtmState().withdrawAmount(atm, card, 5600);
		
	}
}
