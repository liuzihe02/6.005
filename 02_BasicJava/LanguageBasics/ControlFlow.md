# Control Flow Statements

## The if-then and if-then-else Statements

**`if-then`**

```java
void applyBrakes() {
    // the "if" clause: bicycle must be moving
    if (isMoving){ 
        // the "then" clause: decrease current speed
        currentSpeed--;
    }
}
```

**`if-then-else`**

```java
void applyBrakes() {
    if (isMoving) {
        currentSpeed--;
    } else {
        System.err.println("The bicycle has already stopped!");
    } 
}
```

They can also be nested with `else if`:

```java
class IfElseDemo {
    public static void main(String[] args) {

        int testscore = 76;
        char grade;

        if (testscore >= 90) {
            grade = 'A';
        } else if (testscore >= 80) {
            grade = 'B';
        } else if (testscore >= 70) {
            grade = 'C';
        } else if (testscore >= 60) {
            grade = 'D';
        } else {
            grade = 'F';
        }
        System.out.println("Grade = " + grade);
    }
}
```

## The switch statement

```java
class SwitchDemo{
    public static void main(String[] args) {

        int colour = 2;
        String colourString;
        switch (colour) {
            case 1:  colourString = "Red";
                     break;
            case 2:  colourString = "Green";
                     break;
            case 3:  colourString = "Blue";
                     break;
            default: colourString = "Not sure which colour?";
                     break;
        }
        System.out.println(colourString);
    }
}
```

Based on the value of `colour`, we go to different blocks. The body of a `switch` statement is known as a *switch block*. A statement in the switch block can be labeled with one or more `case` or `default` labels. The switch statement evaluates its expression, then executes all statements that follow the matching case label.

We can also have multiple `case` statements combined:

```java
class SwitchDemo2 {
    public static void main(String[] args) {

        int month = 2;
        int year = 2000;
        int numDays = 0;

        switch (month) {
            case 1: case 3: case 5:
            case 7: case 8: case 10:
            case 12:
                numDays = 31;
                break;
            case 4: case 6:
            case 9: case 11:
                numDays = 30;
                break;
            case 2:
                if (((year % 4 == 0) && 
                     !(year % 100 == 0))
                     || (year % 400 == 0))
                    numDays = 29;
                else
                    numDays = 28;
                break;
            default:
                System.out.println("Invalid month.");
                break;
        }
        System.out.println("Number of Days = "
                           + numDays);
    }
}
```

**`if-then-else` alternatives**

We can also use `if-then-else` to achieve the same results as `switch`. An `if-then-else` statement can test expressions based on ranges of values or conditions, whereas a switch statement tests expressions based only on a single integer, enumerated value, or `String` object.

**`break`**

Each `break` statement terminates the `case` or `default` statement. The break statements are necessary because without them, statements in `switch` blocks fall through: All statements after the matching case label are executed in sequence, *regardless of the expression of subsequent case labels*, until a `break` statement is encountered. The below shows what happens without `break`:

```java
class SwitchDemo{
    public static void main(String[] args) {

        int colour = 1;
        String colourString;
        switch (colour) {
            case 1:  colourString = "Red";
                        System.out.println(colourString);
            case 2:  colourString = "Green";
                        System.out.println(colourString);
            case 3:  colourString = "Blue";
                        System.out.println(colourString);
            default: colourString = "Not sure which colour?";
                        System.out.println(colourString);
        }
    }
}
```

**Using Strings in `switch`**

We can also use `String` instead of integers in the `switch` statements:

```java
class SwitchDemo{
    public static void main(String[] args) {

        String colour = "Red";
        String colourString;
        switch (colour) {
            case "Red":  colourString = "Yes, I have chosen Red";
                     break;
            case "Green":  colourString = "Yes, I have chosen Green";
                     break;
            case "Blue":  colourString = "Yes, I have chosen Blue";
                     break;
            default: colourString = "Not sure which colour?";
                     break;
        }
        System.out.println(colourString);
    }
}
```

## The while and do-while statements

`while` statements are used as:

```java
while (expression) {
     statement(s)
}
```

or an infinite loop like

```java
while (true){
    // your code goes here
}
```

We can also use `do-while`, which evaluates the condition at the bottom of the loop instead of the top.

```java
do {
     statement(s)
} while (expression);
```

## The for statement

```java
for (initialization; termination;
     increment) {
    statement(s)
}
```

- The initialization expression initializes the loop; it's executed once, as the loop begins.
- When the termination expression evaluates to false, the loop terminates.
- The increment expression is invoked after each iteration through the loop; it is perfectly acceptable for this expression to increment or decrement a value.

```java
class ForDemo {
    public static void main(String[] args){
         for(int i=1; i<11; i++){
              System.out.println("Count is: " + i);
         }
    }
}
```

We can declare a variable within the initialization expression. The scope of this variable extends from its declaration to the end of the block of the `for` loop. If the variable controlling a `for` loop is not needed outside the loop, best to declare it in the initialization expression.

**`For` iteration through Collection and Arrays**

```java
class EnhancedForDemo {
    public static void main(String[] args){
         int[] numbers = 
             {1,2,3,4,5,6,7,8,9,10};
         for (int item : numbers) {
             System.out.println("Count is: " + item);
         }
    }
}
```

Use this enhanced form instead of the general form wherever possible.

Iteration over a `Map` is like:

```java
for (String key : turtles.keySet()) {
    System.out.println(key + ": " + turtles.get(key));
}
```

### `For` Bugs
```java
for (Edge e : edges) {
    // the edge exists
    if (checkEdgeExist(e)) {
        edges.remove(e);
    }
}
```

Do not modify a `Collection` while you're iterating over it; this will raise `ConcurrentModificationException`. We can either use an `Iterator` with the `remove` method, or `removeIf` method for `Collection`.

## Branching Statements

**The `break` statement**

`break` can come in two forms, unlabeled and labeled. The unlabeled form is:

```java
class BreakDemo {
    public static void main(String[] args) {

        int[] arrayOfInts = 
            { 32, 87, 3, 589,
              12, 1076, 2000,
              8, 622, 127 };
        int searchfor = 12;

        int i;
        boolean foundIt = false;

        for (i = 0; i < arrayOfInts.length; i++) {
            if (arrayOfInts[i] == searchfor) {
                foundIt = true;
                break;
            }
        }

        if (foundIt) {
            System.out.println("Found " + searchfor + " at index " + i);
        } else {
            System.out.println(searchfor + " not in the array");
        }
    }
}
```

An unlabeled `break` statement terminates the innermost `switch`, `for`, `while`, or `do-while` statement, but a labeled `break` terminates an outer statement. The below code searches for a value in a 2D array with nested loops, and when found this terminates the outer `for` loop.

```java
class BreakWithLabelDemo {
    public static void main(String[] args) {

        int[][] arrayOfInts = { 
            { 32, 87, 3, 589 },
            { 12, 1076, 2000, 8 },
            { 622, 127, 77, 955 }
        };
        int searchfor = 12;

        int i;
        int j = 0;
        boolean foundIt = false;

    search:
        for (i = 0; i < arrayOfInts.length; i++) {
            for (j = 0; j < arrayOfInts[i].length;
                 j++) {
                if (arrayOfInts[i][j] == searchfor) {
                    foundIt = true;
                    break search;
                }
            }
        }

        if (foundIt) {
            System.out.println("Found " + searchfor + " at " + i + ", " + j);
        } else {
            System.out.println(searchfor + " not in the array");
        }
    }
}
```

**the `continue` statement**

`continue` skips the current iteration of a loop. The unlabeled form skips to the end of the innermost loop's body and evaluates the boolean expression that controls the loop.

```java
class ContinueDemo {
    public static void main(String[] args) {
        //we want to count the number of p's in this code

        String searchMe = "peter piper picked a " + "peck of pickled peppers";
        int max = searchMe.length();
        int numPs = 0;

        for (int i = 0; i < max; i++) {
            // interested only in p's
            if (searchMe.charAt(i) != 'p')
                continue;

            // process p's
            numPs++;
        }
        System.out.println("Found " + numPs + " p's in the string.");
    }
}
```

A labeled `continue` statement skips the current iteration of an outer loop marked with the given label. The following program uses a nested loop to look for a substring:

```java
class ContinueWithLabelDemo {
    public static void main(String[] args) {

        String searchMe = "Look for a substring in me";
        String substring = "sub";
        boolean foundIt = false;

        int max = searchMe.length() - 
                  substring.length();

    test:
        for (int i = 0; i <= max; i++) {
            //n to check every letter of substring
            int n = substring.length();
            //j to iterate over main string
            int j = i;
            //k iterate over substring
            int k = 0;
            while (n-- != 0) {
                if (searchMe.charAt(j++) != substring.charAt(k++)) {
                    continue test;
                }
            }
            foundIt = true;
                break test;
        }
        System.out.println(foundIt ? "Found it" : "Didn't find it");
    }
}
```

**the `return` statement**

Returning a value is like `return ++count;` whereas returning without using a value is simply `return;`. The data type of the returned value must match the type of the method's declared return value. When a method is declared void, use `return;`.


