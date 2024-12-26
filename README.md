# 6.005
Course notes and assignments fro MIT 6.005 Software Construction (Spring 2016). We take markdown notes in the numbered folders and do our problem set assignments in `ps0`,`ps1` etc.

Accessible via MIT's OCW [page](https://ocw.mit.edu/ans7870/6/6.005/s16/)

## Notes

- We use `JUnit` to write our test files, with VSCode. We need to enable Java tests which automatically creates and configures the `/lib` folder and `.jar` files for us.
  - Sometimes I've deleted the `lib` folder containing the external packages especially `JUnit`. When this happens, just reload developer window and enable java unit tests from the testing page.
  - I have manually moved all `.jar` files to the master `lib` folder
    - Make sure the `lib` folder contains all the required packages
  - If JUnit cannot find the tests, make sure the other tests have valid types and return types, methods too. Don't ignore these other highlighting errors! They may be the reason why another test isnt working
  - You can run tests with coverage to see how good your test cases are; how many lines of code are actually tested

  - Sometimes you might not be able to import the right packages because you didnt set the right classpath. If you have many projects, it may be better to set the classpath each time separately for each project. Setting it explicitly can allow the compiler to find the correct packages:

```java
javac -cp ps2/src ps2/src/poet/Main.java
java -cp ps2/src poet.Main
```

- Each time you rearrange files, make sure to update where Java looks for source files in the `settings.json`
  - Make sure to add both new `src` and `test` files to the source paths
- If you use VS Code to run your java files, VS Code will look at `launch.json`
  - The problem is probably here

### Packages Used
```
antlr-4.9.3-complete.jar
hamcrest-core-1.3.jar
junit-4.13.2.jar
```
