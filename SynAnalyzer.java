
//Syntax analyzer

import java.io.*;

public class SynAnalyzer{
	
	protected MicroRLexer lexer;	//lexical analyzer	
	protected Token token;		//current token
	
	public SynAnalyzer() throws IOException{
		lexer = new MicroRLexer (new InputStreamReader(System.in));
		getToken();	//get first token
	}
	public void getToken() throws IOException{
		token = lexer.nextToken();
	
	}

	//Program ::= source("List.R"){FunctionDef} MainDef
	public void program() throws IOException{
		if(token.symbol() != Symbol.SOURCE){
			ErrorMessage.print(lexer.position(),"key 'source' EXPECTED");
		}
		else{
			getToken();
			if(token.symbol() != Symbol.LPAREN){
				ErrorMessage.print(lexer.position(),"'(' EXPECTED");
			}
			else{
                		getToken();
                		if(token.symbol() != Symbol.LISTR) {
                    			ErrorMessage.print(lexer.position(), "'List.R' EXPECTED");
                		}
				else{
                    			getToken();
                    			if (token.symbol() != Symbol.RPAREN){
                        		ErrorMessage.print(lexer.position(), "')' EXPECTED");
                    			}
                    			else{
                        			getToken();

                    			}
                		}

			}
		}

		while (token.symbol() == Symbol.ID){
            			//getToken();
            		functionDef();
        	}
        	if (token.symbol() == Symbol.MAIN){
            		//getToken();
            		mainDef();
        	}
    }		
	//FunctionDef ::= id < − function ( [id {, id }] ) { {Statement} return ( Expr ) ; }
        public void functionDef() throws IOException{
        getToken();
        if(token.symbol() == Symbol.ASSIGN){
            getToken();
            if(token.symbol() == Symbol.FUNCTION){
                getToken();
                if (token.symbol() == Symbol.LPAREN){
                    getToken();
                    if (token.symbol() == Symbol.ID){
                        getToken();
                        while(token.symbol() == Symbol.COMMA){
                            getToken();
			    if (token.symbol() == Symbol.ID)
                        	getToken();
			    else
				ErrorMessage.print(lexer.position(), "'ID' EXPECTED");    
			}
                        if(token.symbol() == Symbol.RPAREN){
				getToken();
                            if(token.symbol() == Symbol.LBRACE){
                                getToken();
                                statement();
				while(token.symbol != Symbol.RETURN)
					statement();
                                if(token.symbol() == Symbol.RETURN){
                                    getToken();				  
                                    if(token.symbol() == Symbol.LPAREN){
                                        getToken();
                                       // if(token.symbol() == Symbol.ID){
                                         //   getToken();
                                            expr();
                                            if(token.symbol() == Symbol.RPAREN){
                                                getToken();
                                                if(token.symbol() == Symbol.SEMICOLON){
                                                    getToken();
                                                }
						else
                                                    ErrorMessage.print(lexer.position(), "';' EXPECTED");
                                            }
                                            else
                                                ErrorMessage.print(lexer.position(), "')' EXPECTED");                                       
				       }                                       
                                    
                                    else
                                        ErrorMessage.print(lexer.position(), "'(' EXPECTED");
                                }
                                else
                                    ErrorMessage.print(lexer.position(), "'return' EXPECTED");
                            }
                            else
                                ErrorMessage.print(lexer.position(), "'{' EXPECTED");
                        }
			else
                            ErrorMessage.print(lexer.position(), "')' EXPECTED");
                    }
                    else
                        ErrorMessage.print(lexer.position(), "'argument(s)' EXPECTED");
                }
                else
                    ErrorMessage.print(lexer.position(), "'(' EXPECTED");
            }
            else
                ErrorMessage.print(lexer.position(), "'function' EXPECTED");            
        }
        else
            ErrorMessage.print(lexer.position(), "'<-' EXPECTED");

    }
    //MainDef ::= main < − function ( ) { StatementList }
    public void mainDef() throws IOException {
        getToken();
        if (token.symbol() == Symbol.ASSIGN) {
            getToken();
            if (token.symbol() == Symbol.FUNCTION) {
                getToken();
                if (token.symbol() == Symbol.LPAREN) {
                    getToken();
                    if (token.symbol() == Symbol.RPAREN) {
                        getToken();
			if(token.symbol() == Symbol.LBRACE){
                        	getToken();
				statementList();
				if(token.symbol == Symbol.RBRACE){
					getToken();}
				else
					ErrorMessage.print(lexer.position(), "'}' EXPECTED");

			}
			else
				ErrorMessage.print(lexer.position(), "'{' EXPECTED");

                    }
		    else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                } else
                    ErrorMessage.print(lexer.position(), "'(' EXPECTED");
            } else {
                ErrorMessage.print(lexer.position(), "'function' EXPECTED");
            }
        } else
            ErrorMessage.print(lexer.position(), "'<-' EXPECTED");
    }

    //StatementList ::= Statement { Statement }
    public void statementList() throws IOException {

       // if (token.symbol() == Symbol.LBRACE) {
           // getToken();
            statement();
            while (token.symbol() != Symbol.RBRACE) {
                getToken();
                statement();
            }
           // if(token.symbol() == Symbol.RBRACE)
           //     getToken();
           // else
           //     ErrorMessage.print(lexer.position(), "'}' EXPECTED");
       // }
       // else
       //     ErrorMessage.print(lexer.position(), "'{' EXPECTED");

    }

    //Statement ::= if ( Cond ) { StatementList } [else { StatementList }]
    //              | while ( Cond ) { StatementList }
    //              | id < − Expr ;
    //              | print ( Expr ) ;
    public void statement() throws IOException{
        
	switch (token.symbol()){

            case IF: //if ( Cond ) { StatementList } [else { StatementList }]
                getToken();
                if (token.symbol() == Symbol.LPAREN){
                    getToken();
                    condition();
                    if (token.symbol() == Symbol.RPAREN) {
                        getToken();
                        if (token.symbol() == Symbol.LBRACE) {
                            getToken();
                            statementList();
                            if (token.symbol() == Symbol.RBRACE) {
                                getToken();
                                if(token.symbol() == Symbol.ELSE) {
                                    	getToken();
					if (token.symbol() == Symbol.LBRACE) {
						getToken();
                                        	statementList();
                                        if (token.symbol() == Symbol.RBRACE) {
                                            getToken();
                                        }
                                        else
                                            ErrorMessage.print(lexer.position(), "'}' EXPECTED");
                                    }
                                    else
                                        ErrorMessage.print(lexer.position(), "'{' EXPECTED");
                                }
                            }
                            else
                                ErrorMessage.print(lexer.position(), "'}' EXPECTED");
                        }
                        else
				ErrorMessage.print(lexer.position(), "'{' EXPECTED");
                    }
                    else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                }
                else
                    ErrorMessage.print(lexer.position(), "'(' EXPECTED");

                break;

            case WHILE: //  | while ( Cond ) { StatementList }
                getToken();
                if(token.symbol() == Symbol.LPAREN){
                    getToken();
                    condition();
                    if(token.symbol() == Symbol.RPAREN){
                        getToken();
                        if(token.symbol() == Symbol.LBRACE){
                            getToken();
                            statementList();
                            if(token.symbol() == Symbol.RBRACE){
                                getToken();
                            }
                            else
                                ErrorMessage.print(lexer.position(), "'}' EXPECTED");
                        }
			else
                            ErrorMessage.print(lexer.position(), "'{' EXPECTED");
                    }
                    else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                }
                else
                    ErrorMessage.print(lexer.position(), "'(' EXPECTED");
                break;

	 case ID:  //| id < − Expr
                getToken();
                if(token.symbol() == Symbol.ASSIGN){
                    getToken();
		    expr();
		    if(token.symbol() == Symbol.SEMICOLON)
                    	getToken();
                    else
                    	ErrorMessage.print(lexer.position(), "; EXPECTED");
		}
                else
                    ErrorMessage.print(lexer.position(), "'<-' EXPECTED");
                
                break;

          case PRINT:      // | print ( Expr )
                getToken();
                if(token.symbol() == Symbol.LPAREN) {
                    getToken();
                    expr();
                    if(token.symbol() == Symbol.RPAREN)
                        getToken();
                    else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                }
                else
                    ErrorMessage.print(lexer.position(), "'(' EXPECTED");
		if(token.symbol() == Symbol.SEMICOLON)
                    getToken();
                else
                    ErrorMessage.print(lexer.position(), "; EXPECTED");
		break;
	default: break;	//ε
        }
    }
    //Cond ::= AndExpr {|| AndExpr}
    public void condition() throws IOException{
        //getToken();
        andExpr();
        while(token.symbol() == Symbol.OR) {
            getToken();
            andExpr();
        }
    }

    //AndExpr ::= RelExpr {&& RelExpr}
    public void andExpr() throws IOException{
        //getToken();
        relExpr();
        while(token.symbol() == Symbol.AND){
            getToken();
            relExpr();
        }
    }
    //RelExpr ::= [!] Expr RelOper Expr
    public void relExpr() throws IOException{
        if (token.symbol() == Symbol.NOT){
            getToken();
	    
        }
        expr();
        switch(token.symbol()){ //RelOper ::= < | <= | > | >= | == | !=
            case LT:
            case LE:
            case GT:
            case GE:
            case EQ:
            case NE:
                getToken();
                expr();
                break;
            default:
                ErrorMessage.print(lexer.position(), "RELATIONAL OPERATOR EXPECTED");
        }

    }
    //Expr ::= MulExpr {AddOper MulExpr}
    public void expr() throws IOException{        
	mulExpr();
        while(token.symbol() == Symbol.PLUS || token.symbol == Symbol.MINUS){  //AddOper ::= + | −
            getToken();
            mulExpr();
        }
    }

    //MulExpr ::= PrefixExpr {MulOper PrefixExpr}
    public void mulExpr() throws IOException{
        prefixExpr();
        while(token.symbol() == Symbol.TIMES || token.symbol == Symbol.SLASH){  //MulOper ::= * | /
            getToken();
            prefixExpr();
        }
    }
    //PrefixExpr ::= [AddOper] SimpleExpr
    public void prefixExpr() throws IOException{
        if (token.symbol == Symbol.PLUS || token.symbol == Symbol.MINUS){
            getToken();
            simpleExpr();
        }
	simpleExpr();
    }
    //SimpleExpr ::= integer | ( Expr ) | as.integer ( readline ( ) ) 
    // 	 	   | id [ ( [Expr {, Expr}] ) ] | cons ( Expr , Expr )
    //		   | head ( Expr ) | tail ( Expr ) | null ( )

    public void simpleExpr() throws IOException{
        if (token.symbol() == Symbol.INTEGER) //integer
            getToken();

        else if(token.symbol() == Symbol.LPAREN){ //expr
            getToken();
            expr();
            if(token.symbol() == Symbol.RPAREN)
                getToken();
            else
                ErrorMessage.print(lexer.position(), "')' EXPECTED");
        }
	else if(token.symbol() == Symbol.ASINTEGER){  //as.integer(readline())
            getToken();
            if(token.symbol() == Symbol.LPAREN){
                getToken();
                if(token.symbol() == Symbol.READLINE){
                    getToken();
                    if(token.symbol() == Symbol.LPAREN) {
                        getToken();
                        if (token.symbol() == Symbol.RPAREN){ 
    				getToken();
				if (token.symbol() == Symbol.RPAREN)
	                            getToken();
				else
        	                    ErrorMessage.print(lexer.position(), "')' EXPECTED");

			}
                        else
                            ErrorMessage.print(lexer.position(), "')' EXPECTED");
                    }
		    else
                    	ErrorMessage.print(lexer.position(), "'(' EXPECTED");
                }
		else
                	ErrorMessage.print(lexer.position(), "'readline' EXPECTED");
            }
	    else
            	ErrorMessage.print(lexer.position(), "'(' EXPECTED");
        }
	else if (token.symbol() == Symbol.ID) {//id [ ( [Expr {, Expr}] )]
            getToken();
            if (token.symbol() == Symbol.LPAREN) {
                getToken();
                expr();
                while (token.symbol() == Symbol.COMMA) {
                        getToken();
                        expr();
		}

                if (token.symbol() == Symbol.RPAREN)
                        getToken();
                else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                }		    
            
        }
	else if(token.symbol() == Symbol.CONS){//cons ( Expr , Expr )
            getToken();
            if(token.symbol() == Symbol.LPAREN) {
                getToken();
		expr();
                if (token.symbol() == Symbol.COMMA) {
		    getToken();
                    expr();
                    if(token.symbol() == Symbol.RPAREN)
                        getToken();
                    else
                        ErrorMessage.print(lexer.position(), "')' EXPECTED");
                } else
                    ErrorMessage.print(lexer.position(), "',' EXPECTED");
            }
	    else
		    ErrorMessage.print(lexer.position(), "'(' EXPECTED");
        }
	else if(token.symbol() == Symbol.HEAD){   //head ( Expr )
            getToken();
            if(token.symbol() == Symbol.LPAREN){
                getToken();
                expr();
                if(token.symbol() == Symbol.RPAREN) {
                    getToken();
                }
                else
                    ErrorMessage.print(lexer.position(), "')' EXPECTED");
            }
            else
                ErrorMessage.print(lexer.position(), "'(' EXPECTED");
        }
	else if(token.symbol() == Symbol.TAIL){//tail ( Expr )
            getToken();
            if(token.symbol() == Symbol.LPAREN){
                getToken();
                expr();
                if(token.symbol() == Symbol.RPAREN) {
                    getToken();
                }
                else
                    ErrorMessage.print(lexer.position(), "')' EXPECTED");
            }
            else
                ErrorMessage.print(lexer.position(), "'(' EXPECTED");
        }
	else if(token.symbol() == Symbol.NULL) { //null ( )
            getToken();
            if (token.symbol() == Symbol.LPAREN) {
                getToken();
                if (token.symbol() == Symbol.RPAREN) {
                    getToken();
                } else
                    ErrorMessage.print(lexer.position(), "')' EXPECTED");
            } else
                ErrorMessage.print(lexer.position(), "'(' EXPECTED");
        }
	else
		ErrorMessage.print(lexer.position(), "Undefined Token");
    }

}	
