# Variables

Java supports the following 4 vairables:

- **Instance Variables (Non-Static Fields)** These are fields declared without the `static` keyword. These are also unique to each instance of a class, like the `self.apple` variable in Python.
- **Class Variables (Static Fields)** This is any field declared with the `static` modifier. This means there's only one copy of this variable ever (regardless of however many instances)
- **Local Variables** Similar to how an object stores its state in fields, a method will store its information in local variables. These are located between the opening and close brackets of a method. Only visible to the methods which they are declared, unaccessible by rest of class (only within method scope)
- **Parameters** Parameters are always classified as "variables" and not fields. Passed into class methods.
  
"Fields" are excluding local variables and parameters, while "variables" usually refer to all of the above.

> If the name you choose consists of only one word, spell that word in all lowercase letters. If it consists of more than one word, capitalize the first letter of each subsequent word. The names `gearRatio` and `currentGear` are prime examples of this convention. If your variable stores a constant value, such as `static final int NUM_GEARS = 6`, capitalize every letter and separating subsequent words with the underscore character. By convention, the underscore character is never used elsewhere.

## Primitive Data Types

Java is statically typed, so we must declare a variable's type and name before using it. The 8 primitive data types are:

- `byte`: 8-bit signed two's complement integer. Minimum value of -128 and maximum value of 127 inclusive. Can also be used in place of `int` where their range limits help to clarify code. 

> The smaller the size of integer, save more memory in large arrays, especially where memory savings actually matters. Use smaller size integer types for large arrays

- `short`: 16-bit signed two's complement integer. Ranges from -32,768 to 32,767 inclusive
- `int`: By default, the 32-bit signed two's complement integer. Ranges from $-2^{31}$ to $2^{31}-1$ inclusive
- `long`: 64-bit signed two's complement integer. Ranges from $-2^{63}$ to $2^{63}-1$ inclusive
- `float`:  single-precision 32-bit IEEE 754 floating point, with 7-8 significant decimal digits
- `double`: double-precision 64-bit IEEE 754 floating point, 15-17 significant decimal digits
- `boolean`: only two values `true` and `false`
- `char`: a single 16-bit Unicode character.

Note you can also create a `String` object by enclosing your character string within double quotes "this is a string"

**Literals**

Any constant value which can be assigned to the variable is called literal/constant. . A literal is the source code representation of a fixed value. All primitive types can be formed as literals. Note that any number of underscore characters (_) can appear anywhere between digits in a numerical literal, with certain rules.

## Arrays

An array is a container object that holds a fixed number of values of a single type. The length of an array is established at creation. After creation, its length is fixed.

```java
class ArrayDemo {
    public static void main(String[] args) {
        // declares an array of integers
        int[] anArray;

        // allocates memory for 10 integers
        anArray = new int[3];
           
        // initialize first element
        anArray[0] = 100;
        // initialize second element
        anArray[1] = 200;
        // and so forth
        anArray[2] = 300;

        System.out.println("Element at index 0: "
                           + anArray[0]);
        System.out.println("Element at index 1: "
                           + anArray[1]);
        System.out.println("Element at index 2: "
                           + anArray[2]);
    }
}
```

Note that when we allocate an array of `int`, the default `int` value is `0` for all elements. The compiler will assign a reasonable default value for fields of primitive types; for local variables, a default value is never assigned.

**Declaring a Variable to refer to an Array**

```java
// declares an array of integers
int[] anArray;

byte[] anArrayOfBytes;
```

An array's type is written as `type[]`, where `type` is the data type of the contained elements; the brackets are special symbols indicating that this variable holds an array. This declaration does not actually create an array; it simply tells the compiler that this variable will hold an array of the specified type.

**Creating, Initializing and Accessing the Array**

```java
// create an array of integers
anArray = new int[10];
```

This statement uses the `new` operator to allocate an array with enough memory for 10 integer elements and assigns the array to the `anArray` variable.

```java
anArray[0] = 100; // initialize first element
anArray[1] = 200; // initialize second element
anArray[2] = 300; // and so forth
```

This then assigns values to each element of the array.

> We don't need to use `new` to create primitive data types. However, everything else is an object and requires using the `new` keyword for memory allocation

You can also use the following shortcut syntax to initialize the array:

```java
int[] anArray = { 
    100, 200, 300,
    400, 500, 600, 
    700, 800, 900, 1000
};
```

**Multi-Dimensional Arrays**

A multidimensional array is an array whose components are themselves arrays. A consequence of this is that the rows are allowed to vary in length.

```java
class MultiDimArrayDemo {
    public static void main(String[] args) {
        String[][] names = {
            {"Mr. ", "Mrs. ", "Ms. "},
            {"Smith", "Jones"}
        };
        // Mr. Smith
        System.out.println(names[0][0] + names[1][0]);
        // Ms. Jones
        System.out.println(names[0][2] + names[1][1]);
    }
}
```

You can also declare an array of arrays (also known as a multidimensional array) by using two or more sets of brackets, such as `String[][]` names. Each element, therefore, must be accessed by a corresponding number of index values.

Use the `length` property to determine size of array. Very similar to `len()` in Python.

```java
System.out.println(anArray.length);
```

**Copying Arrays**
```java
public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
```

We use `arraycopy` to copy arrays. The two `Object` arguments specify the array to copy from and the array to copy to. The three `int` arguments specify the starting position in the source array, the starting position in the destination array, and the number of array elements to copy.

```java
class ArrayCopyDemo {
    public static void main(String[] args) {
        String[] copyFrom = {
            "Affogato", "Americano", "Cappuccino", "Corretto", "Cortado",   
            "Doppio", "Espresso", "Frappucino", "Freddo", "Lungo", "Macchiato",      
            "Marocchino", "Ristretto" };
        
        String[] copyTo = new String[7];
        System.arraycopy(copyFrom, 2, copyTo, 0, 7);
        for (String coffee : copyTo) {
            System.out.print(coffee + " ");           
        }
    }
}
```

**Array Manipulations**

```java
class ArrayCopyOfDemo {
    public static void main(String[] args) {
        String[] copyFrom = {
            "Affogato", "Americano", "Cappuccino", "Corretto", "Cortado",   
            "Doppio", "Espresso", "Frappucino", "Freddo", "Lungo", "Macchiato",      
            "Marocchino", "Ristretto" };
        
        String[] copyTo = java.util.Arrays.copyOfRange(copyFrom, 2, 9);        

        for (String coffee : copyTo) {
            System.out.print(coffee + " ");           
        }            
    }
}
```

Alternatively, we use the built-in `copyOfRange` method to copy an array, which returns a new array. The second parameter of the `copyOfRange` method is the initial index of the range to be copied, *inclusively*, while the third parameter is the final index of the range to be copied, *exclusively*.

Some other useful methods from `java.util.Arrays` are:

- Searching an array for a specific value to get the index at which it is placed (the `binarySearch` method).

- Comparing two arrays to determine if they are equal or not (the `equals` method).

- Filling an array to place a specific value at each index (the `fill` method).

- Sorting an array into ascending order. This can be done either sequentially, using the `sort` method, or concurrently, using the `parallelSort` method. Parallel sorting of large arrays on multiprocessor systems is faster than sequential array sorting.

- Creating a stream that uses an array as its source (the `stream` method).

- Converting an array to a string. The `toString` method converts each element of the array to a string, separates them with commas, then surrounds them with brackets.

> Note that `Array` is a fixed size while `ArrayList` is of variable size. You cannot append to a fixed-size array in Java!