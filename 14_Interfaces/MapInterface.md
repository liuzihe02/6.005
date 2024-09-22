# The Map Interface

A `Map` is an object that maps keys to values, with no duplicate keys allowed. It models a mathematical function abstraction.

## Basic Operations

- `V put(K key, V value)`: Associates value with key
- `V get(Object key)`: Returns value associated with key
- `V remove(Object key)`: Removes mapping for key
- `boolean containsKey(Object key)`: Returns true if map contains key
- `boolean containsValue(Object value)`: Returns true if map contains value
- `int size()`: Returns number of key-value mappings
- `boolean isEmpty()`: Returns true if map contains no mappings

Like the `Set` and `List` interfaces, Map strengthens the requirements on the `equals` and `hashCode` methods so that two Map objects can be compared for logical equality without regard to their implementation types. Two Map instances are equal if they represent the same key-value mappings.

By convention, all general-purpose `Map` implementations provide constructors that take a `Map` object and initialize the new `Map` to contain all the existing key-value mappings in the specified Map. This standard Map conversion constructor is entirely analogous to the standard `Collection` constructor: It allows the caller to create a `Map` of a desired implementation type that initially contains all of the mappings in another `Map`, regardless of the other `Map`'s implementation type. For example, suppose you have a `Map`, named `m`, the following quickly builds another map implementation containg all key-value pairs in `m`

```java
Map<K, V> copy = new HashMap<K, V>(m);
```

## Bulk Operations

- `void putAll(Map<? extends K,? extends V> m)`: Copies all mappings from m to this map
- `void clear()`: Removes all mappings

## Collection Views

Note that `Map` is not under `Collection`. However, we have some view methods that allow us to view it as a `Collection`:

- `Set<K> keySet()`: Returns `Set` view of keys
- `Collection<V> values()`: Returns `Collection` view of values
- `Set<Map.Entry<K,V>> entrySet()`: Returns `Set` view of key-value mappings. The `Map` interface provides a small nested interface called `Map.Entry`, the type of the elements in this `Set`.

The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa.

With all three `Collection` views, calling an `Iterator`'s `remove` operation removes the associated entry from the backing `Map`.

> This `Iterator` method is the **only** safe way to remove elements. Modifying the map while iteration over the collection is in progress is undefined.

With the `entrySet` view, it is also possible to change the value associated with a key by calling a `Map.Entry`'s `setValue` method during iteration. Note that these are the only safe ways to modify a Map during iteration. The collection supports element removal, which removes the corresponding mapping from the map, via the `Iterator.remove`, `Collection.remove`, `removeAll`, `retainAll` and `clear` operations. It does not support the `add` or `addAll` operations.

## Iteration

The `Collection` views provide the **only** way to iterate over a map:

```java
for (Map.Entry<KeyType, ValType> entry : map.entrySet()) {
    KeyType key = entry.getKey();
    ValType value = entry.getValue();
    // Process key and value
}
```

and with an `Iterator`:

```java
// Filter a map based on some 
// property of its keys.
for (Iterator<Type> it = m.keySet().iterator(); it.hasNext(); ){
    if (it.next().isBogus()){
        it.remove();
    }
}
```

## Implementations

The three general-purpose Map implementations are `HashMap`, `TreeMap` and `LinkedHashMap`. If you need `SortedMap` operations or key-ordered `Collection`-view iteration, use `TreeMap`; if you want maximum speed and don't care about iteration order, use `HashMap`; if you want near-`HashMap` performance and insertion-order iteration, use `LinkedHashMap`. In this respect, the situation for `Map` is analogous to `Set`.

## Frequency Table Example

```java
import java.util.*;

public class Freq {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<String, Integer>();

        // Initialize frequency table from command line
        for (String a : args) {
            //will return the value associated if the key is mapped, else null if no mapping contained
            Integer freq = m.get(a);
            m.put(a, (freq == null) ? 1 : freq + 1);
        }

        System.out.println(m.size() + " distinct words:");
        System.out.println(m);
    }
}
```

The only tricky thing about this program is the second argument of the `put` statement. That argument is a conditional expression that has the effect of setting the frequency to one if the word has never been seen before or one more than its current value if the word has already been seen.

Running `java Freq if it is to be it is up to me to delegate` will produce

```
8 distinct words:
{to=3, delegate=1, be=1, it=2, up=1, if=1, me=1, is=2}
```

If we were to use `TreeMap` instead of `HashMap`, we'd instead get alphabetical order:

```
8 distinct words:
{be=1, delegate=1, if=1, is=2, it=2, me=1, to=3, up=1}
```

If we used `LinkedHashMap` instead, we'd get insertion order, hence printing out the strings in the order that they first appear on command line:

```
8 distinct words:
{be=1, delegate=1, if=1, is=2, it=2, me=1, to=3, up=1}
```