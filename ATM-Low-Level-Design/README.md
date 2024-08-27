# ATM Low-Level Design

## Overview

This project implements the low-level design (LLD) of an Automated Teller Machine (ATM) system using Java. The design uses the State design pattern to manage the various states an ATM can be in, such as Idle, Card Inserted, PIN Authentication, Operation Selection, Cash Withdrawal, and Balance Check. The system is designed to simulate the flow of an ATM transaction, allowing users to withdraw cash or check their balance using a simulated ATM interface.

## Design Principles

- **State Design Pattern**: The ATM system transitions between different states, each represented by a separate class inheriting from an abstract `ATMState` class. The states include `IdleState`, `CardState`, `SelectOperationState`, `CashWithdrawState`, and `CheckBalanceState`.

- **Single Responsibility Principle (SRP)**: Each class is responsible for a specific piece of functionality, such as managing the ATM state, processing notes, or handling card and bank details.

- **Chain of Responsibility Pattern**: The cash withdrawal functionality is implemented using the Chain of Responsibility pattern, where different note processors (`TwoThousandNotesProcessor`, `FiveHundredNotesProcessor`, `OneHundredNotesProcessor`) handle the dispensing of cash.

## Components

### ATM States

- **IdleState**: Represents the state when no card is inserted in the ATM.
  - Allows card insertion.

- **CardState**: Represents the state after a card is inserted.
  - Handles PIN authentication.
  - Transitions to `SelectOperationState` on successful authentication.

- **SelectOperationState**: Represents the state where the user selects an operation.
  - Allows selection of operations such as Cash Withdrawal and Balance Check.
  - Transitions to `CashWithdrawState` or `CheckBalanceState` based on the selected operation.

- **CashWithdrawState**: Represents the state where the user performs a cash withdrawal.
  - Validates the withdrawal amount.
  - Dispenses cash using a chain of note processors.
  - Returns the card and transitions back to `IdleState` upon completion.

- **CheckBalanceState**: Represents the state where the user checks their balance.
  - Displays the balance and returns the card.

### ATM Components

- **ATM**: Manages the ATM state and contains information about the ATM's balance and available notes.

- **Card**: Represents the user's bank card and holds information such as card number, CVV, and associated bank.

- **Bank**: Represents the bank associated with the card, responsible for PIN validation and balance management.

- **NotesProcessor**: Interface for note processors that handle cash dispensing. Implementations include `TwoThousandNotesProcessor`, `FiveHundredNotesProcessor`, and `OneHundredNotesProcessor`.

## Usage

1. **Create a Bank Account**: Initialize a `Bank` object with account details.
2. **Create a Card**: Initialize a `Card` object associated with the bank.
3. **Initialize the ATM**: Create an `ATM` object and set the initial balance and number of notes available.
4. **Insert Card**: Use the `insertCard` method to insert the card into the ATM.
5. **Authenticate Card**: Authenticate the card by providing the correct PIN.
6. **Select Operation**: Choose an operation (e.g., Cash Withdrawal).
7. **Withdraw Cash**: Specify the amount to withdraw.
8. **Check Balance**: Optionally, check the account balance.

## Sample Output

```
welcome to atm low level design

Card inserted. Moving to CardState.
Validating PIN...
Checking PIN...
PIN validated. Moving to SelectOperationState.
Operation selected: CASH_WITHDRAW
Moving to CashWithdrawState.
Withdrawing amount: 5600
Validating withdrawal amount: 5600
Dispensed 2 x 2000 notes.
Dispensed 3 x 500 notes.
Dispensed 1 x 100 notes.
Deducted from bank balance: 5600. New balance: 444400.0
Withdrawal successful.
Exiting. Returning card.
Card returned to the user.
