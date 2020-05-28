# externalsort
external sorting for csv file with customized comparator and support to sort with encrypted file

## Learning purpose
here is a project for you to learn how to do external sorting. the knowledge of it u can view here. https://15445.courses.cs.cmu.edu/fall2019/slides/10-sorting.pdf
You can clone this project, and read the code architecture.
The Entrance class is `ExternalSort.java`; the core method is `sortCsv(...)`; 
To achieve this algorithm, there are two step. 

* first, you need split one large file to a lot of sorted small file which used this `BatchSortedFileProducer.java`

* second, merge them with a lot of steps which used this `SortedFilesMerger.java`

Your task is to implement 3 methods in these two files. (search **'TODO'** to easily find them)

After finished implementation, you can run the `ExternalSortTest.java` test cases to verify your program have bug or not.

**There are a lot of paramter passed in the method you implement, read the comments and be careful with them.**

## Using purpose
If you want to find a library help do external sorting. 

You could search `'ans.txt'` in the project and **use the code to fulfill TODO methods.**

