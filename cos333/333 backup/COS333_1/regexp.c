/* Copyright (C) 1999 Lucent Technologies */
/* Excerpted from 'The Practice of Programming' */
/* by Brian W. Kernighan and Rob Pike */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
/*
static int grep(char*, FILE*, char*);*/
int match(char *re, char *s, int *start, int *end);
static int matchhere(char*, char*, char*, int*, int*);
static int matchstar(int, char*, char*, char*, int*, int*);
static int matchqmark(int c, char * regexp, char* text, char* starttext, int*, int*);
static int matchplus(int c, char * regexp, char * text, char * starttext, int*, int*);
#if 0
/* grep main: search for regexp in files */
int main(int argc, char *argv[])
{
	int i, nmatch;
	FILE *f;

	if (argc < 2)
		printf("usage: grep regexp [file ...]");
	nmatch = 0;
	if (argc == 2) {
		if (grep(argv[1], stdin, NULL))
			nmatch++;
	} else {
		for (i = 2; i < argc; i++) {
			f = fopen(argv[i], "r");
			if (f == NULL) {
				printf("can't open %s:", argv[i]);
				continue;
			}
			if (grep(argv[1], f, argc > 3 ? argv[i] : NULL) > 0)
				nmatch++;
			fclose(f);
		}
	}
	return nmatch == 0;
}

/* grep: search for regexp in file */
static int grep(char *regexp, FILE *f, char *name)
{
	int n, nmatch;
	int * start, *end;
	char buf[BUFSIZ];
	/*char * regexp1 = ".?";*/
	start = (int *) malloc(sizeof(*start));
	end = (int *) malloc(sizeof(*end));
	nmatch = 0;
	while (fgets(buf, sizeof buf, f) != NULL) {
		n = strlen(buf);
		if (n > 0 && buf[n - 1] == '\n')
			buf[n - 1] = '\0';
		if (match(regexp, buf, start, end)) {
			nmatch++;
			if (name != NULL)
				printf("%s:", name);
			printf("%s\n", buf);

		}
		printf("%d %d\n", *start, *end);
		fflush(NULL);
	}
	return nmatch;
}
#endif

/* matchhere: search for regexp at beginning of text */
static int matchhere(char *regexp, char *text, char * starttext, int* start, int * end)
{
	int notmeta;
	notmeta = -1;

	if ((regexp[0] == '+') || (regexp[0] == '?') || (regexp[0] == '+')
	                || (regexp[0] == '*'))
		notmeta = 0;

	if (regexp[0] == '\0') {
		*end = text - starttext;
		return 1;
	}
	if (regexp[1] == '*' && notmeta)
		return matchstar(regexp[0], regexp + 2, text, starttext, start,
		                end);
	if (regexp[1] == '?' && notmeta)
		return matchqmark(regexp[0], regexp + 2, text, starttext,
		                start, end);
	if (regexp[1] == '+' && notmeta)
		return matchplus(regexp[0], regexp + 2, text, starttext, start,
		                end);
	if (regexp[0] == '$' && regexp[1] == '\0') {
		if (*text == '\0') {
			*end = text - starttext;
		}
		return *text == '\0';
	}
	if (*text != '\0' && (regexp[0] == '.' || regexp[0] == *text))
		return matchhere(regexp + 1, text + 1, starttext, start, end);
	return 0;
}

/* match: search for regexp anywhere in text */
int match(char *regexp, char *text, int *start, int *end)
{

	char * starttext;
	int * index;
	int ret;
	if ((regexp == NULL) || (text == NULL) || (start == NULL) || (end
	                == NULL)) {
		return 0;
	}
	index = malloc(sizeof(*index));
	*index = 0;
	starttext = text;
	if (regexp[0] == '^') {
		ret = matchhere(regexp + 1, text, starttext, start, end);
		if (ret) {
			*start = 0;
		} else {
			*start = -1;
			*end = -1;
		}
		return ret;
	}
	do { /* must look even if string is empty */
		if (matchhere(regexp, text, starttext, index, end)) {
			*start = *index;
			return 1;
		} else
			(*index)++;
	} while (*text++ != '\0');
	*start = -1;
	*end = -1;
	return 0;
}

/* matchstar: leftmost longest search for c*regexp */
static int matchstar(int c, char *regexp, char *text, char * starttext, int * start,
                int * end)
{
	char *t;

	for (t = text; *t != '\0' && (*t == c || c == '.'); t++)
		;
	do { /* matches zero or more */
		if (matchhere(regexp, t, starttext, start, end))
			return 1;
	} while (t-- > text);
	return 0;
}

static int matchqmark(int c, char * regexp, char* text, char * starttext, int * start,
                int * end)
{

	char *t;
	int ret;

	t = text;

	/*match*/
	if ((*t == c) || (c == '.')) {
		ret = matchhere(regexp, text +1, starttext, start, end);
		if (ret == 1)
			return ret;
		else
			return matchhere(regexp, text , starttext, start,
			                end);
	}
	/* no match*/
	if ((*t != c) && (c != '.')) {
		return 1;
	}

	return 0;
}

static int matchplus(int c, char * regexp, char * text, char * starttext, int * start,
                int * end)
{

	char *t;

	t = text;

	if ((*t != c) && (c != '.')) {
		return 0; /*failure*/
	}

	t++;

	for (; *t != '\0' && (*t == c || c == '.'); t++)
		;
	do { /* * matches zero or more */
		if (matchhere(regexp, t, starttext, start, end))
			return 1;
	} while (t-- > (c == '.' ? text + 1 : text));
	return 0;

}
