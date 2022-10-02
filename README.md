To run the program run the following commands:
```
javac Rebalancings.java
java Rebalancings
```
Then enter the file path on input prompt on the console. Refer the file sample-input.txt for input format.

As two threads are running in parallel, you can enter one file path then press Enter and then can
enter the next file path.

Limitations:
1. It can process fund allocations for single user in single file.
2. The program supports two threads in parallel currently. 

Thoughts:
All the steps involved in process the user Allotment is sequential in nature, so running one whole process in a single thread might be a good idea.