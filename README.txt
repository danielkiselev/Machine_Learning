README
Author:Daniel Kiselev
Date: 4/26/2018

How to run my code:
To run my code please follow these instructions. The folder is named ‘CSC242Project4’. 
Please enter this directory with “cd CSC242Project4” command. Now you have two options. 
There is a shell script written called “run.sh” which you can type ./run.sh to run the command. 
If it says that you do not have permissions to run this shell script then you can run the command "chmod u+x run.sh". 
Otherwise please follow the instructions below.

cd src
javac *.java
java Main


To run my program to see that it adheres to all the guidelines of the project, please follow the instructions in the terminal after you compile and run the code. 
There are print outs that indicate the demonstration of each of the requirements. When you are done running the program please press control+C to exit or press 0 at the end of the test.  

As stated above there are printouts that indicate the effectiveness of our program. If you run the Neural Net in learning mode or the decision tree, which
relearns the data each run then you will see the progress of how data is calculated in console printouts. All modes feature printouts of data examples used to
test each machine learning implementation, and their respective key data. Neural Net has an additional option to load weight data
that is sourced from learning from 15,000,000 random data samples. Using this option I have found it to have 99% correct predictions
based from 10 runs that each test it using 1000 random data samples.