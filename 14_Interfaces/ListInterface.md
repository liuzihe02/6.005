# List Interface

A `List` is an ordered `Collection` (sometimes called a sequence). Lists may contain duplicate elements. In addition to the operations inherited from `Collection`, the List interface includes the operations:

- `Positional access` — manipulates elements based on their numerical position in the list. This includes methods such as `get`, `set`, `add`, `addAll`, and `remove`
- `Search` — searches for a specified object in the list and returns its numerical position. Search methods include `indexOf` and `lastIndexOf`
- `Iteration` — extends `Iterator` semantics to take advantage of the list's sequential nature, using `listIterator`
- `Range-view` — The `sublist` method performs arbitrary range operations on the list

The two general purpose `List` implementations are `ArrayList` which is usually the better performing implementation and `LinkedList` which is more efficient for frequent insertions/deletions at list start/middle.

## Collection Operations

All of `Collection` and the above additional operations.

- `remove` always removes the *first* occurence of the element
- `add` and `addAll` always appends the new elements to the *end* of the list
- like `Set`, `List` strengthens the `equals` and `hashCode` methods so two `List` objects can be compared for logical equality regardless of implementation. Two `List` are equal if they contain the same elements in the same order

## Positional Access and Search

The basic positional access operations are:
- `E get(int index)` returns element at specified position
- `E set(int index, E element)` replaces element at that position with the specified element, returns the element previously at specified position
- `void add(int index, E element)` inserts element at that specified position, by shifting stuff to the right by one place
- `E remove(int index)` removes element at position from this list, and return the element removed
  - `boolean remove(Object o)` removes the first occurence of the specified element from this list. returns `true` is list contained the specified element
- `int indexOf(Object o)` returns the index of the first occurence of the specified element, or `-1` if not found
- `int lastIndexOf(Object o)` returns the index of the last occurence of specified element, or `-1` if not found

- `boolean addAll(Collection<? extends E> c)` adds all elements in `c` to the end
  - `addAll(int index, Collection<? extends E> c)` adds elements of `c` to the specified index

Here's how we swap two values in a `List`:
```java
public static <E> void swap(List<E> a, int i, int j) {
    E tmp = a.get(i);
    a.set(i, a.get(j));
    a.set(j, tmp);
}
```

## Iterators

`ListIterator` is a powerful iterator for `List` that allows bi-directional travel and list modification.

Here's how we iterate backwards:
```java
// Backward iteration
for (ListIterator<Type> it = list.listIterator(list.size()); it.hasPrevious(); ) {
    Type t = it.previous();
    // Process t
}
```

We use `next()` and `previous()` to move cursor and return elements, while using `hasNext()` and `hasPrevious()` to check for more elments. We also use `nextIndex()` and `previousIndex()` to get indexes of next/previous element.

The `Iterator` interface provides the `remove` operation to remove the last element returned by `next` from the Collection. For `ListIterator`, this operation removes the last element returned by `next` or previous. The `ListIterator` interface provides two additional operations to modify the list — `set` and `add`. The `set` method overwrites the last element returned by `next` or `previous` with the specified element.

The following uses set to replace all occurrences of one specified value with another.
```java
public static <E> void replace(List<E> list, E val, E newVal) {
    for (ListIterator<E> it = list.listIterator(); it.hasNext(); )
        if (val == null ? it.next() == null : val.equals(it.next()))
            it.set(newVal);
}
```
Note the equality test between `val` and `it.next`. You need to special-case a `val` value of `null` to prevent a `NullPointerException`.

The `add` method inserts a new element into the list immediately *before* the current cursor position. The following replaces all occurrences of a specified value with the sequence of values contained in the specified list.

```java
public static <E> 
    void replace(List<E> list, E val, List<? extends E> newVals) {
    for (ListIterator<E> it = list.listIterator(); it.hasNext(); ){
        if (val == null ? it.next() == null : val.equals(it.next())) {
            // removes the elem right before cursor
            it.remove();
            for (E e : newVals)
            // keep adding to right before the cursor
                it.add(e);
        }
    }
}
```

## Range-View Operation

The range-view operation, `subList(int fromIndex, int toIndex)`, returns a `List` view of the portion of this list whose indices range from `fromIndex`, inclusive, to `toIndex`, exclusive. As the term *view* implies, the returned `List` is backed up by the original `List`, so changes in the view are reflected in the original.

We can easily do operations on a sublist like
```java
list.subList(fromIndex, toIndex).clear();
//Note that the preceding idioms return the index of the found element in the subList, not the index in the backing List.
int i = list.subList(fromIndex, toIndex).indexOf(o);
```

## Other Operations

The below are static utility methods part of the `Collections` class.

> the `Collection` *interface* defines a contract for collection implementations. The `Collections` *class* is a utility class containing static methods for operating on collections

- `sort` — sorts a `List` using a merge sort algorithm, which provides a fast, stable sort. (A stable sort is one that does not reorder equal elements.)
- `shuffle` — randomly permutes the elements in a `List`.
- `reverse` — reverses the order of the elements in a `List`.
- `rotate` — rotates all the elements in a `List` by a specified distance.
- `swap` — swaps the elements at specified positions in a `List`.
- `replaceAll` — replaces all occurrences of one specified value with another.
- `fill` — overwrites every element in a `List` with the specified value.
- `copy` — copies the source `List` into the destination `List`.
- `binarySearch` — searches for an element in an ordered `List` using the binary search algorithm.
- `indexOfSubList` — returns the index of the first sublist of one `List` that is equal to another.
- `lastIndexOfSubList` — returns the index of the last sublist of one `List` that is equal to another.

## List Implementations

Usually, `ArrayList` is offers `O(1)` positional access and is fast. If you often add elements to beginning of list or iterator to delete/add elements from the middle, consider `LinkedList`. These specific operations are `O(1)` in `LinkedList` and `O(n)` in `ArrayList`. However, you pay a big price in performance. Positional access requires `O(n)` in a `LinkedList` and `O(1)` in an `ArrayList`.

Furthermore, the constant factor for `LinkedList` is much worse. If you think you want to use a `LinkedList`, measure the performance of your application with both `LinkedList` and `ArrayList` before making your choice; `ArrayList` is usually faster.

`ArrayList` has one tuning parameter — the initial capacity, which refers to the number of elements the `ArrayList` can hold before it has to grow. `LinkedList` has no tuning parameters and seven optional operations, one of which is `clone`. The other six are `addFirst`, `getFirst`, `removeFirst`, `addLast`, `getLast`, and `removeLast`. `LinkedList` also implements the `Queue` interface.