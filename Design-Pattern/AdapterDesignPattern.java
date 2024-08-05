interface Currency {
    public double getCurrencyInDollar();
}

class CurrencyAdaptee implements Currency {
    double amount = 0.0;
    
    CurrencyAdaptee(double amount) {
        this.amount = amount;
    }
    public double getCurrencyInDollar() {
        return amount;
    }
}

interface CurrencyAdapter {
    public double convertCurrency();
}

class CurrencyRupees implements CurrencyAdapter {
    
    Currency currency;
    
    public CurrencyRupees(Currency currency) {
        this.currency = currency;
    }
    
    public double convertCurrency() {
        double amount = currency.getCurrencyInDollar();
        
        return amount * 83.99;
    }
}

class CurrencyPound implements CurrencyAdapter {
    
    Currency currency;
    
    public CurrencyPound(Currency currency) {
        this.currency = currency;
    }
    
    public double convertCurrency() {
        double amount = currency.getCurrencyInDollar();
        
        return amount * 0.78;
    }
}

class AdapterDesignPattern {
    public static void main(String args[]) {
        
        Double dollarAmount = 200.00;
        Currency currency = new CurrencyAdaptee(dollarAmount);
        
        CurrencyAdapter toRupees = new CurrencyRupees(currency);
        double rupeeAmount = toRupees.convertCurrency();
        System.out.println("dollar " + dollarAmount + " to rupees is " + rupeeAmount);
        
        CurrencyAdapter toPound = new CurrencyPound(currency);
        double pountAmount = toPound.convertCurrency();
        System.out.println("dollar " + dollarAmount + " to pounds is " + pountAmount);
    }
}
