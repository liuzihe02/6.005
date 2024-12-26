# Map, Filter, and Reduce

We treat functions as first-class values that we pass around and manipulate

## Example

Suppose we’re given the following problem: write a method that finds the words in the Java files in your project. Breaking it down:

- find all the files in the project, by scanning recursively from the project’s root folder
- restrict them to files with a particular suffix, in this case .java
- open each file and read it in line-by-line
- break each line into words

The recursive portion may look like:

```java
/**
 * Find all the files in the filesystem subtree rooted at folder.
 * @param folder root of subtree, requires folder.isDirectory() == true
 * @return list of all ordinary files (not folders) that have folder as
 *         their ancestor
 */
public static List<File> allFilesIn(File folder) {
    List<File> files = new ArrayList<>();
    for (File f : folder.listFiles()) {
        if (f.isDirectory()) {
            files.addAll(allFilesIn(f));
        } else if (f.isFile()) {
            files.add(f);
        }
    }
    return files;
}
```

## Map/Filter/Reduce Abstraction

Instead of treating individual elements, we treat the whole sequence of elements as a unit. No `for` statements, no `if` statements. No need temporary variable names.

> A sequence is an ordered collection of items

### Map

Map applies a unary function to each element in the sequence.

```python
>>> from math import sqrt
>>> map(sqrt, [1, 4, 9, 16])
[1.0, 2.0, 3.0, 4.0]
>>> map(str.lower, ['A', 'b', 'C'])
['a', 'b', 'c']
```

When you only need the function in one place, however — which often comes up in programming with functions — it’s more convenient to use a `lambda` expression :

```python
>>> (lambda k: 2**k)(5)
32
>>> map(lambda k: 2**k, [1, 2, 3, 4])
[2, 4, 8, 16]
```

Map is useful even if you don't care about the return value of the function. We can map a mutator operation on objects:

```python
map(IOBase.close, streams) # closes each stream on the list
map(Thread.join, threads)  # waits for each thread to finish
```

### Filter

Filter tests each element with a condition. Elements that satisfy the condition are kept; those that don’t are removed. A new list is returned; filter doesn’t modify its input list.

```python
>>> filter(str.isalpha, ['x', 'y', '2', '3', 'a']) 
['x', 'y', 'a']
```

### Reduce

Reduce combines the elements of the sequence together, using a binary function. In addition to the function and the sequence, it also takes an initial value that initializes the reduction (it ends up being the return value if the list is empty)

`reduce(f, list, init)` combines the elements of the list from left to right, as follows:

```
result_0 = init
result_1 = f(result_0 , list[0])
result_2 = f(result_1 , list[1])
...
result_n = f(result_n-1 , list[n-1])
```

```python
>>> reduce(lambda x,y: x+y, [1, 2, 3], 0)
6
```

The return type of reduce need not match the type of the list elements.

```python
#gluing a sequence into a string
>>> reduce(lambda s,x: s+str(x), [1, 2, 3, 4], '') 
'1234'
#flatten nested sublists into a single list
>>> reduce(operator.concat, [[1, 2], [3, 4], [], [5]], [])
[1, 2, 3, 4, 5]
```

#### Databases

Now let’s look at a typical database query example. Suppose we have a database about digital cameras, in which each object is of type `Camera` with observer methods for its properties ( `brand()` , `pixels()` ,  etc.). The whole database is in a list called `cameras` . Then we can describe queries on this database using `map/filter/reduce`:

```python
#What's the highest resolution Nikon sells?**
reduce(max, map(Camera.pixels, filter(lambda c: c.brand() == "Nikon", cameras)))
```

Relational databases use the `map/filter/reduce` paradigm (where it’s called *project/select/aggregate*). A typical SQL query looks like this:

```SQL
select max(pixels) from cameras where brand = "Nikon"
```

### Benefits of Abstracting Out Control

Map/filter/reduce can often make code shorter and simpler, and allow the programmer to focus on the heart of the computation rather than on the details of loops, branches, and control flow.

By arranging our program in terms of map, filter, and reduce, and using immutable datatypes and pure functions (functions that do not mutate data) as much as possible, we’ve created more opportunities for safe concurrency. 

Maps and filters using pure functions over immutable datatypes are instantly parallelizable — invocations of the function on different elements of the sequence can be run in different threads, on different processors, even on different machines, and the result will still be the same. MapReduce is a pattern for parallelizing large computations in this way.

## First Class Functions in Java

In Java, the only first-class values are primitive values (ints, booleans, characters, etc.) and object references. But objects can carry functions with them, in the form of methods - this is a functional object.

### Lambda Expressions in Java
Instead of writing

```java
// {} is an anonymous class that implements the runnable interface
// this only has one method which is run
new Thread(new Runnable() {
    public void run() {
        System.out.println("Hello!");
    }
}).start();
```

we can write

```java
new Thread(() -> {
    System.out.println("Hello");
}).start();
```

Note that lambda expressions in Java take the form of
```
(parameter list) -> { function body }
```
like
```java
(int x, int y) -> { return x + y; }  // Takes two integers, returns their sum
```

### Functional Interfaces in Java

Java provides soem standard functional interfaces to treat functions as first class objects:

- `Function<T,R>` represents unary functions from `T to R`
- `BiFunction<T,U,R>` represents binary functions from `T × U to R`
- `Predicate<T>` represents functions from `T` to boolean

A map exists like:
```java
/**
 * Apply a function to every element of a list.
 * @param f function to apply
 * @param list list to iterate over
 * @return [f(list[0]), f(list[1]), ..., f(list[n-1])]
 */
public static <T,R> List<R> map(Function<T,R> f, List<T> list) {
    List<R> result = new ArrayList<>();
    for (T t : list) {
        result.add(f.apply(t));
    }
    return result;
}
```

```java
// anonymous classes like this one are effectively lambda expressions
Function<String,String> toLowerCase = new Function<>() {
    public String apply(String s) { return s.toLowerCase(); }
};
map(toLowerCase, Arrays.asList(new String[] {"A", "b", "C"}));
```

### Java streams

Collection types like `List` and `Set` provide a `stream()` operation that returns a `Stream` for the collection, and there’s an `Arrays.stream` function for creating a `Stream` from an array.

```java
public class Words {
    static Stream<File> allFilesIn(File folder) {
        File[] children = folder.listFiles();
        Stream<File> descendants = Arrays.stream(children)
                                         .filter(File::isDirectory)
                                         .flatMap(Words::allFilesIn);
        return Stream.concat(descendants,
                             Arrays.stream(children).filter(File::isFile));
    }
}
```