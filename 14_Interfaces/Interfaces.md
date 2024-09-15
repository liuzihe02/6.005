# Interfaces

We separate the interface of an abstract data type from its implementation, using Java `interface`. One advantage is that the interface specifies the contract for the client and nothing more. The interface is all a client programmer needs to understand the ADT. The client should need to depend on the ADT's rep.

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

    @Override public int length() { return a.length; }

    @Override public char charAt(int i) { return a[i]; }

    @Override public MyString substring(int start, int end) {
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




