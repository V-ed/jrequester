package io.github.ved.jrequester;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RequestTest {
	
	@Test
	void testWeightHandling(){
		
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
	void testWeightHandlingModifications(){
		
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
	
}
