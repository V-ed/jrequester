package io.github.ved.jrequester;

import java.util.Arrays;
import java.util.List;

public class OptionLink {
	
	private String optionName;
	private List<String> links;
	
	public OptionLink(String optionName, String... links){
		this(optionName, Arrays.asList(links));
	}
	
	public OptionLink(String optionName, List<String> links){
		this.optionName = optionName;
		this.links = links;
	}
	
	public String getOptionName(){
		return optionName;
	}
	
	public List<String> getLinks(){
		return links;
	}
	
}
