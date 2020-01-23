package cliparser;
import java.util.ArrayList;

public class OptionsParser {
	private OptionsArrayList options;
	
	public OptionsParser() {
		options = new OptionsArrayList();
	}
	
	public void addOption(String optionName, int nbArguments, OptionDelegate delegate) {
		Option option = new Option(optionName, nbArguments, delegate);
		options.add(option);
	}
	
	public void parse(String[] arguments) {
		for(int i=0; i<arguments.length; i++) {
			try {
				Option currentOption = options.findOption(arguments[i]);
				String[] optionArgs = new String[currentOption.getNbArguments()];
				for(int j=1; j<=currentOption.getNbArguments(); j++) {
					optionArgs[j] = arguments[i+j];
				}
				currentOption.getDelegate().optionCallback(optionArgs);
				i = i + currentOption.getNbArguments();
			} catch (OptionDoesNotExistException e) {
				e.printStackTrace();
			}
		}
	}
}
