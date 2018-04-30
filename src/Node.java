import java.util.ArrayList;
import java.util.List;
/*
Intellectual Property of Daniel Kiselev
*/
public class Node {

	Variable attribute;
	List<Leaf> leaves = new ArrayList<>();
	Node child;
	List<String> usedAttributes = new ArrayList<>();


	public Node(Variable attribute) {
		this.attribute = attribute;
	}

}
