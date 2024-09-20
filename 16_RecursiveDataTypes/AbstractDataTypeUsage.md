# Writing a Program with ADTs

## Recipe

Test-frst programming!

### Writing a procedure (a static method):

1. **Spec.** Write the spec, including the method signature (name, argument types, return types, exceptions), and the precondition and the postcondition as a Javadoc comment.
2. **Test.** Create systematic test cases and put them in a JUnit class so you can run them automatically.
   - Writing tests puts pressure on specs to be better. So steps 1 and 2 iterate until better specs and tests
   - A test suite that passes all tests even when you haven't implemented the method is not a good test suite 
3. **Implement.** Write the body of the method. You're done when the tests are all green in JUnit.
   - Implementing the method puts pressure on both the tests and the specs, and you may find bugs in them that you have to go back and fix. So finishing the job may require changing the implementation, the tests, and the specs, and bouncing back and forth among them.

### Writing an ADT:

1. **Spec.** Write specs for the operations of the datatype, including method signatures, preconditions, and postconditions.
   - Including AF, RI, Rep Safety, constructors, fields, method signatures
   - This usually goes into the interface
2. **Test.** Write test cases for the ADT's operations.
   - Again, this puts pressure on the spec, may need to modify specs
3. **Implement.** For an ADT, this part expands to:
   - **Choose rep.** Write down the private fields of a class, or the variants of a recursive datatype. Write down the rep invariant as a comment.
   - **Assert rep invariant.** Implement a `checkRep()` method that enforces the rep invariant. This is critically important if the rep invariant is nontrivial, because it will catch bugs much earlier.
   - **Implement operations.** Write the method bodies of the operations, making sure to call `checkRep()` in them. You're done when the tests are all green in JUnit.

### Writing a program (consisting of ADTs and procedures):

1. **Choose classes and datatypes.** Decide what the types do. Decide which datatypes will be mutable and which immutable.
2. **Choose procedures.** Write your top-level procedure and break it down into smaller steps.
3. **Spec.** Spec out the ADTs and procedures. Keep the ADT operations simple and few at first. Only add complex operations as you need them.
   
4. **Test.** Write test cases for each unit (ADT or procedure).
5. **Implement simply first.** Choose simple, brute-force representations. The point here is to put pressure on the specs and the tests, and try to pull your whole program together as soon as possible.
6. **Reimplement and iterate and optimize.** Now consider edge cases, optimizations etc

## Example Problem: Matrix Multiplicaition

We implement an optimized version of matmuls. 

Suppose $a,b$ are scalars, $X$ is a matrix, and we want compute $aXb$. One inefficient way is to convert all scalars to scalar-multiplied identity matrices like $(a*I)X(b*I)$ Then $aXb$ is slow because do many loops. It'd be much faster to collate all the scalars together and do $(ab * I)X$. This is the optimized version that we'll implement.

### Choose datatypes/specs

We define the operations:
```
make : double → MatExpr
// effects: returns an expression consisting of just the given scalar
```
```
make : double[][] → MatExpr
// requires: array.length > 0, and array[i].lengths are equal and > 0, for all i
// effects: returns an expression consisting of just the given matrix
```

```
times : MatExpr × MatExpr → MatExpr
// requires: m1 and m2 are compatible for multiplication
// effects: returns m1×m2
```

```
isIdentity : MatExpr → boolean
// effects: returns true if the expression evaluates to the multiplicative identity
```

```
optimize : MatExpr → MatExpr
// effects: returns an expression with the same value, but which may be faster to compute
```

### Make test cases

`optimize` will implement our fast matmuls.
- number of scalars: 0,1,2,>2
- Position of scalar in the matmul expression: left-of-matrix, right-of-matrix, left-of-leftMatrix,left-of-rightMatrix,right-of-leftMatrix,right-of-rightMatrix

| Test case | Partitions covered |
|-----------|-------------------|
| X => X | 0 scalars |
| aX => aX | 1 scalar, left-of-matrix |
| a(Xb) => (ab)X | 2 scalars, left-of-matrix, right-of-rightMatrix |
| (aX)b => (ab)X | 2 scalars, right-of-matrix, left-of-leftMatrix |
| (Xa)(bY) => (((ab)X)Y) | 2 scalars, left-of-rightMatrix, right-of-leftMatrix |

### Choose a rep/Skeleton

```
MatExpr = Identity + Scalar(double) + Matrix(double[][]) + Product(MatExpr, MatExpr)
```

```java
/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {
    // ...
}

class Identity implements MatrixExpression {
    public Identity() {
    }
}

// this scalar is a scalar multiplied by an identity
class Scalar implements MatrixExpression {
    private final double value;
    public Scalar(double value) {
        this.value = value;
    }
}

class Matrix implements MatrixExpression {
    private final double[][] array;
    // RI: array.length > 0, and all array[i] are equal nonzero length
    public Matrix(double[][] array) {
        this.array = array; // note: danger!
    }
}

class Product implements MatrixExpression {
    private final MatrixExpression m1;
    private final MatrixExpression m2;
    // RI: m1's column count == m2's row count, or m1 or m2 is scalar
    public Product(MatrixExpression m1, MatrixExpression m2) {
        this.m1 = m1;
        this.m2 = m2;
    }
}
```

### Base Case for Recursive Datatype

We choose the identity matrix here, so we can call methods on the "base case type", and avoid using `null`.

```java
/** Identity for all matrix computations. */
public static final MatrixExpression I = new Identity();
```

### Implementation

#### Implementing `make` with static factory methods

We implement the `make` creator methods. We use `static` methods in the interface for `MatrixExpression`.

```java
/** @return a matrix expression consisting of just the scalar value */
public static MatrixExpression make(double value) {
    return new Scalar(value);
}

/** @return a matrix expression consisting of just the matrix given */
public static MatrixExpression make(double[][] array) {
    return new Matrix(array);
}
```

These factory methods are static methods that play the role of constructors.

#### Implementing `isIdentity`: don't use `instanceof`

One bad way to do it is to use a single `isIdentity` method under the `MatrixExpression` interface, and use `instanceof` to branch out to different scenarios. Never use `instanceof`!

```java
// don't do this
public static boolean isIdentity(MatrixExpression m) {
    if (m instanceof Scalar) {
        return ((Scalar)m).value == 1;
    } else if (m instanceof Matrix) {
        // ... check for 1s on the diagonal and 0s everywhere else
    } else ... // do the right thing for other variant classes
}
```
We should use separate `isIdentity` methods for each variant and write separate instance methods.

```java
class Identity implements MatrixExpression {
    public boolean isIdentity() { return true; }
}
```

```java
//check if the diagonal matrix simply has all ones
class Scalar implements MatrixExpression {
    public boolean isIdentity() { return value == 1; }
}
```

```java
//check if the diagonal of this matrix is all ones, with everywhere zero
class Matrix implements MatrixExpression {
    public boolean isIdentity() { 
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                double expected = (row == col) ? 1 : 0;
                if (array[row][col] != expected) return false;
            }
        }
        return true;
    }
}
```

```java

class Product implements MatrixExpression {
    public boolean isIdentity() { 
        //logical AND operator
        return m1.isIdentity() && m2.isIdentity();
    }
}
```

We see in `Product`'s version of `isIdentity`, this implementatuon is quite limited. Our spec only requires the expression to evaluate to identity. We currently need both of this expression to be identity, but we didnt account for $A * A^{-1}$.

#### Implementing `optimize`: don't use `instanceof`

Here's a bad way to do it with `instanceof`:

```java
// don't do this
class Product implements MatrixExpression {
    public MatrixExpression optimize() {
        if (m1 instanceof Scalar) {
            ...
        } else if (m2 instanceof Scalar) {
            ...
        }
        ...
    }
}
```

We actually need some recursive helper functions:

```
scalars : MatExpr → MatExpr
// effects: returns a MatExpr with no matrices in it, only the scalars
```

```
matrices : MatExpr → MatExpr
// effects: returns a MatExpr with no scalars in it, 
// only matrices in the same order they appear in the input expression
```

These expressions will allow us to pull the scalars out of an expression and move them together in a single multiplication expression.

Modify the interface:
```java
/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {

    // ...

    /** @return the product of all the scalars in this expression */
    public MatrixExpression scalars();

    /** @return the product of all the matrices in this expression.
     * times(scalars(), matrices()) is equivalent to this expression. */
    public MatrixExpression matrices();
}
```

Implement the helper functions:
```java
class Identity implements MatrixExpression {
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return this; }
}

class Scalar implements MatrixExpression {
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return I; }
}

class Matrix implements MatrixExpression {
    public MatrixExpression scalars() { return I; }
    public MatrixExpression matrices() { return this; }
}

class Product implements MatrixExpression {
    public MatrixExpression scalars() {
        return times(m1.scalars(), m2.scalars());
    }
    public MatrixExpression matrices() {
        return times(m1.matrices(), m2.matrices());
    }
}
```

`optimize()` can now separate the scalars and matrices:

```java
class Identity implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Scalar implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Matrix implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Product implements MatrixExpression {
    public MatrixExpression optimize() {
        return times(scalars(), matrices());
    }
}
```