import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Your implementation of a binary search tree.
 *
 * @author Xiying Huang
 * @userid xhuang309
 * @GTID 903089975
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the BST with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular
     * for loop will not work here. What other type of loop would work?
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) throws IllegalArgumentException {
        this();
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        for (T d : data) {
            if (d == null) {
                throw new IllegalArgumentException("None of the data elements"
                        + "can be null");
            }
            add(d);
        }
    }

    /**
     * Add the data as a leaf in the BST. Should traverse the tree to find the
     * appropriate location. If the data is already in the tree, then nothing
     * should be done (the duplicate shouldn't get added, and size should not be
     * incremented).
     * 
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (root == null) {
            root = new BSTNode<T>(data);
            this.size++;
            return;
        }

        add(new BSTNode<T>(data), root);
    }
    /**
     * Helper method to find where data is to be added,
     * used in {@code add(T data)}
     * @param node Node to be added
     * @param root Root node at which node is being added
     */
    private void add(BSTNode<T> node, BSTNode<T> root) {
        int x = root.getData().compareTo(node.getData());

        if (x == 0) {
            return;
        } else if (x < 0) {
            if (root.getRight() == null) {
                root.setRight(node);
                this.size++;
                return;
            }
            add(node, root.getRight());
        } else {
            if (root.getLeft() == null) {
                root.setLeft(node);
                this.size++;
                return;
            }
            add(node, root.getLeft());
        }
    }
    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the successor to replace the data.
     * You must use recursion to find and remove the successor (you will likely
     * need an additional helper method to handle this case efficiently).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        BSTNode<T> removed = new BSTNode<>(null);
        root = remove(data, root, removed);
        return removed.getData();
    }
    /**
     * {@link #remove(Comparable)} helper method.
     *
     * @param data    data to remove from {@link BST}
     * @param node    current node in recursive call
     * @param removed the removed data to return
     * @return root node without the removed data
     */
    private BSTNode<T> remove(T data, BSTNode<T> node, BSTNode<T> removed) {
        if (node == null) {
            throw new NoSuchElementException("Data not found");
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRight(remove(data, node.getRight(), removed));
        } else if (data.compareTo(node.getData()) < 0) {
            node.setLeft(remove(data, node.getLeft(), removed));
        } else {
            removed.setData(node.getData());
            if (node.getLeft() == null) {
                size--;
                return node.getRight();
            } else if (node.getRight() == null) {
                size--;
                return node.getLeft();
            } else {
                size--;
                BSTNode<T> replace = new BSTNode<>(null);
                node.setRight(getSuccecessor(node.getRight(), replace));
                node.setData(replace.getData());
            }
        }
        return node;
    }
    /**
     * Gets the Successor of the parent node for removal and replacement.
     *
     * @param node    node to be deleted
     * @param replace the node replaced
     * @return node that's replaced by the predecessor
     */
    private BSTNode<T> getSuccecessor(BSTNode<T> node, BSTNode<T> replace) {
        if (node.getRight() == null) {
            replace.setData(node.getData());
            return node.getRight();
        } else {
            node.setLeft(getSuccecessor(node.getLeft(), replace));
            return node;
        }
    }


    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) throws IllegalArgumentException,
            NoSuchElementException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        BSTNode<T> tNode = root;

        while (tNode != null) {
            int x = tNode.getData().compareTo(data);
            BSTNode<T> leaf;

            if (x > 0) {
                leaf = tNode.getLeft();
            } else {
                leaf = tNode.getRight();
            }

            if (tNode.getData().equals(data)) {
                return tNode.getData();
            } else if (leaf == null) {
                throw new NoSuchElementException("Node containing {" + data
                        + "} could not be found");
            } else {
                tNode = leaf;
            }
        }

        throw new NoSuchElementException("Node containing {" + data
                + "} could not be found");
    }





    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        try {
            get(data);
        } catch (NoSuchElementException exception) {
            return false;
        }
        return true;
    }


    /**
     * Should run in O(n).
     *
     * @return a preorder traversal of the tree
     */
    public List<T> preorder() {
        return preorder(root);
    }

    /**
     * Helper method for preorder, used in {@code preorder()}
     * @param root Root to start preorder traversal at
     * @return Returns List of elements from preorder traversal
     */
    private List<T> preorder(BSTNode<T> root) {
        if (root == null) {
            return new ArrayList<T>();
        }

        List<T> preList = new ArrayList<>();

        preList.add(root.getData());
        preList.addAll(preorder(root.getLeft()));
        preList.addAll(preorder(root.getRight()));

        return preList;
    }


    /**
     * Should run in O(n).
     *
     * @return an inorder traversal of the tree
     */
    public List<T> inorder() {
        return inorder(root);
    }

    /**
     * Helper method for inorder, used in {@code inorder()}
     * @param root Root to being inorder traversal at
     * @return List of elements obtained by inorder traversal
     */

    private List<T> inorder(BSTNode<T> root) {
        if (root == null) {
            return new ArrayList<T>();
        }

        List<T> inList = inorder(root.getLeft());
        inList.add(root.getData());
        inList.addAll(inorder(root.getRight()));

        return inList;
    }

    /**
     * Should run in O(n).
     *
     * @return a postorder traversal of the tree
     */
    public List<T> postorder() {
        return postorder(root);
    }

    /**
     * Helper method for postorder, used in {@code postorder()}
     * @param root Root to being postorder traversal at
     * @return List of elements obtained by postorder traversal
     */
    private List<T> postorder(BSTNode<T> root) {
        if (root == null) {
            return new ArrayList<T>();
        }

        List<T> postList = postorder(root.getLeft());
        postList.addAll(postorder(root.getRight()));
        postList.add(root.getData());

        return postList;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * To do this, add the root node to a queue. Then, while the queue isn't
     * empty, remove one node, add its data to the list being returned, and add
     * its left and right child nodes to the queue. If what you just removed is
     * {@code null}, ignore it and continue with the rest of the nodes.
     *
     * Should run in O(n).
     *
     * @return a level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> levelList = new ArrayList<>();

        if (size == 0) {
            return levelList;
        }

        Queue<BSTNode<T>> nodeQueue =
                new ArrayBlockingQueue<>(this.size);

        nodeQueue.add(root);

        while (!nodeQueue.isEmpty()) {
            int rowSize = nodeQueue.size();

            while (rowSize-- > 0) {
                BSTNode<T> curr = nodeQueue.remove();
                if (curr.getLeft() != null) {
                    nodeQueue.add(curr.getLeft());
                }

                if (curr.getRight() != null) {
                    nodeQueue.add(curr.getRight());
                }

                levelList.add(curr.getData());
            }
        }

        return levelList;
    }

    /**
     * Clears the tree.
     *
     * Should run in O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Calculate and return the height of the root of the tree. A node's
     * height is defined as {@code max(left.height, right.height) + 1}. A leaf
     * node has a height of 0 and a null child should be -1.
     *
     * Should be calculated in O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return height(root);
    }

    /**
     * Helper method for height(), used in {@code height()}
     * @param root Root of BST to find height for
     * @return Height of BST
     */

    private int height(BSTNode<T> root) {
        if (root == null) {
            return -1;
        }

        return Math.max(height(root.getLeft()), height(root.getRight())) + 1;
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the number of elements in the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * THIS METHOD IS ONLY FOR TESTING PURPOSES.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}
