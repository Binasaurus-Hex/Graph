package SyntaxNodes;

import java.util.ArrayList;
import java.util.List;

public class While implements Node {
    public Node condition;
    public List<Node> condition_block = new ArrayList<>();
    public List<Node> block;
}
