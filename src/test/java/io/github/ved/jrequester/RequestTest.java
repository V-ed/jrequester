package io.github.ved.jrequester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class RequestTest {
	
	@Test
	void commandName(){
		
		Request request = new Request("!hi");
		
		assertEquals("hi", request.getCommand());
		assertEquals("!hi", request.getCommandWithPrefix());
		
	}
	
	@Test
	void argLessLongOptions(){
		
		Request request = new Request("!hi --one --two --three");
		
		assertEquals(3, request.getOptionsData().size());
		
		assertTrue(request.hasOption("one"));
		assertTrue(request.hasOption("two"));
		assertTrue(request.hasOption("three"));
		
	}
	
	@Test
	void argLessShortOptionsSeparated(){
		
		Request request = new Request("!hi -a -b -c");
		
		assertEquals(3, request.getOptionsData().size());
		
		assertTrue(request.hasOption("a"));
		assertTrue(request.hasOption("b"));
		assertTrue(request.hasOption("c"));
		
	}
	
	@Test
	void argLessShortOptionsCombined(){
		
		Request request = new Request("!hi -abc");
		
		assertEquals(3, request.getOptionsData().size());
		
		assertTrue(request.hasOption("a"));
		assertTrue(request.hasOption("b"));
		assertTrue(request.hasOption("c"));
		
	}
	
	@Test
	void argLessCombinedTypesOptions(){
		
		Request request1 = new Request("!hi --one --two -ab");
		
		assertEquals(4, request1.getOptionsData().size());
		
		assertTrue(request1.hasOption("one"));
		assertTrue(request1.hasOption("two"));
		assertTrue(request1.hasOption("a"));
		assertTrue(request1.hasOption("b"));
		
		Request request2 = new Request("!hi -ab --one --two");
		
		assertEquals(4, request2.getOptionsData().size());
		
		assertTrue(request2.hasOption("one"));
		assertTrue(request2.hasOption("two"));
		assertTrue(request2.hasOption("a"));
		assertTrue(request2.hasOption("b"));
		
		Request request3 = new Request("!hi --one -ab --two");
		
		assertEquals(4, request3.getOptionsData().size());
		
		assertTrue(request3.hasOption("one"));
		assertTrue(request3.hasOption("two"));
		assertTrue(request3.hasOption("a"));
		assertTrue(request3.hasOption("b"));
		
	}
	
	@Test
	void weightHandling(){
		
		Request testRequest = new Request("!test -abcd");
		
		testRequest.setOptionWeight("a", 1);
		testRequest.setOptionWeight("b", 2);
		testRequest.setOptionWeight("d", 3);
		
		int weightForA = testRequest.getOption("a").getWeight();
		int weightForB = testRequest.getOption("b").getWeight();
		int weightForC = testRequest.getOption("c").getWeight();
		int weightForD = testRequest.getOption("d").getWeight();
		
		assertEquals(1, weightForA);
		assertEquals(2, weightForB);
		assertEquals(0, weightForC);
		assertEquals(4, weightForD);
		
	}
	
	@Test
	void weightHandlingModifications(){
		
		Request testRequest = new Request("!test -abcd");
		
		testRequest.setOptionWeight("a", 1);
		testRequest.setOptionWeight("b", 2);
		testRequest.setOptionWeight("d", 3);
		
		testRequest.setOptionWeight("a", 3);
		testRequest.setOptionWeight("b", 1);
		testRequest.setOptionWeight("c", 6);
		testRequest.setOptionWeight("d", 2);
		
		int weightForAMod = testRequest.getOption("a").getWeight();
		int weightForBMod = testRequest.getOption("b").getWeight();
		int weightForCMod = testRequest.getOption("c").getWeight();
		int weightForDMod = testRequest.getOption("d").getWeight();
		
		assertEquals(4, weightForAMod);
		assertEquals(1, weightForBMod);
		assertEquals(32, weightForCMod);
		assertEquals(2, weightForDMod);
		
	}
	
	@Test
	void spaceInPrefix(){
		
		String prefix = "yo ";
		
		Request testRequest = new Request("yo test", prefix);
		
		assertEquals(prefix, testRequest.getCommandPrefix());
		assertEquals("test", testRequest.getCommand());
		
	}
	
	@Test
	void createOptionData(){
		
		Request request = new Request("!hi --option hi!");
		
		OptionData requestOption = request.getOption("option");
		
		assertEquals("option", requestOption.getOptionName());
		assertEquals("hi!", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createOptionDataWithSpace(){
		
		Request request = new Request("!hi --option \"hello there!\"");
		
		OptionData requestOption = request.getOption("option");
		
		assertEquals("option", requestOption.getOptionName());
		assertEquals("hello there!", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createShortOptionData(){
		
		Request request = new Request("!hi -o hi!");
		
		OptionData requestOption = request.getOption("o");
		
		assertEquals("o", requestOption.getOptionName());
		assertEquals("hi!", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createShortOptionDataWithSpace(){
		
		Request request = new Request("!hi -o \"hello there!\"");
		
		OptionData requestOption = request.getOption("o");
		
		assertEquals("o", requestOption.getOptionName());
		assertEquals("hello there!", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createOptionDataWithProtectedQuote(){
		
		Request request = new Request("!hi --option \"test \\\" quote\"");
		
		OptionData requestOption = request.getOption("option");
		
		assertEquals("option", requestOption.getOptionName());
		assertEquals("test \" quote", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createShortOptionDataWithProtectedQuote(){
		
		Request request = new Request("!hi -o \"test \\\" quote\"");
		
		OptionData requestOption = request.getOption("o");
		
		assertEquals("o", requestOption.getOptionName());
		assertEquals("test \" quote", requestOption.getContent());
		assertEquals(1, requestOption.getPosition());
	
	}
	
	@Test
	void createMultipleShortOptionData(){
		
		Request request = new Request("!hi -abco hi!");
		
		OptionData requestOption = request.getOption("o");
		
		assertEquals("o", requestOption.getOptionName());
		assertEquals("hi!", requestOption.getContent());
		assertEquals(4, requestOption.getPosition());
		
		OptionData requestOption2 = request.getOption("b");
		
		assertEquals("b", requestOption2.getOptionName());
		assertNull(requestOption2.getContent());
		assertEquals(2, requestOption2.getPosition());
	
	}
	
}
