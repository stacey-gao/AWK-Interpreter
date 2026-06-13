
/*
 * It will hold both a string (the return value) and an enum – what happened, was it:

Normal, Break, Continue, Return
 */
public class ReturnType {

	private String value;
	private Type returnType;
	
	public enum Type {
		NORMAL, BREAK, CONTINUE, RETURN
		//normal and none mean the same thing
	}
	
	public ReturnType(Type returnType) {
		this.returnType = returnType;
		value = null;
	}
	
	public ReturnType(Type returnType, String value) {
		this.returnType = returnType;
		this.value = value;
	}
	
	public String toString() {
		return "Return Type: " + returnType + ", Rerurn Value: " + value;
	}

	//used in IntepretListOfStatements()
	public ReturnType.Type getReturnType() {
		return returnType;
	}
}
