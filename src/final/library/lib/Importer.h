//
// Created by luiscarlos on 6/14/22.
//

#include <string>
#include "PenLexer.h"
#include "PenParser.h"
#include "antlr4-runtime.h"
#include "../grammar/Interpreter.h"
#include "Pen.h"

namespace Import {
    std::map<std::basic_string<char>, GFX::Pen*> load(const char* basePath, const std::string& filename) {
        std::string base = std::string(basePath);
        std::ifstream penFile(base.substr(0, base.find_last_of('/') + 1) + filename);
        std::string content;
        std::stringstream sstr;

        if (penFile.is_open()) {
            penFile >> sstr.rdbuf();
        } else {
            return {};
        }
        content = sstr.str();

        antlr4::ANTLRInputStream input(content);
        PenLexer lexer(&input);
        antlr4::CommonTokenStream tokens(&lexer);

        auto* parser = new PenParser(&tokens);
        PenParser::ProgramContext* tree = parser->program();
        if (parser->getNumberOfSyntaxErrors() == 0) {
            // print LISP-style tree:

            auto *visitor = new Interpreter();
            antlrcpp::Any res = visitor->visitProgram(tree);
            auto pens = std::any_cast<std::map<std::string, GFX::Pen*>>(res);

            return pens;
        }

        std::cerr << "Errors in the .pen file";
        exit(EXIT_FAILURE);
    }
}