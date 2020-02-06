package networking;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Request {
	private Method method;
	private HashMap<String, String> headers;
	private URI uri;
	
	public Request(Method method, HashMap<String, String> headers, String content, URI uri) {
		this.method = method;
		this.headers = headers;
		this.uri = uri;
	}

	public Response send(){
		StringBuilder requestBuilder = new StringBuilder();
		StringBuilder response = new StringBuilder();
		requestBuilder.append(String.format("%s %s HTTP/1.0\r\n\r\n", getMethodString(), getStringPath()));
		requestBuilder.append(String.format("%s\r\n", getStringHeaders()));
		requestBuilder.append(String.format("%s\r\n\r\n", getStringContent()));
		int port = uri.getPort() == -1 ? 80 : uri.getPort();
		
		try {
			Socket socket = new Socket(uri.getHost(), port);
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();
			outStream.write(requestBuilder.toString().getBytes());
			
			int character = inStream.read();
			while(character != -1){
				response.append((char)character);
				character = inStream.read();
			}
			
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Response(response.toString());
	}
	
	private String getMethodString() {
		return method.toString();
	}
	
	private String getStringHeaders() {
		AtomicReference<String> stringHeaders = new AtomicReference<String>();
		stringHeaders.set("");
		headers.forEach((String key, String value) ->{
			stringHeaders.set(stringHeaders.get()+key+": "+value+"\r\n");
			System.out.println(key+value);
		});
		return stringHeaders.get();
	}
	
	//TODO implement this method
	private String getStringContent() {
		return "";
	}
	
	private String getStringPath() {
		String path = uri.getPath().equals("") ? "/":uri.getPath();
		String query = uri.getQuery() == null? "":"?"+uri.getQuery();
		return path+query;
	}
}
