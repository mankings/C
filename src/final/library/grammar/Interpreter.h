
// Generated from Pen.g4 by ANTLR 4.9.3

#pragma once

#include <any>

#include "antlr4-runtime.h"
#include "PenParser.h"
#include "PenBaseVisitor.h"

/**
 * This class defines an abstract visitor for a parse tree
 * produced by PenParser.
 */
class  Interpreter : public PenBaseVisitor {
public:
      /**
       * Visit parse trees produced by PenParser.
       */
     antlrcpp::Any visitProgram(PenParser::ProgramContext *context) override;

     antlrcpp::Any visitStat(PenParser::StatContext *context) override;

     antlrcpp::Any visitExprDirection(PenParser::ExprDirectionContext *context) override;

     antlrcpp::Any visitExprBoolean(PenParser::ExprBooleanContext *context) override;

     antlrcpp::Any visitExprStrings(PenParser::ExprStringsContext *context) override;

     antlrcpp::Any visitExprColor(PenParser::ExprColorContext *context) override;

     antlrcpp::Any visitPen(PenParser::PenContext *context) override;

     antlrcpp::Any visitAssignProperty(PenParser::AssignPropertyContext *context) override;

     antlrcpp::Any visitSetColor(PenParser::SetColorContext *context) override;

     antlrcpp::Any visitSetThickness(PenParser::SetThicknessContext *context) override;

     antlrcpp::Any visitExprDouble(PenParser::ExprDoubleContext *context) override;

     antlrcpp::Any visitExprInt(PenParser::ExprIntContext *context) override;
    
     antlrcpp::Any visitExprSumSub(PenParser::ExprSumSubContext  *context) override;

     antlrcpp::Any visitExprUnary(PenParser::ExprUnaryContext *context) override;

     antlrcpp::Any visitSetDirection(PenParser::SetDirectionContext *context) override;

     antlrcpp::Any visitExprParentesis(PenParser::ExprParentesisContext *context) override;

     antlrcpp::Any visitExprMultDiv(PenParser::ExprMultDivContext *context) override;
};

