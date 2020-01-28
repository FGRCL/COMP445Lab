import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;

import cliparser.OptionDoesNotExistException;
import cliparser.OptionsParser;

public class Httpc {

	public static void main(String[] args) {
		OptionsParser parser = new OptionsParser(1);
		parser.addOption("get", 0, (arguments) ->
			System.out.println("Here")
		);
		
		parser.addOption("-v", 1, (arguments) ->
			System.out.println("doing -v: "+arguments[0])
		);
		try {
			parser.parse(args);
		} catch (OptionDoesNotExistException e) {
			e.printStackTrace();
		}
	}
	
	private static String get(String url) {
		StringBuilder response = new StringBuilder();
		try {
			Socket socket = new Socket(url, 80);

			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			
			outStream.write("GET / HTTP/1.0\r\n\r\n".getBytes());
			
			response = new StringBuilder();
			int character;
			do {
				character = inStream.read();
				response.append(character);
			}while(character != -1);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response.toString();
	}

}
