package io.github.ved.jrequester;

public class Parameter {
	
	public static final int DEFAULT_WEIGHT = 0;
	
	private String name;
	private String content;
	
	private int position;
	private boolean acceptsContent;
	
	private int weightPosition;
	private int weight;
	
	protected Parameter(String paramName, int position){
		this.name = paramName;
		
		this.setPosition(position);
		this.acceptsContent = true;
		this.setWeight(DEFAULT_WEIGHT);
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getContent(){
		return content;
	}
	
	protected void setContent(String parameterContent){
		if(parameterContent == null)
			this.content = null;
		else
			this.content = parameterContent.replaceAll("\"", "");
	}
	
	public int getPosition(){
		return this.position;
	}
	
	protected void setPosition(int position){
		this.position = position;
	}
	
	public boolean doesAcceptContent(){
		return this.acceptsContent;
	}
	
	protected void setAcceptingContent(boolean acceptsContent){
		this.acceptsContent = acceptsContent;
		
		if(!acceptsContent && getContent() != null){
			setContent(null);
		}
	}
	
	public int getWeightPosition(){
		return this.weightPosition;
	}
	
	public int getWeight(){
		return this.weight;
	}
	
	protected int setWeight(int weightPosition){
		
		if(weightPosition == DEFAULT_WEIGHT){
			this.weightPosition = DEFAULT_WEIGHT;
			this.weight = DEFAULT_WEIGHT;
		}
		else{
			
			int weight = calculateWeight(weightPosition - 1);
			
			if(weight == -1)
				return -1;
			
			this.weightPosition = weightPosition;
			this.weight = weight;
			
		}
		
		return this.weight;
		
	}
	
	@Override
	public boolean equals(Object obj){
		
		if(obj instanceof Parameter){
			
			if(getName() == null)
				return false;
			
			Parameter parameterToCompare = (Parameter)obj;
			
			return getName().equals(parameterToCompare.getName());
			
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return this.getContent();
	}
	
	public static int calculateWeight(int weightPosition){
		
		double weight = Math.pow(2, weightPosition);
		
		if(weight > Integer.MAX_VALUE){
			//			Logger.log("Weight index is too high to deal with...",
			//					Logger.LogType.WARNING);
			
			return -1;
		}
		
		return (int)weight;
		
	}
	
}
