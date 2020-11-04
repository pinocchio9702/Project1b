package project2.ver02;

import java.util.Scanner;

import project2.ver02.Account;
import project2.ver02.NormalAccount;
import project2.ver02.HighCreditAccount;

public class AccountManager implements MenuChoice{
	static Account[] account = new Account[50];
	static int AccountNum = 0;
	static Scanner scan = new Scanner(System.in);
	
	//계좌개설을 위한 함수
	public static void makeAccount() {
		System.out.println("****신규계좌 개설****");
		System.out.println("1.보통계좌");
		System.out.println("2.신용신뢰계좌");
		int choice = scan.nextInt();
		scan.nextLine();
		if(choice == 1) {
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
		
		else if(choice == 2) {
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
	
	// 입    금
	public static void depositMoney() {
		System.out.println("----------차니뱅크에 입금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("입금할 금액을 입력해주세요");
		System.out.print("입금액 : ");
		int accountadd = scan.nextInt();
		
		
		for(int i = 0; i < AccountNum; i++) {
			if(account[i].accountNum.equals(checkAccountNum)) {
				if(account[i] instanceof NormalAccount) {
					account[i].balance = (int) (account[i].balance + (account[i].balance *(((NormalAccount)account[i]).interest * 0.01)) + accountadd);
					System.out.println("입금이 완료되었습니다.");
				}
				if(account[i] instanceof HighCreditAccount) {
					if(((HighCreditAccount)account[i]).credit.equalsIgnoreCase("A")) {
						account[i].balance = (int)(account[i].balance + (account[i].balance *(((HighCreditAccount)account[i]).interest * 0.01)) + 
								(account[i].balance * 0.07) + accountadd);
					}
					else if(((HighCreditAccount)account[i]).credit.equalsIgnoreCase("B")) {
						account[i].balance = (int)(account[i].balance + (account[i].balance *(((HighCreditAccount)account[i]).interest * 0.01)) + 
								(account[i].balance * 0.04) + accountadd);
					}
					else if(((HighCreditAccount)account[i]).credit.equalsIgnoreCase("C")) {
						account[i].balance = (int)(account[i].balance + (account[i].balance *(((HighCreditAccount)account[i]).interest * 0.01)) + 
								(account[i].balance * 0.02) + accountadd);
					}
				}
			}
			else{
				System.out.println("계좌번호를 잘못 입력하셨습니다.");
			}
		}
		
	}
	
	// 출    금
	static void withdrawMoney() {
		System.out.println();
		System.out.println("----------차니뱅크에 출금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("출금할 금액을 입력해주세요");
		System.out.print("출금액 : ");
		int accountmin = scan.nextInt();
		
		
		for(int i = 0; i < AccountNum; i++) {
			if(account[i].accountNum.equals(checkAccountNum)) {
				account[i].balance -= accountmin;
				System.out.println("출금이 완료되었습니다.");
			}
		
		}
	}
	
	 // 전체계좌정보출력
	static void showAccInfo() {
		System.out.println("차니뱅크안에 있는 계좌전체정보를 출력합니다!!!!");
		
		for(int i = 0; i < AccountNum; i++) {
			if(account[i] instanceof NormalAccount) {
				System.out.println("-----------------" + i+ (int)1  + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + account[i].accountNum);
				System.out.println("고객 이름 : " + account[i].name);
				System.out.println("잔고 : " + account[i].balance);
				System.out.println("이자률 : " + ((NormalAccount)account[i]).interest + "%");
				System.out.println();
			}
			else if(account[i] instanceof HighCreditAccount) {
				System.out.println("-----------------" + i+1  + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + account[i].accountNum);
				System.out.println("고객 이름 : " + account[i].name);
				System.out.println("잔고 : " + account[i].balance);
				System.out.println("이자률 : " + ((HighCreditAccount)account[i]).interest + "%");
				System.out.println("신용등급 : " + ((HighCreditAccount)account[i]).credit);
				System.out.println();
			}
		}
	}
	
	
	//메뉴 출력
	public static void showMenu() {
		System.out.println("~~~~~차니뱅크에 오신걸 환영합니다!!!!~~~");
		System.out.println("1. 계좌 계설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체 계좌정보 출력");
		System.out.println("5. 프로그램 종료");
	}
	
	
	public static void init() {

		while(true) {
			showMenu();
			
			System.out.print("선택 : ");
			int choseMenu = scan.nextInt();
			System.out.println();
			scan.nextLine();
			if(choseMenu == MAKE) {
				makeAccount(); 
				AccountNum++;
			}
			else if(choseMenu == DEPOSIT) {
				depositMoney();
			}
			else if(choseMenu == WITHDRAW) {
				withdrawMoney();
			}
			else if(choseMenu == INQUIRE) {
				showAccInfo();
			}
			else if(choseMenu == EXIT) {
				break;
			}
		}

	}
}
