import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Merge2BinaryTreesTest {

    @Test
    public void firstTest() {
        assertTrue(result1().equals(
                Merge2BinaryTrees.mergeTrees(sample1(),sample2())
        ));

    }

    private Merge2BinaryTrees.TreeNode sample1() {
        return T(1,
                T(3,
                        T(5, null, null), null),
                T(2, null, null));
    }

    Merge2BinaryTrees.TreeNode sample2() {
        return T(2,
                T(1,
                        null, T(7, null, null)),
                T(3,
                        null, T(4, null, null)));
    }

    Merge2BinaryTrees.TreeNode result1() {
        return T(3,
                T(4,
                        T(5, null, null), T(7, null, null)),
                T(5,
                        null, T(4, null, null)));
    }

    private Merge2BinaryTrees.TreeNode T(int val, Merge2BinaryTrees.TreeNode right, Merge2BinaryTrees.TreeNode left) {
        return new Merge2BinaryTrees.TreeNode(val, right, left);
    }
}
