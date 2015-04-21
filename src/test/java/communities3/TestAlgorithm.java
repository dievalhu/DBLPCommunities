package communities3;

import java.util.List;

import com.dblp.communities.algorithm.radicchi.LabeledRadicchi;
import com.dblp.communities.datastructure.PersonInCommunity;
import com.dblp.communities.datastructure.WebCommunity;
import com.dblp.communities.graphs.Edge;
import com.dblp.communities.graphs.LabeledUndirectedGraph;
import com.dblp.communities.graphs.Node;

public class TestAlgorithm {
	
	public static void main(String[] args) {
		test2();
	}
	
	public static void test2() {
		LabeledUndirectedGraph graph = new LabeledUndirectedGraph(16);
		
		graph.addEdge(new Edge(new Node(0), new Node(1)));
		graph.addEdge(new Edge(new Node(0), new Node(2)));
		graph.addEdge(new Edge(new Node(0), new Node(3)));
		graph.addEdge(new Edge(new Node(1), new Node(2)));
		graph.addEdge(new Edge(new Node(1), new Node(3)));
		graph.addEdge(new Edge(new Node(2), new Node(3)));
		
		graph.addEdge(new Edge(new Node(0), new Node(4)));
		graph.addEdge(new Edge(new Node(2), new Node(6)));
		
		graph.addEdge(new Edge(new Node(4), new Node(5)));
		graph.addEdge(new Edge(new Node(4), new Node(6)));
		graph.addEdge(new Edge(new Node(4), new Node(7)));
		graph.addEdge(new Edge(new Node(5), new Node(6)));
		graph.addEdge(new Edge(new Node(5), new Node(7)));
		graph.addEdge(new Edge(new Node(6), new Node(7)));
		
		graph.addEdge(new Edge(new Node(7), new Node(9)));
		
		graph.addEdge(new Edge(new Node(8), new Node(9)));
		graph.addEdge(new Edge(new Node(8), new Node(10)));
		graph.addEdge(new Edge(new Node(8), new Node(11)));
		graph.addEdge(new Edge(new Node(9), new Node(10)));
		graph.addEdge(new Edge(new Node(9), new Node(11)));
		graph.addEdge(new Edge(new Node(10), new Node(11)));
		
		graph.addEdge(new Edge(new Node(8), new Node(12)));
		graph.addEdge(new Edge(new Node(10), new Node(14)));
		
		graph.addEdge(new Edge(new Node(12), new Node(13)));
		graph.addEdge(new Edge(new Node(12), new Node(14)));
		graph.addEdge(new Edge(new Node(12), new Node(15)));
		graph.addEdge(new Edge(new Node(13), new Node(14)));
		graph.addEdge(new Edge(new Node(13), new Node(15)));
		graph.addEdge(new Edge(new Node(14), new Node(15)));
		
		graph.giveNodeName(0, "a");
		graph.giveNodeName(1, "b");
		graph.giveNodeName(2, "c");
		graph.giveNodeName(3, "d");
		graph.giveNodeName(4, "e");
		graph.giveNodeName(5, "f");
		graph.giveNodeName(6, "g");
		graph.giveNodeName(7, "h");
		graph.giveNodeName(8, "i");
		graph.giveNodeName(9, "j");
		graph.giveNodeName(10, "k");
		graph.giveNodeName(11, "l");
		graph.giveNodeName(12, "m");
		graph.giveNodeName(13, "n");
		graph.giveNodeName(14, "o");
		graph.giveNodeName(15, "p");
		
		LabeledRadicchi r = new LabeledRadicchi(graph, 0.2, "weak", true);
		r.detectCommunities();
		List<WebCommunity> communities = r.getFinalCommunityPartition();
		for (WebCommunity c : communities) {
			System.out.println("COMMUNITY: ");
			for (PersonInCommunity p : c.getElements()) {
				System.out.print(p.getName() + ", ");
			}
			System.out.println();
		}
	}

	public static void test1() {
		LabeledUndirectedGraph graph = new LabeledUndirectedGraph(14);
		graph.addEdge(new Edge(new Node(0), new Node(1)));
		graph.addEdge(new Edge(new Node(0), new Node(2)));
		graph.addEdge(new Edge(new Node(0), new Node(3)));
		graph.addEdge(new Edge(new Node(1), new Node(2)));
		graph.addEdge(new Edge(new Node(1), new Node(3)));
		graph.addEdge(new Edge(new Node(2), new Node(3)));
		
		graph.addEdge(new Edge(new Node(3), new Node(4)));
		
		graph.addEdge(new Edge(new Node(4), new Node(5)));
		graph.addEdge(new Edge(new Node(4), new Node(6)));
		graph.addEdge(new Edge(new Node(5), new Node(6)));
		
		graph.addEdge(new Edge(new Node(6), new Node(8)));
		
		graph.addEdge(new Edge(new Node(7), new Node(8)));
		graph.addEdge(new Edge(new Node(7), new Node(9)));
		graph.addEdge(new Edge(new Node(8), new Node(9)));
		
		graph.addEdge(new Edge(new Node(9), new Node(10)));
		
		graph.addEdge(new Edge(new Node(10), new Node(11)));
		graph.addEdge(new Edge(new Node(10), new Node(12)));
		graph.addEdge(new Edge(new Node(10), new Node(13)));
		graph.addEdge(new Edge(new Node(11), new Node(12)));
		graph.addEdge(new Edge(new Node(11), new Node(13)));
		graph.addEdge(new Edge(new Node(12), new Node(13)));
		
		graph.giveNodeName(0,"a");
		graph.giveNodeName(1,"b");
		graph.giveNodeName(2,"c");
		graph.giveNodeName(3,"d");
		graph.giveNodeName(4,"e");
		graph.giveNodeName(5,"f");
		graph.giveNodeName(6,"g");
		graph.giveNodeName(7,"h");
		graph.giveNodeName(8,"i");
		graph.giveNodeName(9,"j");
		graph.giveNodeName(10,"k");
		graph.giveNodeName(11,"l");
		graph.giveNodeName(12,"m");
		graph.giveNodeName(13,"n");
		
		LabeledRadicchi r = new LabeledRadicchi(graph, 0, "weak", true);
		r.detectCommunities();
		List<WebCommunity> communities = r.getFinalCommunityPartition();
		for (WebCommunity c : communities) {
			System.out.println("COMMUNITY: ");
			for (PersonInCommunity p : c.getElements()) {
				System.out.print(p.getName() + ", ");
			}
			System.out.println();
		}
	}
	
}
