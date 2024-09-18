# Interfaces

We separate the interface of an abstract data type from its implementation, using Java `interface`. One advantage is that the interface specifies the contract for the client and nothing more. The interface is all a client programmer needs to understand the ADT. The client should not need to depend on the ADT's representation details.

> The interface basically defines the ADT in Java!

Another advantage is that multiple different representations of the same ADT can co-exist in the same program, as different classes implementing the same interface. We can have `List` being implemented by `ArrayList` and also `LinkedList`.

## Subtypes

A type is a set of values. The Java `List` type is defined by an interface, where the classes `ArrayList` and `LinkedList` implement this interface. A *subtype* is simply a subset of the *supertype*: `ArrayList` and `LinkedList` are subtypes of `List`.

`B` is a subtype of `A` means every `B` is an `A`, or that every `B` satisfies the specifications for `A`. Hence `B`'s specification is at least as strong as `A`. When we declare a class implementing an interface, the compiler checks *every* method in `A` must appear in `B` with the appropriate type signature.

## Example: `MyString`

Using an interface for a string ADT:
```java
/** MyString represents an immutable sequence of characters. */
public interface MyString { 

    // We'll skip this creator operation for now
    // /** @param b a boolean value
    //  *  @return string representation of b, either "true" or "false" */
    // public static MyString valueOf(boolean b) { ... }

    /** @return number of characters in this string */
    public int length();

    /** @param i character position (requires 0 <= i < string length)
     *  @return character at position i */
    public char charAt(int i);

    /** Get the substring between start (inclusive) and end (exclusive).
     *  @param start starting index
     *  @param end ending index.  Requires 0 <= start <= end <= string length.
     *  @return string consisting of charAt(start)...charAt(end-1) */
    public MyString substring(int start, int end);
}
```
and our implementation:
```java
public class SimpleMyString implements MyString {
    //instance variable, this is undeclared when using the private constructor
    private char[] a;

    /* Create an uninitialized SimpleMyString, with a private constructor */
    private SimpleMyString() {}

    /** Create a string representation of b, either "true" or "false".
     *  @param b a boolean value */
    public SimpleMyString(boolean b) {
        //this means if b is True, then assign a to { 't', 'r', 'u', 'e' },
        //else assign a to { 'f', 'a', 'l', 's', 'e' }
        a = b ? new char[] { 't', 'r', 'u', 'e' } 
              : new char[] { 'f', 'a', 'l', 's', 'e' };
    }

    public int length() { return a.length; }

    public char charAt(int i) { return a[i]; }

    public MyString substring(int start, int end) {
        //uses the private no argument constructor
        SimpleMyString that = new SimpleMyString();
        //create a mew static array of length (end-start)
        that.a = new char[end - start];
        //this.a is the source array, that.a is the destination array
        //start is the starting position while (end-start) is the number of elems to copy
        System.arraycopy(this.a, start, that.a, 0, end - start);
        //return the new SimpyMyString object
        return that;
    }
}
```
which we call using
```java
MyString s = new SimpleMyString(true);
System.out.println("The first character is: " + s.charAt(0));
```

However, notice the private empty constructors we use to make new instances in `substring(..)` before we fill in their reps with data. Adding the constructors that take `boolean b` means we have to declare the empty constructors explicitly. These do-nothing constructors are actually bad pattern: they don't assign any values to the rep, and don't establish any invariants.

Most importantly, this code *breaks the abstraction barrier* as we have to know the concrete implementation class of `MyString` which is `SimpleMyString`. Because interfaces in Java cannot contain constructors, they must directly call one of the concrete class’ constructors. The spec of that constructor won’t appear anywhere in the interface, so there’s no static guarantee that different implementations will provide the same constructors.

To solve this, we use static methods to implement the creator operation `valueOf` as a static factory method in the interface:

```java
public interface MyString { 

    /** @param b a boolean value
     *  @return string representation of b, either "true" or "false" */
    public static MyString valueOf(boolean b) {
        return new FastMyString(b);
    }

    // ...
```

which allows clients to use ADT without breaking the abstraction barrier:

```java
MyString s = MyString.valueOf(true);
System.out.println("The first character is: " + s.charAt(0));
```

## Example `Set`

`Set` is the ADT of finite elements of another type `E`. Note that we use type parameter `<E>` as a placeholder that we'll define `E` later.

```java
/** A mutable set.
 *  @param <E> type of elements in the set */
public interface Set<E> {
```

`Set` is an example of a *generic* type, a type whose specifications are in terms of a placeholder type to be filled in later. So `Set<E>` can stand for `Set<Integer>` or `Set<String>` and so on.

```java
    // example creator operation
    /** Make an empty set.
     *  @param <E> type of elements in the set
     *  @return a new set instance, initially empty */
    public static <E> Set<E> make() { ... }
```
The `make` operation is a static factory method and clients will write `Set<String> stringsSet = Set.make();` and the compile will understand `E` is actually `Sting` here.

```java
    // example observer operations

    /** Get size of the set.
     *  @return the number of elements in this set */
    public int size();

    /** Test for membership.
     *  @param e an element
     *  @return true iff this set contains e */
    public boolean contains(E e);

    // example mutator operations

    /** Modifies this set by adding e to the set.
     *  @param e element to add */
    public void add(E e);

    /** Modifies this set by removing e, if found.
     *  If e is not found in the set, has no effect.
     *  @param e element to remove */
    public void remove(E e);
```
We write the specs for mutator and observer methods in terms of `E`, so we're working with an abstract model of sets.

## Why Interfaces?

Interfaces basically abstract away the implementations and allow for multiple valid implementations. These formalize the idea of an ADT defined as a set of operations. If we use static factory methods rather than explicit constructors, we can switch implementations easily without the client changing code/knowing which implementation we're using.

- **Documentation for both the compiler and for humans** . Not only does an interface help the compiler catch ADT implementation bugs, but it is also much more useful for a human to read than the code for a concrete implementation. Such an implementation intersperses ADT-level types and specs with implementation details - easier to digest
- **Allowing performance trade-offs** . Different implementations of the ADT can provide methods with very different performance characteristics. Different applications may work better with different choices, but we would like to code these applications in a way that is representation-independent.
- **Optional methods** . List from the Java standard library marks all mutator methods as optional. By building an implementation that does not support these methods, we can provide immutable lists. Some operations are hard to implement with good enough performance on immutable lists, so we want mutable implementations, too. Code that doesn’t call mutators can be written to work automatically with either kind of list.
- **Methods with intentionally underdetermined specifications** . An ADT for finite sets could leave unspecified the element order one gets when converting to a list. Some implementations might use slower method implementations that manage to keep the set representation in some sorted order, allowing quick conversion to a sorted list. Other implementations might make many methods faster by not bothering to support conversion to sorted lists.
- **Multiple views of one class** . A Java class may implement multiple methods. For instance, a user interface widget displaying a drop-down list is natural to view as both a widget and a list. The class for this widget could implement both interfaces. In other words, we don’t implement an ADT multiple times just because we are choosing different data structures; we may make multiple implementations because many different sorts of objects may also be seen as special cases of the ADT, among other useful perspectives.