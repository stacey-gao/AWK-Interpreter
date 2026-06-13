import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode{

	private Function<HashMap<String, InterpreterDataType>, String> Execute;
	private boolean variadic; // variadic means can accept any # of parameters (print, printf)

    public BuiltInFunctionDefinitionNode(String functionName,
    		LinkedList<Token> parameters,
    		LinkedList <StatementNode> statements,
    		Function<HashMap<String, InterpreterDataType>, String> execute,
    		boolean variadic) {
        super(functionName, parameters, statements);
        this.Execute = execute;
        this.variadic = variadic;
    }
}
