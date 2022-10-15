import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.*;

import java.awt.Color;

public class Compiler extends PdrawGrammarBaseVisitor<ST> {

   @Override public ST visitProgram(PdrawGrammarParser.ProgramContext ctx) {
      ST res = templates.getInstanceOf("main");
      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         res.add("stat", visit(itr.next()).render());
      }
      return res;
   }

   @Override public ST visitGroup(PdrawGrammarParser.GroupContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Type t = ctx.type().vartype;
      List<Symbol> symbols =  new ArrayList<>();
      Iterator<PdrawGrammarParser.ExprContext> itr = ctx.expr().iterator();
      while (itr.hasNext()) {
         PdrawGrammarParser.ExprContext expr = itr.next();
         stats.add("stat", visit(expr));
         symbols.add(expr.symbol);
      }

      ctx.gt = new GroupType(t, symbols);

      return stats;
   }

   @Override public ST visitIfBody(PdrawGrammarParser.IfBodyContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         stats.add("stat", visit(itr.next()));
      }

      return stats;
   }

   @Override public ST visitElseBody(PdrawGrammarParser.ElseBodyContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         stats.add("stat", visit(itr.next()));
      }

      return stats;
   }

   @Override public ST visitIfelse(PdrawGrammarParser.IfelseContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("ifelse");

      stats.add("stat", visit(ctx.expr()).render());

      res.add("cond", ctx.expr().symbol.getName());
      res.add("stat1", visit(ctx.ifBody()).render());
      if (ctx.elseBody() != null){
         res.add("stat2", visit(ctx.elseBody()).render());
      }

      stats.add("stat", res.render());
      return stats;
   }

   @Override public ST visitStat(PdrawGrammarParser.StatContext ctx) {
      ST res = templates.getInstanceOf("stats");
      res.add("stat", visit(ctx.getChild(0)).render());
      return res;
   }

   @Override public ST visitImpt(PdrawGrammarParser.ImptContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("importStat");

      String importVar = newVar();
      String penVar = newVar();
      Symbol s = new Symbol(penVar, new PenType());
      table.addSymbol(ctx.ID().getText(), s);

      res.add("name", ctx.STRING().getText());
      res.add("var1", importVar);
      res.add("var2", penVar);
      res.add("penname", ctx.ID().getText());

      stats.add("stat", res.render());
      return stats;
   }

   @Override public ST visitAssignWType(PdrawGrammarParser.AssignWTypeContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      String v = newVar();
      Symbol s = new Symbol(v, ctx.type().vartype);
      table.addSymbol(ctx.ID().getText(), s);

      PdrawGrammarParser.ExprContext expr = ctx.expr();
      stats.add("stat", visit(expr));

      String type = getNativeType(expr.symbol.getType().getName());
      if (type == null) {
         GroupType t = (GroupType) expr.symbol.getType();
         type = String.format("std::vector<%s>",   getNativeType(t.getSubType().getName()));
      }
      
      res.add("type", type);
      res.add("var", v);
      res.add("value", expr.symbol.getName());

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitAssignReassign(PdrawGrammarParser.AssignReassignContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("reassignment");

      stats.add("stat", visit(ctx.expr()));
      
      Symbol s = table.getSymbol(ctx.ID().getText());
      res.add("var", s.getName());
      res.add("value", ctx.expr().symbol.getName());

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitAssignPen(PdrawGrammarParser.AssignPenContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("reassignPen");

      PdrawGrammarParser.AssignPropertyContext ap = ctx.assignProperty();
      stats.add("stat", visit(ap));

      Symbol v = table.getSymbol(ctx.ID().getText());
      res.add("var", v.getName());
      res.add("arglist", ap.properties.getAll());

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitAssignProp(PdrawGrammarParser.AssignPropContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("assignemnt");

      Symbol s = table.getSymbol(ctx.ID(0).getText());

      res.add("var", s.getName());
      res.add("sub", ctx.ID(1).getText());
      res.add("value", ctx.expr().symbol.getName());

      stats.add("stat", visit(ctx.expr()).render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitSetProperties(PdrawGrammarParser.SetPropertiesContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Iterator<PdrawGrammarParser.PropertyContext> itr = ctx.property().iterator();
      ctx.properties = new PropertyTable();
      while (itr.hasNext()) {
         PdrawGrammarParser.PropertyContext next = itr.next();
         stats.add("stat", visit(next).render());
         ctx.properties.put(next.symbol.getValue(), next.symbol.internalName());
      }

      return stats;
   }



   @Override public ST visitExprOrAnd(PdrawGrammarParser.ExprOrAndContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("binaryOperation");

      ST exp1 = visit(ctx.e1);
      ST exp2 = visit(ctx.e2);
      Symbol v1 = ctx.e1.symbol;
      Symbol v2 = ctx.e2.symbol;

      String varname = newVar();

      res.add("type", "bool");
      res.add("var", varname);
      res.add("e1", v1.getName());
      res.add("op", ctx.op.getText());
      res.add("e2", v2.getName());

      ctx.symbol = new Symbol(varname, new BooleanType());

      stats.add("stat", exp1.render());
      stats.add("stat", exp2.render());
      stats.add("stat", res.render());

      return stats;
   }   @Override public ST visitExprGroup(PdrawGrammarParser.ExprGroupContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("defineGroup");

      stats.add("stat", visit(ctx.group()).render());
      String newVarName = newVar();

      res.add("type", getNativeType(ctx.group().gt.getSubType().getName()));
      res.add("varname", newVarName);
      res.add("expr", ctx.group().gt.getSymbols().stream().map(Symbol::getName).collect(Collectors.toList()));
      ctx.symbol = new Symbol(newVarName, ctx.group().gt);

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprVar(PdrawGrammarParser.ExprVarContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Symbol s = table.getSymbol(ctx.ID().getText());
      ctx.symbol = new Symbol(s.getName(), s.getType());
      return stats;
   }

   @Override public ST visitExprParentesis(PdrawGrammarParser.ExprParentesisContext ctx) {
      ST res = visit(ctx.expr());
      ctx.symbol = ctx.expr().symbol;
      return res;
   }

   @Override public ST visitExprPoint(PdrawGrammarParser.ExprPointContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      stats.add("stat", visit(ctx.point()).render());
      String varname = newVar();
      ctx.symbol = new Symbol(varname, new PointType());
      ctx.symbol.setValue(ctx.point().symbol.getName());

      res.add("type", "PMath::Point");
      res.add("var", varname);
      res.add("value", ctx.point().symbol.getName());
      
      stats.add("stat", res.render());
      return stats;
   }

   @Override public ST visitExprDirection(PdrawGrammarParser.ExprDirectionContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      String dir = ctx.DIRECTION().getText();
      Double direction = 0.0;
      switch (dir) {
         case "UP":
         case "NORTH":
            direction = 270.0;
            break;

         case "RIGHT":
         case "WEST":
            direction = 0.0;
            break;

         case "DOWN":
         case "SOUTH":
            direction = 90.0;
            break;

         case "LEFT":
         case "EAST":
            direction = 180.0;
            break;
      }

      String varname = newVar();
      res.add("type", "double");
      res.add("var", varname);
      res.add("value", direction);
      ctx.symbol = new Symbol(varname, new DoubleType());
      ctx.symbol.setValue(direction.toString());

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprSumSub(PdrawGrammarParser.ExprSumSubContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("binaryOperation");

      ST e1 = visit(ctx.e1);
      Symbol v1 = ctx.e1.symbol;

      ST e2 = visit(ctx.e2);
      Symbol v2 = ctx.e2.symbol;

      String varname = newVar();

      res.add("type", getNativeType(v1.getType().getName()));
      res.add("var", varname);
      res.add("e1", v1.getName());
      res.add("op", ctx.op.getText());
      res.add("e2", v2.getName());

      ctx.symbol = new Symbol(varname, v1.getType());

      stats.add("stat", e1.render());
      stats.add("stat", e2.render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprUnary(PdrawGrammarParser.ExprUnaryContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("binaryOperation");

      ST exp = visit(ctx.e2);
      Symbol v = ctx.e2.symbol;

      String varname = newVar();

      res.add("type", getNativeType(v.getType().getName()));
      res.add("var", varname);
      res.add("e1", "0");
      res.add("op", ctx.op.getText());
      res.add("e2", v.getName());

      ctx.symbol = new Symbol(varname, v.getType());

      stats.add("stat", exp.render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprBoolOperation(PdrawGrammarParser.ExprBoolOperationContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("binaryOperation");

      ST exp1 = visit(ctx.e1);
      ST exp2 = visit(ctx.e2);
      Symbol v1 = ctx.e1.symbol;
      Symbol v2 = ctx.e2.symbol;

      String varname = newVar();

      res.add("type", "bool");
      res.add("var", varname);
      res.add("e1", v1.getName());
      res.add("op", ctx.op.getText());
      res.add("e2", v2.getName());

      ctx.symbol = new Symbol(varname, new BooleanType());

      stats.add("stat", exp1.render());
      stats.add("stat", exp2.render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprBoolean(PdrawGrammarParser.ExprBooleanContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      String text = ctx.BOOLEAN().getText();
      Boolean value = true;
      switch (text) {
         case "true":
            value = true;
            break;

         case "false":
            value = false;
            break;
      }
      ctx.symbol = new Symbol(new BooleanType());
      ctx.symbol.setValue(value.toString());

      return stats;
   }

   @Override public ST visitExprMultDiv(PdrawGrammarParser.ExprMultDivContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("binaryOperation");

      ST e1 = visit(ctx.e1);
      Symbol v1 = ctx.e1.symbol;

      ST e2 = visit(ctx.e2);
      Symbol v2 = ctx.e2.symbol;

      String varname = newVar();

      res.add("type", getNativeType(v1.getType().getName()));
      res.add("var", varname);
      res.add("e1", v1.getName());
      res.add("op", ctx.op.getText());
      res.add("e2", v2.getName());

      ctx.symbol = new Symbol(varname, v1.getType());

      stats.add("stat", e1.render());
      stats.add("stat", e2.render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitExprString(PdrawGrammarParser.ExprStringContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      ctx.symbol = new Symbol(new StringType());
      ctx.symbol.setValue(ctx.STRING().getText());

      return stats;
   }

   @Override public ST visitExprInt(PdrawGrammarParser.ExprIntContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      ctx.symbol = new Symbol(new IntegerType());
      ctx.symbol.setValue(ctx.INT().getText());

      return stats;
   }

   @Override public ST visitExprDouble(PdrawGrammarParser.ExprDoubleContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      ctx.symbol = new Symbol(new DoubleType());
      ctx.symbol.setValue(ctx.DOUBLE().getText());

      return stats;
   }

   @Override public ST visitExprColor(PdrawGrammarParser.ExprColorContext ctx) {
      ST res = templates.getInstanceOf("colorDecl");

      String hexColor = ctx.COLOR().getText();
      Color color = Color.decode(hexColor);

      res.add("red", color.getRed());
      res.add("green", color.getGreen());
      res.add("blue", color.getBlue());

      ctx.symbol = new Symbol(new ColorType());
      ctx.symbol.setValue(res.render());

      return res;
   }

   @Override public ST visitExprMove(PdrawGrammarParser.ExprMoveContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr(0)).render());
      stats.add("stat", visit(ctx.expr(1)).render());
      
      if (ctx.expr(0).symbol.getType().getName() == "group") {
         ST res = templates.getInstanceOf("batchMethods");
         res.add("groupvar", ctx.expr(0).symbol.getName());
         res.add("method", "moveForward");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      } else {
         ST res = templates.getInstanceOf("method");
         res.add("var", ctx.expr(0).symbol.getName());
         res.add("method", "moveForward");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      }

      ctx.symbol = ctx.expr(0).symbol;

      return stats;
   }
	
	@Override public ST visitExprMoveTo(PdrawGrammarParser.ExprMoveToContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr(0)).render());
      stats.add("stat", visit(ctx.expr(1)).render());
      if (ctx.expr(0).symbol.getType().getName() == "group") {
         ST res = templates.getInstanceOf("batchMethods");
         res.add("groupvar", ctx.expr(0).symbol.getName());
         res.add("method", "setPosition");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      } else {
         ST res = templates.getInstanceOf("method");
         res.add("var", ctx.expr(0).symbol.getName());
         res.add("method", "setPosition");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      }

      ctx.symbol = ctx.expr(0).symbol;
      return stats;
   }

   @Override public ST visitExprMoveRotated(PdrawGrammarParser.ExprMoveRotatedContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr(0)).render());
      stats.add("stat", visit(ctx.expr(1)).render());
      stats.add("stat", visit(ctx.expr(2)).render());

      if (ctx.expr(0).symbol.getType().getName() == "group") {
         ST res1 = templates.getInstanceOf("batchMethods");
         res1.add("groupvar", ctx.expr(0).symbol.getName());
         res1.add("method", "setAngle");
         res1.add("args", ctx.expr(1).symbol.getName());
         
         ST res2 = templates.getInstanceOf("batchMethods");
         res2.add("groupvar", ctx.expr(0).symbol.getName());
         res2.add("method", "moveForward");
         res2.add("args", ctx.expr(2).symbol.getName());
         
         stats.add("stat", res1.render());
         stats.add("stat", res2.render());
      } else {
         ST res1 = templates.getInstanceOf("method");
         res1.add("var", ctx.expr(0).symbol.getName());
         res1.add("method", "setAngle");
         res1.add("args", ctx.expr(1).symbol.getName());
         
         ST res2 = templates.getInstanceOf("method");
         res2.add("var", ctx.expr(0).symbol.getName());
         res2.add("method", "moveForward");
         res2.add("args", ctx.expr(2).symbol.getName());
         
         stats.add("stat", res1.render());
         stats.add("stat", res2.render());
      }

      ctx.symbol = ctx.expr(0).symbol;
      return stats;
   }
	
	@Override public ST visitExprRotate(PdrawGrammarParser.ExprRotateContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr(0)).render());
      stats.add("stat", visit(ctx.expr(1)).render());

      if (ctx.expr(0).symbol.getType().getName() == "group") {
         ST res = templates.getInstanceOf("batchMethods");
         res.add("groupvar", ctx.expr(0).symbol.getName());
         res.add("method", "setAngle");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      } else {
         ST res = templates.getInstanceOf("method");
         res.add("var", ctx.expr(0).symbol.getName());
         res.add("method", "setAngle");
         res.add("args", ctx.expr(1).symbol.getName());
         stats.add("stat", res.render());
      }
      
      ctx.symbol = ctx.expr(0).symbol;

      return stats;
   }

   @Override public ST visitExprInput(PdrawGrammarParser.ExprInputContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("readInput");

      String varname = newVar();
      res.add("type", getNativeType(ctx.input().type().getText()));
      res.add("varname", varname);

      ctx.symbol = new Symbol(varname, ctx.input().type().vartype);
      stats.add("stat", res.render());
      return stats;
   }

   @Override public ST visitOutput(PdrawGrammarParser.OutputContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("print");

      stats.add("stat", visit(ctx.expr()).render());
      res.add("varname", ctx.expr().symbol.getName());

      stats.add("stat", res.render());
      return stats;
   }

   @Override public ST visitRepeatBody(PdrawGrammarParser.RepeatBodyContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         stats.add("stat", visit(itr.next()));
      }

      return stats;
   }

   @Override public ST visitRepeatNtimesBody(PdrawGrammarParser.RepeatNtimesBodyContext ctx) {
      ST stats = templates.getInstanceOf("stats");

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         stats.add("stat", visit(itr.next()));
      }

      return stats;
   }

   @Override public ST visitRepeat(PdrawGrammarParser.RepeatContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("repeatUntil");

      stats.add("stat", visit(ctx.expr()).render());

      res.add("cond", ctx.expr().symbol.getName());
      res.add("stat", visit(ctx.repeatBody()).render());
      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitRepeatNtimes(PdrawGrammarParser.RepeatNtimesContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("repeatNTimes");

      stats.add("stat", visit(ctx.expr()).render());

      res.add("n", ctx.expr().symbol.getName());
      res.add("stat", visit(ctx.repeatNtimesBody()).render());
      stats.add("stat", res.render());

      return stats;
   }
	
	@Override public ST visitExprUp(PdrawGrammarParser.ExprUpContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr()).render());
      if (ctx.expr().symbol.getType().getName() == "group") {
         ST res = templates.getInstanceOf("batchMethods");
         res.add("groupvar", ctx.expr().symbol.getName());
         res.add("method", "up");
         stats.add("stat", res.render());
      } else {
         ST res = templates.getInstanceOf("method");
         res.add("var", ctx.expr().symbol.getName());
         res.add("method", "up");
         stats.add("stat", res.render());
      }

      ctx.symbol = ctx.expr().symbol;

      return stats;
   }
	
	@Override public ST visitExprDown(PdrawGrammarParser.ExprDownContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      stats.add("stat", visit(ctx.expr()).render());
      if (ctx.expr().symbol.getType().getName() == "group") {
         ST res = templates.getInstanceOf("batchMethods");
         res.add("groupvar", ctx.expr().symbol.getName());
         res.add("method", "down");
         stats.add("stat", res.render());
      } else {
         ST res = templates.getInstanceOf("method");
         res.add("var", ctx.expr().symbol.getName());
         res.add("method", "down");
         stats.add("stat", res.render());
      }

      ctx.symbol = ctx.expr().symbol;

      return stats;
   }

   @Override public ST visitSetColor(PdrawGrammarParser.SetColorContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      visit(ctx.expr());

      String varname = newVar();
      res.add("type", "GFX::Color");
      res.add("var", varname);
      res.add("value", ctx.expr().symbol.getName());

      stats.add("stat", res.render());

      ctx.symbol = new Symbol(varname, new ColorType());
      ctx.symbol.setValue("color");

      return stats;
   }

   @Override public ST visitSetThickness(PdrawGrammarParser.SetThicknessContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      PdrawGrammarParser.ExprContext expr = ctx.expr();
      stats.add("stat", visit(expr).render());

      String varname = newVar();
      res.add("type", "int");
      res.add("var", varname);
      res.add("value", expr.symbol.getName());
      stats.add("stat", res.render());

      ctx.symbol = new Symbol(varname, new IntegerType());
      ctx.symbol.setValue("thickness");

      return stats;
   }

   @Override public ST visitSetName(PdrawGrammarParser.SetNameContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      PdrawGrammarParser.ExprContext expr = ctx.expr();
      stats.add("stat", visit(expr).render());

      String varname = newVar();
      res.add("type", "std::string");
      res.add("var", varname);
      res.add("value", expr.symbol.getName());
      stats.add("stat", res.render());

      ctx.symbol = new Symbol(varname, new StringType());
      ctx.symbol.setValue("thickness");

      return stats;
   }

   @Override public ST visitSetPosition(PdrawGrammarParser.SetPositionContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      PdrawGrammarParser.ExprContext expr = ctx.expr();
      stats.add("stat", visit(expr).render());

      String varname = newVar();
      res.add("type", "PMath::Point");
      res.add("var", varname);
      res.add("value", expr.symbol.getName());
      stats.add("stat", res.render());

      ctx.symbol = new Symbol(varname, new PointType());
      ctx.symbol.setValue("thickness");

      return stats;
   }

   @Override public ST visitSetDirection(PdrawGrammarParser.SetDirectionContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("decl");

      PdrawGrammarParser.ExprContext expr = ctx.expr();

      String varname = newVar();
      res.add("type", "double");
      res.add("var", varname);
      res.add("value", expr.symbol.getName());

      stats.add("stat", visit(expr).render());
      stats.add("stat", res.render());

      ctx.symbol = new Symbol(varname, new DoubleType());
      ctx.symbol.setValue("direction");

      return stats;
   }

   @Override public ST visitPen(PdrawGrammarParser.PenContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("newPen");

      stats.add("stat", visit(ctx.assignProperty()));

      String v = newVar();
      Symbol s = new Symbol(v, new PenType());
      table.addSymbol(ctx.ID().getText(), s);

      res.add("varname", v);
      res.add("arglist", ctx.assignProperty().properties.getAll());

      stats.add("stat", res.render());

      return stats;
   }

   @Override public ST visitPoint(PdrawGrammarParser.PointContext ctx) {
      ST stats = templates.getInstanceOf("stats");
      ST res = templates.getInstanceOf("newPoint");
      
      PdrawGrammarParser.ExprContext x = ctx.x;
      PdrawGrammarParser.ExprContext y = ctx.y;

      String varname = newVar();

      stats.add("stat", visit(x).render());
      stats.add("stat", visit(y).render());

      res.add("var", varname);
      res.add("x", x.symbol.getName());
      res.add("y", y.symbol.getName());

      ctx.symbol = new Symbol(varname, new PointType());
      stats.add("stat", res.render());

      return stats;
   }

   private String newVar() {
      numVars++;
      return "var" + numVars;
   }

   private String getNativeType(String t) {
      if (t.toLowerCase().equals("string")) {
         return "std::string";
      } else if (t.toLowerCase().equals("int")) {
         return "int";
      } else if (t.toLowerCase().equals("double")) {
         return "double";
      } else if (t.toLowerCase().equals("boolean")) {
         return "bool";
      } else if (t.toLowerCase().equals("pen")) {
         return "GFX::Pen*";
      }
      return null;
   }

   private int numVars=0;
   private STGroup templates = new STGroupFile("cpp.stg");
   private SymbolTable table = new SymbolTable();
}
