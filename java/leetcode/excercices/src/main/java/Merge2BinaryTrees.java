/*
 * Copyright (c) 2020 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

import java.util.function.Function;
import java.util.function.Supplier;

public class Merge2BinaryTrees {


    public static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return null;
        }
        TreeNode t = new TreeNode(val(t1)+val(t2));
        t.left = mergeTrees(left(t1), left(t2));
        t.right = mergeTrees(right(t1), right(t2));
        return t;
    }

    private static int val(TreeNode t){
        Integer val =onNotNull(t, tree->tree.val);
        if(val==null){
            return 0;
        }
        return val;
    }
    private static TreeNode left(TreeNode t) {
        return onNotNull(t,tree->tree.left);
    }
    private static TreeNode right(TreeNode t) {
        return onNotNull(t,tree->tree.right);
    }

    private static <T> T onNotNull(TreeNode t, Function<TreeNode,T> f){
        if (t != null) {
            return f.apply(t);
        }
        return null;
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this(x, null, null);
        }

        TreeNode(int x, TreeNode left, TreeNode right) {
            val = x;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return recursivePrint(this, "");
        }

        private String recursivePrint(TreeNode tree, String tabs) {
            String result = tabs + String.valueOf(tree.val);
            if (tree.left != null || tree.right != null) {
                result += "\n";
            }
            if (tree.left != null) {
                result += tabs + recursivePrint(tree.left, tabs);
            }
            if (tree.right != null) {
                result += recursivePrint(tree.right, "\t" + tabs);
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            return recursiveEquals(this, (TreeNode) obj);
        }

        private boolean recursiveEquals(TreeNode t1, TreeNode t2) {
            if ((t1 == null && t2 != null)
                    || (t2 == null && t1 != null)) {
                return false;
            }
            if(t1==null && t2==null){
                return true;
            }
            if (t1.val != t2.val) {
                return false;
            }
            if (!recursiveEquals(t1.left, t2.left)) {
                return false;
            }
            if (!recursiveEquals(t1.right, t2.right)) {
                return false;
            }
            return true;
        }
    }
}
