import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;

import cliparser.OptionDoesNotExistException;
import cliparser.OptionsParser;

public class Httpc {
	private static Method method;
	private static boolean verbose = false;
	private static HashMap headers = new HashMap();
	private static String content = "";
	private static String url = "";
	
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
	
		URI uri = null;
		try {
			String url = parser.parse(args)[0];
			if(!url.contains("http://")) {
				url = "http://"+url;
			}
			uri = new URI(url);
		} catch (OptionDoesNotExistException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Request request = new Request(method, verbose, headers, content, uri);
		Response response = request.send();
		
		if(verbose) {
			response.printVerbose();
		}else {
			response.printContent();			
		}
	}
}


