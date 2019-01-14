package io.github.ved.jrequester;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Utils {
	
	static List<String> splitSpacesExcludeQuotes(String string){
		List<String> possibleStrings = new ArrayList<>();
		Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'")
				.matcher(string);
		while(matcher.find()){
			possibleStrings.add(matcher.group());
		}
		
		return possibleStrings;
	}
	
	static List<String> splitSpacesExcludeQuotesMaxed(String string, int maxSize){
		
		if(maxSize == 0){
			List<String> singleEntry = new ArrayList<>();
			singleEntry.add(string);
			
			return singleEntry;
		}
		
		List<String> fullSplit = splitSpacesExcludeQuotes(string);
		
		if(maxSize < 0 || fullSplit.size() <= maxSize){
			return fullSplit;
		}
		
		List<String> splittedUpTo = fullSplit.subList(0, maxSize - 1);
		List<String> allOthers = fullSplit.subList(maxSize,
				fullSplit.size() - 1);
		
		List<String> allAndTruncated = new ArrayList<>(splittedUpTo);
		allAndTruncated.add(String.join(" ", allOthers));
		
		return allAndTruncated;
	}
	
}
