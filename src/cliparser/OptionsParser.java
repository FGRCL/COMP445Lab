package cliparser;
import java.util.Arrays;
import java.util.HashMap;

public class OptionsParser {
	private HashMap<String, Option> options;
	private int nbArguments;
	
	public OptionsParser(int nbArguments) {
		options = new HashMap<String, Option>();
		this.nbArguments = nbArguments;
	}
	
	public void addOption(String optionName, int nbArguments, OptionDelegate delegate) {
		Option option = new Option(optionName, nbArguments, delegate);
		options.put(optionName, option);
	}
	
	public String[] parse(String[] arguments) throws OptionDoesNotExistException {
		String[] programArguments = getProgramArguments(arguments); 
		String[] programOptions = Arrays.copyOfRange(arguments, 0, arguments.length-nbArguments);
		for(int i=0; i<programOptions.length; i++) {
			Option currentOption = options.get(programOptions[i]);
			if(currentOption == null) throw new OptionDoesNotExistException();
			String[] optionArgs = new String[currentOption.getNbArguments()];
			for(int j=0; j<currentOption.getNbArguments(); j++) {
				optionArgs[j] = programOptions[i+j+1];
			}
			currentOption.getDelegate().optionCallback(optionArgs);
			i = i + currentOption.getNbArguments();
		}
		return programArguments;
	}
	
	private String[] getProgramArguments(String[] arguments) {
		String[] programArguments = new String[nbArguments];
		for(int i=0; i<nbArguments; i++) {
			programArguments[i] = arguments[arguments.length-(i+1)];
		}
		return programArguments;
	}	
}
