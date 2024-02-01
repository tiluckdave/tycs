%{
    #include <stdio.h>
    #include <stdlib.h>
    #undef yywrap
    int yywrap() { return 1; }
%}

%%
^(a.*a|A.*A|a.*A|A.*a)$ {
    fprintf(yyout, "%s", yytext);
    printf("%s\n", yytext);
}
. {}
%%

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <filename>\n", argv[0]);
        return 1;
    }

    FILE *file = fopen(argv[1], "r");
    if (!file) {
        perror("Error opening file");
        return 1;
    }

    FILE *outputFile = fopen("output.txt", "w");
    if (!outputFile) {
        perror("Error opening output file");
        fclose(file);
        return 1;
    }

    yyin = file;
    yyout = outputFile;

    yylex();

    fclose(file);
    fclose(outputFile);
    return 0;
}
