# Medical Crowdfunding System – Low Level Design

## Problem Statement
Design a medical crowdfunding platform where patients can create fundraising campaigns and donors can contribute to those campaigns, with real-time tracking and notifications.

---

## Functional Requirements
1. Users should be able to register as patients or donors.
2. Patients can create fundraising campaigns.
3. Donors can donate to one or multiple campaigns.
4. Campaigns should track total funds raised and update status accordingly.
5. System should notify users on donation events and campaign updates.

---

## Entities & Classes

### User
Represents a platform user who can act as a donor, patient, or both.

### Patient
Stores medical details of the patient such as disease, history, and treatment status.

### Campaign
Represents a fundraising campaign with target amount, current amount, and status.

### Donation
Represents a donation made by a donor to a campaign.

---

## Services

### CampaignService
Handles creation, update, deletion, and retrieval of campaigns.

### DonationService
Manages donation flow including validation, payment processing, and storage.

### CampaignTrackingService
Responsible for updating campaign funds and handling state transitions (ACTIVE → COMPLETED).

### NotificationService
Sends notifications to users via different channels.

---

## Payment Layer

### PaymentService (Interface)
Defines contract for payment processing.

### UPIPayment
Concrete implementation for UPI-based payments.

### CardPayment
Concrete implementation for card-based payments.

---

## Notification Layer

### Notifier (Interface)
Defines contract for sending notifications.

### EmailNotifier
Sends email notifications to users.

### SMSNotifier
Sends SMS notifications to users.

### PushNotifier
Sends push notifications to users.

---

## Design Patterns Used

### Strategy Pattern
Used in:
- PaymentService (UPI, Card implementations)
- Notifier (Email, SMS, Push)

Allows flexible switching of payment modes and notification channels.

---

### Observer Pattern (Conceptual)
NotificationService reacts to donation and campaign events.

---

### Dependency Injection
Services are injected into other services (e.g., DonationService uses PaymentService, CampaignService, etc.) to reduce tight coupling.

---

## System Flow

1. User registers as patient/donor  
2. Patient creates a campaign  
3. Donor makes a donation  
4. Payment is processed  
5. Campaign amount is updated  
6. Campaign status changes if target is reached  
7. Notifications are sent to users  

---

## ⚡ Concurrency Handling

- Campaign updates are synchronized at object level to prevent race conditions.
- Ensures correct fund aggregation during concurrent donations.

---

## Sample Execution Flow

- Campaign created with target amount  
- First donation updates campaign partially  
- Second donation completes the campaign  
- Campaign status changes to COMPLETED  

---

## Notes

- Currently uses in-memory storage (`HashMap`)
- Suitable for LLD interviews and conceptual understanding
- Can be extended to production-grade system

---
