package cliparser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	public void parse(String[] arguments) throws OptionDoesNotExistException {
		for(int i=0; i<arguments.length; i++) {
			Option currentOption = options.get(arguments[i]);
			if(currentOption == null) throw new OptionDoesNotExistException();
			String[] optionArgs = new String[currentOption.getNbArguments()];
			for(int j=0; j<currentOption.getNbArguments(); j++) {
				optionArgs[j] = arguments[i+j+1];
			}
			currentOption.getDelegate().optionCallback(optionArgs);
			i = i + currentOption.getNbArguments();
		}
	}
}
