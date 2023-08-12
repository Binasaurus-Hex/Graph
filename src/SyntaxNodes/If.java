package SyntaxNodes;

import java.util.List;

public class If implements Node {
    public Node condition;
    public List<Node> block;
}
