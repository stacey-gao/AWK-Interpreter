import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class Interpreter2Test {

	@Test
	public void InterpretOperationNodeADD() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.ADD, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("4.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeSubtract() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.SUBTRACT, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("0.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeMultiply() {
		var node = new OperationNode(new ConstantNode("3"), OperationNode.Operation.MULTIPLY, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("6.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeDivide() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.DIVIDE, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("1.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeModulo() {
		var node = new OperationNode(new ConstantNode("3"), OperationNode.Operation.MODULO, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("1.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeExponent() {
		var node = new OperationNode(new ConstantNode("3"), OperationNode.Operation.EXPONENT, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
		assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("9.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeConcatenation() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.CONCATENATION, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("22").toString());
	}
	
	@Test
	public void InterpretOperationNodeGreatertan() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.GREATHERTHAN, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("false").toString());
	}
	
	@Test
	public void InterpretOperationNodeGreatertanEqual() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.GREATHERTHAN_EQUALTO, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodeLesstan() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.LESSTHAN, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("false").toString());
	}
	
	@Test
	public void InterpretOperationNodeLesstanEqual() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.LESSTHAN_EQUALTO, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodeEqualComparisonNumbers() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.EQUAL, Optional.of(new ConstantNode("2")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodeEqualComparisonString() {
		var node = new OperationNode(new ConstantNode("foo"), OperationNode.Operation.EQUAL, Optional.of(new ConstantNode("foo")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodeNotEqualComparison() {
		var node = new OperationNode(new ConstantNode("7"), OperationNode.Operation.NOTMATCH, Optional.of(new ConstantNode("8")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodeMatch() {
		var node = new OperationNode(new ConstantNode("foo"), OperationNode.Operation.MATCH, Optional.of(new ConstantNode("foo")));
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("true").toString());
	}
	
	@Test
	public void InterpretOperationNodePreDec() {
		var node = new OperationNode(new ConstantNode("2"), OperationNode.Operation.PREDEC);
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("1.0").toString());
	}
	
	@Test
	public void InterpretOperationNodePostInc() {
		var node = new OperationNode(new ConstantNode("4"), OperationNode.Operation.POSTINC);
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("5.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeUnryPos() {
		var node = new OperationNode(new ConstantNode("7"), OperationNode.Operation.UNARYNEG);
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("-7.0").toString());
	}
	
	@Test
	public void InterpretOperationNodeDollar() {
		var node = new OperationNode(new ConstantNode("1"), OperationNode.Operation.DOLLAR);
		Interpreter interpreter = new Interpreter(node);
		
	    assertEquals(interpreter.getIDT(node, null).toString(), new InterpreterDataType("$1").toString());
	}
	
	


}
