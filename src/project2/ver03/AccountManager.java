package project2.ver03;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AccountManager implements MenuChoice {
	//static HashSet<Account> account = new HashSet<Account>();
	static Account[] account = new Account[50];
	static int AccountNum = 0;
	static Scanner scan = new Scanner(System.in);

	// 계좌개설을 위한 함수
	public static void makeAccount() {
		System.out.println("****신규계좌 개설****");
		System.out.println("1.보통계좌");
		System.out.println("2.신용신뢰계좌");
		int choice = scan.nextInt();
		scan.nextLine();
		if (choice == 1) {
			System.out.print("계좌번호 : ");
			String accountNUm = scan.nextLine();
			System.out.print("고객이름 : ");
			String name = scan.nextLine();
			System.out.print("잔고 : ");
			int balance = scan.nextInt();
			System.out.print("기본이자%(정수형태로 입력) :");
			int interset = scan.nextInt();

			account[AccountNum] = new NormalAccount(accountNUm, name, balance, interset);
			
			System.out.println("계좌개설이 완료되었습니다.");
		}

		else if (choice == 2) {
			System.out.print("계좌번호 : ");
			String accountNUm = scan.nextLine();
			System.out.print("고객이름 : ");
			String name = scan.nextLine();
			System.out.print("잔고 : ");
			int balance = scan.nextInt();
			System.out.print("기본이자%(정수형태로 입력) :");
			int interset = scan.nextInt();
			scan.nextLine();
			System.out.print("신용등급(A,B,C등급) : ");
			String credit = scan.nextLine();

			account[AccountNum] = new HighCreditAccount(accountNUm, name, balance, interset, credit);

			System.out.println("계좌개설이 완료되었습니다.");
		}

//		System.out.print("계좌번호 : ");
//		String accountNUm = scan.nextLine();
//		System.out.print("고객이름 : ");
//		String name = scan.nextLine();
//		System.out.print("잔고 : ");
//		int balance = scan.nextInt();

	}

	// 입 금
	public static void depositMoney() {
		System.out.println("----------차니뱅크에 입금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("입금할 금액을 입력해주세요");
		System.out.print("입금액 : ");
		try {
			int accountadd = scan.nextInt();
			while (true) {
				if (accountadd < 0 || accountadd % 500 != 0) {
					System.out.println("양수의 금액과 500단위수의 금액만 입금 가능합니다. 다시 입금해주세요");
					System.out.print("입금액 : ");
					accountadd = scan.nextInt();
				} else {
					break;
				}
			}

			for (int i = 0; i < AccountNum; i++) {
				if (account[i].accountNum.equals(checkAccountNum)) {
					if (account[i] instanceof NormalAccount) {
						account[i].balance = (int) (account[i].balance
								+ (account[i].balance * (((NormalAccount) account[i]).interest * 0.01)) + accountadd);
						System.out.println("입금이 완료되었습니다.");
					}
					if (account[i] instanceof HighCreditAccount) {
						if (((HighCreditAccount) account[i]).credit.equalsIgnoreCase("A")) {
							account[i].balance = (int) (account[i].balance
									+ (account[i].balance * (((HighCreditAccount) account[i]).interest * 0.01))
									+ (account[i].balance * 0.07) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						} else if (((HighCreditAccount) account[i]).credit.equalsIgnoreCase("B")) {
							account[i].balance = (int) (account[i].balance
									+ (account[i].balance * (((HighCreditAccount) account[i]).interest * 0.01))
									+ (account[i].balance * 0.04) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						} else if (((HighCreditAccount) account[i]).credit.equalsIgnoreCase("C")) {
							account[i].balance = (int) (account[i].balance
									+ (account[i].balance * (((HighCreditAccount) account[i]).interest * 0.01))
									+ (account[i].balance * 0.02) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						}
					}
				} else {
					System.out.println("계좌번호를 잘못 입력하셨습니다.");
				}
			}
		} 
		catch (InputMismatchException e) {
			e.printStackTrace();
		}

	}


	// 출 금
	static void withdrawMoney() {
		System.out.println();
		System.out.println("----------차니뱅크에 출금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("출금할 금액을 입력해주세요");
		System.out.print("출금액 : ");
		int accountmin = scan.nextInt();
		while(true) {
			if(accountmin < 0 || accountmin % 1000 != 0) {
				System.out.println("양수금액과 1000단위 금액만 출금할 수 있습니다.. 다시 출금액을 입력해주세요");
				System.out.print("출금액 : ");
				accountmin = scan.nextInt();
				scan.nextLine();
			}
			else {
				break;
			}
		}
		

		for (int i = 0; i < AccountNum; i++) {
			if(accountmin > account[i].balance) {
				System.out.println("**잔고가 부족합니다. 금액 전체를 출금할까요?(YES or NO)");
				scan.nextLine();
				String balCheck = scan.nextLine();
				
				if(balCheck.equalsIgnoreCase("YES")) {
					account[i].balance = 0;
					System.out.println("전체 금액이 출금되었습니다.");
				}
				else {
					break;
				}
				
			}
			else if (account[i].accountNum.equals(checkAccountNum)) {
				account[i].balance -= accountmin;
				System.out.println("출금이 완료되었습니다.");
			}

		}
	}

	// 전체계좌정보출력
	static void showAccInfo() {
		System.out.println("차니뱅크안에 있는 계좌전체정보를 출력합니다!!!!");

		for (int i = 0; i < AccountNum; i++) {
			if (account[i] instanceof NormalAccount) {
				System.out.println("-----------------" + i + (int) 1 + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + account[i].accountNum);
				System.out.println("고객 이름 : " + account[i].name);
				System.out.println("잔고 : " + account[i].balance);
				System.out.println("이자률 : " + ((NormalAccount) account[i]).interest + "%");
				System.out.println();
			} else if (account[i] instanceof HighCreditAccount) {
				System.out.println("-----------------" + i + 1 + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + account[i].accountNum);
				System.out.println("고객 이름 : " + account[i].name);
				System.out.println("잔고 : " + account[i].balance);
				System.out.println("이자률 : " + ((HighCreditAccount) account[i]).interest + "%");
				System.out.println("신용등급 : " + ((HighCreditAccount) account[i]).credit);
				System.out.println();
			}
		}
	}

	// 메뉴 출력
	public static void showMenu() {
		System.out.println("~~~~~차니뱅크에 오신걸 환영합니다!!!!~~~");
		System.out.println("1. 계좌 계설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체 계좌정보 출력");
		System.out.println("5. 프로그램 종료");
	}

	public static void init() {

		try{
			while(true){
				showMenu();
			
				System.out.print("선택 : ");
				int choseMenu = scan.nextInt();
				System.out.println();
				scan.nextLine();

				if (choseMenu > 5 || choseMenu < 1) {
					MenuSelectException ex = new MenuSelectException();
					throw ex;
				}

				if (choseMenu == MAKE) {
					makeAccount();
					AccountNum++;
				} else if (choseMenu == DEPOSIT) {
					depositMoney();
				} else if (choseMenu == WITHDRAW) {
					withdrawMoney();
				} else if (choseMenu == INQUIRE) {
					showAccInfo();
				} else if (choseMenu == EXIT) {
					break;
				}
			}
			
		} 
		catch (MenuSelectException e) {
			e.printStackTrace();
		}
		catch (InputMismatchException e) {
			System.out.println("[예외발생]" + e.getMessage());
			e.printStackTrace();
		}
		
	}
}
