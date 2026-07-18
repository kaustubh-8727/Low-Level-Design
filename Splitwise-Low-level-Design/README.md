# Splitwise Low Level Design (LLD)

A Java implementation of the core functionality of **Splitwise** using Object-Oriented Design principles and Design Patterns.

## Problem Statement

Design a system similar to **Splitwise** where:

- Users can create a group.
- Members can add expenses.
- Expenses can be split among all group members.
- Support multiple splitting strategies.
- Maintain an individual balance sheet for every user.
- Simplify debts by minimizing the number of transactions.
- Display each user's balance sheet.

---

# Features

- Create Group
- Add Expense
- Equal Split
- Percentage Split
- Maintain User Balance Sheet
- Simplify Debt
- Print Balance Sheet

---

# Class Responsibilities

## User
Represents a user participating in one or more groups.

---

## Split
Represents the share of an expense assigned to a particular user.

---

## Expense
Represents an expense paid by a user along with its split information.

---

## BalanceSheet
Maintains financial information for a user.

Stores:

- Total amount paid
- Total expense
- Pairwise balances with other users

---

## Group
Represents a Splitwise group.

Contains:

- Group information
- Members
- Expenses
- Balance sheet of every member

---

## ISplitExpense
Strategy interface for implementing different expense splitting algorithms.

---

## SplitExpenseEqual
Splits the expense equally among all members.

---

## SplitExpensePercentage
Splits the expense according to the percentage specified for each member.

---

## SplitExpenseFactory
Returns the appropriate expense splitting strategy based on the split type.

---

## ExpenseService
Responsible for:

- Creating expenses
- Delegating split calculation
- Updating the balance sheet

---

## BalanceSheetService
Updates pairwise balances between users after every expense.

---

## BalanceSheetSimplify
Optimizes debts by converting multiple transactions into the minimum number of transactions.

---

## GroupService
Acts as the entry point for all group operations.

Responsible for:

- Creating groups
- Adding expenses
- Simplifying debts
- Printing balance sheets

---

## SplitWiseDesign
Driver class containing the `main()` function to demonstrate the application.

---

# Design Patterns Used

## Strategy Pattern

Used to support multiple expense splitting algorithms.

```
                ISplitExpense
                      |
        -----------------------------
        |                           |
SplitExpenseEqual        SplitExpensePercentage
```

Adding a new split type only requires creating another implementation of `ISplitExpense`.

---

## Factory Pattern

The factory returns the appropriate splitting strategy.

```
SplitType
     |
     v
SplitExpenseFactory
     |
     +----> SplitExpenseEqual
     |
     +----> SplitExpensePercentage
```

This avoids large conditional logic throughout the application.

---

## Service Layer Pattern

Business logic is separated from entities.

```
GroupService
      |
ExpenseService
      |
BalanceSheetService
      |
BalanceSheet
```

This keeps entities lightweight and makes the code easier to maintain.

---

# Execution Flow

## Step 1

Create Users

```
Alice
Bob
Charlie
```

↓

## Step 2

Create Group

```
Trip
```

↓

## Step 3

Add Expense

Example

```
Alice paid ₹300
Split Type : EQUAL
```

↓

## Step 4

ExpenseService

- Creates Expense
- Gets splitting strategy from Factory
- Calculates splits

↓

## Step 5

BalanceSheetService

Updates

- Total Paid
- Total Expense
- Pairwise balances

↓

## Step 6

Print Balance Sheet

Shows who owes whom.

↓

## Step 7

Simplify Debt

Calculates the net balance of every user.

```
+ Positive -> Creditors

- Negative -> Debtors
```

Greedily matches creditors and debtors until all balances become zero.

↓

## Step 8

Print Simplified Balance Sheet

Shows the minimum number of required transactions.

---

# Sample Execution

## Expense 1

```
Alice paid ₹300
```

Equal Split

```
Alice : 100
Bob : 100
Charlie : 100
```

Balances

```
Bob owes Alice : 100

Charlie owes Alice : 100
```

---

## Expense 2

```
Bob paid ₹150
```

Equal Split

```
Alice : 50
Bob : 50
Charlie : 50
```

Balances

```
Alice owes Bob : 50

Charlie owes Bob : 50
```

---

## Before Simplification

```
Bob owes Alice : 50

Charlie owes Alice : 100

Charlie owes Bob : 50
```

---

## After Simplification

```
Charlie owes Alice : 150
```

Bob's transactions cancel out completely.

---

# Overall Architecture

```
                    GroupService
                         |
            -----------------------------
            |                           |
     ExpenseService          BalanceSheetSimplify
            |
     SplitExpenseFactory
            |
      --------------------
      |                  |
 Equal Split      Percentage Split
            |
        Expense
            |
 BalanceSheetService
            |
      BalanceSheet
```

---

# Time Complexity

| Operation | Complexity |
|-----------|------------|
| Create Group | O(1) |
| Add Expense | O(n) |
| Equal Split | O(n) |
| Percentage Split | O(n) |
| Update Balance Sheet | O(n) |
| Simplify Debt | O(n) |
| Print Balance Sheet | O(n²) |

where **n** is the number of users in the group.

---
