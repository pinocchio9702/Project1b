package project2.ver04;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

public class AutoSaverT extends Thread {

	//static String path = "src/project2/AutoSaveAccount.txt";

	@Override
	public void run() {

		try {
			while (true) {

				AccountManager.saveTxt();

				sleep(5000);
				System.out.println();
				System.out.println("5초마다 자동저장되었습니다.");

			}
		}

		catch (InterruptedException e) {
			System.out.println("자동저장 종료");
		} catch (IOException e) {
			System.out.println("뭔가 없음");
		}

	}

}
