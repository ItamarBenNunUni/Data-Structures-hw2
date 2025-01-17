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
    }

    /**
     * 
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapNode.
     *
     */
    public HeapNode insert(int key, String info) {
        HeapNode new_node = new HeapNode();
        new_node.key = key;
        new_node.info = info;
        new_node.rank = 0;
        new_node.mark = false;
        new_node.child = null;
        new_node.parent = null;
        //
        if (start == null) {
            new_node.next = new_node;
            new_node.prev = new_node;
            start = new_node;
            min = new_node;
        } else {
            FibonacciHeap.insert_in_between(start, new_node);
        }
        //
        if (key < min.key) {
            min = new_node;
        }
        sizeTrees++;
        return new_node; // should be replaced by student code
    }

    public static void insert_in_between(HeapNode existing, HeapNode new_node) {
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
        if (min.next == min) { // min tree is the only tree in the heap
            start = min.child;
            min = min.child;
        } else {
            HeapNode child = min.child;
            HeapNode child_prev = child.prev;
            min.prev.next = min.child;
            child.prev = min.prev;
            child_prev.next = min.next;
            min.next.prev = child_prev;
            if (min == start) {
                start = start.next;
            }
            min = null;
        }
        successiveLinking();
        size--;
    }

    public void successiveLinking() {
        int x = (int) Math.ceil(Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2)); // make sure
        HeapNode[] buckets = new HeapNode[x];
        HeapNode curr = start;
        do {
            if (buckets[curr.rank] == null) {
                buckets[curr.rank] = curr;
            } else {
                HeapNode min_of_two = curr;
                while (buckets[min_of_two.rank] != null) {
                    HeapNode max_of_two = buckets[curr.rank];
                    if (buckets[curr.rank].key < curr.key) {
                        min_of_two = buckets[curr.rank];
                        max_of_two = curr;
                    }
                    if (min_of_two.child != null) {
                        FibonacciHeap.insert_in_between(min_of_two.child, max_of_two);
                    } else {
                        min_of_two.child = max_of_two;
                    }
                    max_of_two.parent = min_of_two;
                    buckets[min_of_two.rank] = null;
                    min_of_two.rank++;
                }
                buckets[min_of_two.rank] = min_of_two;
            }
            curr = curr.next;
        } while (curr != start);
        //
        FibonacciHeap fh = buckets_to_heap(buckets);
        this.min = fh.min;
        this.size = fh.size;
    }

    public FibonacciHeap buckets_to_heap(HeapNode[] buckets) {
        FibonacciHeap fh = new FibonacciHeap();
        int countTrees = 0;
        for (HeapNode heapNode : buckets) {
            if (heapNode != null) {
                countTrees++;
                fh.insert(heapNode.key, heapNode.info);// change this, update size and sizetrees
            }
        }
        return fh;
    }

    /**
     * 
     * pre: 0<diff<x.key
     * 
     * Decrease the key of x by diff and fix the heap.
     * 
     */
    public void decreaseKey(HeapNode x, int diff) {
        return; // should be replaced by student code
    }

    /**
     * 
     * Delete the x from the heap.
     *
     */
    public void delete(HeapNode x) {
        return; // should be replaced by student code
    }

    /**
     * 
     * Return the total number of links.
     * 
     */
    public int totalLinks() {
        return 0; // should be replaced by student code
    }

    /**
     * 
     * Return the total number of cuts.
     * 
     */
    public int totalCuts() {
        return 0; // should be replaced by student code
    }

    /**
     * 
     * Meld the heap with heap2
     *
     */
    public void meld(FibonacciHeap heap2) {
        return; // should be replaced by student code
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
        return 0; // should be replaced by student code
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
