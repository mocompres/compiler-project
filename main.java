import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException{

	// we expect exactly one argument: the name of the input file
	if (args.length!=1) {
	    System.err.println("\n");
	    System.err.println("Simple calculator\n");
	    System.err.println("=================\n\n");
	    System.err.println("Please give as input argument a filename\n");
	    System.exit(-1);
	}
	String filename=args[0];

	// open the input file
	CharStream input = CharStreams.fromFileName(filename);
	    //new ANTLRFileStream (filename); // depricated
	
	// create a lexer/scanner
	implLexer lex = new implLexer(input);
	
	// get the stream of tokens from the scanner
	CommonTokenStream tokens = new CommonTokenStream(lex);
	
	// create a parser
	implParser parser = new implParser(tokens);
	
	// and parse anything from the grammar for "start"
	ParseTree parseTree = parser.start();

	// Construct an interpreter and run it on the parse tree
	Interpreter interpreter = new Interpreter();
	interpreter.visit(parseTree);
    }
}

// We write an interpreter that implements interface
// "implVisitor<T>" that is automatically generated by ANTLR
// This is parameterized over a return type "<T>" which is in our case
// simply a Double.

class Interpreter extends AbstractParseTreeVisitor<Double> implements implVisitor<Double> {

    static Environment env=new Environment();
    
    public Double visitStart(implParser.StartContext ctx){
	for(implParser.CommandContext c:ctx.cs) visit(c);
	return null;
    };

    public Double visitSingleCommand(implParser.SingleCommandContext ctx){
	return visit(ctx.c);
    }

    public Double visitMultipleCommands(implParser.MultipleCommandsContext ctx){
	for(implParser.CommandContext c:ctx.cs) visit(c);
	return null;
    }
    
    public Double visitAssignment(implParser.AssignmentContext ctx){
 	Double v=visit(ctx.e);
	env.setVariable(ctx.x.getText(),v);
	return null;
    }
    
    public Double visitOutput(implParser.OutputContext ctx){
	Double v=visit(ctx.e);
	System.out.println(v);
	return null;
    }

	/* -------------------------- command --------------------- */
    public Double visitWhileLoop(implParser.WhileLoopContext ctx){
	while(visit(ctx.c).equals(1.0)){
	    visit(ctx.p);
	}
	return null;
	}
	
	public Double visitIfStatement(implParser.IfStatementContext ctx){
		if(visit(ctx.c).equals(1.0)){
			visit(ctx.p);
		}
		return null;
	}

	public Double visitForLoop(implParser.ForLoopContext ctx){

		double v = visit(ctx.e);
		env.setVariable(ctx.x.getText(),v);

		for (double i = v; i <= visit(ctx.e2); i++) {
			env.setVariable(ctx.x.getText(),i);
			visit(ctx.p);
		}
		return null;
	}

	public Double visitArray(implParser.ArrayContext ctx){
		double s = visit(ctx.e);
		double v = visit(ctx.e1);
		env.setVariable("#" + ctx.x.getText()+s,s);
	
		return null;
	}
	
	/* -------------------------- expr --------------------- */
    public Double visitParenthesis(implParser.ParenthesisContext ctx){
	return visit(ctx.e);
    };
    
    public Double visitVariable(implParser.VariableContext ctx){
	return env.getVariable(ctx.x.getText());
    };
    
    public Double visitPlusMinusOperator(implParser.PlusMinusOperatorContext ctx){
		if (ctx.s.getText().equals("+"))
		return visit(ctx.e1) + visit(ctx.e2);
	else
		return visit(ctx.e1) - visit(ctx.e2);
	};

    public Double visitGangeDividerOperator(implParser.GangeDividerOperatorContext ctx){
		if (ctx.s.getText().equals("*"))
		return visit(ctx.e1) * visit(ctx.e2);
	else
		return visit(ctx.e1) / visit(ctx.e2);
	};
	
    public Double visitConstant(implParser.ConstantContext ctx){
	return Double.parseDouble(ctx.c.getText()); 
	};
	
	public Double visitExprArray(implParser.ExprArrayContext ctx){
		double v = visit(ctx.e);
		return env.getVariable("#" + ctx.a.getText() + v);
	};

	/* -------------------------- condition --------------------- */
    public Double visitUnequal(implParser.UnequalContext ctx){
	Double v1=visit(ctx.e1);
	Double v2=visit(ctx.e2);
	if (v1.equals(v2))  return 0.0;
	else return 1.0;
	}
	
	public Double visitEqual(implParser.EqualContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1.equals(v2))  return 1.0;
		else return 0.0;
	}

	public Double visitSmallerThan(implParser.SmallerThanContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1 < v2)  return 1.0;
		else return 0.0;
	}
	
	
	public Double visitBiggerThan(implParser.BiggerThanContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1 > v2)  return 1.0;
		else return 0.0;
	}

	public Double visitBiggerEqualThan(implParser.BiggerEqualThanContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1 <= v2)  return 1.0;
		else return 0.0;
	}
	
	public Double visitSmallerEqualThan(implParser.SmallerEqualThanContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1 >= v2)  return 1.0;
		else return 0.0;
	}

	
	public Double visitAnd(implParser.AndContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1.equals(1.0) && v2.equals(1.0))  return 1.0;
		else return 0.0;
	}

	public Double visitOr(implParser.OrContext ctx){
		Double v1=visit(ctx.e1);
		Double v2=visit(ctx.e2);
		if (v1.equals(1.0) || v2.equals(1.0))  return 1.0;
		else return 0.0;
	}
}

