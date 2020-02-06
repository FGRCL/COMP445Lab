package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import networking.*;

class HttpcTests {

	@Test
	void getTest() {
		String[] args = {"get", "http://httpbin.org/get?course=networking&assignment=1"};
		result = Httpc.main(args);
		
		assertEquals();
	}

}
