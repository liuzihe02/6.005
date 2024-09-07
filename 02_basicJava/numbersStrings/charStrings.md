# Characters

Most of the time, if you are using a single character value, you will use the primitive char type. For example:

```java
char ch = 'a'; 
// Unicode for uppercase Greek omega character
char uniChar = '\u03A9';
// an array of chars
char[] charArray = { 'a', 'b', 'c', 'd', 'e' };
```

Sometimes we need to use an object; java provides a wrapper class that "wraps" the char in a `Character` object for this purpose. Note that the `Character` class is immutable.

> `char` come in single quotation marks `'a'` which is the `char` literal. `String` types come in double quotation marks `"a"`

**Escape Sequences**

Characters with `\` preceding before it have special meaning to the compiler. Examples are:

- `\n` Newline, Used to start a new line in text output.
- `\t` Tab, Used for indentation and formatting output.
- `\"` Double quote, Used when you need to include double quotes within a string that's already enclosed in double quotes.
- `\\` Backslash, Used when you need to represent a literal backslash in a string.

To print "She said "Hello!" to me.", we use `System.out.println("She said \"Hello!\" to me.");`

# Strings

**Creating Strings**

We create a string literal like `String greeting = "Hello world!";`. We create a `String` object but we don't need to use the `new` keyword here, as strings are exceptions. This method is the "string constant pool".

We can also use the `new` keyword and a constructor. The `String` class allows us to create strings with various constructors like arrays.

```java
char[] helloArray = { 'h', 'e', 'l', 'l', 'o', '.' };
String helloString = new String(helloArray);
```

> Note that the `String` class is immutable. Methods that "modify" strings simply create a new string with the changes.

**Length**

We use the `length()` method to get length of string.

```java
String palindrome = "Dot saw I was Tod";
int len = palindrome.length();
```

We can also convert a string into an array of characters with `getChars` method.

**Concatenating Strings**

Very similar to Python. Use `string1.concat(string2);` or `String quote = "abcd"+"efgh";`

**Formatting Strings**

Similarly to `printf()` or `format` for `PrintStream` objects, the `String` class also has an equivalent `format` method.

```java
String fs;
fs = String.format("The value of the float " +
                   "variable is %f, while " +
                   "the value of the " + 
                   "integer variable is %d, " +
                   " and the string is %s",
                   floatVar, intVar, stringVar);
System.out.println(fs);
```

## Converting Between Numbers and Strings

**Converting Strings to Numbers**

The `Number` subclasses that wrap primitive numeric types ( `Byte`, `Integer`, `Double`,`Float`, `Long`, and `Short`) each provide a class method named `valueOf` that converts a string to an object of that type. `Float.valueOf(s)` first converts the string to a `Float` object, and `(Float.valueOf(s)).floatValue();` then gives the primitive data type.

Each of the `Number` subclasses that wrap primitive numeric types also provides a `parseXXXX()` method (like `parseFloat()`) that can be used to convert strings to primitive numbers. We can replace `(Float.valueOf(s)).floatValue();` with `Float.parseFloat(s);`

**Converting Numbers to Strings**

There are various ways to convert numbers to strings.

```java
int i;
// Concatenate "i" with an empty string; conversion is handled for you.
String s1 = "" + i;
```
or
```java
// The valueOf class method.
String s2 = String.valueOf(i);
```
or each `Number` subclass also has its own `toString()` method
```java
int i;
String s3 = Integer.toString(i);
```

## Manipulating Strings

**Indexing into strings**

We get a single character by using `charAt()`, like `char aChar = sampleString.charAt(9);`.

To get substrings, we use the `substring` method.
- `String substring(int beginIndex, int endIndex)`: Returns a new string that is a substring. The substring begins at the specified `beginIndex` until (including) `endIndex - 1`.
- we can omit `int endIndex` to simply get everything after `beginIndex`

**Other String manipulation methods**

- `String[] split(String regex)` returns an array of strings split by the thing, and elements do not include the thing
- `String trim()` removes leading and trailing white spaces

**Searching for Characters and Substrings in a String**

- `int indexOf(int ch)` and `int lastIndexOf(int ch)` returns the index of the first/last occurrence of the specified character.
- `int indexOf(String str)` and `int lastIndexOf(String str)` returns the index of the first/last occurrence of the specified substring.

**Replacing Characters and Substrings into a String**

In general, we don't need to insert characters as we do this by concatenating modified substrings and required substrings. Some methods for replacing found characters and substrings are:

- `String replace(char oldChar, char newChar)` replaces all occurences of `oldChar` with `newChar`
- `String replace(CharSequence target, CharSequence replacement)` replaces each substring that matches target with the new substring

## Comparing Strings

- `boolean equals(Object anObject)` Returns true if and only if the argument is a String object that represents the same sequence of characters as this object.
- `boolean endsWith(String suffix)`, `boolean startsWith(String prefix)`, Returns true if this string ends with or begins with the substring specified
- `int compareTo(String anotherString)` Compares two strings lexicographically. Returns an integer indicating whether this string is greater than (result is > 0), equal to (result is = 0), or less than (result is < 0) the argument.
- `boolean matches(String regex)` 	Tests whether this string matches the specified regular expression

## StringBuilder Class

`StringBuilder` objects are like `String` objects, except that they can be modified. Internally, these objects are treated like variable-length arrays that contain a sequence of characters. At any point, the length and content of the sequence can be changed through method invocations. Used when you need to concatenate a large number of strings/manipulating long strings.

**Length and Capacity**

Each `StringBuilder()` instance has a `length()` method that returns the length of the character sequence. However, we also have a `capacity()` method that'll auto expand to accomodate additions to the string builder.

- `StringBuilder(String s)` creates a string builder whose value is initialized by that string, plus an empty 16 elements

**Operations**

The principal operations on a `StringBuilder` that are not available in `String` are the `append()` and `insert()` methods, which are overloaded so as to accept data of any type. Each converts its argument to a string and then appends or inserts the characters of that string to the character sequence in the string builder. The append method always adds these characters at the end of the existing character sequence, while the insert method adds the characters at a specified point.

- `StringBuilder append(String s)`
- `StringBuilder delete(int start, int end)` deletes subsequence from `start` to `end-1` inclusive
- `StringBuilder insert(int offset, String s)` The first integer argument indicates the index before which the data is to be inserted. The data (second argument) is converted to a string before insertion
- `StringBuilder reverse()` reverses the string

> You can use any `String` method on a `StringBuilder` object, or any `StringBuilder` methods on `String` objects. We can convert string builder to a string with the `toString()` method. We can also convert the string back into a string builder using the `StringBuilder(String str)` constructor







