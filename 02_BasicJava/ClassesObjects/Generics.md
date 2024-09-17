# Generics

Generics enable types(classes and interfaces) to be parameters when defining classes, interfaces and methods. There is stronger type checking which creates compile time errors before runtime errors, which are more difficult to find.

We also eliminate the need for casting:
```java
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);
```
which will usually produce a compile-time error without explicit type casting. Generics allow:

```java
List<String> list = new ArrayList<String>();
list.add("hello");
String s = list.get(0);   // no cast
```

## Generic Types

A generic class is defined with

```java
class name<Type1, Type2, ..., TypeN> {
    ...
}
```

like
```java
/**
 * Generic version of the Box class.
 * @param <T> the type of the value being boxed
 */
public class Box<T> {
    // T stands for "Type"
    private T t;

    public void set(T t) { this.t = t; }
    public T get() { return t; }
}
```

where the type `T` will be defined later on.

### Naming Conventions

Type parameter names are single, uppercase letters.

- E - Element (used extensively by the Java Collections Framework)
- K - Key
- N - Number
- T - Type
- V - Value
- S,U,V etc. - 2nd, 3rd, 4th types

### Invoking and Instantiating Generic Types

To reference the generic box class, we must perform a generic type invocation which replaces `T` with a concrete value like `Integer`

```java
Box<Integer> integerBox;
```

This doenst actually create a new `Box` object but rather declares that `integerBox` will hold a `Box` of `Integers`. To instantiee the class, we use `new` as usual but with `<Integer>` between class name and `()`

```java
Box<Integer> integerBox = new Box<Integer>();
```

### The Diamond

You can replace the type arguments required to invoke the constructor of a generic class with an empty set of type arguments (`<>`) as long as the compiler can infer, the type arguments. `<>` is informally called the diamond. For example, you can create an instance of Box<Integer> with:
```java
Box<Integer> integerBox = new Box<>();
```

### Multiple Type Parameters

 the generic `OrderedPair` class, which implements the generic `Pair` interface:

```java
public interface Pair<K, V> {
    public K getKey();
    public V getValue();
}
```

```java
public class OrderedPair<K, V> implements Pair<K, V> {

    private K key;
    private V value;

    public OrderedPair(K key, V value) {
	this.key = key;
	this.value = value;
    }

    public K getKey()	{ return key; }
    public V getValue() { return value; }
}
```

can be instantiated like

```java
//declare the interface on the left and the implementation on the right
// we can do this because both interfaces and classes are considered types
Pair<String, Integer> p1 = new OrderedPair<String, Integer>("Even", 8);
Pair<String, String>  p2 = new OrderedPair<String, String>("hello", "world");
```

The diamond `<>` also works as java can infer `K` and `V` types from the left declaration `<String, Integer>`

```java
OrderedPair<String, Integer> p1 = new OrderedPair<>("Even", 8);
OrderedPair<String, String>  p2 = new OrderedPair<>("hello", "world");
```


### Common use cases in `Collection`

`List`

```java
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
for (String name : names) {
    System.out.println(name);
}
```

`Map`

```java
Map<String, Integer> ageMap = new HashMap<>();
ageMap.put("Alice", 30);
ageMap.put("Bob", 25);
for (Map.Entry<String, Integer> entry : ageMap.entrySet()) {
    System.out.println(entry.getKey() + " is " + entry.getValue() + " years old");
}
```

`Set`


```java
Set<Integer> uniqueNumbers = new HashSet<>();
uniqueNumbers.add(1);
uniqueNumbers.add(2);
uniqueNumbers.add(1); // Duplicate, won't be added
System.out.println(uniqueNumbers.size()); // Prints 2
```

`Queue`
```java
Queue<String> queue = new LinkedList<>();
queue.offer("First");
queue.offer("Second");
System.out.println(queue.poll()); // Prints "First"
```

`Deque`
```java
Deque<String> stack = new ArrayDeque<>();
stack.push("Bottom");
stack.push("Top");
System.out.println(stack.pop()); // Prints "Top"
```



## Raw Types

Given

```java
public class Box<T> {
    public void set(T t) { /* 
    ... 
    */ }
}
```

We usually do 

```java
Box<Integer> intBox = new Box<>();
```

Omitting the type argument like something like 
```java
Box rawBox = new Box();
```

is called a raw type of the generic type `Box<T>`. Do not do this.

## Generic Methods

Remember types refer to interfaces and classes. We can also have generic methods. This is similar to declaring a generic type, but the type parameter's scope is limited to the method where it is declared only.

```java
public class Util {
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) &&
               p1.getValue().equals(p2.getValue());
    }
}
```

```java
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
    public K getKey()   { return key; }
    public V getValue() { return value; }
}
```

where the syntax for invoking the method is

```java
Pair<Integer, String> p1 = new Pair<>(1, "apple");
Pair<Integer, String> p2 = new Pair<>(2, "pear");
boolean same = Util.<Integer, String>compare(p1, p2);
```

If we use type inference, we invoke a generic method as an ordinary method, without specifying the type between angle brackets:

```java
Pair<Integer, String> p1 = new Pair<>(1, "apple");
Pair<Integer, String> p2 = new Pair<>(2, "pear");
boolean same = Util.compare(p1, p2);
```
