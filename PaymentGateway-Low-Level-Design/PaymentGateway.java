// work in progress. code is not completed yet.



import java.util.UUID;

enum PaymentType {
    BANK,
    CARD
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

abstract class PaymentObject {
    
    private String paymentObjectId;
    private PaymentType paymentType;
    private String userId;

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
    
    private String accountNumber;
    private String ifcsCode;
    private String accountHolderName;
    private String bankName;
    
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
    
    private String cardNumber;
    private String cardHolderName;
    private String cardCvv;
    private Date cardExpiry;
    
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

class PaymentObjectService {
    
    static Map<String, List<PaymentObject>> userPaymentObjectMap;
    
    public void addPaymentObject(PaymentObjectDO paymentObjectDO);
    
    public PaymentObjectDO getPaymentList(String userId);
}

class BankPaymentObjectService extends PaymentObjectService {
    
    public void addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        BankPaymentObject bankPaymentObject = new BankPaymentObject();
        bankPaymentObject.setPaymentObjectId(UUID.randomUUID().toString());
        bankPaymentObject.setPaymentType(PaymentType.BANK);
        bankPaymentObject.setUserId(paymentObjectDO.userId);
        bankPaymentObject.setAccountNumber(paymentObjectDO.accountNumber);
        bankPaymentObject.setAccountHolderName(paymentObjectDO.accountHolderName);
        bankPaymentObject.setIfcsCode(paymentObjectDO.ifcsCode);
        bankPaymentObject.setBankName(paymentObjectDO.bankName);
        
        List<PaymentObject> userPaymentObject = userPaymentObjectMap.get(paymentObjectDO.userId);
        userPaymentObject.add(bankPaymentObject);
        userPaymentObjectMap.put(paymentObjectDO.userId, userPaymentObject);
    }
    
    public List<PaymentObjectDO> getPaymentList(String userId) {
        
        List<PaymentObject> userPaymentObject = userPaymentObjectMap.get(userId);
        List<PaymentObjectDO> userPaymentObjectDO = new ArrayList<>();
        
        for(PaymentObject paymentObject : userPaymentObject) {
            if(paymentObject.paymentType == PaymentType.BANK) {
                userPaymentObjectDO.add(convertToDO((BankPaymentObject) paymentObject));
            }
        }
        
        return userPaymentObjectDO;
    }
    
    public PaymentObjectDO convertToDO(BankPaymentObject bankPaymentObject) {
        
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
    
    public void addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        CardPaymentObject cardPaymentObject = new CardPaymentObject();
        cardPaymentObject.setPaymentObjectId(UUID.randomUUID().toString());
        cardPaymentObject.setPaymentType(PaymentType.BANK);
        cardPaymentObject.setUserId(paymentObjectDO.userId);
        cardPaymentObject.setCardHolderName(paymentObjectDO.cardHolderName);
        cardPaymentObject.setCardNumber(paymentObjectDO.cardNumber);
        cardPaymentObject.setCardCvv(paymentObjectDO.cardCvv);
        cardPaymentObject.setCardExpiry(paymentObjectDO.cardExpiry);
        
        List<PaymentObject> userPaymentObject = userPaymentObjectMap.get(paymentObjectDO.userId);
        userPaymentObject.add(cardPaymentObject);
        userPaymentObjectMap.put(paymentObjectDO.userId, userPaymentObject);
    }
    
    public List<PaymentObjectDO> getPaymentList(String userId) {
        
        List<PaymentObject> userPaymentObject = userPaymentObjectMap.get(userId);
        List<PaymentObjectDO> userPaymentObjectDO = new ArrayList<>();
        
        for(PaymentObject paymentObject : userPaymentObject) {
            if(paymentObject.paymentType == PaymentType.BANK) {
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
    
    public void addPaymentObject(PaymentObjectDO paymentObjectDO) {
        
        PaymentType paymentType = paymentObjectDO.paymentType;
        PaymentObjectService paymentObjectService = paymentObjectFactory.getPaymentObjectService(paymentType);
        paymentObjectService.addPaymentObject(paymentObjectDO);
        
    }
    
    public PaymentObjectDO getPaymentObject(String userId, String paymentObjectId) {
        PaymentObjectService paymentObjectBankService = paymentObjectFactory.getPaymentObjectService(PaymentType.BANK);
        List<PaymentObjectDO> paymentObjectBankList = paymentObjectBankList.getPaymentList(userId);
        for(PaymentObjectDO paymentObjectDO : paymentObjectBankList) {
            if(paymentObjectDO.paymentObjectId == paymentObjectId) {
                return paymentObjectDO;
            }
        }
        
        PaymentObjectService paymentObjectCardService = paymentObjectFactory.getPaymentObjectService(PaymentType.CARD);
        List<PaymentObjectDO> paymentObjectCardList = paymentObjectBankList.getPaymentList(userId);
        for(PaymentObjectDO paymentObjectDO : paymentObjectCardList) {
            if(paymentObjectDO.paymentObjectId == paymentObjectId) {
                return paymentObjectDO;
            }
        }
        
        return null;
    }
}

public class PaymentGateway {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}
