package multichat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;

//서버가 보내는 Echo메세지를 읽어오는 쓰레드
public class Receiver extends Thread {

	Socket socket;
	BufferedReader in = null;
	HashSet<String> blockName = new HashSet<String>();
	
	//Client가 접속시 생성한 Socket객체를 생성자에서 매개변수로 받음.
	public Receiver(Socket socket) {
		this.socket = socket;
		/*
		 Socket객체를 기반으로 input 스트림을 생성한다.
		 */
		try {
			in = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream(), "UTF-8"));
		}
		catch(Exception e) {
			System.out.println("예외1 : " + e);
		}
	}
	/*
	 		Thread에서 main()의 역할을 하는 메소드로 
	 		직접호출하면 안되고, 반드시 start()를 통해 
	 		간접적으로 호출해야 쓰레드가 생성된다.
	 */
	@Override
	public void run() {
		
		String s = "";
		
		//스트림을 통해 서버가 보낸 내용을 라인단위로 읽어온다.
		while(in != null) {
			try {
				
				s = in.readLine();
				s = URLDecoder.decode(s, "UTF-8");
				if(s == null) {
					break;
				}
				
				String[] strArr = s.split(" ");
				
				
				if(strArr.length > 1) {
					
					if (s.charAt(0) == '!') {
						
						blockName.add(strArr[1]);
						System.out.println(URLDecoder.decode(s, "UTF-8"));
						
					}
					
					else if(s.charAt(0) == '?') {
						blockName.remove(strArr[1]);
					}
					
					if(blockName.contains(strArr[1])) {
						//비어있음
					}
					
					else {
						System.out.println(URLDecoder.decode("Thread Receive : " + s, "UTF-8"));
					}
					
				}
				else {
					System.out.println(URLDecoder.decode("Thread Receive : " + s, "UTF-8"));
					
				}

			}
			
			catch (SocketException e) {
				/*
				클라이언트가 q를 입력하여 접속을 종료하면 무한루프가 발생되므로 
				탈출 할 수 있도록 별도의 catch블럭을 추가하고 break를 걸어준다.
				 */
				System.out.println("SocketException발생됨. 루프탈출");
				break;
			}
			
			catch(Exception e) {
				
				//q이면 종료한는 코드를 넣어주고 싶지만 in과 out으로 객체를 전달하기 떄문에 비교 불가능
				System.out.println("예외>Receiver>run1: " + e);
				
			}
		}
		
		try {
			in.close();
		}
		catch (Exception e) {
			System.out.println("예외>Receiver>run2 : " + e);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
}
