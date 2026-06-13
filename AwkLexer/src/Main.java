import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Stacey Gao ICSI311- Lexer, University at Albany, Fall 2023
 */

public class Main {

	public static void main(String[] args) throws Exception {
		
		try {
			Path myPath = Paths.get("src/someFile.awk");
			String content = new String(Files.readAllBytes (myPath));
			Lexer lexer = new Lexer(content);
		    List<Token> tokens = lexer.Lex();
		    
            for (Token token : tokens) 
                System.out.print(token);
		} 
		catch (IOException e) {
			System.out.print("There is a error with the file.");
		}
	}

}
