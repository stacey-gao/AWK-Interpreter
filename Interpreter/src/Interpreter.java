import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Interpreter {
	
	private HashMap<String, InterpreterDataType> globalVariables = new HashMap<String, InterpreterDataType>();
	private HashMap<String, FunctionDefinitionNode> functions = new HashMap<String, FunctionDefinitionNode>();
	
	public class LineManager {
		
		private List<String> input;
		
		public LineManager(List<String> input){
			this.input= input;
			
		}
		
		/**
		 * get the next line and split it by looking at the global variables to find “FS”
		 * sets NF, NF, FNR
		 * @return false if there is no line to split
		 */
		public boolean SplitAndAssign() {
			for(String string: input) {
				string.split(globalVariables.get("FS").toString());
				globalVariables.put("NR", new InterpreterDataType());
				globalVariables.put("FNR", new InterpreterDataType());
				globalVariables.put("NF", new InterpreterDataType());
			}
	        
			return false;
			
		}

	}

	public Interpreter(ProgramNode node, Path filePath){
		try {
			LineManager manager= new LineManager(Files.readAllLines(filePath));
			
		} catch (IOException e) {
			LineManager manager= new LineManager(new ArrayList<String>());
		}
		
		globalVariables.put("FILENAME", new InterpreterDataType(filePath.toString()));
		setGlobalVariables();
		setBuiltInFunctions();
		
	}
	
	//constructor only used for JUnit testing 
	public Interpreter(Node node) {
		globalVariables.put("FILENAME", new InterpreterDataType("testing.txt"));
		setGlobalVariables();
		setBuiltInFunctions();
	}

	private void setGlobalVariables() {
		globalVariables.put("FS", new InterpreterDataType(" "));
		globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
		globalVariables.put("ORS", new InterpreterDataType("\n"));
		// will not be implementing RS
		
	}
	
	private void setBuiltInFunctions() {
		LinkedList <Token> parameter = new LinkedList<>();
		functions.put("print", new BuiltInFunctionDefinitionNode("print", parameter, null, null , true));
		functions.put("printf", new BuiltInFunctionDefinitionNode("printf", parameter, null, null, true));
		functions.put("getline", new BuiltInFunctionDefinitionNode("getline", parameter, null, null, false));
		functions.put("next", new BuiltInFunctionDefinitionNode("next", parameter, null, null, false));
		functions.put("gsub", new BuiltInFunctionDefinitionNode("gsub", parameter, null, null, false));
		functions.put("match", new BuiltInFunctionDefinitionNode("match", parameter, null, null, false));
		functions.put("sub", new BuiltInFunctionDefinitionNode("sub", parameter, null, null, false));
		functions.put("index", new BuiltInFunctionDefinitionNode("index", parameter, null, null, false));
		functions.put("length", new BuiltInFunctionDefinitionNode("length", parameter, null, null, false));
		functions.put("split", new BuiltInFunctionDefinitionNode("split", parameter, null, null, false));
		functions.put("substr", new BuiltInFunctionDefinitionNode("substr", parameter, null, null, false));
		functions.put("toLower", new BuiltInFunctionDefinitionNode("toLower", parameter, null, null, false));
		functions.put("toUpper", new BuiltInFunctionDefinitionNode("toUpper", parameter, null, null, false));
		
	}
	
	
	//evaluates the node and returns an IDT
	//public for testing, needs heavy debugging
	public InterpreterDataType getIDT(Node node, HashMap<String, InterpreterDataType> localVariables) {
		if(node == null) {
			throw new IllegalArgumentException("getIDT: Node is null");
		}
		
		else if (node instanceof AssignmentNode) {
			if (((AssignmentNode) node).getTarget() instanceof VariableReferenceNode ||
					((AssignmentNode) node).getTarget() instanceof OperationNode) {
				
			InterpreterDataType result = getIDT(((AssignmentNode) node).getExpression(), localVariables);
			localVariables.put(((VariableReferenceNode)((AssignmentNode) node).getTarget()).getName(), result);
			return result;
			}
			else {
				throw new IllegalArgumentException("getIDT: AssigmentNode target is not OperationNode or VariableReferenceNode");
			}
			
		}
		
		else if (node instanceof ConstantNode) {
			new InterpreterDataType((((ConstantNode) node).getValue()));
		}
		
		else if (node instanceof FunctionCallNode) {//RunFuncationCall not implemented yet
			return new InterpreterDataType(RunFunctionCall());
		}

		else if (node instanceof PatternNode) {
			throw new IllegalArgumentException("getIDT: you are trying to pass a pattern to a function or an assignment");
		}
	
		else if (node instanceof TernaryNode) {//needs fixing
			getIDT(((TernaryNode) node).getCondition(), localVariables);
			if(((TernaryNode) node).getStatus() == true) 
				return new InterpreterDataType(((TernaryNode) node).getIfTrue().toString());
			else if (((TernaryNode) node).getStatus() == false) {
				return new InterpreterDataType(((TernaryNode) node).getIfFalse().toString());
			}
		}

		else if (node instanceof VariableReferenceNode) {
			//not array reference
			if (((VariableReferenceNode)node).getIndexExpression() == null) {
				if (globalVariables.get(((VariableReferenceNode)node).getName()) != null)
					return globalVariables.get(((VariableReferenceNode)node).getName());
				else {
					return localVariables.get(((VariableReferenceNode)node).getName());
				}
			}
			//array reference
			else {
				
			}
		}
		
		else if (node instanceof OperationNode) {
			
			//evaluates nested operations
			InterpreterDataType left=null, right = null ;
			if (((OperationNode)node).getLeft() instanceof OperationNode) {
				left = getIDT(((OperationNode)node).getLeft(), localVariables);
			}
			else {
				left = new InterpreterDataType(((ConstantNode) ((OperationNode)node).getLeft()).getValue());
			}
			if (((OperationNode)node).getRight() != null)
				if (((OperationNode)node).getRight().get() instanceof OperationNode ) {
					right = getIDT(((OperationNode)node).getRight().get(), localVariables);
				}
				else {
					right = new InterpreterDataType(((ConstantNode) ((OperationNode)node).getRight().get()).getValue());
				}
			
			//operations
			if (((OperationNode)node).getOperation() == OperationNode.Operation.ADD) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) + Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.SUBTRACT) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) - Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.MULTIPLY) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) * Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.DIVIDE) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) / Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.EXPONENT) {
				return new InterpreterDataType (String.valueOf(Math.pow(Float.parseFloat(left.getValue()), Float.parseFloat(right.getValue()))));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.MODULO) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) % Float.parseFloat(right.getValue())));
			}
			
			//compare
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.GREATHERTHAN) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) > Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.GREATHERTHAN_EQUALTO) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) >= Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.LESSTHAN) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) < Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.LESSTHAN_EQUALTO) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) <= Float.parseFloat(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.EQUAL) {
				try {// compare numbers
					return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) == Float.parseFloat(right.getValue())));
				}
				//comparing strings
				catch (NumberFormatException e) {
					return new InterpreterDataType (Boolean.toString(left.getValue().equals(right.getValue())));
		        }
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.NOT_EQUAL) {
				return new InterpreterDataType (String.valueOf(Float.parseFloat(left.getValue()) != Float.parseFloat(right.getValue())));
			}
			
			///match
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.MATCH) {
				return new InterpreterDataType (Boolean.toString(left.getValue().matches(right.getValue())));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.NOTMATCH) {
				return new InterpreterDataType (Boolean.toString(!(left.getValue().matches(right.getValue()))));
			}
			
			//dollar
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.DOLLAR) {
				//if left is an Operation Node, it already has been evaluated above
				return new InterpreterDataType (String.valueOf("$" + left.getValue()));
			}
			
			//booleans
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.AND) {
				
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.OR) {
				
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.NOT) {

			}
			
			
			//pre/post/unary
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.PREINC) {
				float temp = Float.parseFloat(left.getValue());
				++temp;
				return new InterpreterDataType (String.valueOf(temp));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.POSTINC) {
				float temp = Float.parseFloat(left.getValue());
				temp++;
				return new InterpreterDataType (String.valueOf(temp));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.POSTDEC) {
				float temp = Float.parseFloat(left.getValue());
				temp--;
				return new InterpreterDataType (String.valueOf(temp));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.PREDEC) {
				float temp = Float.parseFloat(left.getValue());
				--temp;
				return new InterpreterDataType (String.valueOf(temp));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.UNARYNEG) {
				float temp = Float.parseFloat(left.getValue());
				temp = -temp;
				return new InterpreterDataType (String.valueOf(temp));
			}
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.UNARYPOS) {
				float temp = Float.parseFloat(left.getValue());
				temp = +temp;
				return new InterpreterDataType (String.valueOf(temp));
			}
			
			
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.CONCATENATION) {
				return new InterpreterDataType (left.getValue() + right.getValue());
			}
			
			else if (((OperationNode)node).getOperation() == OperationNode.Operation.IN) {
				//check to make sure the right hand side is a variable reference and is an array (throw an exception if not)
				//Then look up the left hand side in the array (which will be in globals or locals). 
			}
		}
		else {
			throw new IllegalArgumentException("getIDT: unknown node type");
		}
		return null;
		
	}

	//that takes the function call node and locals and returns a String. (not yet implemented)
	private String RunFunctionCall() {
		return "";
	}
	
	public ReturnType ProcessStatement(HashMap<String, InterpreterDataType> locals, StatementNode node) {
		if (node instanceof AssignmentNode) {
			getIDT(node, locals);
			// target is the left node, and expression is the right node in Assignment Node
			InterpreterDataType right = getIDT(((AssignmentNode) node).getExpression(), locals);
			locals.put(((AssignmentNode) node).getTarget().toString(), right);
			return new ReturnType(ReturnType.Type.NORMAL, right.toString() );
		}
		
		else if  (node instanceof BreakNode) {
			return new ReturnType(ReturnType.Type.BREAK);
		}
		
		else if  (node instanceof ContinueNode) {
			return new ReturnType(ReturnType.Type.CONTINUE);
		}
		
		else if  (node instanceof DeleteNode) {
			//without index
			if (((DeleteNode)node).getIndices().isEmpty()) {
				locals.remove(((DeleteNode)node).getArray());
				globalVariables.remove(((DeleteNode)node).getArray());
			}
			//with index
			else if (((DeleteNode)node).getIndices().isPresent()) {
				InterpreterDataType array;
				if ((array = locals.get(((DeleteNode)node).getArray())) != null){
					//fix this, remove array index in local
				}
				else if ((array = globalVariables.get(((DeleteNode)node).getArray())) != null){
					//fix this, remove array index in global
				}
			}
			return new ReturnType(ReturnType.Type.NORMAL);
		}
		
		else if  (node instanceof DoWhileNode) {
			ReturnType returnValue;
			//evaluates condition if needed
			InterpreterDataType condition = getIDT(((DoWhileNode) node).getCondition(), locals);
			do {
				returnValue = InterpretListOfStatements(((DoWhileNode) node).getBlock().getStatements(),locals);
				if (returnValue.getReturnType() == ReturnType.Type.BREAK)
					break;
				if (returnValue.getReturnType() == ReturnType.Type.RETURN)
					return returnValue;
			}while(Boolean.parseBoolean(condition.toString()));
			return new ReturnType(ReturnType.Type.NORMAL);
		}
		
		else if  (node instanceof ForNode) {
			if (((ForNode) node).getInitializer().isPresent()) {
				ProcessStatement(locals, (StatementNode) ((ForNode) node).getInitializer().get());
			}
			ReturnType returnValue;
			InterpreterDataType condition = getIDT(((ForNode) node).getCondition().get(), locals);
			while(Boolean.parseBoolean(condition.toString())) {
				returnValue = InterpretListOfStatements(((ForNode) node).getBlock().getStatements(),locals);
				if (returnValue.getReturnType() == ReturnType.Type.BREAK)
					break;
				if (returnValue.getReturnType() == ReturnType.Type.RETURN)
					return returnValue;
				//processes iterator if exists
				if (((ForNode) node).getIterator().isPresent()) {
					ProcessStatement(locals, (StatementNode) ((ForNode) node).getIterator().get());
				}
			}
			return new ReturnType(ReturnType.Type.NORMAL);
			
		}
		
		else if  (node instanceof ForEachNode) {
			//code needs fixing (finding array in local and globals )
			Node array = ((ForEachNode) node).getArray().get();
			
			String array1[] = null;// placeholder array
			
			ReturnType returnValue;
			for (var a : array1) {
				returnValue = InterpretListOfStatements(((ForEachNode) node).getBlock().getStatements(),locals);
				if (returnValue.getReturnType() == ReturnType.Type.BREAK)
					break;
				if (returnValue.getReturnType() == ReturnType.Type.RETURN)
					return returnValue;
			}
			return new ReturnType(ReturnType.Type.NORMAL);
		}
		
		else if  (node instanceof FunctionCallNode) {
			return new ReturnType(ReturnType.Type.RETURN, RunFunctionCall());
		}
		
		else if  (node instanceof IfNode) {
			//code needs fixing (iterating through linked list part)
			ReturnType returnValue;
			InterpreterDataType condition = getIDT(((IfNode) node).getCondition(), locals);
			if(Boolean.parseBoolean(condition.toString())) {
				// interprets linked list of statement which is in the BlockNode stored in the ifNode
				returnValue = InterpretListOfStatements(((IfNode) node).getBlock().getStatements(),locals);
				if (returnValue.getReturnType() == ReturnType.Type.BREAK ||
				returnValue.getReturnType() == ReturnType.Type.RETURN)
					return returnValue;
			}
		}
		else if  (node instanceof ReturnNode) {
			if (((ReturnNode) node).getValue() != null) {
				return new ReturnType(ReturnType.Type.RETURN, getIDT(((ReturnNode) node).getValue(),locals).toString());
			}
		}
		else if  (node instanceof WhileNode) {
			ReturnType returnValue;
			//evaluates condition if needed
			InterpreterDataType condition = getIDT(((WhileNode) node).getCondition(), locals);
			while(Boolean.parseBoolean(condition.toString())) {
				returnValue = InterpretListOfStatements(((WhileNode) node).getBlock().getStatements(),locals);
				if (returnValue.getReturnType() == ReturnType.Type.BREAK)
					break;
				if (returnValue.getReturnType() == ReturnType.Type.RETURN)
					return returnValue;
			}
			return new ReturnType(ReturnType.Type.NORMAL);
		}
		else{
			throw new IllegalStateException ("Unidentifed node type in ProcessStatement():" + node);
		}
		
		return null;
		
	}

	private ReturnType InterpretListOfStatements(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> locals) {
		//if return type from each processStatement is not None, return passing “up” the same ReturnType
		
		ReturnType returnValue;
		for (StatementNode statement: statements) {
			returnValue = ProcessStatement(locals, statement);
			if (returnValue.getReturnType() != ReturnType.Type.NORMAL)
				return returnValue;
		}
		return null;
		
	}
	
	
	
	
	
}
