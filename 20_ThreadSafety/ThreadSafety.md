# Thread Safety

Race condition: multiple threads sharing the same mutable variable without coordinating what they’re doing.

There are 4 ways to make variable access safe in shared-memory concurrency:
- **Confinement**: Don’t share the variable between threads.
- **Immutability**: Make the shared data immutable.
- **Threadsafe data type**: Encapsulate the shared data in an existing threadsafe data type that does the coordination for you.
- **Synchronization**: Use synchronization to keep the threads from accessing the variable at the same time

A data type or static method is **threadsafe** if it behaves correctly when used from multiple threads, regardless of how those threads are executed, and without demanding additional coordination from the calling code.
- “behaves correctly” means satisfying its specification and preserving its rep invariant;
- “regardless of how threads are executed” means threads might be on multiple processors or timesliced on the same processor;
- “without additional coordination” means that the data type can’t put preconditions on its caller related to timing, like “you can’t call get() while set() is in progress.”

## Strategy 1: Confinement

Avoid races on mutable data by keeping that data confined to a single thread. Don’t give any other threads the ability to read or write the data directly. Confinement tackles the shared-mutable-data cause of a race condition and solves it simply by making the data not shareable.

Local variables are always thread confined. A local variable is stored in the stack, and each thread has its own stack. There may be multiple invocations of a method running at a time (in different threads or even at different levels of a single thread’s stack, if the method is recursive), but each of those invocations has its own private copy of the variable, so the variable itself is confined.

> if the variable is an object reference, make sure there aren't references to it that can be reached from other threads!

```java
public class Factorial {
    /**
     * Computes n! and prints it on standard output.
     * @param n must be >= 0
     */
    private static void computeFact(final int n) {
        BigInteger result = new BigInteger("1");
        for (int i = 1; i <= n; ++i) {
            System.out.println("working on fact " + n);
            result = result.multiply(new BigInteger(String.valueOf(i)));
        }
        System.out.println("fact(" + n + ") = " + result);
    }
    public static void main(String[] args) {
        new Thread(new Runnable() { // create a thread using an
            public void run() {     // anonymous Runnable
                computeFact(99);
            }
        }).start();
        computeFact(100);
    }
}
```

1. When we start the program, we start with one thread running `main`. 
2. `main` then creates a second thread, and start that thread
3. At this point, we have 2 concurrent threads, but the interleaving is unknown. We do know that each execution of `computeFact` is independent as all variables are self contained

> Here, all objects are immutable. If they were mutable, if we needed to check the object was not aliased from other threads

### Avoid Global Variables

Unlike local variables, static variables are not automatically thread confined. If you have static variables in your program, then you have to make an argument that only one thread will ever use them, and you have to document that fact clearly. Better, you should eliminate the static variables entirely.

```java
// This class has a race condition in it.
public class PinballSimulator {
    private static PinballSimulator simulator = null;
    // invariant: there should NEVER BE MORE THAN ONE PinballSimulator
    //            object created
    private PinballSimulator() {
        System.out.println("created a PinballSimulator object");
    }
    // factory method that returns the sole PinballSimulator object that has already been created;
    // creating it if it doesn't exist
    public static PinballSimulator getInstance() {
        if (simulator == null) {
            simulator = new PinballSimulator();
        }
        return simulator;
    }
}
```

Imagine we have two threads, A and B, both wanting to get an instance of `PinballSimulator`. Here's how they could interleave their executions:

- Thread A calls `getInstance()` and checks if `simulator == null`. It is `null`.
- Before Thread A can create a new `PinballSimulator`, it gets interrupted.
- Thread B now calls `getInstance()` and also checks if `simulator == null`. It's still null.
- Thread B creates a new `PinballSimulator` and assigns it to simulator.
- Thread A resumes execution. It doesn't check `simulator == null` again (that's already done), so it creates *another* `PinballSimulator` and assigns it to simulator.

Now we have two `PinballSimulator` objects created, violating the intended invariant. This happens because the check-then-act sequence (if (`simulator == null`) followed by `simulator = new PinballSimulator()`) is not *atomic*. There's a window of vulnerability between these two operations where another thread can interfere.

> This type of race condition is common in what's known as the "Singleton" design pattern when implemented naively in a multithreaded environment.

To fix this, you'd have to specify only a certain thread is allowed to run `getInstance`, but Java won't help you guarantee this.

In general, static variables are very risky for concurrency - especially if they are mutable. Multiple threads could mutate it and cause nasty side consequences.

## Strategy 2: Immutability

Immutability tackles the shared-mutable-data cause of a race condition and solves it simply by making the shared data immutable.

`final` variables have immutable references, so they are safe to access from multiple threads. However, the object referenced may still be mutable - still need to check this.

We've previously said a type is immutable if an object of the type always represents the same abstract value for its entire lifetime. But that actually allows the type the freedom to mutate its rep, as long as those mutations are invisible to clients. Hence we need a stronger definition of an immutable type.

### Stronger definition of Immutability

To be confident an immutable datatype is threadsafe (without locks), we need a stronger definition of immutability:
- no mutator methods
- all fields are `private` and `final`
- no rep exposure
  - fatal to thread safety
- no mutation whatsoever of mutable objects in the rep

Concretely, this means:
1. Don't provide "setter" methods — methods that modify fields or objects
2. Make all fields `final` and `private`.
3. Don't allow subclasses to override methods. The simplest way to do this is to declare the class as final.
4. If the instance fields include references to mutable objects, don't allow those objects to be changed:
   - Don't provide methods that modify the mutable objects.
   - Don't share references to the mutable objects.
   - Never store references to *external*, mutable objects passed to the constructor;
     - if necessary, create copies of these external objects, and store references to the copies.
     - Similarly, create copies of your internal mutable objects when necessary to avoid returning the originals in your methods.

## Strategy 3: Using Threadsafe Datatypes

We can use threadsafe datatypes to store shared, mutable data

When a datatype in Java library is threadsafe, its documentation will explcitly state that. It’s become common in the Java API to find two mutable data types that do the same thing, one threadsafe and the other not. Threadsafe data types usually incur a performance penalty compared to an unsafe type
- `StringBuffer` is threadsafe while `StringBuilder` is not
- note that these two types don't share an interface, so is not completely replaceable!

### Threadsafe Collections

The collection interfaces' basic implementations are not threadsafe - `ArrayList` , `HashMap` , and `HashSet` , cannot be used safely from more than one thread.

Java provide a set of wrappers that make these collections mutable and threadsafe. These wrappers effectively make **each method atomic** with respect to the other methods. An atomic action *effectively happens all at once* – it doesn’t interleave its internal operations with those of other actions, and none of the effects of the action are visible to other threads until the entire action is complete, so it never looks partially done.

We can create a synchronized collection like:
```java
//initialize it all in one line, so there are no references to the underlying non-threadsafe object!
private static Map<Integer,Boolean> cache =Collections.synchronizedMap(new HashMap<>());
```

#### Wrappers

Each of the six core collection interfaces — `Collection`, `Set`, `List`, `Map`, `SortedSet`, and `SortedMap` — has one static factory method.

**Notes**

- **Don't circumvent the wrapper** - Make sure to throw away references to the underlying non-threadsafe collection, and access it only through the synchronized wrapper. That happens automatically in the line of code above, since the new HashMap is passed only to synchronizedMap() and never stored anywhere else.
  - This way we don't store a reference to the backing collection

- **Iterators are still not threadsafe** - Even though methods themselves ( `get()` , `put()` , `add()` , etc.) are now threadsafe (atomic), iterators created from the collection are still not threadsafe.
  - the collection is still mutable by other threads in between each loop
  - we solve this by using locks

```java
Collection<Type> c = Collections.synchronizedCollection(myCollection);
synchronized(c) {
    for (Type e : c)
        foo(e);
}
```

- **Atomic operations aren't enough to prevent races** -  the way that you use the synchronized collection can still produces races. Consider this code, which uses the `check-get` structure:
```java
if ( ! lst.isEmpty()) { String s = lst.get(0); ... }
```
Even if you make `lst` into a synchronized list, this code still may have a race condition, because another thread may remove the element *between* the `isEmpty()` call and the `get()` call.

### Immutable Collections

Java also has unmodifiable collections, which take away the ability to modify the collection. There are two purposes for this:

- To make a collection immutable once it has been built. In this case, it's good practice not to maintain a reference to the backing collection. This absolutely guarantees immutability.
  - Similar to synchronization wrappers, if you have a reference to the underlying collection, you can still modify the underlying object!
- To allow certain clients read-only access to your data structures. You keep a reference to the backing collection but hand out a reference to the wrapper. In this way, clients can look but not modify, while you maintain full access.

Like synchronization wrappers, each of the six core Collection interfaces has one static factory method.
```java
public static <T> Collection<T> unmodifiableCollection(Collection<? extends T> c);
public static <T> Set<T> unmodifiableSet(Set<? extends T> s);
public static <T> List<T> unmodifiableList(List<? extends T> list);
public static <K,V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m);
public static <T> SortedSet<T> unmodifiableSortedSet(SortedSet<? extends T> s);
public static <K,V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, ? extends V> m);
```

### Concurrent Collections

While the threadsafe collections atomize a single method, but not compound operations. So a combination of operations like `check-get` is still not thread-safe. To solve this, we also have `ConcurrentMap` interface.

This is still not completely threadsafe: two threads could check the key doesn't exist and both try putting the value, potentially overwriting each others' work:
```java
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());
// This is NOT atomic
if (!syncMap.containsKey("key")) {
    syncMap.put("key", 1);
}
```

This concurrent collection object is indeed threadsafe:
```java
ConcurrentMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
// This sequence of operations is atomic
concurrentMap.putIfAbsent("key", 1);
```

## How to Make a Safety Argument

A safety argument needs to catalog all the threads that exist in your program, and the data that that they use, and argue which of the four techniques you are using to protect against races for each data object or variable: confinement, immutability, threadsafe data types, or synchronization. When you use the last two, you also need to argue that all accesses to the data are appropriately atomic – that is, that the invariants are not threatened by interleaving. Since interleaving still occurs in the last 2 techniques, we need to justify why this interleaving doesn't cause problems.

We don't usually use confinement for individual datatypes because we'd need to know all the threads and which objects each thread can access. We usually describe confinement for each module.

Example argument:
```java
/** MyString is an immutable data type representing a string of characters. */
public class MyString {
    private final char[] a;
    private final int start;
    private final int len;
    // Rep invariant:
    //    0 <= start <= a.length
    //    0 <= len <= a.length-start
    // Abstraction function:
    //    represents the string of characters a[start],...,a[start+length-1]
    // Thread safety argument:
    //    This class is threadsafe because it's immutable:
    //    - a, start, and len are final
    //    - a points to a mutable char array, which may be shared with other
    //      MyString objects, but they never mutate it
    //    - the array is never exposed to a client
```

### Bad Safety Arguments

#### String Example

```java
/** MyString is a threadsafe mutable string of characters. */
public class MyString {
    private String text;
    // Thread safety argument:
    //   text is an immutable (and hence threadsafe) String,
    //   so this object is also threadsafe
```

String is indeed immutable and threadsafe; but the rep of that string, specifically the text variable, is not immutable. text is not a `final` variable, and in fact it *can’t* be final in this data type, because we need the data type to support insertion and deletion operations. So reads/writes of the text variable itself are not threadsafe. This argument is false.

#### Graph Example
```java
public void addEdge(Node from, Node to) {
    if ( ! edges.containsKey(from)) {
        edges.put(from, Collections.synchronizedSet(new HashSet<>()));
    }
    edges.get(from).add(to);
    nodes.add(from);
    nodes.add(to);
}
```

Even though the threadsafe set and map data types guarantee that their own `add()` and `put()` methods are atomic and noninterfering, they can’t extend that guarantee to *interactions* between the two data structures. So the rep invariant of Graph is not safe from race conditions. Just using immutable and threadsafe-mutable data types is *not sufficient* when the rep invariant depends on relationships between objects in the rep. We fix this with locks.

### Serializability

What we demand from a threadsafe data type is that when clients call its atomic operations concurrently, the results must come from some sequential ordering of the method calls. In this case, clearing and inserting, that means either `clear -followed-by- insert` , or `insert -followed-by- clear` . This property is called *serializability* : for any set of operations executed concurrently, the result (postconditions) must be a result given by some valid sequential ordering of those operations.