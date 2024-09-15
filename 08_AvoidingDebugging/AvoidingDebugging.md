# Avoiding Debugging

## First Defence - Make Bugs Impossible

We can actually minimize bugs by design - using static checking (checking at compile time).

Immutability is another design principle to minimize bugs. Strings are immutable. Java also gives us *immutable references*, where variables declared with `final` are never reassigned with another name. This makes the reference immutable, but the object in question can still be modified.

## Second Defence - Localize Bugs

Fail fast: should bugs occur, localize them to a small part of a program so we can find them easily. We can check preconditions to do this, as an example of defensive programming. We can insert assertion statements to check if preconditions are satisfied.

```java
/**
 * @param x  requires x >= 0
 * @return approximation to square root of x
 */
public double sqrt(double x) { 
    if (! (x >= 0)) throw new AssertionError();
    ...
}
```

## Assertions

Defensive checks (assertions) are written like

```java
assert (x >= 0);
```

and have the added benefit or documenting the state of the program, for someone reading your code. An assertion statement can also print some text for added info:

```java
assert (x >= 0) : "x is " + x;
```

**Java Assertions are off by default**

You have to enable assertions explicity by passing `-ea` (which stands for enable assertions ) to the Java virtual machine. (We turn off assertions by default to save runtime). Make sure to always default turn these on when running JUnit tests.

> note that Java `assert` are different from JUnit methods `assertTrue()` and `assertEquals()`. The assert statements don’t run without `-ea` , but the JUnit `assert...()` methods always run.

## What to Assert

- Method argument conditions: checking for proper preconditions
- Method return value conditions: checking whether the return values are close (within some error) to the expected return values
- Covering all cases: add an assert statement at the end of a `switch` block to cover all illegal cases; signal when our code has entered into some illegal conditions

## What not to Assert

- Don't assert obvious statements - assertions cost extra compute!
- Don't use assertions to test external conditions. Assertions should only check internal conditions and external conditions should be handled by Exceptions. External failures are not bugs.
- Don't have assertions that have side effects, such as modifying existing variables

## Incremental Development

Unit testing (test modules in isolation) + Regression testing (after adding new feature, run entire suite of regression test as often as possible to catch bugs early)

## Modularity and Encapsulation

**Modularity**: breaking up a system into components- each component can be designed, tested, used separately from rest of system

**Encapsulation**: Restrict a module so other stuff can access its internals - we use `public` and `private` to do this

**Variable Scope**: the scope of a variable is the portions where it exists. Keep variable scopes as small as possible so variables (hence bugs) can be localized. Concrete tips to minimize scope:

- **Always declare a loop variable in the for-loop initializer**

Doing it before the loop:
```java
int i;
for (i = 0; i < 100; ++i) {
```
The the scope exists outside the program and can be modified by other bits. Declaring it as local variable within a narrow scope means:
```java
for (int i = 0; i < 100; ++i) {
```

- **Declare a variable only when you need it, inside the inner-most curly brackes you can**

In a statically typed lang like Java, variable scopes are curly brace blocks. Don't declare all your variables at the start of the function, unneccessarily enlarging its scope.

- **Avoid global variables!**
