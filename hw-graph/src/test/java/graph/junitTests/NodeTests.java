package graph.junitTests;
import graph.*;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class NodeTests {
	@Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

	private final Node<String> n2 = new Node<>("b");
	private final Node<String> n3 = new Node<>("a");
	private final Node<String> n4 = new Node<>("b");
	
	@Test
	public void testGetData() {
		assertEquals("a", n3.getData());
		assertEquals("b", n2.getData());
	}
	
	@Test
	public void testEquals() {
		assertEquals(n2, n4);
	}
	
	@Test
	public void testNotEquals() {
		assertNotEquals(n2, n3);
	}

}
