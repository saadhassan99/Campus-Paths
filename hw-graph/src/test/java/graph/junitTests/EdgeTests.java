package graph.junitTests;
import graph.*;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;


public class EdgeTests {
	@Rule
	public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
	
	private final Node<String> n1 = new Node<>("d");
	private final Node<String> n2 = new Node<>("b");
	private final Edge<String, String> edge_one = new Edge<>(n2, "a");
	private final Edge<String, String> edge_two = new Edge<>(n1, "c");
	private final Edge<String, String> edge_three = new Edge<>(n1, "c");
	
	@Test
	public void testGetLabel() {
		assertEquals("a", edge_one.getLabel());
	}
	
	@Test
	public void testGetChild() {
		assertEquals(n2, edge_one.getChildNode());
	}
	
	@Test
	public void testEquals() {
		assertEquals(edge_three, edge_two);
	}
	
	@Test
	public void testNotEquals() {
		assertNotEquals(edge_one, edge_two);
	}
	
}
