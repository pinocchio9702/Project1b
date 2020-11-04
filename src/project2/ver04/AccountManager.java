package project2.ver04;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class AccountManager implements MenuChoice {
	static HashSet<Account> account = new HashSet<Account>();
	// static Account[] account = new Account[50];
	static Scanner scan = new Scanner(System.in);
	static int count = 0;
	static String path = "src/project2/AccountInfo.obj";
	static String pathText = "src/project2/AutoSaveAccount.txt";
	static AutoSaverT autoSaverT = new AutoSaverT();
	

	// 계좌개설을 위한 함수
	public static void makeAccount() {
		System.out.println("****신규계좌 개설****");
		System.out.println("1.보통계좌");
		System.out.println("2.신용신뢰계좌");
		int choice = scan.nextInt();
		scan.nextLine();
		if (choice == 1) {
			System.out.print("계좌번호 : ");
			String accountNum = scan.nextLine();
			System.out.print("고객이름 : ");
			String name = scan.nextLine();
			System.out.print("잔고 : ");
			int balance = scan.nextInt();
			System.out.print("기본이자%(정수형태로 입력) :");
			int interset = scan.nextInt();
			scan.nextLine();
			Account checkaccount = new NormalAccount(accountNum, name, balance, interset);

			if (account.add(new NormalAccount(accountNum, name, balance, interset))) {
				System.out.println("계좌개설이 완료되었습니다.");
			} else {
				System.out.println("중복계좌가 발견되었습니다. 덮어쓸까요?(y or n)");
				String overlap = scan.nextLine();
				if (overlap.equalsIgnoreCase("y")) {
					account.remove(checkaccount);
					account.add(checkaccount);
					System.out.println("계좌개설이 완료되었습니다.");
				} else if (overlap.equalsIgnoreCase("n")) {
					System.out.println("기존에 있던 계좌를 유지합니다.");
				}

			}

		}

		else if (choice == 2) {
			System.out.print("계좌번호 : ");
			String accountNum = scan.nextLine();
			System.out.print("고객이름 : ");
			String name = scan.nextLine();
			System.out.print("잔고 : ");
			int balance = scan.nextInt();
			System.out.print("기본이자%(정수형태로 입력) :");
			int interset = scan.nextInt();
			scan.nextLine();
			System.out.print("신용등급(A,B,C등급) : ");
			String credit = scan.nextLine();

			Account checkaccount = new HighCreditAccount(accountNum, name, balance, interset, credit);

			if (account.add(new HighCreditAccount(accountNum, name, balance, interset, credit))) {
				System.out.println("계좌개설이 완료되었습니다.");
			} else {
				System.out.println("중복계좌가 발견되었습니다. 덮어쓸까요?(y or n)");
				String overlap = scan.nextLine();
				if (overlap.equalsIgnoreCase("y")) {
					account.remove(checkaccount);
					account.add(checkaccount);
					System.out.println("계좌개설이 완료되었습니다.");
				} else if (overlap.equalsIgnoreCase("n")) {
					System.out.println("기존에 있던 계좌를 유지합니다.");
				}

			}

		}
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

			Iterator itr = account.iterator();
			while (itr.hasNext()) {
				Account account = (Account) itr.next();

				if (account.accountNum.equals(checkAccountNum)) {
					if (account instanceof NormalAccount) {
						account.balance = (int) (account.balance
								+ (account.balance * (((NormalAccount) account).interest * 0.01)) + accountadd);
						System.out.println("입금이 완료되었습니다.");
					}
					if (account instanceof HighCreditAccount) {
						if (((HighCreditAccount) account).credit.equalsIgnoreCase("A")) {
							account.balance = (int) (account.balance
									+ (account.balance * (((HighCreditAccount) account).interest * 0.01))
									+ (account.balance * 0.07) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						} else if (((HighCreditAccount) account).credit.equalsIgnoreCase("B")) {
							account.balance = (int) (account.balance
									+ (account.balance * (((HighCreditAccount) account).interest * 0.01))
									+ (account.balance * 0.04) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						} else if (((HighCreditAccount) account).credit.equalsIgnoreCase("C")) {
							account.balance = (int) (account.balance
									+ (account.balance * (((HighCreditAccount) account).interest * 0.01))
									+ (account.balance * 0.02) + accountadd);
							System.out.println("입금이 완료되었습니다.");
						}
					}
				}
			}
		} catch (InputMismatchException e) {
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
		while (true) {
			if (accountmin < 0 || accountmin % 1000 != 0) {
				System.out.println("양수금액과 1000단위 금액만 출금할 수 있습니다.. 다시 출금액을 입력해주세요");
				System.out.print("출금액 : ");
				accountmin = scan.nextInt();
				scan.nextLine();
			} else {
				break;
			}
		}

		Iterator itr = account.iterator();
		while (itr.hasNext()) {
			Account account = (Account) itr.next();
			if (accountmin > account.balance) {
				System.out.println("**잔고가 부족합니다. 금액 전체를 출금할까요?(YES or NO)");
				scan.nextLine();
				String balCheck = scan.nextLine();

				if (balCheck.equalsIgnoreCase("YES")) {
					account.balance = 0;
					System.out.println("전체 금액이 출금되었습니다.");
				} else {
					break;
				}

			} else if (account.accountNum.equals(checkAccountNum)) {
				account.balance -= accountmin;
				System.out.println("출금이 완료되었습니다.");
			}

		}
	}

	static void saveCount() {
		// account을 가져오는 스트림객체
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(path));

			Account saveAccount = null;

			while (true) {
				saveAccount = (Account) in.readObject();

				if (saveAccount == null) {
					break;
				}
				account.add(saveAccount);
			}

			in.close();

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {
			System.out.println("뭔가 없음");
		}
	}

	// 전체계좌정보출력
	static void showAccInfo() throws FileNotFoundException, IOException, ClassNotFoundException {
		int i = 0;
		System.out.println("차니뱅크안에 있는 계좌전체정보를 출력합니다!!!!");

		Iterator itr = account.iterator();
		while (itr.hasNext()) {
			Account count = (Account) itr.next();

			if (count instanceof NormalAccount) {
				System.out.println("-----------------" + i + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + count.accountNum);
				System.out.println("고객 이름 : " + count.name);
				System.out.println("잔고 : " + count.balance);
				System.out.println("이자률 : " + ((NormalAccount) count).interest + "%");
				System.out.println();
			} else if (count instanceof HighCreditAccount) {
				System.out.println("-----------------" + i + "번째 계좌-------------------");
				System.out.println("계좌번호 : " + count.accountNum);
				System.out.println("고객 이름 : " + count.name);
				System.out.println("잔고 : " + count.balance);
				System.out.println("이자률 : " + ((HighCreditAccount) count).interest + "%");
				System.out.println("신용등급 : " + ((HighCreditAccount) count).credit);
				System.out.println();
			}

			i++;
		}

	}

	static void saveTxt() throws IOException {

		PrintWriter out = new PrintWriter(new FileWriter(pathText));

		Iterator itr = account.iterator();

		
		while (itr.hasNext()) {
			Account account = (Account) itr.next();


			
			if (account instanceof NormalAccount) {
				out.printf("계좌 : %s, 이름 : %s, 잔액 : %s,  이자 : %s", account.accountNum, account.name, account.balance, ((NormalAccount)account).interest);
				out.println();

			} else if (account instanceof HighCreditAccount) {
				out.printf("계좌 : %s, 이름 : %s, 잔액 : %s, 이자 : %s, 신용 : %s", account.accountNum, account.name, account.balance, ((HighCreditAccount)account).interest, ((HighCreditAccount)account).credit);
				out.println();
			}

		}

		out.close();
		
		
//	      try {
//	          PrintWriter out = new PrintWriter(new FileWriter(pathText));
//	          
//	          Iterator itr = account.iterator();
//	          
//	          while(itr.hasNext()) {
//	             
//	             
//	             Account ac = (Account)itr.next();
//	             out.println(ac.name+ac.accountNum+ac.balance);
//	          }
//	          
//	          out.close();
//	          
//	       }
//	       catch(FileNotFoundException e) {
//	          System.out.println("파일없음");
//	       }
//	       catch(IOException e) {
//	          System.out.println("뭔가없음");
//	       }
	    
		

	}

	// 쓰레드를 수동으로 할지 자동으로 할지 선택하는 함수
	static void ThreadOption() {

		// System.out.println(autoSaverT.getState());
		System.out.println("1.자동저장On");
		System.out.println("2.자동저장Off");
		int ThreadCheck = scan.nextInt();
		scan.nextLine();
		if (ThreadCheck == 1) {
			if(autoSaverT.isAlive()) {
				System.out.println("이미 자동저장이 실행중입니다.");
			} else {
				autoSaverT = new AutoSaverT();
				autoSaverT.setDaemon(true);
				autoSaverT.start();
			}
			
		} else if (ThreadCheck == 2) {
			autoSaverT.interrupt();
		}
	}

	// 메뉴 출력
	public static void showMenu() {
		System.out.println("~~~~~차니뱅크에 오신걸 환영합니다!!!!~~~");
		System.out.println("1. 계좌 계설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체 계좌정보 출력");
		System.out.println("5. 저장옵션");
		System.out.println("6. 프로그램 종료");
	}

	public static void init() {

		try {

			while (true) {

				showMenu();

				saveCount();

				System.out.print("선택 : ");
				int choseMenu = scan.nextInt();
				System.out.println();
				scan.nextLine();

				if (choseMenu > 6 || choseMenu < 1) {
					MenuSelectException ex = new MenuSelectException();
					throw ex;
				}

				if (choseMenu == MAKE) {
					makeAccount();
					// AccountNum++;
				} else if (choseMenu == DEPOSIT) {
					depositMoney();
				} else if (choseMenu == WITHDRAW) {
					withdrawMoney();
				} else if (choseMenu == INQUIRE) {
					showAccInfo();
				} else if (choseMenu == SAVEOPTION) {
					ThreadOption();
				} else if (choseMenu == EXIT) {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
					Iterator itr = account.iterator();
					while (itr.hasNext()) {
						Account savscount = (Account) itr.next();
						out.writeObject(savscount);
					}
					out.close();
					break;
				}
			}

		} catch (MenuSelectException e) {
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.out.println("[예외발생]" + e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("파일 없음");
		} catch (IOException e) {
			System.out.println("뭔가 없음");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 없음");
		}

	}
}
