grammar Hello;                          // define a grammar called Hello
actions : (greetings | bye) + EOF ; 
greetings : 'hello' WORD ;              // match keyword hello followed by an identifier
bye : 'bye' WORD ;
WORD : [a-z]+ ;                         // match lower case identifiers
WS : [ \t\r\n]+ -> skip ;               // skip spaces, tabs, newlines, \r (Windows)