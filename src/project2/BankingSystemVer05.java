package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import project2.ver05.Account;
import project2.ver05.Game3by3;
import project2.ver05.MenuChoice;

public class BankingSystemVer05 implements MenuChoice {

	public static Connection con;
	public static PreparedStatement psmt;
	public static ResultSet rs;
	public static Game3by3 game = new Game3by3();

	public BankingSystemVer05() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "kosmo", "1234");
		} catch (ClassNotFoundException e) {
			System.out.println("오라클 드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("DB 연결 실패");
		} catch (Exception e) {
			System.out.println("알수 없는 예외발생");
		}
	}

	static Account[] account = new Account[50];

	static int AccountNum = 0;
	static Scanner scan = new Scanner(System.in);

	// 계좌개설을 위한 함수
	static void makeAccount() {
		System.out.println("****신규계좌 개설****");
		System.out.print("계좌번호 : ");
		String accountNUm = scan.nextLine();
		System.out.print("고객이름 : ");
		String name = scan.nextLine();
		System.out.print("잔고 : ");
		int balance = scan.nextInt();
		scan.nextLine();

		account[AccountNum] = new Account(accountNUm, name, balance);
		try {

			BankingSystemVer05 bankingSystemVer05 = new BankingSystemVer05();

			String query = "INSERT INTO banking_tb VALUES(?, ?, ?)";

			psmt = con.prepareStatement(query);

			psmt.setNString(1, accountNUm);
			psmt.setNString(2, name);
			psmt.setLong(3, balance);

			int affected = psmt.executeUpdate();

			System.out.println(affected + "행이 입력되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null)
					con.close();
				if (psmt != null)
					psmt.close();

				System.out.println("자원반납 완료");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 입 금
	static void depositMoney() {
		System.out.println("----------차니뱅크에 입금합니다----------");
		System.out.println("먼저 계좌번호를 입력해주세요");
		System.out.print("계좌번호 : ");
		String checkAccountNum = scan.nextLine();
		System.out.println("입금할 금액을 입력해주세요");
		System.out.print("입금액 : ");
		int accountadd = scan.nextInt();

		for (int i = 0; i < AccountNum; i++) {
			if (account[i].accountNum.equals(checkAccountNum)) {

				account[i].balance += accountadd;

				BankingSystemVer05 bankingSystemVer05 = new BankingSystemVer05();

				String sql = "UPDATE banking_tb SET balance=? WHERE accountnum=?";

				try {

					psmt = con.prepareStatement(sql);
					psmt.setLong(1, account[i].balance);
					psmt.setString(2, checkAccountNum);

					int affected = psmt.executeUpdate();
					System.out.println(affected + "행이 업데이트 되었습니다.");

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (con != null)
							con.close();
						if (psmt != null)
							psmt.close();

						System.out.println("자원반납 완료");
					} catch (Exception e) {
						// TODO: handle exSystem.out.println("자원반납시 오류발생");
						e.printStackTrace();
					}
				}
				System.out.println("입금이 완료되었습니다 현재 금액 : " + account[i].balance);
			}
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

		for (int i = 0; i < AccountNum; i++) {
			if (account[i].accountNum.equals(checkAccountNum)) {

				account[i].balance -= accountmin;

				BankingSystemVer05 bankingSystemVer05 = new BankingSystemVer05();

				String sql = "UPDATE banking_tb SET balance=? WHERE accountnum=?";

				try {
					psmt = con.prepareStatement(sql);

					psmt.setLong(1, account[i].balance);
					psmt.setString(2, checkAccountNum);

					int affected = psmt.executeUpdate();
					System.out.println(affected + "행이 업데이트 되었습니다.");

				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (con != null)
							con.close();
						if (psmt != null)
							psmt.close();

						System.out.println("자원반납 완료");
					} catch (Exception e) {
						// TODO: handle exSystem.out.println("자원반납시 오류발생");
						e.printStackTrace();
					}
				}
				System.out.println("입금이 완료되었습니다 현재 금액 : " + account[i].balance);
			}
		}
	}

	// 전체계좌정보출력
	static void showAccInfo() {
		System.out.println("차니뱅크안에 있는 계좌전체정보를 출력합니다!!!!");
		try {

			BankingSystemVer05 bankingSystemVer05 = new BankingSystemVer05();

			String sql = "SELECT * FROM banking_tb";

			psmt = con.prepareStatement(sql);

			rs = psmt.executeQuery();

			while (rs.next()) {
				String accountnum = rs.getString(1);
				String name = rs.getString(2);
				String balance = rs.getString(3);

				System.out.printf("%s %s %s\n", accountnum, name, balance);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

//		for (int i = 0; i < AccountNum; i++) {
//			System.out.println("-----------------" + i + 1 + "번째 계좌-------------------");
//			System.out.println("계좌번호 : " + account[i].accountNum);
//			System.out.println("고객 이름 : " + account[i].name);
//			System.out.println("잔고 : " + account[i].balance);
//			System.out.println();
//		}
	}

	// 메뉴 출력
	static void showMenu() {
		System.out.println("~~~~~차니뱅크에 오신걸 환영합니다!!!!~~~");
		System.out.println("1. 계좌 계설");
		System.out.println("2. 입금");
		System.out.println("3. 출금");
		System.out.println("4. 전체 계좌정보 출력");
		System.out.println("5. 3 by 3 퍼즐게임");
		System.out.println("6. 프로그램 종료");
	}

	public static void main(String[] args) {

		while (true) {
			showMenu();

			System.out.print("선택 : ");
			int choseMenu = scan.nextInt();
			System.out.println();
			scan.nextLine();
			if (choseMenu == MAKE) {
				makeAccount();
				AccountNum++;
			} else if (choseMenu == DEPOSIT) {
				depositMoney();
			} else if (choseMenu == WITHDRAW) {
				withdrawMoney();
			} else if (choseMenu == INQUIRE) {
				showAccInfo();
			} else if (choseMenu == GAME3BY3) {
				game.init();
			} else if (choseMenu == EXIT) {
				break;
			}
		}
	}

}
