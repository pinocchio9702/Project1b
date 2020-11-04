package project2.ver02;

public class NormalAccount extends Account{

	public int interest;//이자률
	
	public NormalAccount(String accountNum, String name, int balance, int interest) {
		super(accountNum,name,balance);
		this.interest = interest;
		
	}
}
