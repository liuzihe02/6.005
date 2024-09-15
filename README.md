# 6.005
Course notes and assignments fro MIT 6.005 Software Construction (Spring 2016). We take markdown notes in the numbered folders and do our problem set assignments in `ps0`,`ps1` etc.

## Notes

- We use `JUnit` to write our test files, with VSCode. We need to enable Java tests which automatically creates and configures the `/lib` folder and `.jar` files for us.
  - Sometimes I've deleted the `lib` folder containing the external packages especially `JUnit`. When this happens, just reload developer window and enable java unit tests from the testing page.
  - I have manually moved all `.jar` files to the master `lib` folder

- Each time you rearrange files, make sure to update where Java looks for source files in the `settings.json`
  - Make sure to add both new `src` and `test` files to the source paths
- If you use VS Code to run your java files, VS Code will look at `launch.json`
  - The problem is probably here



### Ranking of Most Important Sections for Software Engineering

1. Version Control (05)
2. Specifications (06)
3. Testing (03)
4. Code Review (04)
5. Debugging (11)
6. Concurrency (19)
7. Thread Safety (20)
8. Abstraction Functions & Rep Invariants (13)
9. Interfaces (14)
10. Static Checking (01)
11. Basic Java (02)
12. Abstract Data Types (12)
13. Mutability & Immutability (09)
14. Designing Specifications (07)
15. Avoiding Debugging (08)
16. Equality (15)
17. Locks & Synchronization (23)
18. Team Version Control (27)
19. Sockets & Networking (21)
20. Queues & Message-Passing (22)
21. Recursion (10)
22. Recursive Data Types (16)
23. Map, Filter, Reduce (25)

### Ranking of Most Important Sections for ML Engineering

24. Concurrency (19)
25. Thread Safety (20)
26. Version Control (05)
27. Testing (03)
28. Debugging (11)
29. Map, Filter, Reduce (25)
30. Abstraction Functions & Rep Invariants (13)
31. Basic Java (02)
32. Static Checking (01)
33. Specifications (06)
34. Abstract Data Types (12)
35. Mutability & Immutability (09)
36. Sockets & Networking (21)
37. Queues & Message-Passing (22)
38. Locks & Synchronization (23)
39. Code Review (04)
40. Interfaces (14)
41. Recursion (10)
42. Recursive Data Types (16)
43. Designing Specifications (07)
44. Avoiding Debugging (08)
45. Equality (15)
46. Team Version Control (27)

### Least Useful:
- Parser Generators(18)
- Little Languages (26)
- GUIs (24)
- Regular Expressions & Grammars (17)

