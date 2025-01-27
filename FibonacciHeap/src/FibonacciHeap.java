// id1: 322520255
// name1: Itamar Ben Nun
// username1: itamarbennun
// id2: 316061787
// name2: Tal Malka
// username2: talmalka2
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

    /**
     *
     * Inserts a node after an existing node as it's next
     *
     */
    public static void insert_after(HeapNode existing, HeapNode new_node) {
        if (existing == null || new_node == null) {
            throw new IllegalArgumentException("Node is null");
        }
        HeapNode ex_prev = existing.prev;
        existing.prev = new_node;
        ex_prev.next = new_node;
        new_node.next = existing;
        new_node.prev = ex_prev;
        new_node.parent = existing.parent;
        if (existing.parent != null) {
            existing.parent.rank++;
        }
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

    /**
     *
     * Performs Consolidating/Successive Linking to fix a head
     *
     */
    public void successiveLinking() {
        // edge case
        if (size == 0) {
            start = null;
            min = null;
            return;
        }
        // build the buckets
        int maxRank = (int) Math.ceil(Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2));
        HeapNode[] buckets = (maxRank == 0) ? new HeapNode[1] : new HeapNode[maxRank];
        HeapNode curr = start;
        do {
            HeapNode currNext = curr.next;
            curr.next = curr;
            curr.prev = curr;
            if (buckets[curr.rank] == null) {
                buckets[curr.rank] = curr;
            } else {// buckets[curr.rank] != null
                int r = curr.rank;
                HeapNode min_node = curr;
                while (buckets[r] != null) {
                    HeapNode existing = buckets[r];
                    HeapNode max_node = (min_node.key <= existing.key) ? existing : min_node;
                    // min_node = (min_node.key >= existing.key) ? existing : min_node;
                    if (max_node == min_node) {
                        min_node = existing;
                    }
                    if (min_node.child != null) {
                        FibonacciHeap.insert_after(min_node.child, max_node);
                    } else {
                        min_node.child = max_node;
                        max_node.parent = min_node;
                        min_node.rank = 1;
                    }
                    buckets[r] = null;
                    r++;
                    links++;
                }
                buckets[r] = min_node;
            }
            curr = currNext;
        } while (curr != start);
        // build a new heap out of the buckets
        FibonacciHeap fh = buckets_to_heap(buckets);
        this.start = fh.start;
        this.min = fh.min;
        this.sizeTrees = fh.sizeTrees;
    }

    /**
     *
     * Turns buckets to a Fibonacci Heap
     *
     */
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

    /**
     *
     * Inserts a new node into the heap, given that complete node
     *
     */
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
            FibonacciHeap.insert_after(start, node);
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
        if (x == null) {
            throw new IllegalArgumentException("Node is null");
        }
        if (diff < 0 || (diff != Integer.MAX_VALUE && x.key - diff < 0)) {
            throw new IllegalArgumentException("Invalid arguments",
                    new Throwable("x.key: " + x.key + ", diff: " + diff));
        }
        x.key -= diff;
        if (x.parent != null && x.parent.key > x.key) {
            cascading_cut(x, x.parent);
        }
        if (x.key < min.key) {
            min = x;
        }
    }

    /**
     *
     * pre: parent != null
     *
     * Cut node from his parent
     *
     */
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
        FibonacciHeap.insert_after(start, node);
        cuts++;
        sizeTrees++;
    }

    /**
     *
     * pre: parent != null
     *
     * Performs Cascading Cuts from node
     *
     */
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
            cuts += x.rank;
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
            HeapNode startChild = x.child;
            HeapNode currChild = startChild;
            do {
                currChild.parent = null;
                currChild = currChild.next;
            } while (currChild != startChild);
            min = previous_min;
            size--;
            sizeTrees = sizeTrees - 1 + x_rank;
        } else { // x has no children
            x.prev.next = x.next;
            x.next.prev = x.prev;
            if (x == start) {
                start = start.next;
            }
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
        if (heap2.start == null) {
            return;
        }
        if (start == null) {
            start = new HeapNode();
            start.key = heap2.start.key;
            start.info = heap2.start.info;
            start.child = heap2.start.child;
            start.next = heap2.start.next;
            start.prev = heap2.start.prev;
            start.parent = heap2.start.parent;
            start.rank = heap2.start.rank;
            start.mark = heap2.start.mark;
            //
            min = new HeapNode();
            min.key = heap2.min.key;
            min.info = heap2.min.info;
            min.child = heap2.min.child;
            min.next = heap2.min.next;
            min.prev = heap2.min.prev;
            min.parent = heap2.min.parent;
            min.rank = heap2.min.rank;
            min.mark = heap2.min.mark;
            //
            cuts = heap2.cuts;
            links = heap2.links;
            size = heap2.size;
            sizeTrees = heap2.sizeTrees;
            //
            heap2.start = null;
            heap2.min = null;
            heap2.size = 0;
            heap2.sizeTrees = 0;
            heap2.cuts = 0;
            heap2.links = 0;
            //
            return;
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
        // min updating
        if (heap2.min.key < min.key) {
            min = heap2.min;
        }
        // heap2 is of no use anymore
        heap2.start = null;
        heap2.min = null;
        heap2.size = 0;
        heap2.sizeTrees = 0;
        heap2.cuts = 0;
        heap2.links = 0;
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
