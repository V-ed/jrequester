package io.github.ved.jrequester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Option {
	
	private String description;
	private boolean acceptsContent;
	private int weight;
	private String name;
	private String[] variants;
	
	public Option(String description, String name, String... variants){
		this(description, true, name, variants);
	}
	
	public Option(String description, boolean acceptsContent, String name,
			String... variants){
		this(description, acceptsContent, OptionData.DEFAULT_WEIGHT, name,
				variants);
	}
	
	public Option(String description, int weight, String name,
			String... variants){
		this(description, true, weight, name, variants);
	}
	
	public Option(String description, boolean acceptsContent, int weight,
			String name, String... variants){
		
		this.description = description;
		this.acceptsContent = acceptsContent;
		this.weight = weight;
		this.name = name;
		this.variants = variants;
		
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean doesAcceptsContent(){
		return this.acceptsContent;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String[] getVariants(){
		return this.variants;
	}
	
	public boolean has(String optionName){
		return getAllOptions().contains(optionName);
	}
	
	public List<String> getAllOptions(){
		List<String> allOptions = new ArrayList<>();
		
		allOptions.add(getName());
		
		allOptions.addAll(Arrays.asList(getVariants()));
		
		return allOptions;
	}
	
}
