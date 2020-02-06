package networking;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import file.*;

import cliparser.OptionDoesNotExistException;
import cliparser.OptionsParser;

public class Httpc {
	private static Method method;
	private static HashMap<String, String> headers = new HashMap<String, String>();
	private static String content;

	
	public static void main(String[] args) {
		AtomicReference<Boolean> verbose = new AtomicReference<Boolean>();
		AtomicReference<Boolean> printToFile = new AtomicReference<Boolean>();
		AtomicReference<String> outputFile = new AtomicReference<String>();
		verbose.set(false);
		printToFile.set(false);
		OptionsParser parser = new OptionsParser(1, new File("help.txt"));
		parser.addOption("get", 0, new File("gethelp.txt"), (arguments) ->{
			if( method == null) {
				method = Method.GET;
			}else {
				
			}
		});
		parser.addOption("post", 0, new File("posthelp.txt"), (arguments) ->{
			if(method == null) {
				method = Method.POST;
			}else {
				
			}
			
		});
		parser.addOption("-v", 0, "-v\tPrints the detail of the response such as protocol, status, and headers.\r\n", (arguments) ->
			verbose.set(true)
		);
		parser.addOption("-h", 1, "-h key:value\tAssociates headers to HTTP Request with the format 'key:value'.", (arguments) ->{
			String header = arguments[0];
			int colonIndex = header.indexOf(':');
			String headerKey = header.substring(0, colonIndex);
			String headerValue = header.substring(colonIndex+1);
			headers.put(headerKey, headerValue);
		});
		parser.addOption("-d", 1, "-d string\tAssociates an inline data to the body HTTP POST request.\r\n", (arguments) ->{
			if(content != null) {
				content = FileReader.getFileContent(new File(arguments[0]));
			}
		});
		parser.addOption("-f", 1, "-f file\tAssociates the content of a file to the body HTTP POST request.\r\n", (arguments)->{
			if(content != null) {
				content = FileReader.getFileContent(new File(arguments[0]));
			}
		});
		parser.addOption("-o", 1, "prints the result to a file", (arguments) ->{
			printToFile.set(true);
			outputFile.set(arguments[0]);
		});
		
		try {
			String url = parser.parse(args)[0];
			if(!url.equals("help")) {
				URI uri = null;
				try {
					if(!url.contains("http://")) {
						url = "http://"+url;
					}
					uri = new URI(url);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				
				Request request = new Request(method, headers, content, uri);
				Response response = request.send();
				
				if((boolean)verbose.get()) {
					response.printVerbose();
				}else {
					response.printContent();			
				}
			}
		} catch (OptionDoesNotExistException e1) {
			System.err.println(e1.getMessage());
		}
	}
}


