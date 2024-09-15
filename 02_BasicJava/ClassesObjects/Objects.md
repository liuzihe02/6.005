# Objects

## Creating Objects

```java
Point originOne = new Point(23, 94);
```

1. Declaration: The code `Point orginOne` are vairable declarations that associate a variable name with an object type
2. Instatiation: The `new` keyword creates the object
3. The last bit `Point(23, 94)` calls the constructor which initializes the new object

**Declaring a Variable to refer to an object**

You can declare a reference variable like `Point originOne;`. However simply declaring a reference variable does not create an object. For this, we need the `new` operator.

**Instatiating a Class**

The `new` operator instantiates a class by allocating memory for a new object and returning a reference to that memory. The `new` operator also invokes the object constructor, requiring a single argument after it: a call to a constructor.

The `new` operator returns a *reference* to the object it created.

**Initializing an Object**

You can recognize a constructor because its declaration uses the same name as the class and it has no return type. If a class has multiple constructors, they must have different signatures.

```java
public class Rectangle {
    public int width = 0;
    public int height = 0;
    public Point origin;

    // four constructors
    public Rectangle() {
        origin = new Point(0, 0);
    }
    public Rectangle(Point p) {
        origin = p;
    }
    public Rectangle(int w, int h) {
        origin = new Point(0, 0);
        width = w;
        height = h;
    }
    public Rectangle(Point p, int w, int h) {
        origin = p;
        width = w;
        height = h;
    }

    // a method for moving the rectangle
    public void move(int x, int y) {
        origin.x = x;
        origin.y = y;
    }

    // a method for computing the area of the rectangle
    public int getArea() {
        return width * height;
    }
}
```
This has 3 different kinds of constructors for the same class.

> All classes have at least one constructor. If a class does not explicitly declare any, the Java compiler automatically provides a no-argument constructor, called the default constructor. This default constructor calls the class parent's no-argument constructor. If the parent has no constructor (Object does have one), the compiler will reject the program - java won't try the parent's parent constructor!

## Using Objects

**Referencing an object's fields**

For code within an object's class, you may simply use it name.

Code outside the object's class must use a reference like `objectReference.fieldName`.

To access a field, you can use a named reference to an object, as in the previous examples, or you can use any expression that returns an object reference. Recall that the `new` operator returns a reference to an object. So you could use the value returned from new to access a new object's fields:
```java
int height = new Rectangle().height;
```
This statement creates a new `Rectangle` object and immediately gets its height. In essence, the statement calculates the default height of a Rectangle. Note that after this statement has been executed, the program no longer has a reference to the created `Rectangle`, because the program never stored the reference anywhere. The object is unreferenced, and its resources are free to be recycled by the Java Virtual Machine.

**Calling an object's methods**

As with instance fields, objectReference must be a reference to an object. You can use a variable name, but you also can use any expression that returns an object reference. The `new` operator returns an object reference, so you can use the value returned from `new` to invoke a new object's methods:
```java
new Rectangle(100, 50).getArea()
```

**Garbage Collector**

 The Java runtime environment deletes objects when it determines that they are no longer being used - garbage collection. 
 
 An object is eligible for garbage collection when there are no more references to that object. References that are held in a variable are usually dropped when the variable goes out of scope. Or, you can explicitly drop an object reference by setting the variable to the special value `null`

 > there can be multiple references to the same object; all references must be dropped before that object is eligible for garbage collection