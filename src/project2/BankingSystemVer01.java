package project2;

import java.util.Scanner;
import project2.ver01.Account;
import project2.ver01.MenuChoice;



public class BankingSystemVer01 implements MenuChoice{
	
	static Account[] account = new Account[50];
	static int AccountNum = 0;
	static Scanner scan = new Scanner(System.in);
	
	//계좌개설을 위한 함수
	static void makeAccount() {
		System.out.println("****신규계좌 개설****");
		System.out.print("계좌번호 : ");
		String accountNUm = scan.nextLine();
		System.out.print("고객이름 : ");
		String name = scan.nextLine();
		System.out.print("잔고 : ");
		int balance = scan.nextInt();
		
		account[AccountNum] = new Account(accountNUm, name, balance);
		
	}
	
	// 입    금
	static void depositMoney() {
		System.out.println("----------차니뱅크에 입금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("입금할 금액을 입력해주세요");
		System.out.print("입금액 : ");
		int accountadd = scan.nextInt();
		
		
		for(int i = 0; i < AccountNum; i++) {
			if(account[i].accountNum.equals(checkAccountNum)) {
				account[i].balance += accountadd;
				System.out.println("입금이 완료되었습니다.");
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
			else{
				System.out.println("계좌번호를 잘못 입력하셨습니다.");
			}
		}
	}
	
	 // 전체계좌정보출력
	static void showAccInfo() {
		System.out.println("차니뱅크안에 있는 계좌전체정보를 출력합니다!!!!");
		
		for(int i = 0; i < AccountNum; i++) {
			System.out.println("-----------------" + i+1  + "번째 계좌-------------------");
			System.out.println("계좌번호 : " + account[i].accountNum);
			System.out.println("고객 이름 : " + account[i].name);
			System.out.println("잔고 : " + account[i].balance);
			System.out.println();
		}
	}
	
	
	//메뉴 출력
	static void showMenu() {
		System.out.println("~~~~~차니뱅크에 오신걸 환영합니다!!!!~~~");
		System.out.println("1. 계좌 계설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체 계좌정보 출력");
		System.out.println("5. 프로그램 종료");
	}
	
	public static void main(String[] args) {
		
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
