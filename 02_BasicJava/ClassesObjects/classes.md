# Classes

## Declaring classes

```java
class MyClass {
    // field, constructor, and 
    // method declarations
}
```

 The class body (the area between the braces) contains all the code that provides for the life cycle of these objects: constructors for initializing new objects, declarations for the fields that provide the state of the class and its objects, and methods to implement the behavior of the class and its objects.

  ```java
 class MyClass extends MySuperClass implements YourInterface {
    // field, constructor, and
    // method declarations
}
```

means that `MyClass` is a subclass of `MySuperClass` and implements the `YourInterface` interface.

Class declarations usually include:

1. Modifiers such as *public, private* and others
2. The class name, with the initial letter capitalized by convention.
3. The name of the class's parent (superclass), if any, preceded by the keyword extends. A class can only extend (subclass) one parent.
4. A comma-separated list of interfaces implemented by the class, if any, preceded by the keyword implements. A class can implement more than one interface.
5. The class body, surrounded by braces, {}.

## Declaring Member Variables

Several kinds of variables are:

- Member variables in a class—these are called *fields*
- Variables in a method or block of code—these are called *local variables*.
- Variables in method declarations—these are called *parameters*

Fields are defined at the start of the class and need:
- Zero or more modifiers like `private` or `public`
- Field's type
- Field's name

```java
public int cadence
public int gear
public int speed
```

**Access Modifiers**

The left-most modifier lets us control whether other classes can access a member fields. `public` means all classes can access this while `private` means this is only accessible within its own class.

For encapsulation purposes, it's common to make fields private and external access requires adding public methods.

```java
public class Bicycle {
        
    private int gear;
        
    public Bicycle(int startGear) {
        gear = startGear;
    }
        
        
    public int getGear() {
        return gear;
    }
        
    public void setGear(int newValue) {
        gear = newValue;
    }
```

**Variable Names**

We follow the same rules for method and class names, except the first letter of class name should be capitalized, and the first (or only) word in a method name should be a verb.

## Defining Methods

Method declarations can compose:
1. Modifiers—such as `public`, `private`, and others
2. The return type—the data type of the value returned by the method, or `void` if the method does not return a value.
3. The method name (first word verb lowercase first letter)
4. The parameter list in parenthesis—a comma-delimited list of input parameters, preceded by their data types, enclosed by parentheses, (). If there are no parameters, you must use empty parentheses.
5. An exception list
6. The method body, enclosed between braces—the method's code, including the declaration of local variables

```java
public double calculateAnswer(double wingSpan, int numberOfEngines,
                              double length, double grossTons) {
    //do the calculation here
}
```

The method signature (used to *identify* method within class) only needs the name and parameter types:

```java
calculateAnswer(double, int, double, double)
```

> the compiler does not consider return type when differentiating methods

**Overloading Methods**

Methods within a class can have the same name if they have different parameter lists (hence different signatures). One reason is we may want to do the same function for different data types, but use overloading sparingly.

> You cannot declare more than one method with the same name and the same number and type of arguments, because the compiler cannot tell them apart

## Constructors for Classes

A class contains constructors that are invoked to create objects from the class blueprint.

```java
public Bicycle(int startCadence, int startSpeed, int startGear) {
    gear = startGear;
    cadence = startCadence;
    speed = startSpeed;
}
```

To create a new `Bicycle` object, a constructor is called by the `new` operator:

```java
Bicycle myBike = new Bicycle(30, 0, 8);
```

> Note: If another class cannot call a MyClass constructor, it cannot directly create MyClass objects.

## Passing Information to Method/Constructor

Parameters refers to the list of variables in a method declaration. Arguments are the actual values that are passed in when the method is invoked. When you invoke a method, the arguments used must match the declaration's parameters in type and order.

**Arbitrary Number of Arguments**

Varargs allow a method to accept an arbitrary number of arguments of the same type. Use an ellipsis `...` after the type in method parameter list. Any number of arguments of that type can be provided. However, only one varargs parameter is allowed per method, and it must be the last parameter in the method param list.
```java
public Polygon polygonFrom(Point... corners) {
    // Method body treats 'corners' as an array
}
```
Internally, java treats `corners` as an array of `Point` objects.

**Parameter Names**

The param name must be unique in its scope (cannot have same name as another param in this method/constructor). However, a parameter can have same name as one of the class' fields. We say the parameters *shadows* the field, but we only use this when setting a particular field

**Passing Primitive Data Type as Arguments**

Primitive types are passed into methods *by value* - any changes to these params exist only within scope of method. After method returns, any new changes during the method are erased.

**Passing Reference Data Type Arguments**

Reference data type parameters, such as objects, are also passed into methods by value. This means that when the method returns, the passed-in reference still references the same object as before. However, the values of the object's fields can be changed in the method, if they have the proper access level.

> Java always uses pass-by-value for all arguments, whether they are primitives or object references. However, for primitives: the value itself is copied (hence the original variable is not modified). For object references: The *reference (memory address) is copied*, not the object itself. The method can use the reference to modify the original object's fields

## Returning a Value from a Method

A method returns to the code that invoked it when
- completes all statements in the method
- reaches a `return` statement
- throws an exception

You declare a method's return type in its method declaration, and use `return` to return that value.

Any method declared `void` doesn't return a value, and `return` can be used to branch out of a control flow block.

> If we declare an object type that we return, all its subclasses are also valid return types (but not super-classes)

## Using `this` keyword

*Within* an instance method/constructor, `this` is a reference to the *current* instance (not class) — the instance whose method or constructor is *being called*.

```java
public class Point {
    public int x = 0;
    public int y = 0;
        
    //constructor
    public Point(int a, int b) {
        x = a;
        y = b;
    }
}
```
is equivalent to
```java
public class Point {
    public int x = 0;
    public int y = 0;
        
    //constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```
We commonly use `this` because a field is shadowed by a method or constructor parameter. To refer to the `Point` field `x`, the constructor must use `this.x`.

**Using `this` within another Constructor**

From within a constructor, you can also use the this keyword to call *another constructor in the same class* - this is explicit constructor invocation. This is good for code reusability and readability.

```java
public class Rectangle {
    private int x, y;
    private int width, height;
        
    public Rectangle() {
        this(0, 0, 1, 1);
    }
    //The two-argument constructor calls the four-argument constructor
    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    ...
}
```
As before, the compiler determines which constructor to call, based on the number and the type of arguments.

> When doing this way, the invocation of another constructor must be the first line in this constructor

## Controlling Access to Members of a Class

Access level modifiers determine which other classes can use a particular field/invoke a particular method. There are 2 levels of access control:

- Top (Class) level - `public` or *package-private* (no modifier)
- Member level - `public`,`private`,`protected`,*package-private* (no modifier)

A class may be declared with `public` where all classes can use this. With package-private (no modifier), only its own package can use this class.

For members, `public` and package-private apply the same. `private` means only its own class can access fields/methods, while `protected` means its own package can access and a subclass in another package.

**Access Levels**
|Modifier|Class|Package|Subclass (outside package)|World|
|:-|:-:|:-:|:-:|:-:|
|`public`|Y|Y|Y|Y|
|`protected`|Y|Y|Y|N|
|no modifier|Y|Y|N|N|
|`private`|Y|N|N|N|

> Always use the most restrictive access level that makes sense for a member. Use `private` unless you have good reason not to.
> 
> Avoid public except constants

If you make all your methods public — including helper methods that are really meant only for local use within the class — then other parts of the program may come to depend on them, which will make it harder for you to change the internal implementation of the class in the future. Your code won’t be as *ready for change*.

## Class Members

We discuss using `static` keyword to create fields/methods belonging to the class, rather than an instance of that class.

> A member not declared as `static` is implicitly an instance member

- `static` basically means the method doesn't have a `self` parameter like in Python
- We need to use the class name instead of a object reference like `ClassName.staticMethod()`
- The method belongs to the class rather than to a specific instance
  - Hence it doesn't have access to instance specific data; it can't use `this`

### Class Variables

If you want to have variables that are common to all objects(instances), we use `static`. Fields that have the `static` modifier in their declaration are called *static fields* or *class variables*. They are associated with the class, rather than with a specific instance. Every instance of the class shares a class variable, which is in one fixed location in memory.

> instance methods (declared without the `static` keyword) must be called on a particular object.

```java
public class Bicycle {
    //instance variables
    private int cadence;
    private int gear;
    private int speed;
        
    // add an instance variable for the object ID
    private int id;
    
    // add a class variable for the
    // number of Bicycle objects instantiated so far
    private static int numberOfBicycles = 0;
        ...
}
```

Classs variables are referenced by the class name itself instead of a specific instance, like `Bicycle.numberOfBicycles`

> note that we can also refer to static fields through an instance (object reference) like `bikeInstance.numberOfBicycles` but this is discouraged to avoid confusion

### Class Methods

Static methods, which have the `static` modifier in their declarations, should be invoked with the class name, without the need for creating an instance of the class, as in

```java
ClassName.methodName(args)
```

> you can also call static methods using instance references like `instanceName.methodName(agrs)` but this is discouraged again

We commonly use static methods to access static fields:
```java
public static int getNumberOfBicycles() {
    return numberOfBicycles;
}
```

- Instance references can access instance variables and instance methods directly.
- Instance references can access class variables and class methods directly.
- Class references can access class variables and class methods directly.
- Class references *cannot* access instance variables or instance methods directly—they must use an instance reference. Also, class methods cannot use the `this` keyword as there is no instance for this to refer to.

### Constants

The `static` modifier in combination with `final` modifier means the value of this field can never change.

```java
static final double PI = 3.141592653589793;
```

> By convention, the names of constant values are fully spelled in capital letters. If the name is composed of more than one word, the words are separated by an underscore (_).

## Initializing Fields
```java
public class BedAndBreakfast {

    // initialize to 10
    public static int capacity = 10;

    // initialize to false
    private boolean full = false;
}
```
This way of initializing static and dynamic fields are good enough for simple initialization. If initilization requires complex logic, we use *static initilization blocks* to initialize fields. Note that the constructors handle complex initialization for instances.

```java
static {
    // whatever code is needed for initialization goes here
}
```
 The runtime system guarantees that static initialization blocks are called in the order that they appear in the source code.