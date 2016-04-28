// Define grammar for shopping list parser
grammar Shopping;
item    : ware ' ' COUNT (' ')? UNIT | COUNT (' ')? (UNIT)? ' ' ware ;
ware    : WARE+ (' ' ware)* ;
COUNT   : [0-9]+ ;
UNIT    : WEIGHT | LENGTH | MISC ;

WEIGHT  : [Kk]'g' | [Kk]'ilo' | [Gg] | [Gg]'ram' ;
LENGTH  : [Mm] | [Mm]'eter' | [Cc]'m' | [Cc]'entimeter' ;
MISC    : [Ss]'tyk' | [Ss]'tyks' | [Pp]'oser' | [Kk]'artoner' | [Kk]'asser' | [Pp]'akker' | [Bb]'undt' ;

WARE    : [a-zA-Z]+ ;

WS      : [\t\r\n]+ -> skip ;
