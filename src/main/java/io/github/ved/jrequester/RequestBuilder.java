package io.github.ved.jrequester;

import java.util.ArrayList;
import java.util.List;

public class RequestBuilder {
	
	private String commandPrefix;
	private char optionPrefix;
	
	private List<Option> options;
	private List<OptionLink> optionsLinks;
	
	public RequestBuilder withCommandPrefix(String commandPrefix){
		this.commandPrefix = commandPrefix;
		return this;
	}
	
	public RequestBuilder withOptionPrefix(char optionPrefix){
		this.optionPrefix = optionPrefix;
		return this;
	}
	
	public RequestBuilder withOption(Option option){
		
		if(this.options == null){
			this.options = new ArrayList<>();
		}
		
		this.options.add(option);
		
		return this;
		
	}
	
	public RequestBuilder withOptionLink(String optionName, List<String> links){
		return this.withOptionLink(new OptionLink(optionName, links));
	}
	
	public RequestBuilder withOptionLink(OptionLink optionLink){
		
		if(this.optionsLinks == null){
			this.optionsLinks = new ArrayList<>();
		}
		
		this.optionsLinks.add(optionLink);
		
		return this;
		
	}
	
	public Request build(String message){
		
		this.verifyPrefixes();
		
		Request request = new Request(message, this.commandPrefix,
				this.optionPrefix);
		
		return this.build(request);
		
	}
	
	public Request build(String[] args){
		
		this.verifyPrefixes();
		
		Request request = new Request(args, this.commandPrefix,
				this.optionPrefix);
		
		return this.build(request);
		
	}
	
	private Request build(Request request){
		
		if(this.options != null){
			request.setOptions(this.options);
		}
		
		if(this.optionsLinks != null){
			this.optionsLinks.forEach(link -> request.setOptionLink(link
					.getOptionName(), link.getLinks().toArray(new String[0])));
		}
		
		return request;
		
	}
	
	private RequestBuilder verifyPrefixes(){
		if(this.commandPrefix == null)
			this.withCommandPrefix(Request.DEFAULT_COMMAND_PREFIX);
		if(this.optionPrefix == '\u0000')
			this.withOptionPrefix(Request.DEFAULT_OPTION_PREFIX);
		return this;
	}
	
}
