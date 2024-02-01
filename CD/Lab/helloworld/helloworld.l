%{
    #include <stdio.h>
    #undef yywrap
    int yywrap() { return 1; }
%}

%%
[\n] printf("Hello World\n");		
%%

int main() { yylex(); }