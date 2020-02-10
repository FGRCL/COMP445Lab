package networking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.BufferedOutputStream;

public class Response {
	private String httpVersion;
	private int statusCode;
	private String statusMessage;
	private HashMap<String, String> headers = new HashMap<String, String>();
	private String content;
	
	public Response(String fullResponse) {
		String[] splitResponse = fullResponse.split("\r\n\r\n");
		String[] headers = splitResponse[0].split("\r\n");
		String[] status = headers[0].split(" ");
		httpVersion = status[0];
		statusCode = Integer.parseInt(status[1]);
		statusMessage = "";
		for(int i=2; i<status.length; i++){
			statusMessage += status[i]+" ";
		}
		for(int i=1; i<headers.length; i++) {
			int splitIndex = headers[i].indexOf(':');
			this.headers.put(headers[i].substring(0, splitIndex).toLowerCase(), headers[i].substring(splitIndex+2));
		}
		if(splitResponse.length > 1) {
			content = splitResponse[1];
		}
	}
	
	public void printVerbose() {
		System.out.println(httpVersion+" "+statusCode+" "+statusMessage);
		for(Entry<String, String> entry : headers.entrySet()) {
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
		printContent();
	}
	
	public void printContent() {
		if(content == null) {
			System.out.println("No content for this request");
		}else {
			System.out.println(content);
		}
	}

	public void writeToFile(File outputFile) {
		try {
			BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(outputFile));
			fos.write(content.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			System.err.println("File "+outputFile+" does not exist.");
		} catch (IOException e) {
			System.err.println("Can't write to file "+outputFile);
		}
	}

	public boolean isRedirect() {
		return statusCode == 302;
	}

	public String getHeader(String string) {
		return headers.get(string);
	}
}
