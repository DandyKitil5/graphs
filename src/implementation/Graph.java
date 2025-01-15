package implementation;

import java.util.*;
import java.util.Map.Entry;

import javax.security.auth.callback.Callback;

/**
 * Implements a graph. We use two maps: one map for adjacency properties
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated
 * with a vertex.
 * 
 * @author cmsc132
 * 
 * @param <E>
 */
public class Graph<E> {
	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;

	public Graph() {
		adjacencyMap = new HashMap<String, HashMap<String, Integer>>();
		dataMap = new HashMap<String, E>();
	}

	public void addVertex(String vertexName, E data) {
		if (dataMap.containsKey(vertexName)) {
			throw new IllegalArgumentException("Error: Already Exists");
		}
		dataMap.put(vertexName, data);
	}

	public void addDirectedEdge(String startVertexName, String endVertexName, int cost) {
		if (!dataMap.containsKey(startVertexName) || !dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		} else {
			if (adjacencyMap.containsKey(startVertexName)) {
				adjacencyMap.get(startVertexName).put(endVertexName, cost);
			} else {
				HashMap<String, Integer> adjacentEdges = new HashMap<String, Integer>();
				adjacentEdges.put(endVertexName, cost);
				adjacencyMap.put(startVertexName, adjacentEdges);
			}
		}
	}

	public String toString() {
		String answer = "Vertices: [";
		TreeMap<String, E> treeDataMap = new TreeMap<String, E>(dataMap);
		Iterator<Map.Entry<String, E>> iterator = treeDataMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, E> entry = iterator.next();
			answer += entry.getKey();
			if (iterator.hasNext())
				answer += ", ";
		}
		answer += "]\nEdges:\n";

		Iterator<Map.Entry<String, E>> iterator2 = treeDataMap.entrySet().iterator();
		while (iterator2.hasNext()) {
			Map.Entry<String, E> entry = iterator2.next();
			answer += "Vertex(" + entry.getKey() + ")--->";
			HashMap<String, Integer> adjMap = adjacencyMap.get(entry.getKey());
			if (adjMap != null) {
				TreeMap<String, Integer> adjTreeMap = new TreeMap<String, Integer>(adjMap);
				answer += adjTreeMap + "\n";
			} else {
				answer += "{}\n";
			}
		}
		return answer.trim();
	}

	public Map<String, Integer> getAdjacentVertices(String vertexName) {
		return adjacencyMap.get(vertexName);
	}

	public int getCost(String startVertexName, String endVertexName) {
		if (!dataMap.containsKey(startVertexName) || !dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		}
		return adjacencyMap.get(startVertexName).get(endVertexName);
	}

	public Set<String> getVertices() {
		return dataMap.keySet();
	}

	public E getData(String vertex) {
		if (!dataMap.containsKey(vertex)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		}
		return dataMap.get(vertex);
	}

	public void doDepthFirstSearch(String startVertexName, CallBack<E> callBack) {
		if (!dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		}
		LinkedList<String> stack = new LinkedList<String>();
		LinkedList<String> visited = new LinkedList<String>();
		int count = 0;
		String curr = startVertexName;
		while (count < dataMap.size()) {
			if (!visited.contains(curr)) {
				visited.add(curr);
				if (adjacencyMap.get(curr) != null) {
					TreeMap<String, Integer> adjTreeMap = 
							new TreeMap<String, Integer>(adjacencyMap.get(curr));
					for (Map.Entry<String, Integer> adjMapEntry : adjTreeMap.entrySet()) {
						if (!visited.contains(adjMapEntry.getKey())) {
							stack.add(adjMapEntry.getKey());
						}
					}
				}
				callBack.processVertex(curr, dataMap.get(curr));
				if (!stack.isEmpty()) {
					curr = stack.getLast();
					stack.removeLast();
				}
			}
			count++;
		}

	}

	public void doBreadthFirstSearch(String startVertexName, CallBack<E> callBack) {
		if (!dataMap.containsKey(startVertexName)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		}
		LinkedList<String> queue = new LinkedList<String>();
		LinkedList<String> visited = new LinkedList<String>();
		int count = 0;
		String curr = startVertexName;

		while (count < dataMap.size()) {
			if (!visited.contains(curr)) {
				visited.add(curr);
				if (adjacencyMap.get(curr) != null) {
					TreeMap<String, Integer> adjTreeMap = 
							new TreeMap<String, Integer>(adjacencyMap.get(curr));
					for (Map.Entry<String, Integer> adjMapEntry 
							: adjTreeMap.entrySet()) {
						if (!visited.contains(adjMapEntry.getKey()) 
								&& !queue.contains(adjMapEntry.getKey())) {
							queue.addFirst(adjMapEntry.getKey());
						}
					}
				}
				callBack.processVertex(curr, dataMap.get(curr));
				if (!queue.isEmpty()) {
					curr = queue.getLast();
					queue.removeLast();
				}
			}
			count++;
		}

	}

	public int doDijkstras(String startVertexName, String endVertexName, ArrayList<String> shortestPath) {
		if (!dataMap.containsKey(startVertexName) || !dataMap.containsKey(endVertexName)) {
			throw new IllegalArgumentException("Error: Vertex Doesn't exist");
		}
		HashMap<String, Integer> cost = new HashMap<String, Integer>();
		HashMap<String, String> previous = new HashMap<String, String>();

		int pathCost = -1;
		int max = Integer.MAX_VALUE;
		
		for (String node : dataMap.keySet()) {
			cost.put(node, max);
		}
		cost.put(startVertexName, 0);

		previous.put(startVertexName, null);

		PriorityQueue<String> findMin = new PriorityQueue<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.compare(cost.get(o1), cost.get(o2));
			}
		});
		findMin.add(startVertexName);

		while (!findMin.isEmpty()) {
			String curr = findMin.poll();
			 if(adjacencyMap.get(curr) != null) {
			TreeMap<String, Integer> adjTreeMap = new TreeMap<String, Integer>(adjacencyMap.get(curr));
			for (Map.Entry<String, Integer> adjMapEntry : adjTreeMap.entrySet()) {
				if (cost.get(curr) + adjMapEntry.getValue() 
				< cost.get(adjMapEntry.getKey())) {
					previous.put(adjMapEntry.getKey(), curr);
					cost.put(adjMapEntry.getKey(), cost.get(curr) 
							+ adjMapEntry.getValue());
					findMin.add(adjMapEntry.getKey());
				}
			}
			 }

		}

		if (cost.get(endVertexName) == max) {
			shortestPath.add("None");
		} else {
			pathCost = cost.get(endVertexName);
		}
		if (pathCost != -1) {
			String current = endVertexName;
			LinkedList<String> tempList = new LinkedList<String>();
			while (current != null) {
				tempList.addFirst(current);
				current = previous.get(current);
			}
			shortestPath.addAll(tempList);
		}

		return pathCost;
	}
}