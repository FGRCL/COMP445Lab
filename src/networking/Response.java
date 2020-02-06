package networking;

public class Response {
	private String headers;
	private String content;
	
	public Response(String fullResponse) {
		String[] splitResponse = fullResponse.split("\r\n\r\n");
		headers = splitResponse[0];
		content = splitResponse[1];
	}
	
	public void printVerbose() {
		System.out.println(headers+"\r\n");
		
		System.out.println(content);
	}
	
	public void printContent() {
		System.out.println(content);
	}
}
