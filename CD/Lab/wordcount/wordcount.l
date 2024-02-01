%{
  #undef yywrap
  #include <stdio.h>
  int yywrap() { return 1; }
  int count = 1;
%}

%%
[a-zA-Z] {}
[^\t\n] { ++count; }
[\n] { return 0; }
%%

int main() {
  yylex();
  printf("\nNumber of words: %d\n", count);
  return 0;
}