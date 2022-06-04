//Micro Syntax
//
//This program is a recursive descent parser for MicroR
//

public class MicroSyn{
	public static void main (String args[]) throws java.io.IOException{
		System.out.println("Source Program");
		System.out.println("--------------");
		System.out.println();
		SynAnalyzer microR = new SynAnalyzer();
		microR.program();

		System.out.println();
		System.out.println();
		System.out.println("PARSE SUCCESSFUL");
	}
}
