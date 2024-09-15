# Static Checking

## Types

By Java convention, primitive types are lowercase, while object types start with a capital letter.

Operations are functions that take inputs and produce outputs
- infix/prefix/postfix operator: `a+b`
- method of an object `obj1.add(obj2)`
- function `Math.sin(theta)` where `Math` is not an object but a class

## Static Typing

Static typing is a particular kind of static checking , which means checking for bugs at compile time. Static typing prevents bugs caused by applying an operation to the wrong types of arguments.

## Static Checking, Dynamic Checking, No Checking

- **Static checking** : the bug is found automatically before the program even runs.
- **Dynamic checking** : the bug is found automatically when the code is executed.
- **No checking** : the language doesn’t help you find the error at all. You have to watch for it yourself, or end up with wrong answers.

Static checking > dynamic checking > no checking

**Static checking** can catch:

- syntax errors, like extra punctuation or spurious words.
- wrong names, like Math.sine(2) . (The right name is sin .)
- wrong number of arguments, like Math.sin(30, 20) .
- wrong argument types, like Math.sin("30") .
- wrong return types, like return "30"; from a function that’s declared to return an int

**Dynamic checking** can catch:
- illegal argument values. For example, the integer expression `x/y` is only erroneous when `y` is actually zero; otherwise it works. So in this expression, divide-by-zero is not a static error, but a dynamic error.
- unrepresentable return values, i.e., when the specific return value can’t be represented in the type.
- out-of-range indexes, e.g., using a negative or too-large index on a string.
- calling a method on a null object reference ( null is like Python None )

Static checking tends to be about types of variables, errors independent of specific value of variables. Static typing guarantees a variable is of a certain type, but we only know until runtime (with dynamic checking) the errors caused by specific values.

## Primitve Types Edge Cases

Primitive numeric types often have edge cases, that can avoid even dynamic checking. The traps are:
- **Integer division** `5/2` does not return a fraction, it returns a truncated integer. We might have hoped it would raise a dynamic error (because a fraction isn’t representable as an integer) but it frequently produces the wrong answer instead.
- **Integer overflow** The `int` and `long` types are actually finite sets of integers, with maximum and minimum values. What happens when you do a computation whose answer is too positive or too negative to fit in that finite range? The computation quietly overflows (wraps around), and returns an integer from somewhere in the legal range but not the right answer.
- Special `float` and `doubles` values. The `float` and `double` types have several special values that aren’t real numbers: `NaN` (Not a Number), `POSITIVE_INFINITY` , and `NEGATIVE_INFINITY` . So operations that you’d expect to produce dynamic errors, like dividing by zero or taking the square root of a negative number, produce one of these special values instead. If you keep computing with it, you’ll end up with a bad final answer.

## Buffer Overflow (Array)
If we use a fixed length array like
```java
int[] a = new int[100];
```
and continue filling up its values, we may overwrite its capacity of `100`. This is known as buffer overflow.

Instead of fixed-length arrays, we use variable length arrays
```java
List<Integer> list = new ArrayList<Integer>();
```

`List` is an interfact, a type that can't be directly constructed with `new`, but instead specifies the operations (or methods) a List must provide. There is several ways to implement this interfact, and `ArrayList` is a class that implements this.

> Note that we wrote `List<Integer>` instead of `List<int>`. When we parameterize a type with `<>`. Java expects object types equivalents not primitive types

## Mutability

Java also gives us immutable references: variables that are assigned once and never reassigned. To make a **reference immutable**, declare it with the keyword `final`:
```java
final int n = 5;
```
It’s good practice to use `final` for declaring the parameters of a method and as many local variables as possible. Like the type of the variable, these declarations are important documentation, useful to the reader of the code and statically checked by the compiler.

## 6.005 Golden Rules

- **Safe From Bugs** - Correct today and correct in the unknown future
- **Easy to Understand** - Communicate clearly with future programmers (including future you)
- **Ready for Change** - Designed to accomodate change without rewriting