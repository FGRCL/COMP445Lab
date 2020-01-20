import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Httpc {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("google.com", 80);
			
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			
			outStream.write("GET / HTTP/1.0\r\n\r\n".getBytes());
			
			int character;
			do {
				character = inStream.read();
				System.out.print((char) character);
				
			}while(character != -1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
