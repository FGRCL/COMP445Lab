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
	private static HashMap<String, String> headers;
	private static File outputFile;
	
	public static void main(String[] args) {
		if(args.length==0) {
			System.err.println("No arguments passed");
			return;
		}
		AtomicReference<Method> method = new AtomicReference<Method>();
		AtomicReference<String> content = new AtomicReference<String>();
		AtomicReference<Boolean> verbose = new AtomicReference<Boolean>();
		verbose.set(false);
		headers = new HashMap<String, String>();
		OptionsParser parser = new OptionsParser(1, new File("help.txt"));
		parser.addOption("get", 0, new File("gethelp.txt"), (arguments) ->{
			if( method.get() == null) {
				method.set(Method.GET);
			}else {
				
			}
		});
		parser.addOption("post", 0, new File("posthelp.txt"), (arguments) ->{
			if(method.get() == null) {
				method.set(Method.POST);
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
			String headerValue = header.substring(colonIndex+2);
			headers.put(headerKey, headerValue);
		});
		parser.addOption("-d", 1, "-d string\tAssociates an inline data to the body HTTP POST request.\r\n", (arguments) ->{
			if(content.get() == null) {
				content.set(arguments[0]); 
				headers.put("Content-Length", content.get().length()+"");
			}
		});
		parser.addOption("-f", 1, "-f file\tAssociates the content of a file to the body HTTP POST request.\r\n", (arguments)->{
			if(content.get() == null) {
				content.set(FileReader.getFileContent(new File(arguments[0])));
				headers.put("Content-Length", content.get().length()+"");
			}
		});
		parser.addOption("-o", 1, "prints the result to a file", (arguments) ->{
			outputFile = new File(arguments[0]);
		});
		
		try {
			String url = parser.parse(args)[0];
			if(method.get() == null) {
				System.err.println("no method specified");
				return;
			}
			if(!url.equals("help")) {
				URI uri = null;
				if(!url.contains("http://")) {
					url = "http://"+url;
				}
				uri = new URI(url);
				
				Request request = new Request(method.get(), headers, content.get(), uri);
				Response response = request.send();
				while(response.isRedirect()) {
					request = new Request(method.get(), headers, content.get(), new URI(uri.getScheme()+"://"+uri.getHost()+response.getHeader("location")));
					response = request.send();
				}
				
				if((boolean)verbose.get()) {
					response.printVerbose();
				}else {
					response.printContent();			
				}
				
				if(outputFile != null) {
					response.writeToFile(outputFile);
				}
			}
		} catch (OptionDoesNotExistException e1) {
			System.err.println(e1.getMessage());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


