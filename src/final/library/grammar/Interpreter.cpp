
// Generated from Pen.g4 by ANTLR 4.9.3


#include <string>
#include <sstream>
#include "../lib/Pen.h"
#include "Args.h"
#include "Interpreter.h"

antlrcpp::Any Interpreter::visitExprBoolean(PenParser::ExprBooleanContext *context) {
    if (context->BOOLEAN() -> getText() == "true")
        return std::make_any<bool>(true);
    return std::make_any<bool>(false);
}

antlrcpp::Any Interpreter::visitExprColor(PenParser::ExprColorContext *context) {
    context->type = "Color";
    std::string text = context->COLOR()->getText();
    std::string hr = text.substr(1, 2);
    std::string hg = text.substr(3, 2);
    std::string hb = text.substr(5, 2);

    int r;
    int g;
    int b;

    std::stringstream ssr;
    ssr << std::hex << hr;
    ssr >> r;

    std::stringstream ssg;
    ssg << std::hex << hg;
    ssg >> g;

    std::stringstream ssb;
    ssb << std::hex << hb;
    ssb >> b;

    return std::make_any<GFX::Color>(GFX::Color(r, g, b));
}

antlrcpp::Any Interpreter::visitExprDirection(PenParser::ExprDirectionContext *context) {
    context->type = "Direction";
    if(context->DIRECTION()->getText() == "UP" || context->DIRECTION()->getText() == "NORTH"){
        return std::make_any<GFX::Direction>(GFX::Direction::UP);
    }

    else if(context->DIRECTION()->getText() == "DOWN" || context->DIRECTION()->getText() == "SOUTH"){
        return std::make_any<GFX::Direction>(GFX::Direction::DOWN);
    }

    else if(context->DIRECTION()->getText() == "RIGHT" || context->DIRECTION()->getText() == "EAST"){
        return std::make_any<GFX::Direction>(GFX::Direction::RIGHT);
    }

    else if(context->DIRECTION()->getText() == "LEFT" || context->DIRECTION()->getText() == "WEST"){
        return std::make_any<GFX::Direction>(GFX::Direction::LEFT);
    }
}

antlrcpp::Any Interpreter::visitExprStrings(PenParser::ExprStringsContext *context) {
    context->type = "String";
    std::string res = context ->STRING() -> getText();
    return std::make_any<std::string>(res);
}

antlrcpp::Any Interpreter::visitPen(PenParser::PenContext *context) {
    if (context->assignProperty() == nullptr) {
        return std::make_any<std::pair<std::string, GFX::Pen*>>(std::pair<std::string, GFX::Pen*> (context->ID()->getText(), new GFX::Pen(GFX::PenArgs {})));
    }
    return std::make_any<std::pair<std::string, GFX::Pen*>>(std::pair<std::string, GFX::Pen*> (context->ID()->getText(), new GFX::Pen(std::any_cast<GFX::PenArgs>(visit(context->assignProperty())))));
}

antlrcpp::Any Interpreter::visitStat(PenParser::StatContext *context) {
    return visit(context -> pen());
}

antlrcpp::Any Interpreter::visitSetThickness(PenParser::SetThicknessContext *context) {
    antlrcpp::Any res = visit(context->expr());
    if (strcmp(context->expr()->type.c_str(), "Double") == 0) {
        return std::make_any<Inter::Arg>(Inter::Arg{.t = Inter::Type::THICKNESS, .value = std::make_any<int>((int) std::any_cast<double>(res))});
    } else if (strcmp(context->expr()->type.c_str(), "Integer") == 0) {
        return std::make_any<Inter::Arg>(Inter::Arg{.t = Inter::Type::THICKNESS, .value = res});
    }
    return nullptr;
}

antlrcpp::Any Interpreter::visitAssignProperty(PenParser::AssignPropertyContext *context) {
    if (context == nullptr)
        return nullptr;

    auto args = GFX::PenArgs {};
    for (PenParser::PropertyContext* ctx : context->property()) {
        auto arg = std::any_cast<Inter::Arg>(visit(ctx));
        if (arg.t == Inter::Type::COLOR) {
            args.color = std::any_cast<GFX::Color>(arg.value);
        } else if (arg.t == Inter::Type::THICKNESS) {
            args.thickness = std::any_cast<int>(arg.value);
        } else if (arg.t == Inter::Type::DIRECTION) {
            args.angle = (double) std::any_cast<GFX::Direction>(arg.value);
        }
    }
    return std::make_any<GFX::PenArgs>(args);
}

antlrcpp::Any Interpreter::visitSetDirection(PenParser::SetDirectionContext *context) {
    antlrcpp::Any res = visit(context->expr());
    if (strcmp(context->expr()->type.c_str(), "Direction") == 0)
        return std::make_any<Inter::Arg>(Inter::Arg {.t = Inter::Type::DIRECTION, .value = visit(context->expr())});
    return nullptr;
}

antlrcpp::Any Interpreter::visitSetColor(PenParser::SetColorContext *context){
    antlrcpp::Any res = visit(context->expr());
    if (strcmp(context->expr()->type.c_str(), "Color") == 0)
        return std::make_any<Inter::Arg>(Inter::Arg {.t = Inter::Type::COLOR, .value = res});
    return nullptr;
}

antlrcpp::Any Interpreter::visitProgram(PenParser::ProgramContext *context){
    std::map<std::string, GFX::Pen*> pens {};

    for (PenParser::StatContext* ctx : context->stat()) {
        antlrcpp::Any v = visit(ctx);
        auto res = std::any_cast<std::pair<std::string, GFX::Pen*>>(v);
        pens[res.first] = res.second;
    }

    return std::make_any<std::map<std::string, GFX::Pen*>>(pens);
}

antlrcpp::Any Interpreter::visitExprDouble(PenParser::ExprDoubleContext *context){
    std::string d;
    d = context -> DOUBLE() -> getText();
    context->type = "Double";
    return std::make_any<double>(atof(d.c_str()));
}

antlrcpp::Any Interpreter::visitExprInt(PenParser::ExprIntContext *context) {
    std::string i;
    i = context -> INT() -> getText();
    context->type = "Integer";
    return std::make_any<int>(atoi(i.c_str()));
}

antlrcpp::Any Interpreter::visitExprMultDiv(PenParser::ExprMultDivContext *context){
    std::any res = nullptr;
    std::any e1 = visit(context->e1);
    std::any e2 = visit(context->e2);

    double e1Val;
    double e2Val;

    if (strcmp(context->e1->type.c_str(), "Integer") == 0) {
        e1Val = std::any_cast<int>(e1);
    } else {
        e1Val = std::any_cast<double>(e1);
    }

    if (strcmp(context->e2->type.c_str(), "Integer") == 0) {
        e2Val = std::any_cast<int>(e2);
    } else {
        e2Val = std::any_cast<double>(e2);
    }

    if(e1.has_value() && e2.has_value()){
        switch(context->op->getText().c_str()[0]){
            case '*':
                res = e1Val * e2Val;
                break;
            case '/':
                res = e1Val / e2Val;
                break;
        }
    }

    context->type = "Double";
    return res;
}

antlrcpp::Any Interpreter::visitExprSumSub(PenParser::ExprSumSubContext *context) {
    antlrcpp::Any res = nullptr;
    antlrcpp::Any e1 = visit(context->e1);
    antlrcpp::Any e2 = visit(context->e2);

    double e1Val;
    double e2Val;

    if (strcmp(context->e1->type.c_str(), "Integer") == 0) {
        e1Val = std::any_cast<int>(e1);
    } else {
        e1Val = std::any_cast<double>(e1);
    }

    if (strcmp(context->e2->type.c_str(), "Integer") == 0) {
        e2Val = std::any_cast<int>(e2);
    } else {
        e2Val = std::any_cast<double>(e2);
    }

    if(e1.has_value() && e2.has_value()){
        switch(context->op->getText().c_str()[0]){
            case '+':
                res = e1Val + e2Val;
                break;
            case '-':
                res = e1Val - e2Val;
                break;
        }
    }

    context->type = "Double";
    return res;
}

antlrcpp::Any Interpreter::visitExprUnary(PenParser::ExprUnaryContext *context){
    std::any res = nullptr;
    std::any e2 = visit(context->e2);

    double e2Val;

    if (strcmp(context->e2->type.c_str(), "Integer") == 0) {
        e2Val = std::any_cast<int>(e2);
    } else {
        e2Val = std::any_cast<double>(e2);
    }

    if(e2.has_value()){
        switch(context->op->getText().c_str()[0]){
            case '+':
                res = +e2Val;
            case '-':
                res = -e2Val;
        }
    }

    return res;
}

antlrcpp::Any Interpreter::visitExprParentesis(PenParser::ExprParentesisContext *context) {
    context->type = context->expr()->type;
    return visit(context->expr());
}
