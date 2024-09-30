# I/O Streams

An I/O Stream represents an input source or an output destination. A program uses an input stream to read data from a source, and an output stream to write data to destination.

## Byte Streams

Programs use *byte streams* to perform input and output of 8-bit bytes. All byte stream classes are descended from `InputStream` and `OutputStream`. Most streams are built on byte streams.

We'll discuss on the file I/O byte streams, `FileInputStream` and `FileOutputStream`.

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyBytes {
    public static void main(String[] args) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream("xanadu.txt");
            out = new FileOutputStream("outagain.txt");
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
```
`CopyBytes` spends most of its time in a simple loop that reads the input stream and writes the output stream, one byte at a time.

### Always Close Streams

Closing a stream when it's no longer needed is very important — so important that `CopyBytes` uses a `finally` block to guarantee that both streams will be closed even if an error occurs. This practice helps avoid serious resource leaks.

One possible error is that `CopyBytes` was unable to open one or both files. When that happens, the stream variable corresponding to the file never changes from its initial `null` value. That's why `CopyBytes` makes sure that each stream variable contains an object reference before invoking close.

### Don't use Byte Streams

`CopyBytes` actually represents low level programming you should avoid. Since `xanadu.txt` contains character level data, you should use character streams instead. Byte streams should only be used for the most primitive datatypes, and we use byte streams here only for illustration purposes.

## Character Streams

All character stream classes are descended from `Reader` and `Writer`. As with byte streams, there are character stream classes that specialize in file I/O: `FileReader` and `FileWriter`

```java
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCharacters {
    public static void main(String[] args) throws IOException {
        FileReader inputStream = null;
        FileWriter outputStream = null;
        try {
            inputStream = new FileReader("xanadu.txt");
            outputStream = new FileWriter("characteroutput.txt");

            // c here represents the actual character being read
            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
```

`CopyCharacters` is very similar to `CopyBytes`. The most important difference is that `CopyCharacters` uses `FileReader` and `FileWriter` for input and output in place of `FileInputStream` and `FileOutputStream`. Notice that both programs use an `int` variable to store the character or byte. However, in `CopyCharacters`, the `int` variable holds a `character` value in its last 16 bits; in `CopyBytes`, the `int` variable holds a byte value in its last 8 bits.

### Line Oriented I/O streams

```java
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class CopyLines {
    public static void main(String[] args) throws IOException {

        BufferedReader inputStream = null;
        PrintWriter outputStream = null;

        try {
            inputStream = new BufferedReader(new FileReader("xanadu.txt"));
            outputStream = new PrintWriter(new FileWriter("characteroutput.txt"));

            String l;
            while ((l = inputStream.readLine()) != null) {
                outputStream.println(l);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
```

The `CopyLines` example invokes `BufferedReader.readLine` and `PrintWriter.println` to do input and output one line at a time.

### Buffered Streams

Unbuffered I/O means each read or write request is handled directly by the underlying OS. This can make a program much less efficient, since each such request often triggers disk access, network activity, or some other operation that is relatively expensive.

Buffered input streams read data from a memory area known as a buffer; the native input API is called only when the buffer is empty. Similarly, buffered output streams write data to a buffer, and the native output API is called only when the buffer is full.

We can use a wrapper to convert an unbuffered stream to a buffered one:
```java
inputStream = new BufferedReader(new FileReader("xanadu.txt"));
outputStream = new BufferedWriter(new FileWriter("characteroutput.txt"));
```

There are four buffered stream classes used to wrap unbuffered streams: `BufferedInputStream` and `BufferedOutputStream` create buffered byte streams, while `BufferedReader` and `BufferedWriter` create buffered character streams.

## Scanning and Formatting

### Scanning

Objects of type `Scanner` are useful for breaking down formatted input into tokens and translating individual tokens according to their data type.

```java
import java.io.*;
import java.util.Scanner;

public class ScanXan {
    public static void main(String[] args) throws IOException {
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));
            while (s.hasNext()) {
                System.out.println(s.next());
            }
        } finally {
            if (s != null) {
                //still need to close scanner object
                s.close();
            }
        }
    }
}
```

By default, a scanner uses whitespace to separate tokens. White spaces include blanks, tabs, line terminators. The output of this program looks like
```
In
Xanadu
did
...
```

To use a different token separator, invoke `useDelimiter()`, specifying a regular expression. For example, suppose you wanted the token separator to be a comma, optionally followed by white spaces. You would invoke
```java
s.useDelimiter(",\\s*");
```

#### Translating Individual Tokens

The `ScanXan` example treats all input tokens as simple `String` values. Scanner also supports tokens for all of the Java language's primitive types (except for char), as well as `BigInteger` and `BigDecimal`. Also, numeric values can use thousands separators.

```java
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;

public class ScanSum {
    public static void main(String[] args) throws IOException {
        Scanner s = null;
        double sum = 0;
        try {
            s = new Scanner(new BufferedReader(new FileReader("usnumbers.txt")));
            s.useLocale(Locale.US);
            while (s.hasNext()) {
                if (s.hasNextDouble()) {
                    sum += s.nextDouble();
                } else {
                    s.next();
                }   
            }
        } finally {
            s.close();
        }
        System.out.println(sum);
    }
}
```

`usnumbers.txt` looks like
```
8.5
32,767
3.14159
1,000,000.1
```
and the output string is `1032778.74159`.

### Formatting

Stream objects that implement formatting are instances of either `PrintWriter`, a character stream class, or `PrintStream`, a byte stream class.

> The only PrintStream objects you are likely to need are `System.out` and `System.err`. When you need to create a formatted output stream, instantiate `PrintWriter`, not `PrintStream`

Like all byte and character stream objects, instances of `PrintStream` and `PrintWriter` implement a standard set of write methods for simple byte and character output:
- `print` and `println` format individual values in a standard way.
- `format` formats almost any number of values based on a format string, with many options for precise formatting

#### The `format` method

The `format` method formats multiple arguments based on a format string. The format string consists of static text embedded with format specifiers:
```java
public class Root2 {
    public static void main(String[] args) {
        int i = 2;
        double r = Math.sqrt(i);
        System.out.format("The square root of %d is %f.%n", i, r);
    }
}
```

outputs `The square root of 2 is 1.414214.`

All format specifiers begin with a `%` and end with a 1- or 2-character conversion that specifies the kind of formatted output being generated. Examples:

- `d` formats an integer value as a decimal value.
- `f` formats a floating point value as a decimal value.
- `n` outputs a platform-specific line terminator.

> Except for %% and %n, all format specifiers must match an argument. If they don't, an exception is thrown.

## I/O from the Command Line

### Standard Streams

The Java platform supports three Standard Streams: Standard Input, accessed through `System.in`; Standard Output, accessed through `System.out`; and Standard Error, accessed through `System.err`. These objects are defined automatically and do not need to be opened. Standard Output and Standard Error are both for output; having error output separately allows the user to divert regular output to a file and still be able to read error messages.

These are all byte streams. To use Standard Input as a character stream, wrap `System.in` in `InputStreamReader`:
```java
InputStreamReader cin = new InputStreamReader(System.in);
```

### Console

The Console object also provides input and output streams that are true character streams, through its `reader` and `writer` methods.

```java
import java.io.Console;
import java.util.Arrays;

public class ConsoleDemo {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available");
            System.exit(1);
        }
        String name = console.readLine("Enter your name: ");
        char[] password = console.readPassword("Enter password: ");
        console.format("Hello, %s!%n", name);
        console.writer().println("Your password length: " + password.length);
        Arrays.fill(password, ' '); // Clear password from memory
    }
}
```