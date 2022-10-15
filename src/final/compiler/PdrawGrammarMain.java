import java.io.FileWriter;
import java.io.IOException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class PdrawGrammarMain {
   public static void main(String[] args) {
      try {
         // create a CharStream that reads from standard input:
         CharStream input = CharStreams.fromStream(System.in);
         // create a lexer that feeds off of input CharStream:
         PdrawGrammarLexer lexer = new PdrawGrammarLexer(input);
         // create a buffer of tokens pulled from the lexer:
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         // create a parser that feeds off the tokens buffer:
         PdrawGrammarParser parser = new PdrawGrammarParser(tokens);
         // replace error listener:
         //parser.removeErrorListeners(); // remove ConsoleErrorListener
         //parser.addErrorListener(new ErrorHandlingListener());
         // begin parsing at program rule:
         ParseTree tree = parser.program();
         if (parser.getNumberOfSyntaxErrors() == 0) {
            // print LISP-style tree:
            // System.out.println(tree.toStringTree(parser));
            Analise analysis = new Analise();
            Boolean analysisResult = analysis.visit(tree);

            System.out.println("Analysis Result:" + analysisResult);
            if (analysisResult) {
               Compiler compiler = new Compiler();
               String compilationResults = compiler.visit(tree).render();
               System.out.println("Compilation Result:");
               System.out.println(compilationResults);
            
               FileWriter fw = new FileWriter("../library/compilation.cpp");
               fw.write(compilationResults);
               fw.close();
            }

            System.out.println();
         }
      }
      catch(IOException e) {
         e.printStackTrace();
         System.exit(1);
      }
      catch(RecognitionException e) {
         e.printStackTrace();
         System.exit(1);
      }
   }
}
