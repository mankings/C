#include <iostream>
#include <antlr4-runtime.h>
#include "./PenLexer.h"
#include "./PenParser.h"
#include "grammar/Interpreter.h"

using namespace std;
using namespace antlr4;
using namespace antlr4::tree;

int main(int argc, const char* argv[]) {
   std::istream *stream;
   // create a ANTLRInputStream that reads from standard input:
   ANTLRInputStream* input;
   stream = &cin;
   input = new ANTLRInputStream(*stream);
   // create a lexer that feeds off of input ANTLRInputStream:
   ./PenLexer* lexer = new ./PenLexer(input);
   // create a buffer of tokens pulled from the lexer:
   CommonTokenStream* tokens = new CommonTokenStream(lexer);
   // create a parser that feeds off the tokens buffer:
   ./PenParser* parser = new ./PenParser(tokens);
   // replace error listener:
   //parser->removeErrorListeners(); // remove ConsoleErrorListener
   //parser->addErrorListener(new ErrorHandlingListener());
   // begin parsing at program rule:
   ParseTree* tree = parser->program();
   if (parser->getNumberOfSyntaxErrors() == 0) {
      // print LISP-style tree:
      // cout << tree->toStringTree(parser) << endl;
      grammar/Interpreter* visitor0 = new grammar/Interpreter();
      visitor0->visit(tree);
   }
}
