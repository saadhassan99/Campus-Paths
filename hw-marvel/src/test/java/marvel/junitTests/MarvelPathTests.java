package marvel.junitTests;

import graph.Graph;
import graph.Node;
import marvel.MarvelPaths;
import org.junit.Before;
import org.junit.Test;


public class MarvelPathTests {
	private Graph<String,String> g;
	
	@Before
	public void setUp() throws Exception {
		g = MarvelPaths.buildGraph("cars.tsv");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBuildGraphWithNullInput() throws Exception {
		MarvelPaths.buildGraph(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBFSWithNullGraph() {
		MarvelPaths.BFS(null, new Node<> ("honda"), new Node<>("toyota"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBFSWithNullStartOfPath() {
		MarvelPaths.BFS(g,null, new Node<>("toyota"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBFSWithNullEndOfPath() {
		MarvelPaths.BFS(g, new Node<>("toyota"), null);
	}

}
