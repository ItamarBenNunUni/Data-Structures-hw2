/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap {
    public HeapNode start;
    public HeapNode min;
    public int sizeTrees;
    public int size;
    public int cuts;
    public int links;

    /**
     *
     * Constructor to initialize an empty heap.
     *
     */
    public FibonacciHeap() {
        start = null;
        min = null;
        size = 0;
        sizeTrees = 0;
        cuts = 0;
        links = 0;
    }

    /**
     *
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapNode.
     *
     */
    public HeapNode insert(int key, String info) {
        if (key < 0 || info == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        HeapNode new_node = new HeapNode();
        new_node.key = key;
        new_node.info = info;
        new_node.rank = 0;
        new_node.mark = false;
        new_node.child = null;
        new_node.parent = null;
        insert_node(new_node);
        return new_node; // should be replaced by student code
    }

    // add contract
    public static void insert_in_between(HeapNode existing, HeapNode new_node) {
        if (existing == null || new_node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        HeapNode ex_next = existing.next;
        existing.next = new_node;
        ex_next.prev = new_node;
        new_node.prev = existing;
        new_node.next = ex_next;
    }

    /**
     *
     * Return the minimal HeapNode, null if empty.
     *
     */
    public HeapNode findMin() {
        return min;
    }

    /**
     *
     * Delete the minimal item
     *
     */
    public void deleteMin() {
        delete(min);
    }

    // add contract
    public void successiveLinking() {
        if (size == 0) {
            start = null;
            min = null;
            return;
        }
        int maxRank = (int) Math.ceil(Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2));
        HeapNode[] buckets = new HeapNode[maxRank + 1];
        HeapNode curr = start;
        do {
            HeapNode new_node = new HeapNode();
            new_node.key = curr.key;
            new_node.info = curr.info;
            new_node.child = curr.child;
            new_node.rank = curr.rank;
            new_node.next = new_node;
            new_node.prev = new_node;
            if (buckets[new_node.rank] == null) {
                buckets[new_node.rank] = new_node;
            } else {
                HeapNode min_of_two = new_node;
                while (min_of_two.rank < buckets.length && buckets[min_of_two.rank] != null) {
                    HeapNode max_of_two = buckets[min_of_two.rank];
                    if (buckets[min_of_two.rank].key < new_node.key) {
                        min_of_two = buckets[min_of_two.rank];
                        max_of_two = new_node;
                    }
                    if (min_of_two.child != null) {
                        FibonacciHeap.insert_in_between(min_of_two.child, max_of_two);
                    } else {
                        min_of_two.child = max_of_two;
                    }
                    max_of_two.parent = min_of_two;
                    buckets[min_of_two.rank] = null;
                    min_of_two.rank++;
                    links++;
                }
                buckets[min_of_two.rank] = min_of_two;
            }
            curr = curr.next;
        } while (curr != start);
        //
        FibonacciHeap fh = buckets_to_heap(buckets);
        this.start = fh.start;
        this.min = fh.min;
        this.sizeTrees = fh.sizeTrees;
    }

    // add contract
    public FibonacciHeap buckets_to_heap(HeapNode[] buckets) {
        if (buckets == null) {
            throw new IllegalArgumentException("Buckets is null");
        }
        FibonacciHeap fh = new FibonacciHeap();
        for (HeapNode heapNode : buckets) {
            if (heapNode != null) {
                fh.insert_node(heapNode);
            }
        }
        return fh;
    }

    // add contract
    public void insert_node(HeapNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        if (start == null) {
            node.next = node;
            node.prev = node;
            start = node;
            min = node;
        } else {
            FibonacciHeap.insert_in_between(start, node);
        }
        //
        if (node.key < min.key) {
            min = node;
        }
        sizeTrees++;
        size++;
    }

    /**
     *
     * pre: 0<diff<x.key
     *
     * Decrease the key of x by diff and fix the heap.
     *
     */
    public void decreaseKey(HeapNode x, int diff) {
        if (x == null || diff < 0 || (diff != Integer.MAX_VALUE && x.key - diff < 0)) {
            throw new IllegalArgumentException("Invalid arguments",
                    new Throwable("x.key: " + x.key + ", diff: " + diff));
        }
        x.key -= diff;
        if (x.parent != null && x.parent.key > x.key) {
            if (!x.parent.mark) { // cut
                cut(x, x.parent);
            } else { // cascading cut
                cascading_cut(x, x.parent);
            }
        }
        if (x.key < min.key) {
            min = x;
        }
    }

    // add contract
    public void cut(HeapNode node, HeapNode parent) {
        if (node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        node.parent = null;
        node.mark = false;
        parent.rank--;
        if (node.next == node) {
            parent.child = null;
        } else {
            parent.child = node.next;
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    // add contract
    public void cascading_cut(HeapNode node, HeapNode parent) {
        if (node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        cut(node, parent);
        if (parent.parent != null) {
            if (!parent.mark) {
                parent.mark = true;
            } else {
                cascading_cut(parent, parent.parent);
            }
        }
    }

    /**
     *
     * Delete the x from the heap.
     *
     */
    public void delete(HeapNode x) {
        if (x == null) {
            throw new IllegalArgumentException("Node is null");
        }
        boolean delete_min = x == min;
        HeapNode previous_min = min; // for the case of deleting node which is not the min
        if (!delete_min) {
            decreaseKey(x, Integer.MAX_VALUE);
        }
        if (x.child != null) { // x has children
            int x_rank = x.rank;
            HeapNode first_child = x.child;
            HeapNode last_child = first_child.prev;
            if (x.next == x) { // x is the root of the only tree in the heap
                start = first_child;
            } else { // x is not the root of the only tree in the heap - there are more trees
                x.prev.next = x.child;
                first_child.prev = x.prev;
                last_child.next = x.next;
                x.next.prev = last_child;
                if (x == start) {
                    start = start.next;
                }
            }
            x = null;
            min = previous_min;
            size--;
            sizeTrees = sizeTrees - 1 + x_rank;
        } else { // x has no children
            x.prev.next = x.next;
            x.next.prev = x.prev;
            if (x == start) {
                start = start.next;
            }
            x = null;
            min = previous_min;
            size--;
            sizeTrees--;
        }
        if (delete_min) {
            successiveLinking();
        }
    }

    /**
     *
     * Return the total number of links.
     *
     */
    public int totalLinks() {
        return links; // should be replaced by student code
    }

    /**
     *
     * Return the total number of cuts.
     *
     */
    public int totalCuts() {
        return cuts; // should be replaced by student code
    }

    /**
     *
     * Meld the heap with heap2
     *
     */
    public void meld(FibonacciHeap heap2) {
        if (heap2 == null) {
            throw new IllegalArgumentException("Heap is null");
        }
        cuts += heap2.cuts;
        links += heap2.links;
        size += heap2.size;
        sizeTrees += heap2.sizeTrees;
        //
        HeapNode heap2_start = heap2.start;
        HeapNode heap2_last = heap2.start.prev;
        HeapNode last = start.prev;
        // linking this's last with heap2's start
        last.next = heap2_start;
        heap2_start.prev = last;
        // linking this's start with heap2's last
        start.prev = heap2_last;
        heap2_last.next = start;
        //
        if (heap2.min.key < min.key) {
            min = heap2.min;
        }
        //
        heap2 = null;
    }

    /**
     *
     * Return the number of elements in the heap
     *
     */
    public int size() {
        return size; // should be replaced by student code
    }

    /**
     *
     * Return the number of trees in the heap.
     *
     */
    public int numTrees() {
        return sizeTrees; // should be replaced by student code
    }

    /**
     * Class implementing a node in a Fibonacci Heap.
     *
     */
    public static class HeapNode {
        public int key;
        public String info;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean mark;
    }
}
