abstract class Message {
    
    IMessageSender messageSender;
    
    public Message(IMessageSender messageSender) {
        this.messageSender = messageSender;
    }
    
    public abstract void sendMessage();
}

class SMSMessage extends Message {
    
    SMSMessage(IMessageSender messageSender) {
        super(messageSender);
    }
    
    public void sendMessage() {
        messageSender.send();
    }
}

class EmailMessage extends Message {
    
    EmailMessage(IMessageSender messageSender) {
        super(messageSender);
    }
    
    public void sendMessage() {
        messageSender.send();
    }
}

class PagerMessage extends Message {
    
    PagerMessage(IMessageSender messageSender) {
        super(messageSender);
    }
    
    public void sendMessage() {
        messageSender.send();
    }
}

// bridge
interface IMessageSender {
    
    public void send();
}

class SMSSender implements IMessageSender {
    public void send() {
        System.out.println("message is send through sms....");
    }
}

class EmailSender implements IMessageSender {
    public void send() {
        System.out.println("message is send through email....");
    }
}

class PagerSender implements IMessageSender {
    public void send() {
        System.out.println("message is send through pager....");
    }
}

class BridgeDesignPattern {
    public static void main(String args[]) {
        
        IMessageSender messageSMSSender = new SMSSender();
        IMessageSender messageEmailSender = new EmailSender();
        
        SMSMessage smsMessage = new SMSMessage(messageSMSSender);
        smsMessage.sendMessage();
        
        EmailMessage emailMessage = new EmailMessage(messageEmailSender);
        emailMessage.sendMessage();
        
    }
}
