# Packages

A package is a grouping of related types providing access protection and name space management. Note that types refers to classes, interfaces (also enumerations and annotations).

## Creating a Package

To create a package, you choose a name for the package and put a `package` statement with that name at the top of *every source file* that contains the types (classes, interfaces, enumerations, and annotation types) that you want to include in the package.

There can be only one package statement in each source file, and it applies to all types in the file.
> If you put multiple types in a single source file, only one can be public, and it must have the same name as the source file.
>
> You can include non-public types in the same file as a public type (this is strongly discouraged, unless the non-public types are small and closely related to the public type), but only the public type will be accessible from outside of the package. All the top-level, non-public types will be package private.

Package names are written in all lower case to avoid conflict with the names of classes or interfaces.

## Using Package Members

The types that comprise a package are known as the package members.

To use a `public` package member from outside its package, you must do one of the following:

- Refer to the member by its fully qualified name
  - Use the member's fully qualified name, which includes the package name like `graphics.Rectangle`
- Import the package member
  - To import a specific member, we add an `import` statement at the beginning of file before any type declariation, but after the `package` statement
  - import graphics.Rectangle;
- Import the member's entire package
  - use the asterisk `import graphics.*`
  - now you can refer to any type within `graphics` by its name like `Rectangle`

## Managing Directory Structure

**Source Files**

put the source file in a directory whose name reflects the name of the package to which the type belongs. Put

```java
//in the Rectangle.java file 
package graphics;
public class Rectangle {
   ... 
}
```

in the directory `.....\graphics\Rectangle.java`

**Class Files**

Like the `.java` source files, the compiled `.class` files should be in a series of directories that reflect the package name. However, the path to the `.class` files does not have to be the same as the path to the `.java` source files. You can arrange your source and class directories separately, as:

```
<path_one>\sources\com\example\graphics\Rectangle.java
<path_two>\classes\com\example\graphics\Rectangle.class
```

The full path to the classes directory, `<path_two>\classes`, is called the class path, and is set with the `CLASSPATH` system variable. 