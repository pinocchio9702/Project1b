package project2.ver05;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class StatementConnectDB implements StatementConnect{
	public static Connection con;
	public static PreparedStatement psmt; //동적쿼리를 처리를 위한 객체
	public static ResultSet rs;
	
	public StatementConnectDB(String diver, String user, String pass) {
		System.out.println("생성자 호출");
		try {
			Class.forName(ORACLE_DRIVER);
			connect(user, pass);
		}
		catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
			e.printStackTrace();
		}
	}

	
}
