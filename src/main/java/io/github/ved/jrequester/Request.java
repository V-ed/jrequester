package io.github.ved.jrequester;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Request implements Utils {
	
	public final static String DEFAULT_COMMAND_PREFIX = "!";
	public final static char DEFAULT_OPTION_PREFIX = '-';
	
	private String initialMessage;
	
	private String command;
	private String content;
	
	private String commandPrefix;
	
	private Map<String, OptionData> options;
	private Map<OptionData, List<String>> optionsLinks;
	private char optionsPrefix;
	
	private List<OptionData> duplicateOptions;
	
	public Request(String[] args){
		this(args, DEFAULT_OPTION_PREFIX);
	}
	
	public Request(String receivedMessage){
		this(receivedMessage, DEFAULT_OPTION_PREFIX);
	}
	
	public Request(String[] args, char optionsPrefix){
		this(args, DEFAULT_COMMAND_PREFIX, optionsPrefix);
	}
	
	public Request(String receivedMessage, char optionsPrefix){
		this(receivedMessage, DEFAULT_COMMAND_PREFIX, optionsPrefix);
	}
	
	public Request(String[] args, String commandPrefix, char optionsPrefix){
		this(buildMessageFromArgs(args, optionsPrefix), commandPrefix,
				optionsPrefix);
	}
	
	public Request(String receivedMessage, String commandPrefix,
			char optionsPrefix){
		
		this.initialMessage = receivedMessage;
		
		this.commandPrefix = commandPrefix;
		
		this.optionsPrefix = optionsPrefix;
		
		if(!receivedMessage.startsWith(commandPrefix)){
			setContent(receivedMessage);
		}
		else{
			
			String[] messageSplit = splitCommandAndContent(receivedMessage);
			
			setCommand(messageSplit[0]);
			setContent(messageSplit[1]);
			
		}
		
		if(hasContent()){
			setupOptions();
		}
		
	}
	
	private class PossibleOption {
		String name;
		int index;
		int startPos;
		int endPos;
		
		public PossibleOption(String name, int index, int startPos, int endPos){
			this.name = name;
			this.index = index;
			this.startPos = startPos;
			this.endPos = endPos;
		}
	}
	
	private void setupOptions(){
		
		options = new HashMap<>();
		
		// Splits the content : Search for all spaces, except those
		// in double quotes and put all what's found in the
		// possibleOptions ArrayList.
		// Necessary since .split() removes the wanted Strings.
		List<String> possibleOptions = Utils.splitSpacesExcludeQuotes(content);
		
		duplicateOptions = new ArrayList<>();
		
		boolean canRoll = true;
		
		for(int i = 0; i < possibleOptions.size() && canRoll; i++){
			
			PossibleOption possibleOption = new PossibleOption(
					possibleOptions.get(i), i, -1, -1);
			
			if(possibleOption.name.equals(getOptionsPrefix() + ""
					+ getOptionsPrefix())){
				
				// If string is double option prefix, remove it and stop taking options
				possibleOption.startPos = getContent().indexOf(
						possibleOption.name);
				possibleOption.endPos = possibleOption.startPos
						+ possibleOption.name.length();
				
				canRoll = false;
				
			}
			else if(stringIsOption(possibleOption.name)){
				
				// If string is structured as an option, create it.
				canRoll = handleOptionCreation(possibleOption, possibleOptions);
				
			}
			
			if(possibleOption.startPos != -1){
				String contentToRemove = getContent().substring(
						possibleOption.startPos, possibleOption.endPos);
				
				setContent(getContent().replaceFirst(
						Pattern.quote(contentToRemove), ""));
			}
			
			i = possibleOption.index;
			
		}
		
		if(hasContent())
			setContent(getContent().trim().replaceAll("\"", ""));
		
	}
	
	private boolean tryGetOptionContent(OptionData newOption,
			PossibleOption possibleOption, List<String> possibleOptionList){
		
		String possibleOptionContent;
		
		try{
			possibleOptionContent = possibleOptionList
					.get(possibleOption.index + 1);
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
		
		// If the following String isn't another option, set
		// said String as the content for the current option.
		if(!stringIsOption(possibleOptionContent)){
			
			newOption.setContent(possibleOptionContent);
			
			possibleOption.index++;
			
			possibleOption.endPos = getContent().indexOf(possibleOptionContent)
					+ possibleOptionContent.length();
			
		}
		
		return true;
		
	}
	
	private boolean stringIsOption(String string){
		return string != null
				&& string.matches(getOptionsPrefixProtected() + "{1,2}[^\\s]+");
	}
	
	private boolean handleOptionCreation(PossibleOption possibleOption,
			List<String> possibleOptionsList){
		
		boolean canRoll = true;
		
		possibleOption.startPos = getContent().indexOf(possibleOption.name);
		possibleOption.endPos = possibleOption.startPos
				+ possibleOption.name.length();
		
		if(possibleOption.name.matches(getOptionsPrefixProtected()
				+ "{2}[^\\s]+")){
			// Doubled option prefix means that all letters count as one option
			
			String optionName = possibleOption.name.substring(2);
			
			OptionData newOption = new OptionData(optionName,
					possibleOption.index);
			
			if(options.containsValue(newOption)){
				
				duplicateOptions.add(newOption);
				
			}
			else{
				
				canRoll = tryGetOptionContent(newOption, possibleOption,
						possibleOptionsList);
				
				options.put(newOption.getOptionName(), newOption);
				
			}
			
		}
		else{
			// Single option prefix means that all letters counts as a different option
			
			String singleOptions = possibleOption.name.substring(1);
			
			for(int j = 0; j < singleOptions.length() && canRoll; j++){
				
				OptionData newOption = new ShortOptionData(
						singleOptions.charAt(j), possibleOption.index + j);
				
				if(options.containsValue(newOption)){
					
					duplicateOptions.add(newOption);
					
				}
				else{
					
					if(j == singleOptions.length() - 1){
						
						canRoll = tryGetOptionContent(newOption,
								possibleOption, possibleOptionsList);
						
					}
					
					options.put(newOption.getOptionName(), newOption);
					
				}
				
			}
			
		}
		
		return canRoll;
		
	}
	
	public String getInitialMessage(){
		return this.initialMessage;
	}
	
	public String getCommand(){
		if(!this.hasCommand())
			return null;
		
		return this.getCommandNoFormat().substring(getCommandPrefix().length());
	}
	
	public String getCommandNoFormat(){
		return this.command;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public boolean hasCommand(){
		return this.getCommandNoFormat() != null;
	}
	
	public boolean isCommand(){
		return this.hasCommand()
				&& this.getCommandNoFormat().startsWith(getCommandPrefix());
	}
	
	public boolean isOnlyCommandPrefix(){
		return this.hasCommand()
				&& this.getCommandNoFormat().equals(getCommandPrefix());
	}
	
	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		
		if(content == null || content.length() == 0)
			this.content = null;
		else
			this.content = content;
		
	}
	
	public boolean hasContent(){
		return this.getContent() != null;
	}
	
	public String getCommandPrefix(){
		return this.commandPrefix;
	}
	
	public Map<String, OptionData> getOptionsData(){
		return this.options;
	}
	
	public Map<OptionData, List<String>> getOptionsLinks(){
		return this.optionsLinks;
	}
	
	public OptionData getOptionFromPosition(int position){
		
		for(Map.Entry<String, OptionData> optionsEntry : this.options
				.entrySet()){
			
			if(optionsEntry.getValue().getWeightPosition() == position)
				return optionsEntry.getValue();
			
		}
		
		return null;
		
	}
	
	public OptionData getOptionFromWeight(int weight){
		
		for(Map.Entry<String, OptionData> optionEntry : this.options.entrySet()){
			
			if(optionEntry.getValue().getWeight() == weight)
				return optionEntry.getValue();
			
		}
		
		return null;
		
	}
	
	public OptionData getOption(String... optionNames){
		
		if(!hasOptions()){
			return null;
		}
		
		if(optionNames == null || optionNames.length == 0)
			throw new IllegalArgumentException(
					"The optionsName parameter cannot be null / empty!");
		
		for(String optionName : optionNames){
			
			if(getOptionsLinks() != null){
				
				for(Map.Entry<OptionData, List<String>> entry : getOptionsLinks()
						.entrySet()){
					
					if(entry.getValue().contains(optionName))
						return entry.getKey();
					
				}
				
			}
			
			OptionData optionFound = getOptionsData().get(optionName);
			
			if(optionFound != null)
				return optionFound;
			
		}
		
		return null;
		
	}
	
	public char getOptionsPrefix(){
		return this.optionsPrefix;
	}
	
	private String getOptionsPrefixProtected(){
		return Pattern.quote(String.valueOf(getOptionsPrefix()));
	}
	
	// public boolean hasOption(String optionName){
	// 	if(getOptionsData() == null)
	// 		return false;
	
	// 	return this.getOptionsData().containsKey(optionName);
	// }
	
	public boolean hasOption(String... optionNames){
		return getOption(optionNames) != null;
	}
	
	public boolean hasOptions(){
		return getOptionsData() != null;
	}
	
	public void onOptionPresent(String optionName,
			Consumer<OptionData> onOptionPresent){
		onOptionPresent(optionName, onOptionPresent, null);
	}
	
	public void onOptionPresent(String optionName,
			Consumer<OptionData> onOptionPresent, Runnable onOptionNotPresent){
		
		OptionData option = null;
		
		option = getOption(optionName);
		
		if(option != null){
			onOptionPresent.accept(option);
		}
		else if(onOptionNotPresent != null){
			onOptionNotPresent.run();
		}
		
	}
	
	// public boolean addOption(String optionName){
	// 	return this.getOptionsData().put(optionName, new OptionData(optionName)) == null;
	// }
	
	// public boolean addOption(String optionName, String optionContent){
	// 	return this.getOptionsData().put(optionName,
	// 			new OptionData(optionName, optionContent)) == null;
	// }
	
	private String[] splitCommandAndContent(String command){
		
		// Remove leading / trailing spaces, as well as shrinking consecutives
		// whitespace.
		// Also, this adds a space at the end to force the split to be at least
		// of size 2, which means there will always be a command and some
		// content.
		String[] splitted = command.trim().replaceAll("\\s+", " ").concat(" ")
				.split(" ", 2);
		
		splitted[0] = splitted[0].toLowerCase();
		
		if(splitted[1].length() != 0){
			splitted[1] = splitted[1].trim();
		}
		
		return splitted;
		
	}
	
	public List<OptionData> getDuplicateOptionList(){
		return this.duplicateOptions;
	}
	
	public boolean hasError(){
		return this.getDuplicateOptionList() != null
				&& this.getDuplicateOptionList().size() != 0;
	}
	
	public String getDefaultErrorMessage(){
		
		if(!hasError()){
			return "This request has no errors!";
		}
		else{
			
			String pluralTester;
			
			if(this.getDuplicateOptionList().size() == 1)
				pluralTester = "That option";
			else
				pluralTester = "Those options";
			
			StringBuilder message = new StringBuilder();
			
			message.append(pluralTester).append(
					" has been entered more than once : ");
			
			List<OptionData> handledDupes = new ArrayList<>();
			
			for(int i = 0; i < this.getDuplicateOptionList().size(); i++){
				
				OptionData option = this.getDuplicateOptionList().get(i);
				
				if(!handledDupes.contains(option)){
					
					handledDupes.add(option);
					
					message.append("\n");
					
					if(this.getDuplicateOptionList().size() != 1)
						message.append(i + 1).append(". ");
					else
						message.append("~ ");
					
					message.append(option.getOptionName());
					
				}
				
			}
			
			if(this.getDuplicateOptionList().size() == 1)
				pluralTester = "that option";
			else
				pluralTester = "those options";
			
			message.append("\n")
					.append(String
							.format("Only the first instance of %1$s will be taken into consideration.",
									pluralTester));
			
			return message.toString();
			
		}
		
	}
	
	public void setOptionLinkMap(List<List<String>> map){
		
		if(getOptionsData() != null)
			getOptionsData().forEach((key, option) -> {
				
				for(List<String> optionsGroup : map){
					
					if(optionsGroup.contains(key)){
						
						if(this.optionsLinks == null){
							this.optionsLinks = new HashMap<>();
						}
						
						this.optionsLinks.put(option, optionsGroup);
						break;
						
					}
					
				}
				
			});
		
	}
	
	public void setOptionLink(String optionName, String... links){
		
		if(links.length != 0){
			
			onOptionPresent(optionName, (option) -> {
				
				if(this.optionsLinks == null){
					this.optionsLinks = new HashMap<>();
				}
				
				List<String> list = new ArrayList<>(Arrays.asList(links));
				
				this.optionsLinks.put(option, list);
				
			});
			
		}
		
	}
	
	public void setOptionContentLess(String optionName){
		OptionData optionFound = getOption(optionName);
		
		if(optionFound == null){
			throw new IllegalStateException("Option \"" + optionName
					+ "\" is not present or linked in this request.");
		}
		else{
			
			if(optionFound.getContent() != null){
				
				if(!hasContent())
					setContent(optionFound.getContent());
				else
					setContent(getContent() + " " + optionFound.getContent());
				
			}
			
			optionFound.setAcceptingContent(false);
			
		}
	}
	
	public void setOptionsAsContentLess(List<String> optionsToTreatAsContentLess){
		this.setOptionsAsContentLess(optionsToTreatAsContentLess
				.toArray(new String[0]));
	}
	
	public void setOptionsAsContentLess(String[] optionsToTreatAsContentLess){
		
		for(String optionName : optionsToTreatAsContentLess){
			try{
				this.setOptionContentLess(optionName);
			}
			catch(IllegalStateException e){}
		}
		
	}
	
	public void setOptionWeight(String optionName, int weightPosition)
			throws IllegalArgumentException{
		this.setOptionWeight(getOption(optionName), weightPosition);
	}
	
	public void setOptionWeight(OptionData option, int weightPosition)
			throws IllegalArgumentException{
		
		if(option == null){
			throw new IllegalArgumentException("The parameter cannot be null.");
		}
		if(weightPosition < 1){
			throw new IllegalArgumentException(
					"The importance parameter can only be 1 or above.");
		}
		
		final int prevWeightPosition = option.getWeightPosition();
		
		if(option.setWeight(weightPosition) > OptionData.DEFAULT_WEIGHT){
			
			final boolean isWeightSmaller = prevWeightPosition != OptionData.DEFAULT_WEIGHT
					&& prevWeightPosition > weightPosition;
			
			int smallestPosition = isWeightSmaller ? weightPosition
					: prevWeightPosition;
			
			int vector = isWeightSmaller ? 1 : -1;
			
			this.options.forEach((s, optionO) -> {
				
				if(optionO.getWeight() != OptionData.DEFAULT_WEIGHT
						&& prevWeightPosition != 0
						&& !option.equals(optionO)
						&& optionO.getWeightPosition() >= smallestPosition
						&& optionO.getWeightPosition() <= option
								.getWeightPosition()){
					
					optionO.setWeight(optionO.getWeightPosition() + vector);
					
				}
				
			});
			
		}
		
	}
	
	public void setOptions(Option... options){
		
		if(options != null && options.length > 0){
			
			List<List<String>> paramsHelpMap = new ArrayList<>();
			List<String> contentLessParams = new ArrayList<>();
			
			for(Option commandParamHelp : options){
				
				paramsHelpMap.add(commandParamHelp.getAllOptions());
				
				if(!commandParamHelp.doesAcceptsContent()){
					contentLessParams.add(commandParamHelp.getName());
				}
				
				if(commandParamHelp.getWeight() != 0){
					try{
						setOptionWeight(commandParamHelp.getName(),
								commandParamHelp.getWeight());
					}
					catch(IllegalArgumentException e){}
				}
				
			}
			
			setOptionLinkMap(paramsHelpMap);
			setOptionsAsContentLess(contentLessParams);
			
		}
		
	}
	
	public static String buildMessageFromArgs(String[] args, char optionsPrefix){
		StringBuilder builder = new StringBuilder();
		
		for(String arg : args){
			
			String protectedOptionPrefix = Pattern.quote(String
					.valueOf(optionsPrefix));
			String optionRegex = "^(" + protectedOptionPrefix + "{1,2}.+|"
					+ protectedOptionPrefix + "{2})$";
			
			if(arg.matches(optionRegex)){
				builder.append(arg);
			}
			else{
				builder.append("\"").append(arg).append("\"");
			}
			
			builder.append(" ");
		}
		
		String argsRequest = builder.toString();
		
		return argsRequest.isEmpty() ? "" : argsRequest.substring(0,
				argsRequest.length() - 1);
	}
	
}
