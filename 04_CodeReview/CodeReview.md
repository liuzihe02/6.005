# Code Review

Code review is careful, systematic study of source code by people who are not the original author.

- **Improving the code**: Finding/anticipating bugs, checking clarity of code, consistency with project
- **Improving the programmer** teach each other about new language features, changes in design/new techniques

## Style Standards

Important to be self-consistent, follow the conventions of current project.

## Smelly Example #1

Poor "Code Hygiene" - bad code. Below is one example `dayOfYear`

```java
public static int dayOfYear(int month, int dayOfMonth, int year) {
    if (month == 2) {
        dayOfMonth += 31;
    } else if (month == 3) {
        dayOfMonth += 59;
    } else if (month == 4) {
        dayOfMonth += 90;
    } else if (month == 5) {
        dayOfMonth += 31 + 28 + 31 + 30;
    } else if (month == 6) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31;
    } else if (month == 7) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30;
    } else if (month == 8) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31;
    } else if (month == 9) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31;
    } else if (month == 10) {
        dayOfMonth += 31 + SS28 + 31 + 30 + 31 + 30 + 31 + 31 + 30;
    } else if (month == 11) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
    } else if (month == 12) {
        dayOfMonth += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 31;
    }
    return dayOfMonth;
}
```

## Don't Repeat Yourself (DRY)

Avoid duplication of code - in case bug in both copies, and fixes in one place does not correct the other. Avoid copy-pasting if you can reuse code!

## Comments Where Needed

Good comments increases of code, safer from bug (key assumptions documented), ready for change.

One crucial comment is specification - appears before method/class to document behaviour. In Java, conventionally written as Javadoc comment, starts `/**` and includes `@` -syntax, like `@param` and `@return`.
```java
/**
 * Compute the hailstone sequence.
 * See http://en.wikipedia.org/wiki/Collatz_conjecture#Statement_of_the_problem
 * @param n starting number of sequence; requires n > 0.
 * @return the hailstone sequence starting at n and ending with 1.
 *         For example, hailstone(3)=[3,10,5,16,8,4,2,1].
 */
public static List<Integer> hailstoneSequence(int n) {
    ...
}
```

Also document the source of a piece of code adapted from software, like in stackoverflow. This indicates outdated code.

```java
// read a web page into a string
// see http://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
String mitHomepage = new Scanner(new URL("http://www.mit.edu").openStream(), "UTF-8").useDelimiter("\\A").next();
```

## Fail Fast

*Failing fast* means code should reveal bugs ASAP. Earlier a problem is observed, easier it is to find and fix. Static checking fails faster than dynamic checking - this is better. `dayOfYear` doesnt fail fast - passing in arguments in the wrong order will quietly return the wrong answer.

## Avoid Magic Numbers

Some universal constants are `0,1,2`. Other seemingly "random" numbers like `43` or `52` should be documented with comments or assigned a variable. NEVER hardcode constants that are computed by hand.

## One Purpose For Each Variable

In the `dayOfYear` example, `dayOfMonth` is reused to return the `dayOfYear` which is not the `dayOfMonth`. Don't reuse variables - you'll confuse readers and introduce bugs. It's a good idea to use `final` for method parameters, so these variables are never reassigned:

```java
public static int dayOfYear(final int month, final int dayOfMonth, final int year) {
    ...
}
```

## Use Good Names

Use a longer descriptive name like `secondsPerDay` instead of `tmp`, avoid variable names like `tmp`,`temp`,`data`.

- `methodsAreNamedWithCamelCaseLikeThis`
- `variablesAreAlsoCamelCase`
- `CONSTANTS_ARE_IN_ALL_CAPS_WITH_UNDERSCORES`
- `ClassesAreCapitalized`
- `packages.are.lowercase.and.separated.by.dots`

Method names are usually verb phrases like `getDate` or `isUpperCase` while variable and class names are usually noun phrases.

## Use Whitespace to Help the Reader

Never use the tab key - different tools treat tab characters differently — sometimes expanding them to 4 spaces, sometimes to 2 spaces, sometimes to 8. Just use spaces for indentation.

## Avoid Global Variables

global variables are declared `public static` in Java. `public` means it is accessible anywhere, while `static` means there is a single instance of this (class variable).

## Don't Print Results, Return Them

Results should be written instead of being printed onlu. Only the highest-level parts of the program should interact with the console. Lower-level parts should take input as parameters and return output as results.

