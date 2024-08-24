# Payment Gateway Low-Level Design

## Overview

This project implements a low-level design for a payment gateway system. It supports multiple payment methods, such as Bank and Card transactions. The design includes various components like Users, Payment Objects (Bank and Card), Transaction Handling, and Services to manage these components.

The system allows users to store multiple payment methods and execute transactions securely. This design is extensible and can be easily adapted to include additional payment methods or features as required.

## Features

### User Management
- Creation and retrieval of user information.
- Each user can have multiple payment objects (Bank accounts, Credit/Debit cards).

### Payment Object Management
- Supports Bank and Card payment types.
- Payment objects are stored and retrieved based on the user.

### Transaction Management
- Handles transactions between users using different payment objects.
- Supports multiple transaction statuses (Pending, Success, Failed).
- Stores transaction history for each user.

### Extensibility
- Easy to add new payment types by extending `PaymentObject` and implementing corresponding services.
- Transaction handling can be extended to include additional features like refunds or chargebacks.

## Class Diagram

The system is divided into the following main components:

- **User**: Represents the user of the payment system.
- **PaymentObject**: Abstract class for different types of payment methods.
  - **BankPaymentObject**: Represents bank account details.
  - **CardPaymentObject**: Represents card details.
- **PaymentObjectService**: Abstract class for handling payment objects.
  - **BankPaymentObjectService**: Handles bank-related payment objects.
  - **CardPaymentObjectService**: Handles card-related payment objects.
- **TransactionDO**: Represents the data object for a transaction.
- **TransactionController**: Manages the creation and processing of transactions between users.


## Future Enhancements

- **Security**: Implement additional security measures such as encryption for sensitive data and OAuth for user authentication.
- **Refunds**: Add functionality to handle refunds.
- **Notifications**: Integrate with an email or SMS service to notify users of successful transactions.

## Sample Output

```plaintext
welcome to payment gateway low level design

Payment Objects for User 1:
Payment Object ID: 4f542b16-6dd9-469a-a652-0265eb3cc317, Payment Type: BANK, Account Number: 687757463838596, Bank Name: ABC bank

Payment Objects for User 2:
Payment Object ID: 4bbe3c94-afcb-4bd9-b518-32bf9df6396c, Payment Type: CARD, Card Number: 687757463838596, Card Holder Name: jack

Transaction History for User 1:
Transaction ID: 6b2171d4-c168-4a5c-9b1c-5079c602d78b, Amount: 50, Status: SUCCESS, Sender ID: 44e598c2-beb8-4936-bd9e-ab005e4ad748, Receiver ID: c00cafc4-3afa-4623-b432-4bf28cbdb53d

Transaction History for User 2:
Transaction ID: 6b2171d4-c168-4a5c-9b1c-5079c602d78b, Amount: 50, Status: SUCCESS, Sender ID: 44e598c2-beb8-4936-bd9e-ab005e4ad748, Receiver ID: c00cafc4-3afa-4623-b432-4bf28cbdb53d
