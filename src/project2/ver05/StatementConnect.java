package project2.ver05;

public interface StatementConnect {
	String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	String ORCLE_URL = "jdbc:oracle:thin://@localhost:1521:orcl";
	
	void connect(String user, String pass) ;//DB연결
	void close(); //자원 반납
	void insert(Account account);
	void update(int balance, String accountNum);
	
	String scanValue(String title);
	
	
}
