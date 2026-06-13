import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType {

	private HashMap <String,InterpreterDataType> variables;
	
	public InterpreterArrayDataType(){
		this.variables = new HashMap<String,InterpreterDataType>();
	}
}
