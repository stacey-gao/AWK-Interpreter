import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Stacey Gao ICSI311- Parser1, University at Albany, Fall 2023
 */
public class TokenHandler {

	private LinkedList <Token> tokens;
	private int tokenIndex;
	
	/**
	 * @param tokens from lexer
	 */
	public TokenHandler(LinkedList tokens){
		this.tokens = tokens;
		tokenIndex = 0;
	}
	
	/**
	 * peek “j” tokens ahead and return the token
	 * @param j
	 * @return token “j” tokens ahead
	 */
	public Optional<Token> Peek(int j){
		if (MoreTokens()) 
			return Optional.of(tokens.get(tokenIndex +j));
		else
			return Optional.empty();
		
	}
	
	/**
	 * @return true if the token list is not empty
	 */
	public boolean MoreTokens() {
		return !tokens.isEmpty();
	}
	
	/**
	 * @param type
	 * @return token from the list if the head of token list is the same type as what was passed in
	 * @return in all other cases, returns Optional.Empty()
	 */
	public Optional<Token> MatchAndRemove(Token.TokenType type){
		
		if(MoreTokens() && tokens.getFirst().getTokenType() == type) {
			Token temp = tokens.getFirst();
			tokens.remove(0);
			return Optional.of(temp);
		}
		return Optional.empty();
		
	}
	
	
	
}
