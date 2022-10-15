grammar PdrawGrammar;

program: stat* EOF;

stat:
    impt Terminator
    | pen Terminator
    | assign Terminator
    | output Terminator
    | expr Terminator
    | ifelse
    | repeat
    | repeatNtimes
;

impt: 'import' STRING 'as' ID;

assign returns [Symbol symbol]:
    type ID '->' expr               #AssignWType
    | ID '->' assignProperty        #AssignPen
    | ID '->' expr                  #AssignReassign
    | ID '.' prop=ID '->' expr      #AssignProp
;

assignProperty returns [PropertyTable properties]:
    '{' property (',' property)* '}' #setProperties
;

type returns [Type vartype]:
    'Group'         {$vartype = new GroupType();}
    | 'String'      {$vartype = new StringType();}
    | 'Double'      {$vartype = new DoubleType();}
    | 'Int'         {$vartype = new IntegerType();}
    | 'Boolean'     {$vartype = new BooleanType();}
    | 'Direction'   {$vartype = new DirectionType();}
    | 'Color'       {$vartype = new ColorType();}
    | 'Pen'         {$vartype = new PenType();}
;

expr returns[Symbol symbol]:
    point                                                          #ExprPoint
    | input                                                        #ExprInput
    | e1=expr op=('or'|'and') e2=expr                              #ExprOrAnd
    | e1=expr op=('==' | '!=' | '<' | '>' | '>=' |'<=') e2=expr    #ExprBoolOperation
    | e1=expr op=('*' | '/') e2=expr                               #ExprMultDiv
    | e1=expr op=('+' | '-') e2=expr                               #ExprSumSub
    | op=('+' | '-') e2=expr                                       #ExprUnary
    | '(' expr ')'                                                 #ExprParentesis
    | expr 'move' expr 'rotated' expr                              #ExprMoveRotated
    | expr 'move' expr                                             #ExprMove
    | expr 'moveTo' expr                                           #ExprMoveTo
    | expr 'rotate' expr                                           #ExprRotate
    | expr 'up'                                                    #ExprUp
    | expr 'down'                                                  #ExprDown
    | group                                                        #ExprGroup
    | INT                                                          #ExprInt
    | DOUBLE                                                       #ExprDouble
    | COLOR                                                        #ExprColor
    | BOOLEAN                                                      #ExprBoolean
    | DIRECTION                                                    #ExprDirection
    | STRING                                                       #ExprString
    | ID                                                           #ExprVar
;

property returns [Symbol symbol]:
    'color'        ':'    expr              #setColor
    | 'thickness'  ':'    expr              #setThickness
    | 'direction'  ':'    expr              #setDirection
    | 'position'   ':'    expr              #setPosition
    | 'name'       ':'    expr              #setName
;

pen: 'Pen' ID '->' assignProperty;

input: 'input' type;

output: 'output' expr;

ifelse: 'if' '(' expr ')' '->' '{' ifBody '}' ('else' '->' '{' elseBody '}')?;
ifBody: stat*;
elseBody: stat*;

repeat: 'repeat' '(' expr ')' '->' '{' repeatBody '}';
repeatBody: stat*;

repeatNtimes: 'repeat' '(' expr ')' 'times' '->' '{' repeatNtimesBody '}';
repeatNtimesBody: stat*;

point returns [Symbol symbol]:
    '(' x=expr ',' y=expr ')'
;

group returns [GroupType gt]: type '[' expr (',' expr)* ']' ;

Terminator: ';' ;

DIRECTION:
    'UP' | 'NORTH'
    | 'DOWN' | 'SOUTH'
    | 'RIGHT' | 'WEST'
    | 'LEFT' | 'EAST'
;

COLOR:
    '#' HEX HEX HEX HEX HEX HEX
;

fragment HEX: [a-fA-F0-9] ;

STRING: 
    '"' .*? '"'
    | '\'' .*? '\''
;

DOUBLE:
    [0-9]+ '.' [0-9]*
    | [0-9]* '.' [0-9]+
;

INT:
    [0-9]+
;

BOOLEAN:
    'true'
    | 'false'
;

ID: [a-zA-Z][a-zA-Z_0-9]*;

WS : [ \n\r\t]+ -> skip ;

COMMENT: 
    ('//' .*? ('\n'|EOF)
    |'/*' .*? '*/') -> skip
;