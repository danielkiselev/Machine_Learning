import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
Intellectual Property of Daniel Kiselev
*/
public class NeuralNet {
	double correct = 0;
	NNLayer inputLayer;
	NNLayer h1Layer;
	NNLayer h2Layer;
	NNLayer outLayer;
	List<String[]> dataList;
	List<String[]> weightList;
	double learningRate = 0.1;
	public NeuralNet(int mode) {
		dataList = LoadData("iris.data.txt");

		NetMake();//initializes all the parts
		if(mode == 2) {
			Learn();//learns from 150k random samples based off 150 unique data sets;
		}
		else {

			Load();
		}


	}

	public void Load() {
		weightList = LoadWeights("weights.txt",5);
		for(String[] weights : weightList) {
			Integer layer = null;
			boolean bias = false;
			Integer n1 = null;
			Integer n2 = null;
			double weightOfBias = 0;
			double weight = 0;
			for(int i = 0; i<5; i++) {
				if(i == 0) {
					layer = Integer.parseInt(weights[i]);
				}
				else if(i == 1) {
					bias = Boolean.parseBoolean(weights[i]);
				}
				else if(i == 2 && !bias) {
					n1 = Integer.parseInt(weights[i]);
				}
				else if(i == 2 && bias) {
					weightOfBias = Double.parseDouble(weights[i]);
				}
				else if(i == 3) {
					n2 = Integer.parseInt(weights[i]);
				}
				else if(i == 4) {
					weight = Double.parseDouble(weights[i]);
				}
			}
			if(bias) {
				setBias(layer,n2,weight,weightOfBias);
			}
			else{
				setWeight(n1,n2,weight);
			}
		}
	}

	public void setWeight(int n1, int n2, double weight) {
		inputLayer.LoadWeights(n1,n2, weight);
		h1Layer.LoadWeights(n1,n2, weight);
		h2Layer.LoadWeights(n1,n2, weight);
	}

	public void setBias(int layer, int n2, double value, double weightOfBias) {
		if(layer == 1) {
			inputLayer.LoadBias(layer,n2, value,weightOfBias);
		}
		if(layer == 2) {
			h1Layer.LoadBias(layer,n2, value,weightOfBias);
		}
		if(layer == 3) {
			h2Layer.LoadBias(layer,n2, value,weightOfBias);
		}
	}


	public void Print() {
		inputLayer.printWeights();
		h1Layer.printWeights();
		h2Layer.printWeights();
	}

	public int Test(String data []) {//prints the test # and the results/guess

		System.out.println();
		System.out.print("Input: ");
		for(int i = 0; i<data.length; i++) {
			if(i == data.length-1) {
				System.out.print(data[i]);
			}
			else {
				System.out.print(data[i]+",");
			}

		}
		System.out.println();
		inputLayer.clearValues();
		h1Layer.clearValues();
		h2Layer.clearValues();
		outLayer.clearValues();
		inputLayer.BeginInput(data);
		Print();
		if(data[4].equals(outLayer.Answer())){
			System.out.println("Correct");
			//Print();
			return 1;

		}
		else {
			System.out.println("Wrong");
			return 0;
		}
	}




	public void Learn() {//Used for teaching sample data
		//I run the sample data 150,000 times, and print out the before/after in weights every 1000 samples
		//To reflect the progress in learning
		for(int i = 0; i<150000; i++) {

			if(i%1000 == 0) {
				System.out.println("Run# "+i);
				//inputLayer.printWeights();
				//h1Layer.printWeights();
				//h2Layer.printWeights();
			}

			String goal = startInput(inputLayer);
			for(NNNode n : outLayer.nodes) {
				double y;
				if(n.goal.equals(goal)) {
					y = 1;
				}else {
					y = 0;
				}
				n.gradient = (n.value)*(1-n.value)*(n.value-y);
			}
			backProp();//
			inputLayer.clearValues();
			h1Layer.clearValues();
			h2Layer.clearValues();
			outLayer.clearValues();
			//if(i%100000 == 0) {
				/*
				System.out.println("Sample After-Run Weight # "+i);
				inputLayer.printWeights();
				h1Layer.printWeights();
				h2Layer.printWeights();
				*/
			//}
		}
	}



	public String startInput(NNLayer in) {//randomly chooses a data set to teach the net
		int dataSZ = dataList.size();
		int random = (int )(Math.random() * dataSZ);
		String data [] = dataList.get(random);
		int dataLen = data.length;
		for(int i = 0; i<dataLen; i++) {
			//System.out.println(data[i]);
		}
		in.BeginInput(data);
		//outLayer.print();
		return(data[dataLen-1]);
	}

	public void backProp() {//we identify/calculate all the required data by using backpropagation
		for(NNNode n : h2Layer.nodes) {
			double sigma = 0;
			for(NNConnect con : n.connections) {
				sigma += (con.weight)*(con.to.gradient);
			}
			n.gradient = (n.value)*(1-n.value)*(sigma);
		}
		for(NNNode n : h1Layer.nodes) {
			double sigma = 0;
			for(NNConnect con : n.connections) {
				sigma += (con.weight)*(con.to.gradient);
			}
			n.gradient = (n.value)*(1-n.value)*(sigma);
		}

		//adjusting

		for(NNNode n : inputLayer.nodes) {
			for(NNConnect con : n.connections) {
				double grad = con.to.gradient;
				double value = n.value;
				double changeW = (-learningRate)*(value)*(grad);
				double changeB = (-learningRate)*(grad);
				con.weight+=changeW;
				inputLayer.bias.value+=changeB;
			}
		}
		for(NNNode n : h1Layer.nodes) {
			for(NNConnect con : n.connections) {
				double grad = con.to.gradient;
				double value = n.value;
				double changeW = (-learningRate)*(value)*(grad);
				double changeB = (-learningRate)*(grad);
				con.weight+=changeW;
				h1Layer.bias.value+=changeB;
			}
		}
		for(NNNode n : h2Layer.nodes) {
			for(NNConnect con : n.connections) {
				double grad = con.to.gradient;
				double value = n.value;
				double changeW = (-learningRate)*(value)*(grad);
				double changeB = (-learningRate)*(grad);
				con.weight+=changeW;
				h2Layer.bias.value+=changeB;
			}
		}


	}


	public void NetMake() {//Creates the neural net's structure and assigns responsibility based on parameters in layers
		//(int function,int size, int layer)
		// function key  0=input, 1=hidden, 2=out
		inputLayer = new NNLayer(0,4,1);
		h1Layer = new NNLayer(1,4,2);
		h2Layer = new NNLayer(1,3,3);
		outLayer = new NNLayer(2,3,4);

		inputLayer.next = h1Layer;
		h1Layer.next = h2Layer;
		h2Layer.next = outLayer;
		//creates all the nodes in each layer
		int indexer = 0;
		for(NNNode n : inputLayer.nodes) {
			n.globalIndex = indexer;
			indexer++;
		}
		for(NNNode n : h1Layer.nodes) {
			n.globalIndex = indexer;
			indexer++;
		}
		for(NNNode n : h2Layer.nodes) {
			n.globalIndex = indexer;
			indexer++;
		}
		for(NNNode n : outLayer.nodes) {
			n.globalIndex = indexer;
			indexer++;
		}
		//assigns other functionality depending on function parameter
		inputLayer.AddFunction();
		h1Layer.AddFunction();
		h2Layer.AddFunction();
		outLayer.AddFunction();

	}



	public List<String[]> LoadData(String fileName) {//Simple file parser, converts to the list of string arrays
    	List<String[]> myList = new ArrayList<>();
		String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	int lineSZ = line.length();
            	int index = 0;
            	String [] data = new String[5];
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

	public static List<String[]> LoadWeights(String fileName, int param) {//Simple file parser, converts to the list of string arrays
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

}
