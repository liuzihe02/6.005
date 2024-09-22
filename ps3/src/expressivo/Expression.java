/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

/**
 * An immutable data type representing a polynomial expression of:
 * + and *
 * nonnegative integers and floating-point numbers
 * variables (case-sensitive nonempty strings of letters)
 * 
 * <p>
 * PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of
 * existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing
 * methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {

    // Datatype definition
    // Expression = Number(value:Double) + Variable(name:String)
    // + Plus(left:Expression, right:Expression)
    // + Times(left:Expression, right:Expression)

    /**
     * Parse an expression.
     * 
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid. use a try except block
     */
    public static Expression parse(String input) throws IllegalArgumentException {
        try {

            // Create a stream of characters from the string
            CharStream stream = new ANTLRInputStream(input);

            // Make a parser
            ExpressionParser parser = makeParser(stream);

            // Generate the parse tree using the starter rule.
            // root is the starter rule for this grammar.
            // Other grammars may have different names for the starter rule.
            ParseTree tree = parser.root();

            // *** Debugging option #1: print the tree to the console
            System.err.println(tree.toStringTree(parser));

            // *** Debugging option #2: show the tree in a window
            Trees.inspect(tree, parser);

            // *** Debugging option #3: walk the tree with a listener
            ParseTreeWalker walker = new ParseTreeWalker();
            ExpressionListener listener = new MakeExpression();
            walker.walk(listener, tree);

            // now actually evaluate the string expression
            MakeExpression exprMaker = new MakeExpression();
            walker.walk(exprMaker, tree);

            //for the root Expression
            return exprMaker.getExpression();
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal argument!");
        }
    }

    /**
    * Make a parser that is ready to parse a stream of characters.
    * To start parsing, the client should call a method on the returned parser
    * corresponding to the start rule of the grammar, e.g. parser.root() or
    * whatever it happens to be.
    * During parsing, if the parser encounters a syntax error, it will throw a
    * ParseCancellationException.
    * 
    * @param stream stream of characters
    * @return a parser that is ready to parse the stream
    */
    public static ExpressionParser makeParser(CharStream stream) {
        // Make a lexer. This converts the stream of characters into a
        // stream of tokens. A token is a character group, like "<i>"
        // or "</i>". Note that this doesn't start reading the character stream yet,
        // it just sets up the lexer to read it.
        ExpressionLexer lexer = new ExpressionLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);

        // Make a parser whose input comes from the token stream produced by the lexer.
        ExpressionParser parser = new ExpressionParser(tokens);
        parser.reportErrorsAsExceptions();

        return parser;
    }

    /**
     * @return a parsable representation of this expression, such that
     *         for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     *         Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);

    /**
     * @return hash code value consistent with the equals() definition of structural
     *         equality, such that for all e1,e2:Expression,
     *         e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();

    // More instance methods

    // /**
    //  * static method
    //  * Check if this expression is a Number expression
    //  * 
    //  * @return true iff this expression is a Number
    //  */
    // public static boolean isNumber(Expression E) {
    //     // TODO:
    // };

}
