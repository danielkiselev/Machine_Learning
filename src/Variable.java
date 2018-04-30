import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
Intellectual Property of Daniel Kiselev
*/
public class Variable {

	String name;//name discriptor of the variable
	String options [] = null;//possible values for each variable
	//String data[];
	HashMap<Integer, String> mapData = new HashMap<>();//all the data indexed
	int dataCount = 0;
	int total = 0;
	HashMap<String, Integer> mapVal = new HashMap<>();//initial break up of options and weighting
	HashMap<Integer, String> dataSplit = new HashMap<>();//goal data index, options
	double entropy;
	double entropyOptionsD[] = null;
	boolean goal;//true if this is the goal attribute
	List<Integer> activeIndex = new ArrayList<>();//activate index
	boolean active = true;
	int index;//index of variable

	public Variable(int index,String name, String options[], boolean goal) {
		this.options = options;
		this.name = name;
		this.goal = goal;
		this.index = index;
		establishMap();
	}

	private void establishMap() {//initial creation of hashmap for later tracking
		for(int i = 0; i<options.length; i++) {
			mapVal.put(options[i], 0);
		}
	}

	public void addMap(String key) {//editing hashmap by overwriting with incrimenting value.
		int value = mapVal.get(key) +1;
		mapVal.put(key, value);//keeps track of duplicate results
		mapData.put(total, key);//keeps track of order of data
		total++;
	}

	public void printData() {
		for(Integer i: activeIndex) {
			//System.out.println(name+" datacount"+i+ " | "+ mapData.get(i));
		}
	}

	public double prob(String key) {//returns probability of a possible result
		double res = 0;
		int val = mapVal.get(key);
		res = (double)val/activeIndex.size();
		return res;
	}

	public void entropyLocal() {//returns Entropy(T)
		double res = 0;
		double sigma[] = new double[options.length];
		for(int i = 0; i<sigma.length;i++) {
			double pTemp = prob(options[i]);
			if(pTemp == 0) {
				sigma[i] = 0;
			}
			else {
				double p1 = Math.log(pTemp)/Math.log(options.length);
				sigma[i] = pTemp*(p1);
			}

		}

		for(int i = 0; i<sigma.length;i++) {
			res+=sigma[i];
		}
		entropy = -res;
		System.out.println("name:"+name+ " | Entropy:"+entropy);
	}

	public double entropyOptions(Variable goal) {//returns Entropy(T,X)
		HashMap<Integer, String> goalData = goal.mapData;
		int goalOptionSZ = goal.options.length;
		double entroParts[] = new double[options.length];
		int optTotal[] = new int[options.length];
		String goalOptions [] = goal.options;
		for(int j = 0; j<entroParts.length;j++) {
			entroParts[j] = 0;
			int goalOptionsResulting [] = new int [goalOptionSZ];
			//int yes = 0;
			//int no = 0;
			double probParts[] = new double [goalOptionSZ];

			for(Integer k: activeIndex) {
				//System.out.println(mapData.get(k)+" data  " + options[j]+"options[j]");
				if(mapData.get(k).equals(options[j])) {
					//System.out.println("pass");
					for(int z = 0; z<goalOptionSZ;z++) {
						if(goalData.get(k).equals(goalOptions[z])) {
							goalOptionsResulting [z]++;
							dataSplit.put(k,options[j]);
							//System.out.println("yes");
						}
					}

				}
			}
			for(int z = 0; z<goalOptionSZ;z++) {
				optTotal[j] += goalOptionsResulting [z];
			}
			if(optTotal[j]!=0) {
				//System.out.println(name+" OptTotal" + optTotal[j]);
				for(int z = 0; z<goalOptionSZ;z++) {
					probParts[z] = (double)goalOptionsResulting [z]/optTotal[j];
				}

				double sigma[] = new double[goalOptionSZ];
				for(int i = 0; i<goalOptionSZ;i++) {
					if(probParts[i] == 0) {
						sigma[i] = 0;
					}
					else {
						double p1 = (double)Math.log(probParts[i])/Math.log(goalOptionSZ);
						sigma[i] = probParts[i]*(p1);
					}
				}

				for(int i = 0; i<goalOptionSZ;i++) {
					entroParts[j]=sigma[i];
				}
				entroParts[j] = -entroParts[j];

			}//got all entropy values with options against goals
			else {

			}
		}

		entropyOptionsD = entroParts;
		double res = 0;
		for(int j = 0; j<entroParts.length;j++) {
			double prob = prob(options[j]);
			if(prob == 0) {

			}
			else {
				res += prob*entroParts[j];
			}

		}
		System.out.println("name:"+name+ " | Entropy 2:"+res);//Entropy(t,x)
		return res;
	}





}
