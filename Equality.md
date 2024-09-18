# Equality

The abstraction function (mapping from concrete representation to abstract value) is integral to our definition of equality of objects. The takeaway here is that `==` and `equals` can have very different behaviour on different objects and languages; this can introduce many subtle bugs!

## Ways to Regard Equality

**Using an abstraction function** An abstraction function `f:R -> A` maps concrete reps of data types to their corresponding abstract values. We say that `a` equals `b` if and only if `f(a)=f(b)`. Note that the exact representations `a` and `b` can be different

**Using observation** "Observation" means calling operations on the objects. Two objects are equal if and only if they cannot be distinguished by calling any operations of the ADT

## `==` vs `equals()`

- `==` operator compares references- **referential equality**. Two objects are equal if they point to the same storage address in memory.
- `equals()` operation compares the contents of the objects - **object equality**. Two objects are equal if they contain the same contents, hence it is our responsibility to define what "contents" means and its implementation. The equals operation has to be custom defined for every ADT.

| Language | Reference Equality | Object Equality |
|---------|---------------------|----------|
|Java|`==`|`equals()`|
|Python|`is`|`==`|

> Note that `==` is referential equality in Java, while `is` is referential equality in Python
>
> `equals()` tests object equality in Java, and `==` tests object equality in Python. This is flipped, and is a little confusing!

## Equality of Immutable Types

The default implementation of `equals()` is defined by the `Object` class, and is exactly the same as referential equality:

```java
public class Object {
    ...
    public boolean equals(Object that) {
        return this == that;
    }
}
```

Hence we need to *override* this method with our own:

```java
public class Duration {
    ...   
    // Problematic definition of equals()
    public boolean equals(Duration that) {
        //if two Duration objects have equal length, then they basically are equal by the operation method
        return this.getLength() == that.getLength();        
    }
}
```

However, this causes some problems - the following code doesn't work!

```java
Duration d1 = new Duration (1, 2);
Duration d2 = new Duration (1, 2);
Object o2 = d2;
d1.equals(d2) → true
d1.equals(o2) → false
```

Even though `d2` and `o2` refer to the same object in memory, there is still different results for `equals()`.

We actually have two `equals()` method in this class, an implicit `equals(Object)` inherited from `Object`, and the new `equals(Duration)` In other words, we have no actually overwritten our old `equals()` method, as our method signature is different. We've simply added a new method:

```java
public class Duration extends Object {
    //these are two distinct methods with different method signatures!

    // explicit method that we declared:
    public boolean equals (Duration that) {
        //we can compare literals with ==
        return this.getLength() == that.getLength();
    }
    // implicit method inherited from Object:
    public boolean equals (Object that) {
        return this == that;
    }
}
```

To avoid this common error, we use the annotation `@Override`, where the compiler actually checks a method with the same signature actually exists in the superclass, and raise an error if it doesn't exist"

```java
@Override
///use Object here to override the default implementation
public boolean equals (Object thatObject) {
    // check if of right type
    if (!(thatObject instanceof Duration)) return false;
    // type casting
    Duration thatDuration = (Duration) thatObject;
    return this.getLength() == thatDuration.getLength();
}
```
> The typecasting using `(Duration)` is neccessary. The compiler thinks that `thatObject` is of type `Object`, and won't allow you to assign `Object` to `Duration` without explicit typecasting. This is a common pattern after using `instanceof`

### `instanceof`

Using `instanceof` is dynamic checking to test if an object is an instance of a particular type. We don't like dynamic checking - hence **do not implement `instanceof` anywhere except in `equals()` method**. Do not inspect object's runtime types.

## The Object Contract

The specification of the `Object` class is so important it is termed the *Object contract*. When you override the `Object`'s `equals` method, you must adhere to the object contract:
- `equals` must define an equivalence relation
- `equals` must be consistent: repeated calls with same args to same method must yield same results
- for a non-null reference `x`, `x.equals(null)` must return `false`
- `hashCode` must produce the same results for two objects deemed equal by the `equals` method

### Breaking the hash condition

A hash table is a representation for mapping: an ADT that maps keys to values. They offer `O(1)` lookup. Key's don't have to be ordered or have any property, except to implement `equals` and `hashCode`.

A hash table is an array initialized to the size of expected number of elements. When a key/value is present, we first compute the hashcode of the key, then convert it to an index in the array range (like via `%` division). The `(key,value)` pair is then inserted at that index.

The rep invariant of hash tables is that keys are stored in the slots determined by their hash codes.

Hashcodes are designed so that keys are spread over slots. However, occasionally a *collision* occurs, where 2 keys are placed at the same index. In this scenario, we add the `(key,value)` pair to a *list* in the slot of the hash table array. The hash table stores a list of `(key,value)` pairs rather than a single value - this pair is called a *hash bucket*.

On insertion, we add the pair to the list in the array slot determined by the hash code. For lookup, we hash the key, find the right slot, then examine each pair one by one until a pair is found whose key equals the query key.

Now it should be clear why the `Object` contract requires equal objects to have the same hashcode. If two equal objects had distinct hashcodes, they might be placed in different slots. So if you attempt to lookup a value using a key equal to the one with which it was inserted, the lookup may fail. *Don't quite understand*

`Object`'s default `hashCode()` implementation is the same as `equals()`:
```java
public class Object {
  ...
  public boolean equals(Object that) { return this == that; }
  public int hashCode() { return /* the memory address of this */; }
}
```
This breaks the object contract:
```java
Duration d1 = new Duration(1, 2);
Duration d2 = new Duration(1, 2);
d1.equals(d2) → true
// two objects are equal yet have different hash codes!
d1.hashCode() → 2392
d2.hashCode() → 4823
```

**Computing hashcodes**

A simple and dreastic way is ensure object contract always holds is for all keys is have the same hash code. This has horrible performance as every lookup becomes a linear list. A more reasonable way is to compute the hashcode of every relevant component of the object used in determining equality, then combining all these hashcodes using a few math operations.

If you don't override `hashCode`, you get the default one from `Object`, which is always based on memory address. This default `hashCode` will break the object contract.

> **Always override `hashCode` when you override `equals`**

## Equality of Mutable Types

Two objects are equal when they cannot be distinguished by observation. With mutable objects, we can interpret equality in two ways:

- **Observational equality**: when the two objects cannot be distinguished by any observation that doesn't *change the state* of objects (no mutation). Tests whewther two objects "look" the same, without modifying them.
- **Behavioural equality**: when two objects cannot be distinguished by **any** observation, even state changes. Tests whether two objects "behave" the same, even after mutation.

For immutable objects, observational and behavioural equality are identical as there are no mutator methods. For mutable objects, Java uses only observational equality for most mutable data types, especially collections. If two distinct `List` objects contain the same sequence of elements, then `equals()` is `true`. However, when `equals()` and `hashCode()` can be affected by mutation in default Java, we break the rep invariant of a hash table that uses that object as a key. This is problematic.

For mutable objects, **`equals()` should implement behavioural equality**. This means two refernces should be `equals()` if and only if they are **aliases for the same object**. Hence mutable objects should inherit `equals()` and `hashCode()` from `Object`. 

> In Python, `==` uses object equality for built-in types, for both mutable and immutable objects. However, for custom defined objects, `==` by default implements reference equality and should be overwritten with a custom implementation if you want object equality.

If we want to measure observational equality (the state), then we should define a new method like `similar()`.

## Final Rules

**For immutable types**:
- `equals()` should compare abstract values - it should test behavioural equality via **object equality**, not reference equality
- `hashCode()` should map the abstract value to an integer

So immutable types must override both `equals()` and `hashCode()` from `Object`.

**For mutable types**:
- `equals()` should just compare references - it should test behavioural equality via **reference equality**, not object equality
- `hashCode()` maps reference (not representation/abstract value) to an integer

Hence mutable types should just inherit `equals()` and `hashCode()` from `Object`. However, Java doesn't follow this rule for collections, which creates subtle bugs! Java tests object equality for built-in mutable types like lists and sets.

> For immutable types, consider 2 objects with the same states (even with distinct memory locations) - the abstraction function maps to the same abstract value. Hence they are identical.
> 
> For mutable types, consider 2 objects with the same states currently. Due to mutations, these two states can diverge overtime which would make them distinct. Object equality which only compares states would claim they are equal currently; but they may be distinct in the future. Hence we use reference equality to ensure consistency.

### Primitive Types and their Wrappers

Primitive types like `int` and their wrappers to create objects like `Integer` behave differently.

| Type | `==` Behaviour | `equals()` Behaviour |
|---------|---------------------|----------|
|Primitive|Object equality|NA, primitives dont have methods|
|Object (Wrapper)|Reference equality|Object Equality|

```java
Integer x = new Integer(3);
Integer y = new Integer(3);
x.equals(y) → true
//returns false as different addresses
x==y → false
```
for primitive types, `==` implements object equality as these literals can be compared directly, and are not objects.

```java
(int)x == (int)y → true
```

so you need to be careful when doing autoboxing and unboxing.


