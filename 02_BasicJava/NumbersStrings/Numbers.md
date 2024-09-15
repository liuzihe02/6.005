# Numbers

## Numbers class

Most of the times you'll use primitive types like

```java
int i = 500;
float gpa = 3.65f;
byte mask = 0x7f;
```

Sometimes we use the `Number` wrapper object instead of primitive types. `Number` objects are with a capital letter `Byte`,`Float` etc. We do this because

1. Method expects an object as argument
2. To use constants defined by the class, such as MIN_VALUE and MAX_VALUE, that provide the upper and lower bounds of the data type
    - The `MIN_VALUE` and `MAX_VALUE` constants contain the smallest and largest values that can be contained by an object of that type.
3. To use class methods for converting values to and from other primitive types, for converting to and from strings, and for converting between number systems

> Note that primitives are faster and use less memory. `Number` wrapper allow primitives to be used where objects are required - java automatically converts this (with boxing and unboxing).

```java
int primitiveInt = 5;
Integer wrappedInt = Integer.valueOf(5);

// Autoboxing
Integer autoboxed = primitiveInt;

// Unboxing
int unboxed = wrappedInt;
```

**Methods implemented by all `Number`**

- `byte byteValue`, `int intValue()` etc: converts this `Number` object to the primitive data type
- `int compareTo(Byte anotherByte)` compares the `Number`object to the argument
- `boolean equals(Object obj)`, determines whether `Number` is equal to argument

**Conversion Methods**

Each `Number` class also contains other methods for converting numbers to/from string, to/from number systems. Examples include:

- `static Integer decode(String s)` converts a string to an integer
- The `valueOf` method converts a string to a number; allows different bases like base 10, base 5 etc
- `toString` method converts a number to a string

## Formatting Numeric Print Output

We've been using `System.out.print` and `System.out.println`. `System.out` is a `PrintStream` object, so we can use `format` and `printf` equivalently

```java
System.out.format("Name: %s, Age: %d", "Alice", 30);
```

or 

```java
public PrintStream format(String format, Object... args)
//e.g.
public PrintStream format("Name: %s, Age: %d", "Alice", 30)
```

The first parameter, `format`, is a format string specifying how the objects in the second parameter, `args`, are to be formatted. The format string contains plain text as well as format specifiers, which are special characters that format the arguments of `Object... args`.

## Java Math Class

We use the `Math` class in `java.lang` package for advanced math computations. The methods in `Math` class are all static, so we call them directly like `Math.cos(angle)`

**Constants and Basic Methods**

The constants are `Math.E` and `Math.PI`. The below code demonstrates some basic methods.

```java
public class BasicMathDemo {
    public static void main(String[] args) {
        double a = -191.635;
        double b = 43.74;
        int c = 16, d = 45;

        System.out.printf("The absolute value " + "of %.3f is %.3f%n", 
                          a, Math.abs(a));

        System.out.printf("The ceiling of " + "%.2f is %.0f%n", 
                          b, Math.ceil(b));

        System.out.printf("The floor of " + "%.2f is %.0f%n", 
                          b, Math.floor(b));
        //rint is integer closest in value.
        System.out.printf("The rint of %.2f " + "is %.0f%n", 
                          b, Math.rint(b));

        System.out.printf("The max of %d and " + "%d is %d%n",
                          c, d, Math.max(c, d));

        System.out.printf("The min of of %d " + "and %d is %d%n",
                          c, d, Math.min(c, d));
    }
}
```

**Exponential and Logarithmic Methods**

The below methods implement exponentiation, natural logs, power, square roots etc.

```java
public class ExponentialDemo {
    public static void main(String[] args) {
        double x = 11.635;
        double y = 2.76;

        System.out.printf("The value of " + "e is %.4f%n",
                          Math.E);

        System.out.printf("exp(%.3f) " + "is %.3f%n",
                          x, Math.exp(x));

        System.out.printf("log(%.3f) is " + "%.3f%n",
                          x, Math.log(x));

        System.out.printf("pow(%.3f, %.3f) " + "is %.3f%n",
                          x, y, Math.pow(x, y));

        System.out.printf("sqrt(%.3f) is " + "%.3f%n",
                          x, Math.sqrt(x));
    }
}
```

**Trigonometric Methods**

The value passed into these methods is all radians. Use `toRadians` to convert from degrees to radians.

```java
public class TrigonometricDemo {
    public static void main(String[] args) {
        double degrees = 45.0;
        double radians = Math.toRadians(degrees);
        
        System.out.format("The value of pi " + "is %.4f%n",
                           Math.PI);

        System.out.format("The sine of %.1f " + "degrees is %.4f%n",
                          degrees, Math.sin(radians));

        System.out.format("The cosine of %.1f " + "degrees is %.4f%n",
                          degrees, Math.cos(radians));

        System.out.format("The tangent of %.1f " + "degrees is %.4f%n",
                          degrees, Math.tan(radians));

        System.out.format("The arcsine of %.4f " + "is %.4f degrees %n", 
                          Math.sin(radians), 
                          Math.toDegrees(Math.asin(Math.sin(radians))));

        System.out.format("The arccosine of %.4f " + "is %.4f degrees %n", 
                          Math.cos(radians),  
                          Math.toDegrees(Math.acos(Math.cos(radians))));

        System.out.format("The arctangent of %.4f " + "is %.4f degrees %n", 
                          Math.tan(radians), 
                          Math.toDegrees(Math.atan(Math.tan(radians))));
    }
}
```

**Random Numbers**

The `random()` method returns a pseudo-randomly selected number between `0.0` and `1.0`. The range includes `0.0` but not `1.0`. In other words: `0.0 <= Math.random() < 1.0`.