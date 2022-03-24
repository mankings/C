grammar Hello;                  // define a grammar called Hello
greetings : 'Hello' ID ;        // match keyword hello followed by an identifier
ID : [a-z]+ ;                   // match lower case identifiers
WS : [ \t\r\n]+ -> skip ;       // skip spaces, tabs, newlines, \r (Windows)