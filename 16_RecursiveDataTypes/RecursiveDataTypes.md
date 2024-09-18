# Recursive Data Types

Just like how a recursive function is defined in terms of itself, a recursive datatype is also defined in terms of itself. The same need for *base* and *recursive* cases now appear as *variants* of the ADT.

## Immutable lists

Immutability is powerful not just because of safety, but laso for sharing. Sharing has performance benefits: less memory consumed, less time spent copying. We define a data type for an immutable list, with four fundamental operations:

| Operation | Signature | Description |
|-----------|-----------|-------------|
| empty | `void → ImList` | Returns an empty list |
| cons | `E × ImList → ImList` | Returns new list by adding element to front |
| first | `ImList → E` | Returns first element, requires nonempty |
| rest | `ImList → ImList` | Returns list without first element, requires nonempty |

We impement this datatype in java with a generic interface:

```java
public interface ImList<E> {
    // static factory method that takes no arguments and produces instance of Empty
    //this way we can hide Empty class, clients only need to use ImList
    public static <E> Imlist<E> empty() {
        return new Empty<>()
    }
    public ImList<E> cons(E e);
    public E first();
    public ImList<E> rest();
}
```

We'll write two classes both implementing this interface:
- `Empty` represents teh resutls of the empty operation
- `Cons` represents the result of the cons operation (concat element + list)

```java
public class Empty<E> implements ImList<E> {
    //empty constructor, creates an empty list
    public Empty() {
    }
    //adds an element to an empty list, return a new Cons object
    public ImList<E> cons(E e) {
        return new Cons<>(e, this);
    }
    public E first() {
        // we dont need this for Empty
        throw new UnsupportedOperationException();
    }
    public ImList<E> rest() {
        // we dont need this too
        throw new UnsupportedOperationException();
    }
}
```

```java
public class Cons<E> implements ImList<E> {
    private final E e; //always the first element
    private final ImList<E> rest; //rest of the list

    //constructor to create a new Cons
    public Cons(E e, ImList<E> rest) {
        this.e = e;
        this.rest = rest;
    }
    //adds a new element to start of the list
    public ImList<E> cons(E e) {
        //this is the recursive step
        return new Cons<>(e, this);
    }
    //returns the first element
    public E first() {
        return e;
    }
    //returns the rest of the list
    public ImList<E> rest() {
        return rest;
    }
}
```

Example code will be like:

```java
//creates an empty list
ImList<Integer> nil = ImList.empty();
//creates an array of size [0]
nil.cons(0)

//assigns x to an array [0,1,2]
ImList<Integer> x = nil.cons(2).cons(1).cons(0);
//returns 0
x.first()
//returns [1,2]
x.rest()
//returns 1
x.rest().first()

//assigns y to [4,1,2]
ImList<Integer> y = x.rest().cons(4);
```

Note that even with we have different objects like `x` and `y`, these two objects share some memory `[1,2]` as they essentially comprise a huge number of pointers.

For `ImList`, then two implementations `Empty` and `Cons` *cooperate* to implement the ADT - you need them both. This is unlike `ArrayList` and `LinkedList` which are two alternative representations for the `List` ADT.

## Recursive Datatype Definitions

The ADT `ImList` and concrete classes `Empty` and `Cons` form a recursive data type. `Cons` implements `ImList`, but also uses `ImList` in its own rep, hence it is recursive.

The **datatype definition** of the ADT `ImList`:
```
ImList<E> = Empty + Cons(first:E, rest:ImList)
```
Explanation: the set `ImList` consists of values formed in two ways: ethier via `Empty` constructor, or applying `Cons` constructor to an element and another `ImList`.

Formally, a datatype definition has:
- The **abstract datatype** on the left, defined by its **representation** (**concrete datatype**) on the right
- representation consists of **variants** of the datatype separated by **+**
- each variant is a constructor with zero or more (named and typed) arguments

Another example is the datatype definition for the ADT binary tree:

```
Tree<E> = Empty + Node(e:E, left:Tree<E>, right:Tree<E>)
```

## Functions across Recursive Datatypes
This way of thinking about datatypes — as a recursive definition of an ADT with concrete variants — provides a convenient way to describe operations over the datatype, as *functions with one case per variant*.

Suppose we wanted to implement the size of an `ImList`:

We think of this as

```
size(Empty) = 0
size(Cons(first: E, rest: ImList)) = 1 + size(rest)
```
which is implemented as

```java
public interface ImList<E> {
    // ...
    public int size();
}

public class Empty<E> implements ImList<E> {
    // ...
    public int size() { return 0; }
}

public class Cons<E> implements ImList<E> {
    // ...
    public int size() { return 1 + rest.size(); }
}
```

This design pattern of implementing an operation across different variants is called the *interpreter pattern*:
- declaring the oepration in the ADT interface
- implement the operation recursively in each concrete variant

## Null VS Empty

Do not use `null` over the `Empty` class. Using an object, rather than a `null`, to signal the base case or endpoint of a data structure is an example of a design pattern called *sentinel objects*. The sentinel object acts like an object in the datatype, so we can call methods on it- even on an empty list.

If we had to use `null`, our code would be full of checks like:
```java
if (lst != null) n = lst.size();
```
whereas having the `Empty` class allows us to write:
```java
n = lst.size();
```
which will always work.

## Declared Types Vs Actual Type

All statically-checked object-oriented language works this way.

There are two worlds in type checking: **compile-time** before the program runs, and **run-time** when the program is executing.

At compile time, every variable has a **declared type**, stated in its code declaration. The compiler uses the declared types of variables (and method return values) to deduce declared types.

At run time, every object has an **actual type** , given by the constructor that created the object. For example,  `new String()` makes an object whose actual type is `String` . `new Empty()` makes an object whose actual type is `Empty`. `new ImList()` is forbidden by Java, because `ImList` is an interface — it has no object values of its own, and no constructors.

## Immutability and Backtracking

Immutable lists allow sharing of data, but only at the end of lists. (We add to the front of the list). Backtracking doesn't work well with mutable data types - you'd have to undo your mutations everytime you backtrack. With immutable types that creates new copies, you can simply throw away states instead of modifying them!

Immutable lists have the nice property that each step taken on the path can share all the information from the previous steps, just by adding to the front of the list. When you have to backtrack, you stop using the current step’s state — but you still have references to the previous step’s state.

