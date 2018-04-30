import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
Intellectual Property of Daniel Kiselev
*/
public class WillWait {

	static Variable vars[] = new Variable[11];
	static Variable varsInitial[] = new Variable[11];
	Node root = null;
	Node active;
	List<Integer> usedVar = new ArrayList<>();//keeps track of already used attributes in tree
	int sampleSZ = 0;
	HashMap<Integer, String> goalData = new HashMap<>();//index, result


	public WillWait(){//creates variables and adds their data set from parsed file.
		initialize();
		List<String[]> myList = LoadData("WillWait-data.txt");
		for(String[] rest: myList) {
			for(int i = 0; i<11; i++) {
				vars[i].addMap(rest[i]);
				if(i==10) {
					goalData.put(sampleSZ, rest[i]);
				}
			}
			sampleSZ++;
		}


		System.out.println(sampleSZ);
		for(int i = 0; i<11; i++) {//calculates entropy for all variables, cause why not
			for(int j = 0; j<sampleSZ; j++) {
				vars[i].activeIndex.add(j);
			}
			vars[i].printData();
			vars[i].entropyLocal();
		}
		varsInitial = vars;

		Variable max = entropyGain();// gain(t,x) = entropy(t)-entropy(t,x)
		usedVar.add(max.index);
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("SELECT Final gain:"+max.name);
		System.out.println(" ");
		System.out.println(" ");
		if(root == null) {
			root = new Node(max);
			active = root;
		}
		List<String> split = new ArrayList<>();
		for(int i = 0; i<max.entropyOptionsD.length;i++) {
			//System.out.println(max.options[i]+" entropy parts:"+max.entropyOptionsD[i]);
			if(max.entropyOptionsD[i] == 0) {
				split.add(max.options[i]);
			}
		}
		leafer(split,max,root);

		System.out.println(" ");
		System.out.println(" ");
		System.out.println("~~~~~"+"DECISION TREE"+"~~~~~");
		System.out.println(" ");
		System.out.println(" ");

		treeBuilder(root, 0);//we pass in root to build the tree visually via console.
		//It is already built in terms of data structures, so it's just a matter of a few recursive calls.

	}


	public int Test(String[] testData) {
		int sz = testData.length;
		if(sz!=11) {
			System.out.println("Invalid test!");
		}
		else {
			System.out.println();
			System.out.print("Input: ");
			for(int i = 0; i<sz; i++) {
				if(i == sz-1) {
					System.out.print(testData[i]);
				}
				else {
					System.out.print(testData[i]+",");
				}

			}
			System.out.println();
			boolean valid = true;
			Node current = root;
			while(valid) {
				Variable att = current.attribute;
				for(int i = 0; i<sz-1; i++) {
					if(i==att.index) {
						//System.out.println(testData[i]);
						for(Leaf l : current.leaves) {
							//System.out.println(l.name);
							if(l.name.equals(testData[i])) {
								if(l.terminal) {
									//System.out.println();
									System.out.println("Your answer was:"+testData[sz-1]+"| WE GUESSED: "+l.goal);
									if(testData[sz-1].equals(l.goal)) {
										return 1;
									}
									else {
										return 0;
									}
								}
								else if(l.randomT){
									//System.out.println();
									System.out.println("Your answer was:"+testData[sz-1]+"| WE GUESSED: "+l.goal);
									if(testData[sz-1].equals(l.goal)) {
										return 1;
									}
									else {
										System.out.println(" ");
										return 0;
									}
								}
								else if(l.next!=(null)){
									current = l.next;
								}
								else {
									System.out.println("Your answer was:"+testData[sz-1]+"| WE GUESSED: "+l.goal);
									if(testData[sz-1].equals(l.goal)) {
										return 1;
									}
									else {
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}

	public void nodeBuilder(Leaf leaf) {
		/*
		 * Essentially Reloads all the variables, adjusting for currently avaliable data sets
		 * It keeps going between here and the leafer function until we finish and return to the
		 * class function.
		 */
		vars = new Variable[11];
		initialize();
		List<String[]> myList = LoadData("WillWait-data.txt");
		int count = 0;
		for(String[] rest: myList) {
			if(leaf.data.contains(count)) {
				for(int i = 0; i<11; i++) {
					vars[i].activeIndex.add(count);
					vars[i].addMap(rest[i]);
				}
				count++;
			}
			else {
				for(int i = 0; i<11; i++) {
					vars[i].total++;
				}
				count++;
			}
		}
		for(int i = 0; i<11; i++) {
			vars[i].printData();
			vars[i].entropyLocal();
		}
		Variable max = entropyGain();
		usedVar.add(max.index);
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("SELECT Final gain:"+max.name);
		System.out.println(" ");
		System.out.println(" ");
		Node node = new Node(max);
		List<String> split = new ArrayList<>();
		for(int i = 0; i<max.entropyOptionsD.length;i++) {
			//System.out.println(max.options[i]+" entropy parts:"+max.entropyOptionsD[i]);
			if(max.entropyOptionsD[i] == 0) {
				split.add(max.options[i]);
			}
		}
		leafer(split, max, node);
		leaf.next = node;

	}

	public void leafer(List<String> split, Variable max, Node curr) {
		/*runs through the options in the max entropy variable
		 *Essentially builds all the children of a particular node/variable
		 *And has an iterative loop to run the proper non-terminal children
		 */
		int szOpt = max.options.length;
		//int szData = max.total;
		for(int i = 0; i<szOpt; i++) {
			Leaf opt;
			if(split.contains(max.options[i])) {
				opt = new Leaf(max.options[i],true);
				boolean yesNo;
				for(Integer j: max.activeIndex) {
					if(max.dataSplit.get(j).equals(max.options[i])) {
						opt.data.add(j);
						if(vars[10].mapData.get(j).equals("Yes")) {
							opt.goal = "Yes";
						}
						else if(vars[10].mapData.get(j).equals("No")) {
							opt.goal = "No";
						}
					}
				}
				if(opt.data.isEmpty()) {
					opt.goalData = goalData;
					opt.setGoalRandom(varsInitial[max.index]);
					//opt.goal = "SAMPLE DOES NOT EXIST";
					//curr.leaves.add(opt);
				}
				System.out.println(opt.name+":Leaf |"+opt.goal);
				opt.printData();
			}
			else {
				opt = new Leaf(max.options[i],false);
				for(Integer j: max.activeIndex) {
					if(max.dataSplit.get(j).equals(max.options[i])) {
						opt.data.add(j);

					}
				}
				System.out.println(opt.name+":Leaf |non-term");
				opt.printData();
				opt.goalData = goalData;
				opt.setGoal();
				//curr.leaves.add(opt);
			}
			curr.leaves.add(opt);
		}
		for(Leaf l :curr.leaves) {//if it contains any non-terminal leaves, it get's passed to node builder for further analysis
			if(!l.terminal) {
				nodeBuilder(l);
			}
		}

	}

	public void treeBuilder(Node n, int spaces) {//Builds the tree, keeps track of the spaces for visual appeal
		if(spaces == 0) {
			for (int i = 0; i < spaces; i++) {
		        System.out.print(" ");
		    }

			System.out.print(n.attribute.name);
		}else {
			System.out.print(n.attribute.name);
		}

			spaces+=n.attribute.name.length();
		for(Leaf temp : n.leaves) {
			System.out.println();
			for (int i = 0; i < spaces; i++) {
		        System.out.print(" ");
		    }

			if(temp.randomT) {
				System.out.print(" -- "+temp.name+" --> " + temp.goal+" | Randomly Chosen No Data To Train"+" | Probability: "+ temp.prob);
			}
			else if(temp.terminal) {
				System.out.print(" -- "+temp.name+" --> " + temp.goal);
			}
			else if(!temp.next.equals(null)) {
				System.out.print(" -- "+temp.name+" --> ");
				String tempName = " -- "+temp.name+" --> ";
				treeBuilder(temp.next, (spaces+tempName.length()));
			}
			else {
				System.out.print(" -- "+temp.name+" --> "+ temp.goal+" | Probability: "+ temp.prob);
				//temp.printData();
			}
		}

	}

	public Variable entropyGain() {
		double res = 0;
		Variable max = null;
		Variable T = vars[10];
		double Gain[] = new double[10];
		for(int i = 0; i<10; i++) {
			if(usedVar.contains(i)) {

			}
			else {
				Gain[i] = (T.entropy) - (vars[i].entropyOptions(T));//calculates gain and find highest
				//System.out.println("gain:"+vars[i].name+ " | "+Gain[i]);
				if(Gain[i]>res) {
					res = Gain[i];
					max = vars[i];
					//System.out.println("SELECT gain:"+vars[i].name+ " | "+Gain[i]);
				}
			}

		}


		return max;
	}



	public List<String []> LoadData(String fileName) {//file parser
    	List<String []> myList = new ArrayList<>();
		String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	int lineSZ = line.length();
            	int index = 0;
            	String [] data = new String[11];
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

	private void initialize() {//Creates variables/Attributes and adds their descriptors
		String yesNO[] = new String[]{"Yes", "No"};
		String patrons[] = new String[]{"None", "Some", "Full"};
		String price[] = new String[]{"$", "$$", "$$$"};
		String type[] = new String[]{"French", "Italian", "Thai", "Burger"};
		String waitEstimate[] = new String[]{"0-10", "10-30", "30-60", ">60"};
		vars[0] = new Variable(0,"Alternate", yesNO,false);
		vars[1] = new Variable(1,"Bar", yesNO,false);
		vars[2] = new Variable(2,"Fri/Sat", yesNO,false);
		vars[3] = new Variable(3,"Hungry", yesNO,false);
		vars[4] = new Variable(4,"Patrons", patrons,false);
		vars[5] = new Variable(5,"Price", price,false);
		vars[6] = new Variable(6,"Raining", yesNO,false);
		vars[7] = new Variable(7,"Reservation", yesNO,false);
		vars[8] = new Variable(8,"Type", type,false);
		vars[9] = new Variable(9,"WaitEstimate", waitEstimate,false);
		vars[10] = new Variable(10,"WillWait", yesNO,true);
	}

}
