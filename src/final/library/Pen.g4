grammar Pen;

program: stat* EOF;

stat:
    pen Terminator
;


expr returns [std::string type]:
    e1=expr op=('*' | '/') e2=expr     #ExprMultDiv
    | e1=expr op=('+' | '-') e2=expr   #ExprSumSub
    | op=('+' | '-') e2=expr           #ExprUnary
    | '(' expr ')'                     #ExprParentesis
    | DOUBLE                           #ExprDouble
    | INT                              #ExprInt
    | STRING                           #ExprStrings
    | BOOLEAN                          #ExprBoolean
    | COLOR                            #ExprColor
    | DIRECTION                        #ExprDirection
;

pen:
    'Pen' ID ('->' assignProperty)?
;


assignProperty: '{' property (',' property)* '}';

property:
    'color'        ':'    expr              #setColor
    | 'thickness'  ':'    expr              #setThickness
    | 'direction'  ':'    expr              #setDirection
    | 'position'   ':'    expr              #setPosition
    | 'name'       ':'    expr              #setName
;

Terminator: ';' ;

ID: [a-zA-Z][a-zA-Z_0-9]*;

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
    [0-9]+ '.' [0-9]+
;

INT:
    [0-9]+
;

BOOLEAN:
    'true'
    | 'false'
;

WS : [ \n\r\t]+ -> skip ;

COMMENT: 
    ('//' .*? ('\n'|EOF)
    |':)' .*? '(:') -> skip
;
