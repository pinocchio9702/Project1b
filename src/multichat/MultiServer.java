package multichat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MultiServer implements maxMember {

	boolean block = false;
	String blockName = "";

	// 맴버변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	// 클라이언트 정보저장을 위한 Map컬렉션 생성
	Map<String, PrintWriter> clientMap;

	HashSet<String> blackList = new HashSet<String>();

	HashSet<String> pWords = new HashSet<String>();

	public static Connection con;
	public static PreparedStatement psmt;
	public static ResultSet rs;

	// 생성자
	public MultiServer() {
		// 클라이언트의 이름과 출력스트림을 저장할 HashMap 컬렉션 생성
		clientMap = new HashMap<String, PrintWriter>();
		// 동기화 설정. 쓰레드가 사용자 정보에 동시에 접근하는 것을 차단
		Collections.synchronizedMap(clientMap);

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

	// 서버의 초기화를 담당할 메소드
	public void init() {

		blackList.add("siba");
		blackList.add("nojam");

		pWords.add("siba");
		pWords.add("nojam");

		try {
			/*
			 * 9999번 포트를 설정하여 서버객체를 생성하고 클라이언트의 접속을 대기한다.
			 */
			// 클라이언트의 접속을 대기
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			while (true) {
				// 접속요청 허가
				socket = serverSocket.accept();
				System.out.println(socket.getInetAddress() + "(클라이언트)의" + socket.getPort() + "  포트를 통해 "
						+ socket.getLocalAddress() + " (서버)의" + socket.getLocalPort() + " 포트로 연결됨");

				// 내부클래스의 객체 생성 및 쓰레드 시작
				// 객체를 생성해야 새로운 클라이언트가 하나의 소켓을 할당받을 수 있다.
				Thread mst = new MultiServerT(socket);

				/*
				 * mst 쓰레드의 역할 : 객체를 생성하고 그 객체를 실행하는 동안에 다른 역할을 할 수 없다. 그러므로 쓰레드를 생성해야한다.
				 */
				// mst.run();
				mst.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}

	// 접속된 모든 클라이언트에게 메세지를 전달하는 메소드
	public void sendAllMsg(String name, String msg, String flag) {

		// Map에 저장된 객체의 키값(접속자명)을 먼저 얻어온다.
		Iterator<String> it = clientMap.keySet().iterator();

		// 저장된 객체(클라이언트)의 갯수만큼 반복한다.
		while (it.hasNext()) {
			try {

				// 컬렉션의 key는 클라이언트의 접속자명이다.
				String clientName = it.next();

				PrintWriter it_out = (PrintWriter) clientMap.get(clientName);
				// it.next()가 이름을 나타내고 s가 메세지를 나타냄

				if (flag.equals("One")) {
					// Flag가 One이면 해당 클라이언트 한명에게만 전송
					if (name.equals(clientName)) {
						// 컬럭션에 저장된 접속자명과 일치하는 경우 메세지 전송

						// it_out은 clientName과 똑같은 이름만 보내준다.
						it_out.println(URLEncoder.encode("[귓속말]" + msg, "UTF-8"));
					}
				}

				else if (flag.equals("List")) {
					// Flag가 One이면 해당 클라이언트 한명에게만 전송
					if (name.equals(clientName)) {
						// 컬럭션에 저장된 접속자명과 일치하는 경우 메세지 전송

						// it_out은 clientName과 똑같은 이름만 보내준다.
						it_out.println(URLEncoder.encode("[접속자명]" + msg,"UTF-8"));

					}
				}
				
				else if (flag.equals("Block")) {
					
					if (name.equals(clientName)) {
						System.out.println(blockName);
						it_out.println(URLEncoder.encode("! " + blockName + " ! : " + msg,"UTF-8") );
					}
					
				}
				
				else if (flag.equals("Unblock")) {
					
					if (name.equals(clientName)) {
						System.out.println(blockName);
						it_out.println(URLEncoder.encode("? " + blockName + " ? : " + msg,"UTF-8") );
					}
					
				}

				else {
					if (name.equals(" ")) {
						// 접속, 퇴장에서 사용되는 부분
						it_out.println(URLEncoder.encode(msg, "UTF-8"));
					}

					else {
						// 메세지를 보낼때 사용되는 부분
						it_out.println(URLEncoder.encode("[ " + name + " ] :" + msg, "UTF-8"));
					}

				}

			} catch (Exception e) {
				System.out.println("예외 : " + e);
			}
		}
	}

	// 내부클래스
	class MultiServerT extends Thread {
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;
		boolean doubleCheck = true;
		boolean whisper = false;

		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
			} catch (Exception e) {
				System.out.println("예외 : " + e);
			}
		}

		@Override
		public void run() {
			String name = "";
			String s = "";
			boolean badWord = false;
			String whisName = "";

			try {
				// 클라이언트의 이름을 읽어와서 저장
				name = in.readLine();
				name = URLDecoder.decode(name, "UTF-8");
				/*
				 * 방금 접속한 클라이언트를 제외하면 나머지에게 사용자의 입장을 알려준다.
				 */

				if (clientMap.containsKey(name)) {
					out.println("중복된 이름이 있습니다. 접속을 거부합니다.");
					in = null;
					doubleCheck = false;
				}

				else if (blackList.contains(name)) {
					out.println("블랙리스티의 이름입니다. 접속을 거부합니다.");
					in = null;
					doubleCheck = false;
				}

				else if (java.lang.Thread.activeCount() > MAXMEMBER + 1) {
					out.println("접속자 수가 가득찼습니다. 더이상 접속하실 수 없습니다.");
					in = null;
					doubleCheck = false;
				} else {

					sendAllMsg(" ", name + "님이 입장하셨습니다.", "All");
					doubleCheck = true;
					clientMap.put(name, out);
				}

				System.out.println(name + " 접속");
				System.out.println("현재 접속사 수는 " + clientMap.size() + "명 입니다.");

				// 입력한 메세지는 모든 클라리언트에게 Echo된다.

				while (in != null) {
					// name = in.readLine()과 같은걸 받지 않는듯???????
					// 새로 전달된 in을 s에 넣는거 아닐까???
					// 그거 아니면 말이 안됨???
					s = in.readLine();
					s = URLDecoder.decode(s, "UTF-8");
					
					if (s == null) {
						break;
					}
					// 읽어온 메세지를 서버의 콘솔에 출력하고...
					System.out.println(name + " >> " + s);

					if (whisper == true) {

						if (pWords.contains(s)) {
							sendAllMsg(whisName, "********", "One");
							badWord = true;

						} else if (!(pWords.contains(s))) {
							badWord = false;
						}

						if (s.charAt(0) == '/') {
							System.out.println("들어는 감1?");
							
							
							
							// 만약 /로 시작한다면 명령어이다.
							/*
							 * 귓속말은 아래와 같이 전송하게 된다. -> /to 대화명 대화내용 : 따라서 split()을 통해 space로 문자열을 분리한다.
							 */
							String[] strArr = s.split(" ");
							
							
							
							/*
							 * 대화내용에 스페이스가 있는 경우 문장의 끝까지 출력해야 하므로 배열의 크기만큼 반복하면서 문자열을 이어준다.
							 */
							String msgContent = "";
							for (int i = 2; i < strArr.length; i++) {
								msgContent += strArr[i] + " ";
							}

							if (strArr[0].equals("/to")) {
								if (badWord == false) {
									// 함수 호출시 One이면 한명한테만 전송
									sendAllMsg(strArr[1], msgContent, "One");
								}
							}

							else if (strArr[0].equals("/unfixto")) {
								System.out.println("여기는 들어감?");
								whisper = false;
							}
						} else {
							System.out.println("마지막으로 이게 전달되야해!");
							sendAllMsg(whisName, s, "One");
						}

					} 
					
					else {

						if (pWords.contains(s)) {
							sendAllMsg(name, "********", "All");
							badWord = true;

						} else if (!(pWords.contains(s))) {
							badWord = false;
						}

						/*
						 * 클라이언트가 전송한 메세지가 명령어인지 판단한다.
						 */

						if (s.charAt(0) == '/') {
							// 만약 /로 시작한다면 명령어이다.
							/*
							 * 귓속말은 아래와 같이 전송하게 된다. -> /to 대화명 대화내용 : 따라서 split()을 통해 space로 문자열을 분리한다.
							 */
							String[] strArr = s.split(" ");
							
							for(int i =0; i < strArr.length; i++) {
								System.out.println(i + "번째 인덱스" + strArr[i]);
							}
							/*
							 * 대화내용에 스페이스가 있는 경우 문장의 끝까지 출력해야 하므로 배열의 크기만큼 반복하면서 문자열을 이어준다.
							 */
							String msgContent = "";
							for (int i = 2; i < strArr.length; i++) {
								msgContent += strArr[i] + " ";
							}

							if (strArr[0].equals("/to")) {
								if (badWord == false) {
									// 함수 호출시 One이면 한명한테만 전송
									sendAllMsg(strArr[1], msgContent, "One");
								}
							}
							
							else if (strArr[0].equals("/block")) {
								blockName = strArr[1];
								sendAllMsg(name, "님을 차단했습니다.", "Block");
							}
							
							else if(strArr[0].equals("/unblock")) {
								blockName = strArr[1];
								sendAllMsg(name, "차단을 풀었습니다..", "Unblock");
							}

							else if (strArr[0].equals("/fixto")) {
								whisName = strArr[1];
								whisper = true;
							}

							else if (strArr[0].equals("/list")) {

								Set<String> keys = clientMap.keySet();// keySet() => ket값을 얻어오는 함수 => Set(collection)에
																		// 저장함

								for (String key : keys) {
									System.out.println(key);
									sendAllMsg(name, key, "List");
								}
							}


						} else {
							// 만약 /로시작하지 않으면 일반 메세지이다.
							// all이면 모두한테 전송
							if (badWord == false) {
								sendAllMsg(name, s, "All");
							}
						}
					}
				

				MultiServerha DBsave = new MultiServerha();

				String query = "INSERT INTO chat_talking VALUES(?, ?, sysdate)";

				psmt = con.prepareStatement(query);

				psmt.setString(1, name);
				psmt.setString(2, s);

				int affected = psmt.executeUpdate();

				System.out.println(affected + "행이 입력되었습니다.");

				// 클라이언트에게 Echo해준다.
				}

			} catch (Exception e) {
				System.out.println("예외 : " + e);
			} finally {
				/*
				 * 클라이언트가 접속을 종료하면 Socket예외가 발생하게 되어 finally절로 진입하게 된다. 이때 "대화명"을 통해 정보를 삭제한다.
				 */
				if(doubleCheck == true) {
					clientMap.remove(name);
					sendAllMsg("", name + "님이 퇴장하셨습니다.", "All");
					System.out.println(name + " [" + Thread.currentThread().getName() + " ] 퇴장");
					// 퇴장하는 클라이언트의 쓰레드명을 보여준다.
					System.out.println("현재 접속자 수는 " + clientMap.size() + "명 입니다.");
					try {
						in.close();
						out.close();
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
//
//				try {
//					if (con != null)
//						con.close();
//					if (psmt != null)
//						psmt.close();
//
//					System.out.println("자원반납 완료");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}

			}
		}
	}

}