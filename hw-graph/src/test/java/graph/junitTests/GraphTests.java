package graph.junitTests;
import graph.*;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import javax.xml.crypto.NodeSetData;


public class GraphTests {
	@Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

	private final Node<String> NODE_A = new Node<>("a");
	private final Node<String> NODE_B = new Node<>("b");
	
	private final Graph<String, String> graph = new Graph<>(false);
	private final HashSet<Node<String>> nodes = new HashSet<>();
	private final HashSet<Node<String>> nodes2 = new HashSet<>();
	
	@Before
	public void buildNodes2() {
		nodes2.add(NODE_A);
		nodes2.add(NODE_B);
	}
	
	
	@Test
	public void testIsEmptyWhenConstructed() {
		assertTrue(graph.isEmpty());
	}
	
	@Test
	public void testSizeWhenConstructed() {
		assertEquals(0, graph.size());
	}
	
	@Test
	public void testGetNodesWhenConstructed() {
		assertEquals(nodes,graph.getNodes());
	}
	
	@Test
	public void testAddingOneNode() {
		assertTrue(graph.addNode(NODE_A));
	}
	
	@Test
	public void testIsEmptyAfterAddingOneNode() {
		testAddingOneNode();
		assertFalse(graph.isEmpty());
	}
	
	@Test
	public void testSizeAfterAddingOneNode() {
		testAddingOneNode();
		assertEquals(1, graph.size());
	}
	
	@Test
	public void testContainsNodeAAfterAddingNodeA() {
		testAddingOneNode();
		assertTrue(graph.containsNode(NODE_A));
	}


	@Test
	public void testRemoveReflexiveEdgeOnNodeA() {
		testAddingReflexiveEdgeOnNodeA();
		assertTrue(graph.removeEdge(NODE_A, NODE_A, "AA"));
		assertTrue(graph.getChildren(NODE_A).isEmpty());
	}

	@Test
	public void testRemoveEdgeOnNodeAWithNonExistingEdge() {
		testAddingReflexiveEdgeOnNodeA();
		assertFalse(graph.removeEdge(NODE_A, NODE_B, "AA"));
	}
	
	@Test
	public void testNotContainsNodeBAfterAddingNodeA() {
		testAddingOneNode();
		assertFalse(graph.containsNode(NODE_B));
	}

	@Test
	public void testGetChildrenAfterAddingReflexiveEdge() {
		testAddingReflexiveEdgeOnNodeA();
		HashSet<Node<String>> childrenOfA = new HashSet<>();
		childrenOfA.add(NODE_A);
		assertEquals(childrenOfA, graph.getChildren(NODE_A));
	}
	
	@Test
	public void testChildrenOfNodeAWithoutAddingEdge() {
		testAddingOneNode();
		assertTrue(graph.getChildren(NODE_A).isEmpty());
	}
	
	@Test
	public void testGetNodesAfterAddingOneNode() {
		testAddingOneNode();
		nodes.add(NODE_A);
		assertEquals(nodes,graph.getNodes());
	}
	
	@Test
	public void testAddingSameNodeTwice() {
		testAddingOneNode();
		assertFalse(graph.addNode(NODE_A));
	}
	
	@Test
	public void testSizeAfterAddingSameNodeTwice() {
		testAddingSameNodeTwice();
		assertEquals(1,graph.size());
	}
	
	@Test
	public void testAddingTwoDifferentNodes() {
		testAddingOneNode();
		assertTrue(graph.addNode(NODE_B));
	}

	@Test
	public void testGettingDataAfterAddingTwoDifferentNodes() {
		testAddingTwoDifferentNodes();
		assertEquals(graph.getData(NODE_A), "a");
		assertEquals(graph.getData(NODE_B),"b");
	}

	@Test
	public void testSizeAfterAddingTwoDifferentNodes() {
		testAddingTwoDifferentNodes();
		assertEquals(2,graph.size());
	}
	
	@Test
	public void testGetNodesAfterAddingTwoDifferentNodes() {
		testAddingTwoDifferentNodes();
		nodes.add(NODE_A);
		nodes.add(NODE_B);
		assertEquals(nodes,graph.getNodes());
	}


	@Test
	public void testAddingReflexiveEdgeOnNodeA() {
		testAddingOneNode();
		assertTrue(graph.addEdge(NODE_A, NODE_A, "AA"));
	}

	@Test
	public void testRemoveNodeBAfterAddingTwoInverseDirectionEdges() {
		AddingTwoInverseDirectionsEdges();
		assertTrue(graph.removeNode(NODE_B));
		assertFalse(graph.containsNode(NODE_B));
	}

	
	@Test
	public void testRemoveEdgeWithExistingEdgeButDifferentLabel() {
		testAddingReflexiveEdgeOnNodeA();
		assertFalse(graph.removeEdge(NODE_A, NODE_A, "AB"));
	}

	@Test
	public void testChildrenOfAAferMakingAGraphWithTwoNodesAndFourEdges() {
		testMakingAGraphWithTwoNodesAndFourEdges();
		HashSet<Node<String>> childrenOfA = new HashSet<>();
		childrenOfA.add(NODE_A);
		childrenOfA.add(NODE_B);
		assertEquals(childrenOfA, graph.getChildren(NODE_A));
	}
	
	@Test
	public void testAddingOneEdgeBetweenTwoNodes() {
		testAddingTwoDifferentNodes();
		assertTrue(graph.addEdge(NODE_A, NODE_B, "AB"));
	}
	
	@Test
	public void testChildrenOfNodeAAfterAddingOneEdgeBetweenTwoNodes() {
		testAddingOneEdgeBetweenTwoNodes();
		HashSet<Node<String>> childrenOfA = new HashSet<>();
		childrenOfA.add(NODE_B);
		assertEquals(childrenOfA, graph.getChildren(NODE_A));
	}
	
	@Test
	public void testChildrenOfNodeBAfterAddingOneEdgeBetweenNodeAAndNodeB() {
		testAddingOneEdgeBetweenTwoNodes();
		assertTrue(graph.getChildren(NODE_B).isEmpty());
	}

	@Test
	public void testChildrenOfNodeBAfterAddingTwoInverseDirectionsEdges() {
		AddingTwoInverseDirectionsEdges();
		HashSet<Node<String>> childrenOfB = new HashSet<>();
		childrenOfB.add(NODE_A);
		assertEquals(childrenOfB, graph.getChildren(NODE_B));
	}

	@Test
	public void AddingTwoInverseDirectionsEdges() {
		testAddingOneEdgeBetweenTwoNodes();
		assertTrue(graph.addEdge(NODE_B, NODE_A, "BA"));
	}
	
	@Test
	public void testChildrenOfNodeAAfterAddingTwoInverseDirectionsEdges() {
		AddingTwoInverseDirectionsEdges();
		HashSet<Node<String>> childrenOfA = new HashSet<>();
		childrenOfA.add(NODE_B);
		assertEquals(childrenOfA, graph.getChildren(NODE_A));
	}
	

	
	@Test
	public void testGetChildrenOfAAfterRemovingB() {
		AddingTwoInverseDirectionsEdges();
		graph.removeNode(NODE_B);
		assertTrue(graph.getChildren(NODE_A).isEmpty());
	}
	
	@Test
	public void testSizeAfterRemovingNodeB() {
		testRemoveNodeBAfterAddingTwoInverseDirectionEdges();
		assertEquals(graph.size(), 1);
	}
	
	@Test
	public void testMakingAGraphWithTwoNodesAndFourEdges() {
		testAddingReflexiveEdgeOnNodeA();
		assertTrue(graph.addNode(NODE_B));
		assertTrue(graph.addEdge(NODE_A, NODE_B, "AB"));
		assertTrue(graph.addEdge(NODE_B, NODE_A, "BA"));
		assertTrue(graph.addEdge(NODE_B, NODE_B, "BB"));
	}
	

	
	@Test
	public void testChildrenOfBAferMakingAGraphWithTwoNodesAndFourEdges() {
		testMakingAGraphWithTwoNodesAndFourEdges();
		HashSet<Node<String>> childrenOfB = new HashSet<>();
		childrenOfB.add(NODE_A);
		childrenOfB.add(NODE_B);
		assertEquals(childrenOfB, graph.getChildren(NODE_A));
	}
		
}
