%{
    #define YY_NO_UNPUT 1
    #include <stdio.h>
    int yylex();
    int yywrap() { return 1; }
%}

%%
[0-9]   printf("Number: %s\n", yytext);
%%

int main() { yylex(); }
