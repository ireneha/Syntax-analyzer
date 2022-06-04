// MicroRLex class 
// This class is a MicroR lexical analyzer which reads a MicroR source program 
// and outputs the list of tokens comprising that program. 

import java.io.*;

public class MicroRLex {

  private static final int MAX_TOKENS = 1000;

  public static void main (String args []) throws IOException {

    int i, n;
    Token [] token = new Token [MAX_TOKENS];
    MicroRLexer lexer = new MicroRLexer (new InputStreamReader (System . in));

    System . out . println ("Source Program");
    System . out . println ("--------------");
    System . out . println ();

    n = -1;
    do {
      if (n < MAX_TOKENS)
        token [++n] = lexer . nextToken ();
      else
	ErrorMessage . print ("Maximum number of tokens exceeded");
    } while (token [n] . symbol () != Symbol . EOF);

    System . out . println ();
    System . out . println ("List of Tokens");
    System . out . println ("--------------");
    System . out . println ();
    for (i = 0; i < n; i++)
      System . out . println (token [i]);
    System . out . println ();
  }

}
