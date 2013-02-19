package com.ea.throttle.mock;

enum CardType{
	VISA,
	MASTER,
	PAYPAL,;
}

enum TransactionType{
	DEBIT,
	CREDIT,
}

public class FinInst{

	String cardNumber;
	CardType cardType;   // Either VISA or MASTERCARD or PAYPAL
	TransactionType transactionType;  // Either DEBIT or CREDIT

	// Generate random Financial Instrument
	FinInst(String cardNumber){
		this.cardNumber = cardNumber;
		this.cardType = Base.randomEnum(CardType.class);
		this.transactionType = Base.randomEnum(TransactionType.class);
	}

}