import java.util.Collections;
import java.util.Iterator;

public class Analise extends PdrawGrammarBaseVisitor<Boolean> {

   public SymbolTable table = new SymbolTable();

   @Override
   public Boolean visitProgram(PdrawGrammarParser.ProgramContext ctx) {
      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         if (!visit(itr.next()))
            return false;
      }
      return true;
   }

   @Override
   public Boolean visitStat(PdrawGrammarParser.StatContext ctx) {
      Boolean res = visit(ctx.getChild(0));
      return res;
   }

   @Override
   public Boolean visitImpt(PdrawGrammarParser.ImptContext ctx) {
      Boolean res = true;
      String id = ctx.ID().getText();

      if (table.exists(id)) {
         ErrorHandling.printError(ctx,
               String.format("%s is already defined as Type ", id, table.getSymbol(id).getType().getName()));
         res = false;
      }

      table.addSymbol(id, new Symbol(id, new PenType()));

      return res;
   }

   @Override
   public Boolean visitAssignWType(PdrawGrammarParser.AssignWTypeContext ctx) {

      String s = ctx.ID().getText();
      //Boolean res = table.exists(s);
      Boolean res = true;

      res = res && visit(ctx.type()) && visit(ctx.expr());

      if (res && !table.exists(ctx.ID().getText())) {
         if (!(ctx.expr().symbol.getType().getName().equals(ctx.type().getText().toLowerCase()))) {

            ErrorHandling.printError(ctx,
                  String.format("%s does not match type %s", ctx.expr().getText(), ctx.type().getText()));
            res = false;

         } else {

            switch (ctx.type().getText()) {

               case "Int":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "String":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "Boolean":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "Double":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "Direction":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "Color":
                  table.addSymbol(s, new Symbol(s, ctx.type().vartype));
                  break;
               case "Group":
                  if (ctx.expr().symbol.getType().getName() != "group") {
                     ErrorHandling.printError(ctx, String.format("Expression is not a group"));
                     res = false;
                  }
                  table.addSymbol(s, new Symbol(s, ctx.expr().symbol.getType()));
                  break;
               default:
                  break;

            }
         }
      } else {

         ErrorHandling.printError(ctx,
               String.format("%s is already defined as ", ctx.ID().getText(), table.getSymbol(s).getType().getName()));
         res = false;

      }

      return res;
   }

   @Override
   public Boolean visitAssignPen(PdrawGrammarParser.AssignPenContext ctx) {

      String s = ctx.ID().getText();
      Boolean res = table.exists(s);

      if (res) {

         if (!(table.getSymbol(s).getType().getName() == "pen")) {
            ErrorHandling.printError(ctx, String.format("%s is not a defined Pen", ctx.ID().getText()));
            res = false;
         } else {
            res = visit(ctx.assignProperty());
         }

      } else {

         ErrorHandling.printError(ctx, String.format("%s is not defined", ctx.ID().getText()));
         res = false;

      }

      return res;
   }


   @Override
   public Boolean visitAssignReassign(PdrawGrammarParser.AssignReassignContext ctx) {
      String s = ctx.ID().getText();
      Boolean res = true;
      res = res && table.exists(s) && visit(ctx.expr());

      if (res) {
         if (!table.getSymbol(s).getType().getName().equals(ctx.expr().symbol.getType().getName())) {
            ErrorHandling.printError(ctx,
                  String.format("%s is not a valid value Type for variable %s", ctx.expr().getText(), s));
            res = false;

         } else if (table.getSymbol(s).getType().getName().equals("Pen")) {
            ErrorHandling.printError(ctx, String.format("Can only assign properties to a Pen"));
            res = false;
         }

      } else {

         ErrorHandling.printError(ctx, String.format("%s is not defined", ctx.ID().getText()));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitAssignProp(PdrawGrammarParser.AssignPropContext ctx) {
      Boolean res = visit(ctx.expr());
      return res;
   }

   @Override
   public Boolean visitSetProperties(PdrawGrammarParser.SetPropertiesContext ctx) {
      for (PdrawGrammarParser.PropertyContext pctx : ctx.property()) {
         if (!visit(pctx))
            return false;
      }
      return true;
   }

   @Override
   public Boolean visitType(PdrawGrammarParser.TypeContext ctx) {
      return true;
   }

   @Override
   public Boolean visitExprVar(PdrawGrammarParser.ExprVarContext ctx) {
      Boolean res = true;

      if (table.exists(ctx.ID().getText())) {
         ctx.symbol = table.getSymbol(ctx.getText());
      } else {
         ErrorHandling.printError(ctx, String.format("Symbol %s not found", ctx.ID().getText()));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitExprParentesis(PdrawGrammarParser.ExprParentesisContext ctx) {
      Boolean res = visit(ctx.expr());
      ctx.symbol = ctx.expr().symbol;
      return res;

   }

   @Override
   public Boolean visitExprDirection(PdrawGrammarParser.ExprDirectionContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new DirectionType());
      return res;
   }

   @Override
   public Boolean visitExprSumSub(PdrawGrammarParser.ExprSumSubContext ctx) {
      Boolean res = true;
      Boolean resE1 = visit(ctx.e1);
      Boolean resE2 = visit(ctx.e2);

      res = res && resE1;
      res = res && resE2;

      String e1Text = ctx.e1.getText();
      String e2Text = ctx.e2.getText();

      if (res) {
         Type e1 = ctx.e1.symbol.getType();
         Type e2 = ctx.e2.symbol.getType();

         if (ctx.e1.symbol.getType().getName() == "id") {

            if (!table.getSymbol(e1Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e1Text));
               res = false;

            }

         } else {
            if (!e1.isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e1Text));
               res = false;
            }
         }

         if (ctx.e2.symbol.getType().getName() == "id") {

            if (!table.getSymbol(e2Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e2Text));
               res = false;

            }
         } else {
            if (!e2.isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e2Text));
               res = false;
            }
         }

         if (e1.getName() == "double") {
            ctx.symbol = new Symbol(new DoubleType());
         } else {
            ctx.symbol = new Symbol(new IntegerType());
         }

      } else {

         if (!resE1) {
            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e1Text));
            res = false;
         }
         if (!resE2) {
            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e2Text));
            res = false;
         }

      }

      return res;
   }

   @Override
   public Boolean visitExprUnary(PdrawGrammarParser.ExprUnaryContext ctx) {
      Boolean res = visit(ctx.e2);
      ctx.symbol = ctx.e2.symbol;
      return res;
   }

   @Override
   public Boolean visitExprBoolean(PdrawGrammarParser.ExprBooleanContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new BooleanType());
      return res;
   }

   @Override
   public Boolean visitExprInt(PdrawGrammarParser.ExprIntContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new IntegerType());
      return res;
   }

   @Override
   public Boolean visitExprInput(PdrawGrammarParser.ExprInputContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.input());


      if(res) {

         switch (ctx.input().type().getText()) {

            case "Int":
               ctx.symbol = new Symbol(new IntegerType());
               break;
            case "String":
               ctx.symbol = new Symbol(new StringType());
               break;
            case "Boolean":
               ctx.symbol = new Symbol(new DoubleType());
               break;
            case "Double":
               ctx.symbol = new Symbol(new DoubleType());
               break;
            default:
               break;
         }
         
      }
      return res;
   }

   @Override
   public Boolean visitExprDouble(PdrawGrammarParser.ExprDoubleContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new DoubleType());
      return res;
   }

   @Override
   public Boolean visitExprGroup(PdrawGrammarParser.ExprGroupContext ctx) {
      Boolean res = visit(ctx.group());
      ctx.symbol = new Symbol(ctx.group().gt);
      return res;
   }

   @Override
   public Boolean visitExprPoint(PdrawGrammarParser.ExprPointContext ctx) {
      Boolean res = visit(ctx.point());
      ctx.symbol = ctx.point().symbol;
      return res;
   }

   @Override
   public Boolean visitExprMultDiv(PdrawGrammarParser.ExprMultDivContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.e1) && visit(ctx.e2);

      Type e1 = ctx.e1.symbol.getType();
      Type e2 = ctx.e2.symbol.getType();
      String e1Text = ctx.e1.getText();
      String e2Text = ctx.e2.getText();

      if (ctx.e1.symbol.getType().getName() == "id") {

         if (table.exists(e1Text)) {

            if (!table.getSymbol(e1Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e1Text));
               res = false;

            }

         } else {

            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e1Text));
            res = false;

         }

      } else {
         if (!e1.isNumeric()) {

            ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e1Text));
            res = false;
         }
      }

      if (ctx.e2.symbol.getType().getName() == "id") {

         if (table.exists(e2Text)) {

            if (!table.getSymbol(e2Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e2Text));
               res = false;
            }
         } else {

            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e2Text));
            res = false;

         }

      } else {
         if (!e2.isNumeric()) {
            ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e2Text));
            res = false;
         }
      }

      ctx.symbol = new Symbol(ctx.e1.symbol.getType());
      return res;

   }

   @Override
   public Boolean visitExprString(PdrawGrammarParser.ExprStringContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new StringType());
      return res;
   }

   @Override
   public Boolean visitExprOrAnd(PdrawGrammarParser.ExprOrAndContext ctx) {
      Boolean res = true;
      
      res = res && visit(ctx.e1) && visit(ctx.e2);
      String e1Text = ctx.e1.getText();
      String e2Text = ctx.e2.getText();

      if (res) {
         Type e1 = ctx.e1.symbol.getType();
         Type e2 = ctx.e2.symbol.getType();

         if (ctx.e1.symbol.getType().getName() == "id") {

            if (table.exists(e1Text)) {

               if (!table.getSymbol(e1Text).getType().getName().toLowerCase().equals("boolean")) {

                  ErrorHandling.printError(ctx, String.format("%s is not a boolean variable", e1Text));
                  res = false;

               }

            } else {

               ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e1Text));
               res = false;

            }

         } else {
            if (!e1.getName().toLowerCase().equals("boolean")) {

               ErrorHandling.printError(ctx, String.format("%s is not a Boolean value", e1Text));
               res = false;
            }
         }

         if (ctx.e2.symbol.getType().getName() == "id") {

            if (table.exists(e2Text)) {

               if (!table.getSymbol(e2Text).getType().getName().toLowerCase().equals("boolean")) {

                  ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e2Text));
                  res = false;
               }
            } else {

               ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e2Text));
               res = false;

            }

         } else {
            if (!e2.getName().toLowerCase().equals("boolean")) {
               ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e2Text));
               res = false;
            }
         }

         ctx.symbol = new Symbol(new BooleanType());
      }

      return res;
   }

   @Override
   public Boolean visitExprColor(PdrawGrammarParser.ExprColorContext ctx) {
      Boolean res = true;
      ctx.symbol = new Symbol(new ColorType());
      return res;
   }

   @Override
   public Boolean visitExprMove(PdrawGrammarParser.ExprMoveContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr(0)) && visit(ctx.expr(1));
      Symbol s1 = ctx.expr(0).symbol;
      Symbol s2 = ctx.expr(1).symbol;

      if (!s1.getType().getName().equals("pen") && !s1.getType().getName().equals("group")) {
         ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
         res = false;
      }

      if (!s2.getType().getName().equals("int")) {
         ErrorHandling.printError(ctx, String.format("Expression is not an integer"));
         res = false;
      }

      ctx.symbol = ctx.expr(0).symbol;

      return res;
   }

   @Override
   public Boolean visitExprMoveTo(PdrawGrammarParser.ExprMoveToContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr(0)) && visit(ctx.expr(1));

      if (res) {
         Symbol s1 = ctx.expr(0).symbol;
         Symbol s2 = ctx.expr(1).symbol;

         if (!s1.getType().getName().equals("pen") && !s1.getType().getName().equals("group")) {
            ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
            res = false;
         }

         if (!s2.getType().getName().equals("point")) {
            ErrorHandling.printError(ctx, String.format("Expression is not a Point"));
            res = false;
         }

         ctx.symbol = ctx.expr(0).symbol;
      }

      return res;
   }

   @Override
   public Boolean visitExprRotate(PdrawGrammarParser.ExprRotateContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr(0)) && visit(ctx.expr(1));

      if (res) {
         Symbol s1 = ctx.expr(0).symbol;
         Symbol s2 = ctx.expr(1).symbol;

         if (!s1.getType().getName().equals("pen") && !s1.getType().getName().equals("group")) {
            ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
            res = false;
         }

         if (!s2.getType().getName().equals("int") && !s2.getType().getName().equals("double")
               && !s2.getType().getName().equals("direction")) {
            ErrorHandling.printError(ctx, String.format("Expression is not a number"));
            res = false;
         }

         ctx.symbol = ctx.expr(0).symbol;
      }

      return res;
   }

   @Override
   public Boolean visitExprMoveRotated(PdrawGrammarParser.ExprMoveRotatedContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr(0)) && visit(ctx.expr(1)) && visit(ctx.expr(2));
      Symbol s1 = ctx.expr(0).symbol;
      Symbol s2 = ctx.expr(1).symbol;
      Symbol s3 = ctx.expr(2).symbol;

      if (!s1.getType().getName().equals("pen") && !s1.getType().getName().equals("group")) {
         ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
         res = false;
      }

      if (!s2.getType().getName().equals("int")) {
         ErrorHandling.printError(ctx, String.format("Movement size is not an integer"));
         res = false;
      }

      if (!s3.getType().getName().equals("int") && !s3.getType().getName().equals("double")) {
         ErrorHandling.printError(ctx, String.format("Rotation is not a number"));
         res = false;
      }

      ctx.symbol = ctx.expr(0).symbol;

      return res;
   }

   @Override
   public Boolean visitExprUp(PdrawGrammarParser.ExprUpContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr());
      Symbol s = ctx.expr().symbol;

      if (!s.getType().getName().equals("pen") && !s.getType().getName().equals("group")) {
         ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
         res = false;
      }

      ctx.symbol = ctx.expr().symbol;

      return res;
   }

   @Override
   public Boolean visitExprDown(PdrawGrammarParser.ExprDownContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr());
      Symbol s = ctx.expr().symbol;

      if (res){
         if (!s.getType().getName().equals("pen") && !s.getType().getName().equals("group")) {
            ErrorHandling.printError(ctx, String.format("There is not a Pen or a valid group found"));
            res = false;
         }

         ctx.symbol = ctx.expr().symbol;
      }

      return res;
   }

   @Override
   public Boolean visitSetColor(PdrawGrammarParser.SetColorContext ctx) {
      String s = ctx.expr().getText();

      Boolean res = visit(ctx.expr());
      res = res && ctx.expr().symbol.getType().getName() == "GFX::Color";

      if (!res) {
         ErrorHandling.printError(ctx, String.format("%s is not Type Color", s));
         res = false;

      }

      return res;
   }

   @Override
   public Boolean visitSetThickness(PdrawGrammarParser.SetThicknessContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr());
      res = res && ctx.expr().symbol.getType().isNumeric();
      String s = ctx.expr().getText();

      if (!res) {
         ErrorHandling.printError(ctx, String.format("%s is not a valid Type for Thickness", s));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitSetDirection(PdrawGrammarParser.SetDirectionContext ctx) {
      Boolean res = ctx.expr().symbol.getType().getName() == "direction";
      String s = ctx.expr().getText();

      if (!res) {
         ErrorHandling.printError(ctx, String.format("%s is not a valid Type for Direction", s));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitSetName(PdrawGrammarParser.SetNameContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr());
      res = res && (ctx.expr().symbol.getType().getName() == "string");
      String s = ctx.expr().getText();

      if (!res) {
         ErrorHandling.printError(ctx, String.format("%s is not a valid Type for Name", s));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitSetPosition(PdrawGrammarParser.SetPositionContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr());
      res = res && (ctx.expr().symbol.getType().getName() == "point");
      String s = ctx.expr().getText();

      if (!res) {
         ErrorHandling.printError(ctx, String.format("%s is not a valid Type for Position", s));
         res = false;
      }

      return res;
   }

   @Override
   public Boolean visitPen(PdrawGrammarParser.PenContext ctx) {

      Boolean res = true;
      String s = ctx.ID().getText();

      if (!table.exists(s)) {
         table.addSymbol(s, new Symbol(s, new PenType()));
      } else {
         ErrorHandling.printError(ctx, String.format("%s is already a Pen", s));
         res = false;

      }
      return res;
   }

   @Override
   public Boolean visitOutput(PdrawGrammarParser.OutputContext ctx) {
      Boolean res = true;
      res = visit(ctx.expr());
      return res;
   }

   @Override
   public Boolean visitIfBody(PdrawGrammarParser.IfBodyContext ctx) {
      Boolean res = true;

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         res = res && visit(itr.next());
      }

      return res;
   }

   @Override
   public Boolean visitElseBody(PdrawGrammarParser.ElseBodyContext ctx) {
      Boolean res = true;

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         res = res && visit(itr.next());
      }

      return res;
   }

   @Override
   public Boolean visitIfelse(PdrawGrammarParser.IfelseContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.expr()) && visit(ctx.ifBody());

      if (res) {
         if (ctx.elseBody() != null) {
            res = res && visit(ctx.elseBody());
         }

         if (!ctx.expr().symbol.getType().getName().equals("boolean")) {
            ErrorHandling.printError(ctx, String.format("%s is not a boolean variable", ctx.expr().getText()));
            res = false;
         }
      }

      return res;
   }

   @Override
   public Boolean visitRepeat(PdrawGrammarParser.RepeatContext ctx) {
      Boolean res = true;
      res = visit(ctx.expr()) && visit(ctx.repeatBody());

      if (table.exists(ctx.expr().getText())) {
         if (!ctx.expr().symbol.getType().getName().equals("boolean")) {
            ErrorHandling.printError(ctx, String.format("%s is not a boolean variable", ctx.expr().getText()));
            res = false;

         }
      } else {

         if (ctx.expr().symbol.getType().getName() != "boolean") {

            ErrorHandling.printError(ctx, String.format("%s is not a boolean variable", ctx.expr().getText()));
            res = false;

         }

      }
      return res;
   }

   @Override
   public Boolean visitRepeatBody(PdrawGrammarParser.RepeatBodyContext ctx) {
      Boolean res = true;

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         res = res && visit(itr.next());
      }

      return res;
   }

   @Override
   public Boolean visitRepeatNtimes(PdrawGrammarParser.RepeatNtimesContext ctx) {
      Boolean res = true;
      res = visit(ctx.expr()) && visit(ctx.repeatNtimesBody());

      if (!ctx.expr().symbol.getType().getName().equals("int")) {
         ErrorHandling.printError(ctx, String.format("%s is not a integer variable", ctx.expr().getText()));
         res = false;

      }

      return res;
   }

   @Override
   public Boolean visitRepeatNtimesBody(PdrawGrammarParser.RepeatNtimesBodyContext ctx) {
      Boolean res = true;

      Iterator<PdrawGrammarParser.StatContext> itr = ctx.stat().iterator();
      while (itr.hasNext()) {
         res = res && visit(itr.next());
      }

      return res;
   }

   @Override
   public Boolean visitExprBoolOperation(PdrawGrammarParser.ExprBoolOperationContext ctx) {
      Boolean res = true;
      Boolean resE1 = visit(ctx.e1);
      Boolean resE2 = visit(ctx.e2);

      res = res && resE1 && resE2;

      String e1Text = ctx.e1.getText();
      String e2Text = ctx.e2.getText();

      if (res) {
         Type e1 = ctx.e1.symbol.getType();
         Type e2 = ctx.e2.symbol.getType();

         if (ctx.e1.symbol.getType().getName() == "id") {

            if (!table.getSymbol(e1Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e1Text));
               res = false;

            }

         } else {
            if (!e1.isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e1Text));
               res = false;
            }
         }

         if (ctx.e2.symbol.getType().getName() == "id") {

            if (!table.getSymbol(e2Text).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", e2Text));
               res = false;

            }
         } else {
            if (!e2.isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a Numeric value", e2Text));
               res = false;
            }
         }

         ctx.symbol = new Symbol(new BooleanType());

      } else {

         if (!resE1) {
            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e1Text));
            res = false;
         }
         if (!resE2) {
            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", e2Text));
            res = false;
         }

      }

      return res;
   }

   @Override
   public Boolean visitPoint(PdrawGrammarParser.PointContext ctx) {
      Boolean res = true;
      res = res && visit(ctx.x);
      res = res && visit(ctx.y);

      Type xType = ctx.x.symbol.getType();
      Type yType = ctx.y.symbol.getType();
      String xText = ctx.x.getText();
      String yText = ctx.y.getText();

      if (xType.getName() == "id") {

         if (table.exists(xText)) {

            if (!table.getSymbol(xText).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", xText));
               res = false;
            }
         } else {
            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", xText));
            res = false;
         }
      } else {
         if (!xType.isNumeric()) {
            ErrorHandling.printError(ctx, String.format("%s is not numeric", xText));
            res = false;
         }
      }
      if (yType.getName() == "id") {

         if (table.exists(yText)) {

            if (!table.getSymbol(yText).getType().isNumeric()) {

               ErrorHandling.printError(ctx, String.format("%s is not a numeric variable", yText));
               res = false;

            }

         } else {

            ErrorHandling.printError(ctx, String.format("%s is not a defined variable", yText));
            res = false;

         }

      } else {

         if (!yType.isNumeric()) {

            ErrorHandling.printError(ctx, String.format("%s is not numeric", yText));
            res = false;

         }

      }

      ctx.symbol = new Symbol(new PointType());

      return res;
   }

   @Override
   public Boolean visitGroup(PdrawGrammarParser.GroupContext ctx) {
      ctx.gt = new GroupType(new IntegerType(), Collections.emptyList());
      return true;
   }
}