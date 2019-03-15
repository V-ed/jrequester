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
		
		this.setName(name);
		this.setDescription(description);
		this.setAcceptsContent(acceptsContent);
		this.setWeight(weight);
		this.setVariants(variants);
		
	}

	// Package protected constructor for Option Building
	Option(){
		this.setAcceptsContent(true);
	}
	
	public String getDescription(){
		return this.description;
	}
	
	void setDescription(String description){
		this.description = description;
	}
	
	public boolean doesAcceptsContent(){
		return this.acceptsContent;
	}
	
	void setAcceptsContent(boolean acceptsContent){
		this.acceptsContent = acceptsContent;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	void setWeight(int weight){
		this.weight = weight;
	}
	
	public String getName(){
		return this.name;
	}
	
	void setName(String name){
		if(name == null){
			throw new IllegalArgumentException(
					"The option's name cannot be null!");
		}
		
		this.name = name;
	}
	
	public String[] getVariants(){
		return this.variants;
	}
	
	void setVariants(String... variants){
		this.variants = variants;
	}
	
	public boolean has(String optionName){
		return this.getAllOptions().contains(optionName);
	}
	
	public List<String> getAllOptions(){
		List<String> allOptions = new ArrayList<>();
		
		allOptions.add(this.getName());
		
		allOptions.addAll(Arrays.asList(this.getVariants()));
		
		return allOptions;
	}
	
}
