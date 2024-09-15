# Operators

## Assignment, Arithmetic, and Unary Operators

**Assignment Operator**
```java
 int cadence = 0;
 ```

 **Arithmetic Operators**

 Same as Python. `+ - * / %`

 **Unary Operators**

Unary operators act on only one operand. Examples:

- `+` indicates positive values while `-` negates a value
- `++` and `--` operators increment and decrement a value by `1` respectively
- `!` is the logical not operator; inverts a boolean

Note that `++i` and `i++` both increment it by `1`. The only difference is that the prefix version (++result) returns the new incremented value, whereas the postfix version (result++) returns the original value first (then increments it). This makes sense as `result` comes first before `++`.

```java
class PrePostDemo {
    public static void main(String[] args){
        int i = 3;
        i++;
        // prints 4
        System.out.println(i);
        ++i;			   
        // prints 5
        System.out.println(i);
        // prints 6
        System.out.println(++i);
        // prints 6
        System.out.println(i++);
        // prints 7
        System.out.println(i);
    }
}
```

## Equality, Relational, and Conditional Operators

**Equality and Relational Operators**

```
==      equal to
!=      not equal to
>       greater than
>=      greater than or equal to
<       less than
<=      less than or equal to
```

Same as Python.

**Conditional Operators**

```
&& Conditional-AND
|| Conditional-OR
```

```java
class ConditionalDemo1 {
    public static void main(String[] args){
        int value1 = 1;
        int value2 = 2;
        if((value1 == 1) && (value2 == 2))
            System.out.println("value1 is 1 AND value2 is 2");
        if((value1 == 1) || (value2 == 1))
            System.out.println("value1 is 1 OR value2 is 1");
    }
}
```

Note that the first clause is evaluated first, and only evaluate later clauses if neccessary.

For simple `if-then-else` clauses, we can also use the `?:` operator.

```java
result = someCondition ? value1 : value2;
```

This meanns if `someCondition` is true, assign the value of `value1` to `result`. Otherwise, assign the value of `value2` to `result`.

**Type Comparator**

The `instanceof` operator compares an object to a specified type. You can use it to test if an object is an instance of a class, an instance of a subclass, or an instance of a class that implements a particular interface. Note that `null` is not an instance of anything.