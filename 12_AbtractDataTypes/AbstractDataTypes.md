# Abstract Data Types

## What Abstraction Means

- **Abstraction** - Hiding low level details with simpler, higher-level data
- **Modularity** - Dividing system into individual components, each of which can be designed, implemented, tested, and reused separately from the entire system
- **Encapsulation** - Building walls around a module (hard shell or capsule), so the module is responsible for its own internal behaviour and other bugs elsewhere in the system doesn't affect the module
- **Information Hiding** - Hiding details of module's implementation from rest of the system, so details can be easily changed without affecting rest of system
- **Separation of Concerns** Each feature is the responsibility of one module; not shared by multiple modules

A type is characterized by the *operations* that can be performed on it, rather than its internal structure (implementation). Hence we focus on operations rather than internal representation, and enables information hiding and encapsulation. This allows changing the internal representation without changing client code. Some types like (Integer and Boolean) are built-in while some types are user-defined.

## Classifying Types and their Operations

Types (whether built-in or user defined) are classified as mutable or immutable.
- The objects of a mutable type can be changed: they provide operations which when executed cause the results of other operations on the *same* object to give *different* results
  - So `Date` is mutable, because you can call `setMonth` and observe the change with the `getMonth` operation
  - But `String` is immutable, because its operations create new `String` objects rather than changing existing ones.
- Some types provide both mutable forms and immutable forms like `StringBuilder` and `String`

Operations of an abstract type are:
- **Creators** create new objects of that type; it can take objects as arguments but not objects of the type being constructed
  - `t* -> T`
- **Producers** create new objects from old objects of that type
  - `T+, t* -> T`
- **Observers** take objects of that abstract type and return objects of a different type
  - `T+, t* -> t`
- **Mutators** change objects itself
  - `T+, t* -> void|t|T`

We can summarize these distinctions schematically. Each `T` is the abstract type itself; each `t` is some other type. The `+` marker indicates that the type may occur *one* or more times, and the `*` marker indicates that it occurs *zero* or more times. `|` indicates or.

A creator operation is often implemented as a **constructor** like `new ArrayList()`. A creator can also be a static method like `Arrays.asList()` - this is a **factory method**.

Mutators are often signaled by a `void` return type. But not all mutators return void; `Set.add()` modifies the set and returns a Boolean indicating if the set was changed or not.

> Classes, Interfaces, Primitive data types are all types. Methods are not types! Methods are members of classes or interfaces.

### ADT Examples

`int` is a primitive integer type, hence immutable:
- creators: numeric literals `0`,`1`
- producers: arithmetic operators `*`,`+`
- observers: comparison operators `==`,`<`
- mutators: none

`String` is another immutable type:
- creators: `String` constructors
- producers: `concat`, `substring`
- observers: `length`,`charAt`
- mutators: none

`List` which is a mutable type. Not that `List` is also an interface, so other classes provide the actual implementation
- creators: `Arraylist` and `LinkedList` constructors
- producers: `Collections.unmodifiableList`
- observers: `size`,`get`
- mutators: `add`,`remove`,`Collections.sort`


| Concept | Java Implementation | Examples |
|---------|---------------------|----------|
|ADT|Single Class|`String`|
||Interfaces + Classes|`List` + `ArrayList`|
| Creator | Constructor | `ArrayList()` |
| | Static method | `Collections.singletonList()`, `Arrays.asList()` |
| | Constant | `BigInteger.ZERO` |
| Observer | Instance method | `List.get()` |
| | Static method | `Collections.max()` |
| Producer | Instance method | `String.trim()` |
| | Static method | `Collections.unmodifiableList()` |
| Mutator | Instance method | `List.add()` |
| | Static method | `Collections.copy()` |
| Representation | private fields | - |


## Designing an ADT

- Better to have few,simple operations that can be combined powerfully rather than many complex operations
- Each operation should have coherent behaviour rather than handling many special cases
  - `sum` operation doesnt make sense for `List` as it'll have to handle `int` and `String` types
- Don't mix generic and domain specific features
  - `Deck` type representing playing cards shouldn't have `add` method accepting `int` and `String` objects. A generic type like `List` shouldnt have domain-specific methods like `dealCards`

## Representation Independence

Most crucially, ADTs need to be representation independent: use of ADT is independent of how internals are implmented. Changes in internal representation have no effect on input-output behaviour of ADT.

These public operations of our type and their specifications are the only information that a client of this type is allowed to know. Because the clients only depend on specs of the public methods, not on private fields, we can easily make changes without having to inspect and change client code. (Code using our type remain exactly the same)

## Testing ADTs

We build a test suite for an ADT by creating tests for each of its operations. The only way to test creators, producers, and mutators is by calling observers on the created objects. The only way to test observers is by creating objects for them to observe. Hence we each test case must mix different kinds of operations.

Here are the specs for a string ADT of only `"true"` and `"false"`:
```java
/** MyString represents an immutable sequence of characters. */
public class MyString { 

    //////////////////// Example of a creator operation ///////////////
    /** @param b a boolean value
     *  @return string representation of b, either "true" or "false" */
    public static MyString valueOf(boolean b) { ... }

    //////////////////// Examples of observer operations ///////////////
    /** @return number of characters in this string */
    public int length() { ... }

    /** @param i character position (requires 0 <= i < string length)
     *  @return character at position i */
    public char charAt(int i) { ... }

    //////////////////// Example of a producer operation ///////////////    
    /** Get the substring between start (inclusive) and end (exclusive).
     *  @param start starting index
     *  @param end ending index.  Requires 0 <= start <= end <= string length.
     *  @return string consisting of charAt(start)...charAt(end-1) */
    public MyString substring(int start, int end) { ... }
}
```

Here's how we might partition the input space:
```java
// testing strategy for each operation of MyString:
//
// valueOf():
//    true, false
// length(): 
//    string len = 0, 1, n
//    string = produced by valueOf(), produced by substring()
// charAt(): 
//    string len = 1, n
//    i = 0, middle, len-1
//    string = produced by valueOf(), produced by substring()
// substring():
//    string len = 0, 1, n
//    start = 0, middle, len
//    end = 0, middle, len
//    end-start = 0, n
//    string = produced by valueOf(), produced by substring()
```

Here's a compact test suite covering all of these partitions:
```java
@Test public void testValueOfTrue() {
    MyString s = MyString.valueOf(true);
    assertEquals(4, s.length());
    assertEquals('t', s.charAt(0));
    assertEquals('r', s.charAt(1));
    assertEquals('u', s.charAt(2));
    assertEquals('e', s.charAt(3));
}

@Test public void testValueOfFalse() {
    MyString s = MyString.valueOf(false);
    assertEquals(5, s.length());
    assertEquals('f', s.charAt(0));
    assertEquals('a', s.charAt(1));
    assertEquals('l', s.charAt(2));
    assertEquals('s', s.charAt(3));
    assertEquals('e', s.charAt(4));
}

@Test public void testEndSubstring() {
    MyString s = MyString.valueOf(true).substring(2, 4);
    assertEquals(2, s.length());
    assertEquals('u', s.charAt(0));
    assertEquals('e', s.charAt(1));
}

@Test public void testMiddleSubstring() {
    MyString s = MyString.valueOf(false).substring(1, 2);
    assertEquals(1, s.length());
    assertEquals('a', s.charAt(0));
}

@Test public void testSubstringIsWholeString() {
    MyString s = MyString.valueOf(false).substring(0, 5);
    assertEquals(5, s.length());
    assertEquals('f', s.charAt(0));
    assertEquals('a', s.charAt(1));
    assertEquals('l', s.charAt(2));
    assertEquals('s', s.charAt(3));
    assertEquals('e', s.charAt(4));
}

@Test public void testSubstringOfEmptySubstring() {
    MyString s = MyString.valueOf(false).substring(1, 1).substring(0, 0);
    assertEquals(0, s.length());
}
```