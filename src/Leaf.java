import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/*
Intellectual Property of Daniel Kiselev
*/

public class Leaf {

	boolean randomT = false;
	String name = null;
	List<Integer> data = new ArrayList<>();
	boolean terminal;
	Node next = null;
	String goal;
	double prob;
	HashMap<Integer, String> mapData = new HashMap<>();
	List<String> usedAttributes = new ArrayList<>();
	HashMap<Integer, String> goalData = new HashMap<>();
	HashMap<String,Integer> probData = new HashMap<>();

	public Leaf(String name,boolean terminal) {
		this.name = name;
		this.terminal = terminal;
	}


	public void setGoal() {
		for(Integer temp : data) {
			String tempGoal = goalData.get(temp);
			if(probData.containsValue(tempGoal)) {
				int val = probData.get(tempGoal);
				probData.put(tempGoal, (val+1));
			}
			else {
				probData.put(tempGoal, 1);
			}
		}
		double max = 0;
		String maxS = null;
		double opt = 0;
		for(String key : probData.keySet()) {
			opt++;
			if(probData.get(key)>max) {
				max = probData.get(key);
				maxS = key;
			}
		}
		if(maxS!=null) {
			goal = maxS;
			prob = (double)(max/(opt));
			prob = Math.floor(prob * 100) / 100;
		}
		//randomT = true;
	}

	public void setGoalRandom(Variable v) {

		for(int key : v.dataSplit.keySet()) {
			String tempGoal = goalData.get(key);
			if(probData.containsValue(tempGoal)) {
				int val = probData.get(tempGoal);
				probData.put(tempGoal, (val+1));
			}
			else {
				probData.put(tempGoal, 1);
			}
		}

		double max = 0;
		String maxS = null;
		double opt = 0;
		for(String key : probData.keySet()) {
			opt++;
			if(probData.get(key)>max) {
				max = probData.get(key);
				maxS = key;
			}
			if(probData.get(key)==max) {
				double random1 = (double)(Math.random());
				double random = (double)(Math.random());
				if(random1>random) {
					max = probData.get(key);
					maxS = key;
				}
			}
		}
		if(maxS!=null) {
			goal = maxS;
			prob = (double)(max/(opt));
			prob = Math.floor(prob * 100) / 100;
		}
		randomT = true;
	}



	public void printData() {
		for(Integer temp : data) {
			System.out.println(name+" | Contains INDEX:"+(temp+1));
		}
	}

}
