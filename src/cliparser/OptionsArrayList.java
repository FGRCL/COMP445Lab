package cliparser;
import java.util.ArrayList;

public class OptionsArrayList extends ArrayList<Option>{
	Option foundOption;
	public Option findOption(String optionName) throws OptionDoesNotExistException {
		for(Option op : this){
			if(op.getOptionName().equals(optionName)) {
				foundOption = op;
			}
		}
		
		if(foundOption == null) {
			throw new OptionDoesNotExistException();
		}else {
			return foundOption;
		}
	}
}
