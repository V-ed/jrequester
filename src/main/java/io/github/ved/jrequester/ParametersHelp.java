package io.github.ved.jrequester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParametersHelp {
	
	private String parameterDescription;
	private boolean acceptsContent;
	private int weight;
	private String param;
	private String[] paramVariants;
	
	public ParametersHelp(String parameterDescription, String param,
			String... paramVariants){
		this(parameterDescription, true, param, paramVariants);
	}
	
	public ParametersHelp(String parameterDescription, boolean acceptsContent,
			String param, String... paramVariants){
		this(parameterDescription, acceptsContent, Parameter.DEFAULT_WEIGHT,
				param, paramVariants);
	}
	
	public ParametersHelp(String parameterDescription, int weight,
			String param, String... paramVariants){
		this(parameterDescription, true, weight, param, paramVariants);
	}
	
	public ParametersHelp(String parameterDescription, boolean acceptsContent,
			int weight, String param, String... paramVariants){
		
		this.parameterDescription = parameterDescription;
		this.acceptsContent = acceptsContent;
		this.weight = weight;
		this.param = param;
		this.paramVariants = paramVariants;
		
	}
	
	public String getParameterDescription(){
		return this.parameterDescription;
	}
	
	public boolean doesAcceptsContent(){
		return this.acceptsContent;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	public String getParam(){
		return this.param;
	}
	
	public String[] getParamVariants(){
		return this.paramVariants;
	}
	
	public boolean hasParam(String param){
		return getAllParams().contains(param);
	}
	
	public List<String> getAllParams(){
		List<String> allParams = new ArrayList<>();
		
		allParams.add(getParam());
		
		allParams.addAll(Arrays.asList(getParamVariants()));
		
		return allParams;
	}
	
}
