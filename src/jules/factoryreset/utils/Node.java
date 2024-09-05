package jules.factoryreset.utils;

public class Node implements Comparable<Node> {
	public int row;
	public int col;
	public int gCost;
	public int hCost;
	public Node parent;

	public Node(int row, int col, int gCost, int hCost) {
		this.row = row;
		this.col = col;
		this.gCost = gCost;
		this.hCost = hCost;
		this.parent = null;
	}

	public int f() {
		return gCost + hCost;
	}

	@Override
	public int compareTo(Node other) {
		return Integer.compare(this.f(), other.f());
	}
}
