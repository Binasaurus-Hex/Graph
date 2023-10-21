package SyntaxNodes;

import java.util.List;

public class For implements Node {
    public Node iteration; // name of current iteration (optional)
    public Node sequence; // currently just arrays

    public List<Node> block;
}
