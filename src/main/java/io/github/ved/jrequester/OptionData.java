package io.github.ved.jrequester;

public class OptionData {
	
	public static final int DEFAULT_WEIGHT = 0;
	
	private String optionName;
	private String content;
	
	private int position;
	private boolean acceptsContent;
	
	private int weightPosition;
	private int weight;
	
	protected OptionData(String optionName, int position){
		this.optionName = optionName;
		
		this.setPosition(position);
		this.acceptsContent = true;
		this.setWeight(DEFAULT_WEIGHT);
		
	}
	
	public String getOptionName(){
		return optionName;
	}
	
	public String getContent(){
		return content;
	}
	
	protected void setContent(String optionContent){
		
		if(optionContent == null){
			this.content = null;
		}
		else{
			
			String wrapper = null;
			
			if(optionContent.matches("^\".*\"$")){
				wrapper = "\"";
			}
			else if(optionContent.matches("^'.*'$")){
				wrapper = "'";
			}
			
			if(wrapper == null){
				this.content = optionContent;
			}
			else{
				this.content = optionContent.substring(1, optionContent.length() - 1).replaceAll("\\\\" + wrapper, wrapper);
			}
			
		}
		
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
		
		if(obj instanceof OptionData){
			
			if(getOptionName() == null)
				return false;
			
			OptionData optionToCompare = (OptionData)obj;
			
			return getOptionName().equals(optionToCompare.getOptionName());
			
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
