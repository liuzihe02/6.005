# Expressions, Statements, and Blocks

Operators may be used in building expressions, which compute values; expressions are the core components of statements; statements may be grouped into blocks.

**Expressions**

These are small code chunks like `value1 == value2` or `1 * 2 * 3`.

> note that `==` compares object references, not the contents of the objects! Like even if `set1` and `set2` have the same objects, `set1==set2` will return `False`

**Statements**

A statement comprises expressions and forms a complete unit of execution, these terminate with a `;`. We have *expressions statements*, *declaration statements* like `double aValue = 8933.234;` and *control flow statements*.

**Blocks**

A block is a group of zero or more statements between balanced braces `{}` and can be used anywhere a single statement is allowed.