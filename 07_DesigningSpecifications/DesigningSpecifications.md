# Designing Specifications

In this reading we’ll look at different specs for similar behaviors, and talk about the tradeoffs between them. We’ll look at three dimensions for comparing specs:

- How **deterministic** it is. Does the spec define only a single possible output for a given input, or allow the implementor to choose from a set of legal outputs?

- How **declarative** it is. Does the spec just characterize what the output should be, or does it explicitly say how to compute the output?

- How **strong** it is. Does the spec have a small set of legal implementations, or a large set?

## Deterministic VS Underdetermined Specs

```java
static int findFirst(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == val) return i;
    }
    return arr.length;
}
```
```java
static int findLast(int[] arr, int val) {
    for (int i = arr.length - 1 ; i >= 0; i--) {
        if (arr[i] == val) return i;
    }
    return -1;
}
```

Consider this specification:
```java
static int findExactlyOne(int[] arr, int val)
  requires: val occurs exactly once in arr
  effects:  returns index i such that arr[i] = val
```
Both `findFirst` and `findLast` satisfy the specification `findExactlyOne`; these two implementations are equivalent and substitutable for one another. This specification is also **deterministic**, as when presented with a state satisfying precondition, as the outcome is completely determined.

Now consider this slightly different specification:
```java
static int findAnyIndex(int[] arr, int val)
  requires: val occurs in arr
  effects:  returns index i such that arr[i] = val
```

This specification is not deterministic. It doesn’t say which index is returned if `val` occurs more than once. It simply says that if you look up the entry at the index given by the returned value, you’ll find `val` . This specification allows multiple valid outputs for the same input. We call this **underdetermined**.

> note that underdetermined is different from non-deterministic. The latter means the same input and function produces different outputs. Underdetermined code need not be non-deterministic

This underdetermined find spec is again satisfied by both `findFirst` and `findLast` , each resolving the underdeterminedness in its own (fully deterministic) way. A client of find `findAnyIndex` spec can’t rely on which index will be returned if val appears more than once. Underdeterminism in specifications offers a choice that is made by the implementor at implementation time. An underdetermined spec is typically implemented by a fully-deterministic implementation.

## Declarative VS Operational Specs

Operational specifications give an explicit series of steps that the method performs(like pseudocode descriptions). Declarative specifications don’t give details of intermediate steps. Instead, they just give properties of the final outcome, and how it’s related to the initial state.

Almost always, declarative specifications are preferable. They’re usually shorter, easier to understand, and most importantly, they don’t inadvertently expose implementation details that a client may (incorrectly) rely on, and may cause bugs when implementation changes. If we wanted `findAnyIndex` spec, we would not specify to start from lowest index and keep searching upwards to highest index.

> Programmers may use operational specifications so the spec comment explains the implementation. Don’t do that. When it’s necessary, use comments within the body of the method, not in the spec comment.

## Stronger VS Weaker Specs

Suppose you want to change the implementation/specification of an existing method in production. How to compare behaviours of two specifications to decide whether it's safe to replace old spec with new spec?

A specification S2 is stronger than or equal to S1 if:
- S2's precondition is weaker than or equal to S1
- S2's postcondition is stronger than or equal to S1

Then S2 can replace S1. You can always weaken the precondition; easier to call method. Can always strengthen the postcondition, even more requirements on the return output.

```java
static int findExactlyOne(int[] a, int val)
  requires: val occurs exactly once in a
  effects:  returns index i such that a[i] = val
```

can be replaced with

```java
static int findOneOrMore_AnyIndex(int[] a, int val)
  requires: val occurs at least once in a
  effects:  returns index i such that a[i] = val
```

which has a weaker precondition. This in turn can be replaced with

```java
static int findOneOrMore_FirstIndex(int[] a, int val)
  requires: val occurs at least once in a
  effects:  returns lowest index i such that a[i] = val
```

which has a stronger postcondition.

## Designing good specifications

### Specifications should be coherent

Specs shouldn't have many different cases or goals. Long arg lists, deeply nested if-statements, or boolean flags are signs of trouble.

```java
static int sumFind(int[] a, int[] b, int val)
  effects: returns the sum of all indices in arrays a and b at which
             val appears
```

This basically does 2 things, finding in 2 arrays, and then summing them. Easier to use 2 separate procedures

### Results of a call should be informative

```java
static V put (Map<K,V> map, K key, V val)
  requires: val may be null, and map may contain null values
  effects:  inserts (key, val) into the mapping,
              overriding any existing mapping for key, and
              returns old value for key, unless none,
              in which case it returns null
```
This method puts values into a map. The precondition allows `null` values to be stored. However, the postcondition uses `null` as a special return value for a missing key - this is useless. If `null` is returned, we can't tell if this is because `null` was stored or if the key doesn't exist. Returning `null` is useless unless you know didn't insert `null` values.

### Specification should be strong enough

Specification should be strong enough to provide clear guarantees for what the method does, cover all possible scenarios including edge cases.

```
static void addAll(List<T> list1, List<T> list2)
  effects: adds the elements of list2 to list1,
             unless it encounters a null element,
             at which point it throws a NullPointerException
```

This `addAll` should specify what happens when an exception is thrown, to avoid client guessing about state of the data.

### Specification should be weak enough

Spec should allow flexibility in implementation, doesn't over-guarantee things beyond control.

```java
static File open(String filename)
  effects: opens a file named filename
```

The `open` method should not guarantee to always open a file, as beyond control. Should say it attempts to open a file, if it succeeds then certain properties and if fail etc.

## Use abstract types where possible

In Java, using abstract types often means using an interface type like `Map` or `Reader`, instead of specific implementation types like `HashMap` or `FileReader`.

```java
static ArrayList<T> reverse(ArrayList<T> list)
  effects: returns a new list which is the reversal of list, i.e.
             newList[i] == list[n-i-1]
             for all 0 <= i < n, where n = list.size()
```

This forces the client to pass in `ArrayList`, and return `ArrayList`. Since the specification doesn't depend on anything specific about `ArrayList`, better to use abstract `List`.

## Include Precondition or Postcondition?

The most common use of precondition is because it is hard or expensive for method to check this. We then specify the preconditions that are required. If the preconditions are easy to check, just throw an exception when arguments are inappropriate - fail fast principle.








