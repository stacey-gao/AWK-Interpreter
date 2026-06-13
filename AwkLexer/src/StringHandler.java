/**
 * @author Stacey Gao ICSI311- Lexer, University at Albany, Fall 2023
 */
public class StringHandler {

	private String content;
	private int fingerPosition;
	
	
	public StringHandler(String content) {
		this.content= content;
		fingerPosition= 0;
	}
	
	/**
	 * doesn’t move the index
	 * @param i characters
	 * @return character that is “i” characters ahead
	 */
	public char Peek(int i) {
		return content.charAt(fingerPosition + i);
	}
	
	/**
	 * doesn’t move the index
	 * @param i characters
	 * @return string of the next “i” characters
	 */
	public String StringPeek(int i) {
		String nextCharacters = "";
		for (int count=0; count< i ;count++) {
			nextCharacters= nextCharacters + content.charAt(fingerPosition+ count) ;
		}
			
		return nextCharacters;
		
	}
	
	/**
	 * moves the index
	 * @return the next character
	 */
	public char getChar() {
		fingerPosition ++;
		if (IsDone()== true)
			return ' ';
		return content.charAt(fingerPosition);
		
	}
	
	/**
	 * moves the index ahead “i” positions
	 * @param i positions
	 */
	public void Swallow(int i) {
		fingerPosition= fingerPosition +i;
	}
	
	/**
	 * @return true, if we are at the end of the document
	 */
	public boolean IsDone() {
		if (fingerPosition > content.length()-1) 
			return true;
		else
			return false;
	}
	
	/**
	 * @return the rest of the document as a string
	 */
	public String Remainder() {
		String restOfCharacters = "";
		for (int count=0; count< content.length()-fingerPosition ;count++) {
			restOfCharacters= "" + content.charAt(fingerPosition+ count) ;
		}
			
		return restOfCharacters;
		
	}
	
	
}
