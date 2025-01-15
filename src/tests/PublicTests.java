package tests;

import static org.junit.Assert.*;
import implementation.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class PublicTests {

	@Test
	public void pub01TestGraphMisc() {
		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Graph<Double> graph = createGraph();
		StringBuffer results = new StringBuffer();

		results.append(graph.toString() + "\n");
		results.append(TestingSupport.HARDCODEMARKER);

		assertTrue(TestingSupport.isResultCorrect(testName, results.toString(), true));
	}

	@Test
	public void pub02TestBFS() {
		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Graph<Double> graph = createGraph();
		StringBuffer results = new StringBuffer();
		PrintCallBack<Double> printCallBack = new PrintCallBack<Double>();

		graph.doBreadthFirstSearch("ST", printCallBack);
		results.append(printCallBack.getResult());
		results.append(TestingSupport.HARDCODEMARKER);

		assertTrue(TestingSupport.isResultCorrect(testName, results.toString(), true));
	}

	@Test
	public void pub03TestDFS() {
		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Graph<Double> graph = createGraph();
		StringBuffer results = new StringBuffer();
		PrintCallBack<Double> printCallBack = new PrintCallBack<Double>();
		
		graph.doDepthFirstSearch("ST", printCallBack);
		results.append(printCallBack.getResult());
		results.append(TestingSupport.HARDCODEMARKER);

		assertTrue(TestingSupport.isResultCorrect(testName, results.toString(), true));
	}

	@Test
	public void pub04TestDijkstras() {
		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		Graph<Double> graph = createGraph();
		String startVertex = "ST";
		StringBuffer results = new StringBuffer();

		results.append(runDijkstras(graph, startVertex));
		results.append(TestingSupport.HARDCODEMARKER);

		assertTrue(TestingSupport.isResultCorrect(testName, results.toString(), true));
	}

	/* Graph where the data of each vertex is a Double value */
	private Graph<Double> createGraph() {
		Graph<Double> graph = new Graph<Double>();

		/* Adding vertices to the graph */
		String[] vertices = new String[] { "ST", "A", "B", "C", "D", "M" };
		for (int i = 0; i < vertices.length; i++) {
			graph.addVertex(vertices[i], i + 1000.50);
		}

		/* Adding directed edges */
		graph.addDirectedEdge("ST", "A", 11);
		graph.addDirectedEdge("ST", "B", 6);
		graph.addDirectedEdge("A", "C", 2);
		graph.addDirectedEdge("B", "A", 4);
		graph.addDirectedEdge("B", "D", 3);
		graph.addDirectedEdge("C", "D", 5);
		graph.addDirectedEdge("D", "C", 7);

		return graph;
	}

	private static String runDijkstras(Graph<Double> graph, String startVertex) {
		ArrayList<String> shortestPath = new ArrayList<String>();
		StringBuffer results = new StringBuffer();
		Set<String> vertices = graph.getVertices();
		TreeSet<String> sortedVertices = new TreeSet<String>(vertices);
		
		for (String endVertex : sortedVertices) {
			int shortestPathCost = graph.doDijkstras(startVertex, endVertex, shortestPath);
			results.append("Shortest path cost between " + startVertex + " " + endVertex + ": " + shortestPathCost);
			results.append(", Path: " + shortestPath + "\n");
			shortestPath.clear();
		}

		return results.toString();
	}
}
