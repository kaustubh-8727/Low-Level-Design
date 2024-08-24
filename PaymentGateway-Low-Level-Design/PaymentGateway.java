import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

enum PaymentType {
    BANK,
    CARD
}

enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED
}

class User {
    String userId;
    String emailId;
    String name;
    
    public User(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
        this.userId = UUID.randomUUID().toString();
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getEmailId() {
        return emailId;
    }
    
    public String getUserName() {
        return name;
    }
}

class UserController {
    
    List<User> usersList = new ArrayList<>();
    
    public void addUser(User user) {
        usersList.add(user);
    }
    
    public User getUserDetails(String userId) {
        for(User user : usersList) {
            if(user.getUserId().equals(userId)) {
                return user;
            }
        }
        
        return null;
    }
}

class PaymentObjectDO {

    String paymentObjectId;
    PaymentType paymentType;
    String userId;
    String accountNumber;
    String ifcsCode;
    String accountHolderName;
    String bankName;
    String cardNumber;
    String cardHolderName;
    String cardCvv;
    Date cardExpiry;

    // Getters and Setters
    public String getPaymentObjectId() {
        return paymentObjectId;
    }

    public void setPaymentObjectId(String paymentObjectId) {
        this.paymentObjectId = paymentObjectId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfcsCode() {
        return ifcsCode;
    }

    public void setIfcsCode(String ifcsCode) {
        this.ifcsCode = ifcsCode;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public Date getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(Date cardExpiry) {
        this.cardExpiry = cardExpiry;
    }
}

abstract class PaymentObject {
    
    String paymentObjectId;
    PaymentType paymentType;
    String userId;

    public void setPaymentObjectId(String paymentObjectId) {
        this.paymentObjectId = paymentObjectId;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

class BankPaymentObject extends PaymentObject {
    
    String accountNumber;
    String ifcsCode;
    String accountHolderName;
    String bankName;
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setIfcsCode(String ifcsCode) {
        this.ifcsCode = ifcsCode;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}

class CardPaymentObject extends PaymentObject {
    
    String cardNumber;
    String cardHolderName;
    String cardCvv;
    Date cardExpiry;
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    public void setCardExpiry(Date cardExpiry) {
        this.cardExpiry = cardExpiry;
    }
}

abstract class PaymentObjectService {
    
    static Map<String, List<PaymentObject>> userPaymentObjectMap = new HashMap<>();
    
    abstract public PaymentObjectDO addPaymentObject(PaymentObjectDO paymentObjectDO);
    
    abstract public List<PaymentObjectDO> getPaymentList(String userId);
}

class BankPaymentObjectService extends PaymentObjectService {
    
    public PaymentObjectDO addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        BankPaymentObject bankPaymentObject = new BankPaymentObject();
        bankPaymentObject.setPaymentObjectId(UUID.randomUUID().toString());
        bankPaymentObject.setPaymentType(PaymentType.BANK);
        bankPaymentObject.setUserId(paymentObjectDO.userId);
        bankPaymentObject.setAccountNumber(paymentObjectDO.accountNumber);
        bankPaymentObject.setAccountHolderName(paymentObjectDO.accountHolderName);
        bankPaymentObject.setIfcsCode(paymentObjectDO.ifcsCode);
        bankPaymentObject.setBankName(paymentObjectDO.bankName);

        List<PaymentObject> userPaymentObject = new ArrayList<>();
        if(userPaymentObjectMap.containsKey(paymentObjectDO.userId)) {
            userPaymentObject = userPaymentObjectMap.get(paymentObjectDO.userId);
        }
        userPaymentObject.add(bankPaymentObject);
        userPaymentObjectMap.put(paymentObjectDO.userId, userPaymentObject);
        

        return convertToDO1(bankPaymentObject);
    }
    
    public List<PaymentObjectDO> getPaymentList(String userId) {
        
        List<PaymentObject> userPaymentObject = new ArrayList<>();
        if(userPaymentObjectMap.containsKey(userId)) {
            userPaymentObject = userPaymentObjectMap.get(userId);
        }
        List<PaymentObjectDO> userPaymentObjectDO = new ArrayList<>();
        for(PaymentObject paymentObject : userPaymentObject) {
            if(paymentObject.paymentType == PaymentType.BANK) {
                userPaymentObjectDO.add(convertToDO1((BankPaymentObject) paymentObject));
            }
        }
        
        return userPaymentObjectDO;
    }

    public PaymentObjectDO convertToDO1(BankPaymentObject bankPaymentObject) {
        
        PaymentObjectDO paymentObjectDO = new PaymentObjectDO();
        paymentObjectDO.paymentObjectId = bankPaymentObject.paymentObjectId;
        paymentObjectDO.paymentType = bankPaymentObject.paymentType;
        paymentObjectDO.userId = bankPaymentObject.userId;
        paymentObjectDO.accountHolderName = bankPaymentObject.accountHolderName;
        paymentObjectDO.accountNumber = bankPaymentObject.accountNumber;
        paymentObjectDO.ifcsCode = bankPaymentObject.ifcsCode;
        paymentObjectDO.bankName = bankPaymentObject.bankName;
        
        return paymentObjectDO;
    }
    
}

class CardPaymentObjectService extends PaymentObjectService {
    
    public PaymentObjectDO addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        CardPaymentObject cardPaymentObject = new CardPaymentObject();
        cardPaymentObject.setPaymentObjectId(UUID.randomUUID().toString());
        cardPaymentObject.setPaymentType(PaymentType.CARD);
        cardPaymentObject.setUserId(paymentObjectDO.userId);
        cardPaymentObject.setCardHolderName(paymentObjectDO.cardHolderName);
        cardPaymentObject.setCardNumber(paymentObjectDO.cardNumber);
        cardPaymentObject.setCardCvv(paymentObjectDO.cardCvv);
        cardPaymentObject.setCardExpiry(paymentObjectDO.cardExpiry);
        
        List<PaymentObject> userPaymentObject = new ArrayList<>();
        if(userPaymentObjectMap.containsKey(paymentObjectDO.userId)) {
            userPaymentObject = userPaymentObjectMap.get(paymentObjectDO.userId);
        }
        userPaymentObject.add(cardPaymentObject);
        userPaymentObjectMap.put(paymentObjectDO.userId, userPaymentObject);
        
        return convertToDO(cardPaymentObject);
    }
    
    public List<PaymentObjectDO> getPaymentList(String userId) {
        
        List<PaymentObject> userPaymentObject = new ArrayList<>();
        if(userPaymentObjectMap.containsKey(userId)) {
            userPaymentObject = userPaymentObjectMap.get(userId);
        }
        List<PaymentObjectDO> userPaymentObjectDO = new ArrayList<>();
        
        for(PaymentObject paymentObject : userPaymentObject) {
            if(paymentObject.paymentType == PaymentType.CARD) {
                userPaymentObjectDO.add(convertToDO((CardPaymentObject) paymentObject));
            }
        }
        
        return userPaymentObjectDO;
    }
    
    public PaymentObjectDO convertToDO(CardPaymentObject cardPaymentObject) {
        
        PaymentObjectDO paymentObjectDO = new PaymentObjectDO();
        paymentObjectDO.paymentObjectId = cardPaymentObject.paymentObjectId;
        paymentObjectDO.paymentType = cardPaymentObject.paymentType;
        paymentObjectDO.userId = cardPaymentObject.userId;
        paymentObjectDO.cardNumber = cardPaymentObject.cardNumber;
        paymentObjectDO.cardHolderName = cardPaymentObject.cardHolderName;
        paymentObjectDO.cardCvv = cardPaymentObject.cardCvv;
        paymentObjectDO.cardExpiry = cardPaymentObject.cardExpiry;
        
        return paymentObjectDO;
    }
    
}

class PaymentObjectFactory {

    public PaymentObjectService getPaymentObjectService(PaymentType paymentType) {
        if (paymentType == PaymentType.BANK) {
            return new BankPaymentObjectService();
        } else if (paymentType == PaymentType.CARD) {
            return new CardPaymentObjectService();
        }
        return null;
    }
}


class PaymentObjectController {
    
    PaymentObjectFactory paymentObjectFactory;
    
    public PaymentObjectController() {
        paymentObjectFactory = new PaymentObjectFactory();
    }
    
    public PaymentObjectDO addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        PaymentType paymentType = paymentObjectDO.paymentType;
        PaymentObjectService paymentObjectService = paymentObjectFactory.getPaymentObjectService(paymentType);
        
        return paymentObjectService.addPaymentObject(paymentObjectDO);
        
    }
    
    public PaymentObjectDO getPaymentObject(String userId, String paymentObjectId) {
        PaymentObjectService paymentObjectBankService = paymentObjectFactory.getPaymentObjectService(PaymentType.BANK);
        List<PaymentObjectDO> paymentObjectBankList = paymentObjectBankService.getPaymentList(userId);
        for(PaymentObjectDO paymentObjectDO : paymentObjectBankList) {
            if(paymentObjectDO.paymentObjectId == paymentObjectId) {
                return paymentObjectDO;
            }
        }
        
        PaymentObjectService paymentObjectCardService = paymentObjectFactory.getPaymentObjectService(PaymentType.CARD);
        List<PaymentObjectDO> paymentObjectCardList = paymentObjectCardService.getPaymentList(userId);
        for(PaymentObjectDO paymentObjectDO : paymentObjectCardList) {
            if(paymentObjectDO.paymentObjectId == paymentObjectId) {
                return paymentObjectDO;
            }
        }
        
        return null;
    }
    
    public List<PaymentObjectDO> getAllPaymentObjects(String userId) {
        PaymentObjectService paymentObjectBankService = paymentObjectFactory.getPaymentObjectService(PaymentType.BANK);
        List<PaymentObjectDO> paymentObjectBankList = paymentObjectBankService.getPaymentList(userId);
        
        PaymentObjectService paymentObjectCardService = paymentObjectFactory.getPaymentObjectService(PaymentType.CARD);
        List<PaymentObjectDO> paymentObjectCardList = paymentObjectCardService.getPaymentList(userId);
        
        List<PaymentObjectDO> allPaymentObjects = new ArrayList<>();
        allPaymentObjects.addAll(paymentObjectBankList);
        allPaymentObjects.addAll(paymentObjectCardList);
        
        return allPaymentObjects;
    }
}

class TransactionDO {

    String transactionId;
    String amount;
    String senderId;
    String receiverId;
    String senderPaymentObjectId;
    String receiverPaymentObjectId;
    TransactionStatus transactionStatus;

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderPaymentObjectId() {
        return senderPaymentObjectId;
    }

    public void setSenderPaymentObjectId(String senderPaymentObjectId) {
        this.senderPaymentObjectId = senderPaymentObjectId;
    }

    public String getReceiverPaymentObjectId() {
        return receiverPaymentObjectId;
    }

    public void setReceiverPaymentObjectId(String receiverPaymentObjectId) {
        this.receiverPaymentObjectId = receiverPaymentObjectId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}

class TransactionController {
    
    PaymentObjectController paymentObjectController;
    PaymentProcessor paymentProcessor;
    Map<String, List<TransactionDO>> userTransactionHistory;
    
    public TransactionController() {
        paymentObjectController = new PaymentObjectController();
        paymentProcessor = new PaymentProcessor();
        userTransactionHistory = new HashMap<>();
    }
    
    public TransactionDO makePayment(TransactionDO transactionDO) {
        
        PaymentObjectDO senderPaymentObject = paymentObjectController.getPaymentObject(transactionDO.senderId, transactionDO.senderPaymentObjectId);
        PaymentObjectDO receiverPaymentObject = paymentObjectController.getPaymentObject(transactionDO.receiverId, transactionDO.receiverPaymentObjectId);
    
        paymentProcessor.processPayment(senderPaymentObject, receiverPaymentObject);
        
        transactionDO.transactionId = UUID.randomUUID().toString();
        transactionDO.transactionStatus = TransactionStatus.SUCCESS;
        
        List<TransactionDO> senderTrxnList = new ArrayList<>();
        List<TransactionDO> receiverTrxnList = new ArrayList<>();
        if(userTransactionHistory.containsKey(transactionDO.senderId)) {
            senderTrxnList = userTransactionHistory.get(transactionDO.senderId);
        }
        if(userTransactionHistory.containsKey(transactionDO.receiverId)) {
            senderTrxnList = userTransactionHistory.get(transactionDO.receiverId);
        }
        
        senderTrxnList.add(transactionDO);
        receiverTrxnList.add(transactionDO);
        userTransactionHistory.put(transactionDO.senderId, senderTrxnList);
        userTransactionHistory.put(transactionDO.receiverId, receiverTrxnList);
        
        return transactionDO;
    }
    
    public List<TransactionDO> getTransactionHistory(String userId) {
        
        List<TransactionDO> transactionList = new ArrayList<>();
        if(userTransactionHistory.containsKey(userId)) {
            transactionList = userTransactionHistory.get(userId);
        }
        
        return transactionList;
    }
}

class PaymentProcessor {
    
    public boolean processPayment(PaymentObjectDO senderPaymentObject, PaymentObjectDO receiverPaymentObject) {
        
        // implement bank/card specific logic to make payment
        
        return true;
    }
}

public class PaymentGateway {
	public static void main(String[] args) {
	    
		System.out.println("welcome to payment gateway low level design\n");
		
		// intialize controllers
		UserController userController = new UserController();
		PaymentObjectController paymentObjectController = new PaymentObjectController();
		TransactionController transactionController = new TransactionController();
		
		// create users
		User user1 = new User("john", "john@payment.com");
		User user2 = new User("jack", "jack@payment.com");
		
		String user1Id = user1.getUserId();
		String user2Id = user2.getUserId();
		
		// create bank payment object for user1
		PaymentObjectDO user1PaymentObject = new PaymentObjectDO();
		user1PaymentObject.setBankName("ABC bank");
		user1PaymentObject.setAccountNumber("687757463838596");
		user1PaymentObject.setAccountHolderName("john");
		user1PaymentObject.setIfcsCode("DC4567");
		user1PaymentObject.setUserId(user1Id);
		user1PaymentObject.setPaymentType(PaymentType.BANK);
		
		// create card payment object for user 2
		PaymentObjectDO user2PaymentObject = new PaymentObjectDO();
		user2PaymentObject.setCardNumber("687757463838596");
		user2PaymentObject.setCardHolderName("jack");
		user2PaymentObject.setCardCvv("244");
		user2PaymentObject.setUserId(user2Id);
		user2PaymentObject.setCardExpiry(new Date());
		user2PaymentObject.setPaymentType(PaymentType.CARD);
		
		// submit payment object type
		user1PaymentObject = paymentObjectController.addPaymentObject(user1PaymentObject);
		user2PaymentObject = paymentObjectController.addPaymentObject(user2PaymentObject);
		
		// create transaction
		TransactionDO transaction = new TransactionDO();
		transaction.setSenderId(user1Id);
		transaction.setSenderPaymentObjectId(user1PaymentObject.paymentObjectId);
		transaction.setAmount("50");
		transaction.setReceiverId(user2Id);
		transaction.setReceiverPaymentObjectId(user2PaymentObject.paymentObjectId);
		
		// perform the payment
		transactionController.makePayment(transaction);
		
		// get user1 all payment objects
		List<PaymentObjectDO> user1PaymentObjectList = paymentObjectController.getAllPaymentObjects(user1Id);
		System.out.println("Payment Objects for User 1:");
        for (PaymentObjectDO payment : user1PaymentObjectList) {
            System.out.println("Payment Object ID: " + payment.getPaymentObjectId() +
                               ", Payment Type: " + payment.getPaymentType() +
                               ", Account Number: " + payment.getAccountNumber() +
                               ", Bank Name: " + payment.getBankName() + "\n");
        }
		
		// get user2 all payment objects
		List<PaymentObjectDO> user2PaymentObjectList = paymentObjectController.getAllPaymentObjects(user2Id);
		System.out.println("Payment Objects for User 2:");
        for (PaymentObjectDO payment : user2PaymentObjectList) {
            System.out.println("Payment Object ID: " + payment.getPaymentObjectId() +
                               ", Payment Type: " + payment.getPaymentType() +
                               ", Card Number: " + payment.getCardNumber() +
                               ", Card Holder Name: " + payment.getCardHolderName() + "\n");
        }
		
		// get user1 transaction history
		List<TransactionDO> user1TransactionHistory =  transactionController.getTransactionHistory(user1Id);
		System.out.println("Transaction History for User 1:");
		for (TransactionDO trxn : user1TransactionHistory) {
            System.out.println("Transaction ID: " + trxn.getTransactionId() +
                               ", Amount: " + trxn.getAmount() +
                               ", Status: " + trxn.getTransactionStatus() +
                               ", Sender ID: " + trxn.getSenderId() +
                               ", Receiver ID: " + trxn.getReceiverId() + "\n");
        }
		
		// get user2 transaction history
		List<TransactionDO> user2TransactionHistory =  transactionController.getTransactionHistory(user2Id);
		System.out.println("Transaction History for User 2:");
		for (TransactionDO trxn : user2TransactionHistory) {
            System.out.println("Transaction ID: " + trxn.getTransactionId() +
                               ", Amount: " + trxn.getAmount() +
                               ", Status: " + trxn.getTransactionStatus() +
                               ", Sender ID: " + trxn.getSenderId() +
                               ", Receiver ID: " + trxn.getReceiverId() + "\n");
        }
		
	}
}
