import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import cliparser.OptionDoesNotExistException;
import cliparser.OptionsParser;

public class Httpc {
	private static Method method;
	private static boolean verbose = false;
	private static HashMap headers = new HashMap();
	private static String content = "";
	
	public static void main(String[] args) {
		OptionsParser parser = new OptionsParser(1);
		parser.addOption("get", 0, (arguments) ->
			method = Method.GET
		);
		parser.addOption("post", 0, (arguments) ->
			method = Method.POST
		);
		parser.addOption("-v", 0, (arguments) ->
			verbose = true
		);
		parser.addOption("-h", 1, (arguments) ->{
			String header = arguments[0];
			int colonIndex = header.indexOf(':');
			String headerKey = header.substring(0, colonIndex);
			String headerValue = header.substring(colonIndex+1);
			headers.put(headerKey, headerValue);
		}
		);
		parser.addOption("-d", 1, (arguments) ->
			content = arguments[0]
		);
		parser.addOption("-f", 1,(arguments)->
			System.out.println("file")
		);
	
		try {
			String url = parser.parse(args)[0];
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
	
	public enum Method{
		GET, POST
	}
}


