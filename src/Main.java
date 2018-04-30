import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/*
Intellectual Property of Daniel Kiselev
*/
public class Main {
	/*
	 * Pretty straight forward, essentially the way that the user interfaces with the project
	 * Parse a few user inputs to test functionality of parts of the project
	 */
	static List<String[]> dataList;
	static List<String[]> weightData;
	public static void main(String[] args) throws IOException {
		System.out.println("Welcome to our Super Swaggy Machine Learning Project");
		System.out.println("All parts of this project are wholly original and property of the Authors");




		boolean valid = true;
		while(valid) {
			int example = 0;
			int design = 0;
			int example1 = 0;
			Integer example2 = 0;
			while(example1 == 0) {
				example1 = testPrompt1();
			}

			while(design == 0) {
				design = DesignPrompt();
			}
			if(design == 1) {
				while(example == 0) {
					example = testPrompt();
				}

				if(example == 1) {
					dataList = LoadData("WillWait-data.txt",11);
					int dataSZ = dataList.size();

					if(example1 == 1) {
						int random = (int )(Math.random() * dataSZ);
						String data [] = dataList.get(random);
						System.out.println("learning...");
						WillWait ww = new WillWait();
						System.out.println();
						System.out.println("Testing...");

						if(ww.Test(data) == 1) {
							System.out.println("Correct!");
						}

					}
					else if(example1 == 2) {
						System.out.println("learning...");
						WillWait ww = new WillWait();
						System.out.println();
						System.out.println("Testing...");
						int correct = 0;
						for(int i = 0; i<100;i++) {
							int random = (int )(Math.random() * dataSZ);
							String data [] = dataList.get(random);
							System.out.println("Testing # " + (i+1));
							correct += ww.Test(data);
						}
						System.out.println("Accuracy: "+ correct +"%");
					}
				}


				else if(example == 2) {
					dataList = LoadData("iris.data.discrete.txt",5);
					int dataSZ = dataList.size();



					if(example1 == 1) {
						int random = (int )(Math.random() * dataSZ);
						String data [] = dataList.get(random);
						//String sizing[] = new String[]{"S","ML","S","S","Iris-setosa"};
						System.out.println("learning...");
						Iris iris = new Iris();
						System.out.println();
						System.out.println("Testing...");

						if(iris.Test(data) == 1) {
							System.out.println("Correct!");
						}

					}
					else if(example1 == 2) {
						//String sizing[] = new String[]{"S","ML","S","S","Iris-setosa"};
						System.out.println("learning...");
						Iris iris = new Iris();
						System.out.println();
						System.out.println("Testing...");
						int correct = 0;
						for(int i = 0; i<100;i++) {
							int random = (int )(Math.random() * dataSZ);
							String data [] = dataList.get(random);
							System.out.println("Testing # " + (i+1));
							correct += iris.Test(data);
						}
						System.out.println("Accuracy: "+ correct +"%");
					}
				}
			}
			else if(design == 2) {
				dataList = LoadData("iris.data.txt",5);
				int dataSZ = dataList.size();
				while(example2 == 0) {
					example2 = testPrompt2();
				}

				if(example1 == 1) {
					int random = (int )(Math.random() * dataSZ);
					String data [] = dataList.get(random);
					//String sizing[] = new String[]{"S","ML","S","S","Iris-setosa"};
					System.out.println("learning...");
					NeuralNet nn = new NeuralNet(example2);
					System.out.println();
					System.out.println("Testing...");
					nn.Test(data);

				}
				else if(example1 == 2) {
					//String sizing[] = new String[]{"S","ML","S","S","Iris-setosa"};
					System.out.println("learning...");
					NeuralNet nn = new NeuralNet(example2);
					System.out.println();
					System.out.println("Testing...");
					double correct = 0;
					for(int i = 0; i<100;i++) {
						int random = (int )(Math.random() * dataSZ);
						String data [] = dataList.get(random);
						System.out.println("Testing # " + (i+1));
						correct += nn.Test(data);
					}
					correct = correct;
					System.out.println("Accuracy: "+ correct +"%");

				}

			}

			int validInt = 2;
			while(validInt == 2) {
		    	System.out.println();
				validInt = retest();
			}
			if(validInt == 1) {
				valid = true;
			}
			if(validInt == 0) {
				valid = false;
			}
		}

	}






	public static int retest() {
    	System.out.println();
    	System.out.println("Would you like to run another dataset example?");
    	System.out.println("Provide input as a number");
    	System.out.println("-----RETEST Options-----");
    	System.out.println("(1) YES");
    	System.out.println("(0) NO");

    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	int checkedInput = runRetest(input);

    	if(checkedInput != 2 ) {

    		return checkedInput;
    	}

    return 2;
}

	public static int runRetest(int input) {
		if(input == 1) {
			return 1;
		}
		else if(input == 0) {
			return 0;
		}
		else {
			System.out.println("Invalid Input");
			return 2;
		}
	}

	public static int DesignPrompt() {
    	System.out.println();
    	System.out.println("Please choose a Machine Learning implimentation");
    	System.out.println("Provide input as a number");
    	System.out.println("-----Design Options-----");
    	System.out.println("(1) Decision Tree Learning");
    	System.out.println("(2) Neural Net");

    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	int checkedInput = runInput(input);
    	if(checkedInput != 0 ) {
    		return checkedInput;
    	}
    return 0;
	}

	public static int testPrompt1() {
    	System.out.println();
    	System.out.println("Please choose a Test Case");
    	System.out.println("Each Run has the program re-learn a randomized data sample");
    	System.out.println("A Run Has a test of 100 random data examples against each Machines Learning Method");
    	System.out.println("Provide input as a number");
    	System.out.println("-----Example Options-----");
    	System.out.println("(1) Single Run");//Each run relearns the data and returns its accuracy
    	System.out.println("(2) 100 Runs");
    	//System.out.println("(3) Custom");

    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	int checkedInput = runInput(input);
    	if(checkedInput != 0 ) {
    		return checkedInput;
    	}
    return 0;
	}

	public static List<String[]> LoadData(String fileName, int param) {//Simple file parser, converts to the list of string arrays
    	List<String[]> myList = new ArrayList<>();
		String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	int lineSZ = line.length();
            	int index = 0;
            	String [] data = new String[param];
            	StringBuilder inputTemp = new StringBuilder();
                for(int i = 0; i<lineSZ; i++) {
                	char cTemp = line.charAt(i);
                	if(i+1 == lineSZ) {
                		inputTemp.append(cTemp);
                		data[index] = inputTemp.toString();
                	}
                	else if(cTemp == ',') {
                		data[index] = inputTemp.toString();
                		inputTemp.setLength(0);
                		index++;
                	}
                	else {
                		inputTemp.append(cTemp);
                	}
                }
                myList.add(data);
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file "+fileName);
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file "+fileName);
        }

        return myList;
	}




	public static int testPrompt() {
    	System.out.println();
    	System.out.println("Please choose a Decision Tree Learning Example");
    	System.out.println("Provide input as a number");
    	System.out.println("-----Example Options-----");
    	System.out.println("(1) Restaurant");
    	System.out.println("(2) Discrete Iris");

    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	int checkedInput = runInput(input);
    	if(checkedInput != 0 ) {
    		return checkedInput;
    	}
    return 0;
	}


	public static int testPrompt2() {
    	System.out.println();
    	System.out.println("Would You Like to Load a Saved Neural Net, or have it Learn");
    	System.out.println("Provide input as a number");
    	System.out.println("-----Learning Options-----");
    	System.out.println("(1) Load Save File");
    	System.out.println("(2) Learn");

    	Scanner scanner = new Scanner(System.in);
    	int input = scanner.nextInt();
    	int checkedInput = runInput(input);
    	if(checkedInput != 0 ) {
    		return checkedInput;
    	}
    return 0;
	}

public static int runInput(int input) {
	if(input == 1) {
		return 1;
	}
	else if(input == 2) {
		return 2;
	}
	else {
		System.out.println("Invalid Input");
		return 0;
	}
}



}
