import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author Stacey Gao ICSI311- Parser1, University at Albany, Fall 2023
 */
public class ParserMain {

	public static void main(String[] args) throws Exception {
		
		try {
			Path myPath = Paths.get("src/testfile");
			String content = new String(Files.readAllBytes (myPath));
			Lexer lexer = new Lexer(content);
		    LinkedList<Token> tokens = lexer.Lex();
		    Parser parser = new Parser(tokens);
		    Optional<Node> node = parser.ParseOperation();
		    
            System.out.print(node);
		} 
		catch (IOException e) {
			System.out.print("There is a error with the file.");
		}
	}

}