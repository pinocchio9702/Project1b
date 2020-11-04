package project2.ver05;


import java.util.Objects;
import java.util.Random;
import java.util.Scanner;


public class Game3by3 {

	static String[][] GameBored = { { "1", "2", "3" }, { "4", "5", "6" }, { "7", "8", "X" } };
	static String[][] endGameCheck = { { "1", "2", "3" }, { "4", "5", "6" }, { "7", "8", "X" } };
	
	static String temp;//x를 이동시키기 위한 변수
	static int x_first = 2;
	static int x_secend = 2;


	static void ShowBored() {

		System.out.println("=====================");

		for (int i = 0; i < GameBored.length; i++) {
			for (int j = 0; j < GameBored[i].length; j++) {
				System.out.printf("%3s", GameBored[i][j]);
			}
			System.out.println();
		}

		System.out.println("=====================");

	}// end of showBored

	static void find_X_index() {
		// x인덱스 찾기
		for (int i = 0; i < GameBored.length; i++) {
			for (int j = 0; j < GameBored[i].length; j++) {
				if (GameBored[i][j].equals("X")) {
					x_first = i;
					x_secend = j;
				}
			}
		}
	}

	static void moveLeft(boolean flag) {

		if (x_secend == 0) {
			if (flag == false) {
				System.out.println();
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxx이동불가xxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println();
			} else {
				moveRight(true);
			}
		} else {
			temp = GameBored[x_first][x_secend - 1];
			GameBored[x_first][x_secend - 1] = GameBored[x_first][x_secend];
			GameBored[x_first][x_secend] = temp;
		}

	}

	static void moveRight(boolean flag) {

		if (x_secend == 2) {
			if (flag == false) {
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxx이동불가xxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
			} else {
				moveLeft(true);
			}
		} else {
			temp = GameBored[x_first][x_secend + 1];
			GameBored[x_first][x_secend + 1] = GameBored[x_first][x_secend];
			GameBored[x_first][x_secend] = temp;
		}

	}

	static void moveUp(boolean flag) {

		if (x_first == 0) {
			if (flag == false) {
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxx이동불가xxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
			} else {
				moveDown(true);
			}
		} else {
			temp = GameBored[x_first - 1][x_secend];
			GameBored[x_first - 1][x_secend] = GameBored[x_first][x_secend];
			GameBored[x_first][x_secend] = temp;
		}

	}

	static void moveDown(boolean flag) {

		if (x_first == 2) {
			if (flag == false) {
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
				System.out.println("xxxxxxx이동불가xxxxxxxxxxx");
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxx");
			} else {
				moveUp(true);
			}
		} else {
			temp = GameBored[x_first + 1][x_secend];
			GameBored[x_first + 1][x_secend] = GameBored[x_first][x_secend];
			GameBored[x_first][x_secend] = temp;
		}

	}
	

	public static void init() {

		Scanner scan = new Scanner(System.in);
		Random rand = new Random();
		boolean gameFlag = true;
		
		
		//x를 섞는 for문
		for (int i = 0; i < 2; i++) {
			int Mix = rand.nextInt(3);
			
			if (Mix == 0) {
				
				moveLeft(true);
			} else if (Mix == 1) {
				
				moveRight(true);
			} else if (Mix == 2) {
				
				moveUp(true);
			} else if (Mix == 3) {
				
				moveDown(true);
			}
			
			if(Objects.deepEquals(GameBored, endGameCheck)) {
				i = 0;
			}
			
		}
		
		while (true) {
			
			// System.out.println("���� ����" + rand.nextInt(10)); 0 ~ 9

			//gameFlag = equalArr();
			
			//System.out.println(Objects.deepEquals(GameBored, endGameCheck));
			
			if(Objects.deepEquals(GameBored, endGameCheck)) {
				System.out.println("==^^정답입니다.^^==");
				ShowBored();
				System.out.print("재시작하시겠습니까?(y 누르면 재시작, 나머지는 종료)");
				String exit = scan.nextLine();
				if(exit.equalsIgnoreCase("y")) {
					
					for (int i = 0; i < 100; i++) {
						
						int Mix = rand.nextInt(3);
						
						if (Mix == 0) {
							moveLeft(true);
						} else if (Mix == 1) {
							moveRight(true);
						} else if (Mix == 2) {
							moveUp(true);
						} else if (Mix == 3) {
							moveDown(true);
						}
						
						if(Objects.deepEquals(GameBored, endGameCheck)) {
							i = 0;
						}
						
					}
					
					continue;
					
				}
				else {
					break;
				}
			}
			
			//게임보드를 보여줌
			ShowBored();

			//X의 인덱스 위치를 찾음
			find_X_index();

			System.out.println("[ 이동 ] a:Left, d:Right, w:Up, s:Down");
			System.out.println("[ 종료 ] x:Exit");
			System.out.print("키를 입력해주세요:");

			String MoveKey = scan.nextLine();

			if (MoveKey.equalsIgnoreCase("a")) {
				moveLeft(false);
			}

			else if (MoveKey.equalsIgnoreCase("d")) {
				moveRight(false);
			}

			else if (MoveKey.equalsIgnoreCase("w")) {
				moveUp(false);
			}

			else if (MoveKey.equalsIgnoreCase("s")) {
				moveDown(false);
			}

			else if (MoveKey.equalsIgnoreCase("x")) {
				break;
			}
			
		}

	}
}
