# The Set Interface

A `Set` is a `Collection` that cannot contain duplicate elements. The `Set` interface contains *only* methods inherited from `Collection` with the restriction that duplicate elements are prohibited.

>  `Set` also adds a stronger contract on the behavior of the `equals` and `hashCode` operations, allowing `Set` instances to be compared meaningfully even if their implementation types differ. Two `Set` instances are equal if they contain the same elements.

Three general-purpose `Set` implementations:
- `HashSet`: stores its elements in a hash table, best-performing implementation, makes no guarantees on order of iteration
  -  iteration is linear in the sum of the number of entries and the number of buckets (the capacity).
  -  choosing an initial capacity that's too high can waste both space and time.
  -  choosing an initial capacity that's too low wastes time by copying the data structure each time it's forced to increase its capacity
  -  default capacity is 16
- `TreeSet`: stores elements in red-black tree, orders elements based on values, but substantially slower than `HashSet`
- `LinkedHashSet`: Implemented as a hash table with linked list running through it, orders elements via insertion order

We do

```java
Collection<Type> noDups = new HashSet<Type>(c);
```

to quickly remove all duplicates from a collection

## Basic Operations

Iterating over a set using a `for-each` construct:

```java
public class FindDups {
    public static void main(String[] args) {
        Set<String> s = new HashSet<String>();
        for (String a : args)
               s.add(a);
        System.out.println(s.size() + " distinct words: " + s);
    }
}
```

> Note that we always refer to `Collection` by its interface type `Set` rather than by its implementation type. This is a *strongly* reccommended practice because you have flexibility to merely change the constructor

The other `Set` operations are same as that of `Collection` type.



