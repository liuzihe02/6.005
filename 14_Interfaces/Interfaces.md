# Interfaces

Often, separate groups of programmers agree to a "contract" that spells out software behaviour. Each group should write code without knowledge of the internal representation of the code of the other groups. Interfaces are such contracts. Each group can modify software internals at anytime, as long as everyone adheres to the established interface and specifications.

In Java, interfaces is a reference type that contains *only* constants, method signatures, default methods, static methods, and nested types. Interfaces cannot be instantiated - they can only be *implemented* by other clases or *extended* by other interfaces.

> The method body for these method signatures are defined in the respective classes, while default and static methods are written in the interface itself

```java
public interface OperateCar {

   // constant declarations, if any

   // method signatures
   
   // An enum with values RIGHT, LEFT
   int turn(Direction direction,
            double radius,
            double startSpeed,
            double endSpeed);
   int changeLanes(Direction direction,
                   double startSpeed,
                   double endSpeed);
   int signalTurn(Direction direction,
                  boolean signalOn);
   int getRadarFront(double distanceToCar,
                     double speedOfCar);
   int getRadarRear(double distanceToCar,
                    double speedOfCar);
         ......
   // more method signatures
}
```

Note that method signatures have no braces and are terminated with a semicolon.

To use an interface, we write a class that implements the interface. This class provides a method body for each of the methods declared in the interface.

```java
public class OperateBMW760i implements OperateCar {

    // the OperateCar method signatures, with implementation --
    // for example:
    public int signalTurn(Direction direction, boolean signalOn) {
       // code to turn BMW's LEFT turn indicator lights on
       // code to turn BMW's LEFT turn indicator lights off
       // code to turn BMW's RIGHT turn indicator lights on
       // code to turn BMW's RIGHT turn indicator lights off
    }

    // other members, as needed -- for example, helper classes not 
    // visible to clients of the interface
}
```

In the robotic car example above, it is the automobile manufacturers who will implement the interface. Chevrolet's implementation will be substantially different from that of Toyota, but both manufacturers will adhere to the same interface. The guidance manufacturers, who are the clients of the interface, will build systems that call the interface methods. We can see how the interface is an industry standard *Application Programming Interface* (API).

## Defining an Interface

```java
public interface GroupedInterface extends Interface1, Interface2, Interface3 {

    // constant declarations
    
    // base of natural logarithms
    double E = 2.718282;
 
    // method signatures
    void doSomething (int i, double x);
    int doSomethingElse(String s);
}
```

`public` means this interface can be used by any class in any package. If you do not specify the interface is public, this is only accessible to classes defined in the same package as the interface.

> An interface can extend multiple number of interfaces, while a class can only extend one other class

The interface body can also contain abstract methods (method signatures), default methods, and static methods.
- An abstract method within an interface is followed by a semicolon, but no braces (an abstract method does not contain an implementation).
- Default methods are defined with the `default` modifier, and static methods with the `static` keyword.
- All abstract, default, and static methods in an interface are implicitly `public`, so you can omit the `public` modifier.

In addition, an interface can contain constant declarations. All constant values defined in an interface are implicitly `public`, `static`, and `final`. Once again, you can omit these modifiers.

## Implementing an Interface

We use `implements` in the class declaration for a class to implement an interface. Note that one class can implement multiple interfaces.

> A class that implements an interface must implement *all* the methods declared in the interface. (Except for default etc)

Suppose we have an `Relatable` interface to compare the size of objects:

```java
public interface Relatable {
        
    // this (object calling isLargerThan)
    // and other must be instances of 
    // the same class returns 1, 0, -1 
    // if this is greater than, 
    // equal to, or less than other
    public int isLargerThan(Relatable other);
}
```

is implemented by the `Rectangle` class:

```java
public class RectanglePlus 
    implements Relatable {
    public int width = 0;
    public int height = 0;
    public Point origin;

    // constructors
    ...

    // a method for computing
    // the area of the rectangle
    public int getArea() {
        return width * height;
    }
    
    // a method required to implement
    // the Relatable interface
    public int isLargerThan(Relatable other) {
        // this does typecasting, assuming other is actually a RectanglePlus object
        RectanglePlus otherRect = (RectanglePlus)other;
        if (this.getArea() < otherRect.getArea())
            return -1;
        else if (this.getArea() > otherRect.getArea())
            return 1;
        else
            return 0;               
    }
}
```

> The `isLargerThan` method, as defined in the `Relatable` interface, takes an object of type `Relatable`. The line of code `RectanglePlus otherRect = (RectanglePlus)other;` casts `other` to a `RectanglePlus` instance. Type casting tells the compiler what the object really is. Invoking `getArea` directly on the `other` instance like `other.getArea()` would fail to compile because the compiler does not understand that other is actually an instance of `RectanglePlus`.

## Using an Interface as a Type

When you define a new interface, you are defining a new reference data type. If you define a reference variable whose type is an interface, any object you assign to it *must* be an instance of a class that implements the interface.

> An interface name can be used anywhere a type can be used! They are interchangeable

```java
public Object findLargest(Object object1, Object object2) {
   Relatable obj1 = (Relatable)object1;
   Relatable obj2 = (Relatable)object2;
   if ((obj1).isLargerThan(obj2) > 0)
      return object1;
   else 
      return object2;
}
```

Here we have to cast `object1` to a `Relatable` type to invoke the `isLargerThan` method. Otherwise, Java still thinks `object1` is of a general type `Object` and is unable to invoke `isLargerThan`.

If we want to make meaningful comparisons between 2 `Relatable` types, they both have to have the same class! Otherwise we run into errors/get meaningless results.


## Editing Interfaces

Consider an interface `DoIt`:

```java
public interface DoIt {
   void doSomething(int i, double x);
   int doSomethingElse(String s);
}
```

Suppose at a later time, we want to add a third method so the new interface is

```java
public interface DoIt {

   void doSomething(int i, double x);
   int doSomethingElse(String s);
   boolean didItWork(int i, double x, String s);
   
}
```

If we directly make this change, then all the classes implementing the old `DoIt` will break because they do not implement the old interface. 
> Try to anticipate the complete interface and specify it from the beginning.

If we really want to add new methods, we can either create a new interface `DoItPlus` that extends the old interace `DoIt`:

```java
public interface DoItPlus extends DoIt {

   boolean didItWork(int i, double x, String s);
   
}
```
and give clients the choice to switch to the new interface or keep the old one.

Alternatively, we can use default methods where we must provide an implementation (method body):

```java
public interface DoIt {

   void doSomething(int i, double x);
   int doSomethingElse(String s);
   default boolean didItWork(int i, double x, String s) {
       // Method body where we write in the interface
   }
   
}
```

## Default Methods

Typically when we add new methods to the interface, all the classes implementing this interface will be forced to implement the new methods too. Otherwise, the design will just break down.

Default interface methods allow us to add new methods to an interface that are *automatically available* in the implementations. Therefore, we don’t need to modify the implementing classes. However, we do need to write the implementation for default methods in the interface declaration.

In this way, backward compatibility is neatly preserved without having to refactor the implementers.

```java
public interface Vehicle {
    ///methods
    String speedUp();
    
    //default methods
    default String turnAlarmOn() {
        return "Turning the vehicle alarm on.";
    }
}
```

is implemented by

```java
public class Car implements Vehicle {
    
    // constructors/getters
    
    @Override
    public String speedUp() {
        return "The car is speeding up.";
    }
}
```

and our main class:
```java
public static void main(String[] args) { 
    Vehicle car = new Car("BMW");
    System.out.println(car.speedUp());
    System.out.println(car.turnAlarmOn());
}
```
We can see that we can directly access the `default` methods from `Car` class.

### Multiple Interface Conflicts

If a class implements several interfaces that define the *same* default methods (same method signature), then we run into errors at compile time (the Diamond Problem).
```java
public interface Alarm {

    default String turnAlarmOn() {
        return "Turning the alarm on.";
    }
}
```

```java
public class Car implements Vehicle, Alarm {
    // ...
}
```

To resolve this, we must user `@Override` and either provide a custom implementation for these methods:

```java
@Override
public String turnAlarmOn() {
    // custom implementation
}
```
or have them simply choose where to take the method from. Here we take it from the `Vehicle` interface:
```java
@Override
public String turnAlarmOn() {
    return Vehicle.super.turnAlarmOn();
}
```

