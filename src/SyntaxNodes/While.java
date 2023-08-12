package SyntaxNodes;

import java.util.List;

public class While implements Node {
    public Node condition;
    public List<Node> block;
}
