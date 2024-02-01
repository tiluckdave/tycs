%{
#include <stdio.h>
#include <ctype.h>
int yywrap() { return 1; }
int comment = 0;
%}

%%
"/*" {
    comment = 1;
    fprintf(yyout, "%s", yytext);
}
"*/" {
    comment = 0;
    fprintf(yyout, "%s", yytext);
}
"//".*|"/*"|"*/" {
    fprintf(yyout, "%s", yytext);
}
[a-z] {
    if (!comment) {
        fprintf(yyout, "%c", toupper(yytext[0]));
    } else {
        fprintf(yyout, "%s", yytext);
    }
}
[A-Z] {
    if (!comment) {
        fprintf(yyout, "%c", tolower(yytext[0]));
    } else {
        fprintf(yyout, "%s", yytext);
    }
}
\n {
    fprintf(yyout, "\n");
}
. {
    if (comment) {
        fprintf(yyout, "%s", yytext);
    } else {
        fprintf(yyout, "%c", yytext[0]);
    }
}
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

    printf("\n");

    fclose(file);

    return 0;
}
