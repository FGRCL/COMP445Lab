package test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import file.FileReader;
import networking.*;

class HttpcTests {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@BeforeEach
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	@Test
	void getTest() {
		//given
		String[] args = {"get", "http://httpbin.org/get?course=networking&assignment=1"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertNotNull(consoleOut);
		assertFalse(consoleOut.isEmpty());
		assertTrue(consoleOut.contains("\"assignment\": \"1\""));
		assertTrue(consoleOut.contains("\"course\": \"networking\""));
	}
	
	@Test
	void getVerboseTest() {
		//given
		String[] args = {"get", "-v","http://httpbin.org/get?course=networking&assignment=1"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertNotNull(consoleOut);
		assertFalse(consoleOut.isEmpty());
		assertTrue(consoleOut.contains("\"assignment\": \"1\""));
		assertTrue(consoleOut.contains("\"course\": \"networking\""));
		assertTrue(consoleOut.contains("200 OK"));
	}
	
	@Test
	void postTest() {
		//given
		String[] args = {"post", "-h", "Content-Type:application/json", "-d", "{\"Assignment\": 1}", "http://httpbin.org/post"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertNotNull(consoleOut);
		assertFalse(consoleOut.isEmpty());
		assertTrue(consoleOut.contains("\"data\": \"{\\\"Assignment\\\": 1}\""));
	}
	
	@Test
	void postTestWithFile() {
		//given
		String fileName = "data.json";
		String[] args = {"post", "-h", "Content-Type:application/json", "-f", fileName, "http://httpbin.org/post"};
		File data = null;
		try {
			data = new File(fileName);
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(data));
			os.write("{\"Assignment\": 1}".getBytes());
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertNotNull(consoleOut);
		assertFalse(consoleOut.isEmpty());
		assertTrue(consoleOut.contains("\"data\": \"{\\\"Assignment\\\": 1}\""));
		data.delete();
	}
	
	@Test
	void postVerboseTest() {
		//given
		String[] args = {"post","-v", "-h", "Content-Type:application/json", "-d", "{\"Assignment\": 1}", "http://httpbin.org/post"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertNotNull(consoleOut);
		assertFalse(consoleOut.isEmpty());
		assertTrue(consoleOut.contains("\"data\": \"{\\\"Assignment\\\": 1}\""));
		assertTrue(consoleOut.contains("200 OK"));
	}
	
	@Test
	void helpProgramTest() {
		//given
		String[] args = {"help"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertEquals(FileReader.getFileContent(new File("help.txt")).replaceAll("\\s+",""), consoleOut.replaceAll("\\s+",""));
	}
	
	@Test
	void helpOptionTest() {
		//given
		String[] args = {"help", "get"};
		
		//when
		Httpc.main(args);
		
		//then
		String consoleOut = outContent.toString();
		assertEquals(FileReader.getFileContent(new File("gethelp.txt")).replaceAll("\\s+",""), consoleOut.replaceAll("\\s+",""));
	}

	@AfterEach
	public void restoreStreams() {
		outContent.reset();
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
}
