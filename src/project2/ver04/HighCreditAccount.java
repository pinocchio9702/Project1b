package project2.ver04;

public class HighCreditAccount extends Account{

	int interest;//이자률
	String credit;
	
	public HighCreditAccount(String accountNum, String name, int balance, int interset, String credit) {
		super(accountNum,name,balance);
		this.interest = interset;
		this.credit = credit;
	}
}
