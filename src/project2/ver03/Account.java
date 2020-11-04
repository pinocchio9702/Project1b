package project2.ver03;

public abstract class Account{
	public String accountNum; //계좌번호
	public String name; //이름
	public int balance; //잔고
	
	
	public Account(String accountNum, String name, int balance) {
		this.accountNum = accountNum;
		this.name = name;
		this.balance = balance;
	}
}