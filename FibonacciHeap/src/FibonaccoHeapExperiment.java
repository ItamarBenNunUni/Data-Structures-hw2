import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FibonaccoHeapExperiment {

    public static void FirstExperiment(int n) {
        double timeSum = 0, sizeSum = 0, linkSum = 0, cutSum = 0, treeSum = 0;
        for (int j = 1; j <= 20; j++) {
            Instant start = Instant.now();
            FibonacciHeap heap = new FibonacciHeap();
            Integer[] keys = new Integer[n];
            for (int i = 1; i <= n; i++) {
                keys[i - 1] = i;
            }
            List<Integer> keysList = Arrays.asList(keys);
            Collections.shuffle(keysList);
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[n];
            for (Integer k : keysList) {
                nodes[k - 1] = heap.insert(k, String.valueOf(k));
            }
            heap.deleteMin();
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            timeSum += duration.toMillis();
            sizeSum += heap.size;
            linkSum += heap.totalLinks();
            cutSum += heap.totalCuts();
            treeSum += heap.numTrees();
        }
        System.out.printf(
                "Average time: %.2f ms, Average size: %.2f, Average links: %.2f, Average cuts: %.2f, Average trees: %.2f%n",
                (timeSum / 20), (sizeSum / 20), (linkSum / 20), (cutSum / 20), (treeSum / 20));
    }

    public static void SecondExperiment(int n) {
        double timeSum = 0, sizeSum = 0, linkSum = 0, cutSum = 0, treeSum = 0;
        for (int j = 1; j <= 20; j++) {
            Instant start = Instant.now();
            FibonacciHeap heap = new FibonacciHeap();
            Integer[] keys = new Integer[n];
            for (int i = 1; i <= n; i++) {
                keys[i - 1] = i;
            }
            List<Integer> keysList = Arrays.asList(keys);
            Collections.shuffle(keysList);
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[n];
            for (Integer k : keysList) {
                nodes[k - 1] = heap.insert(k, String.valueOf(k));
            }
            for (int k = 1; k <= (n / 2); k++) {
                heap.deleteMin();
            }
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            timeSum += duration.toMillis();
            sizeSum += heap.size;
            linkSum += heap.totalLinks();
            cutSum += heap.totalCuts();
            treeSum += heap.numTrees();
        }
        System.out.printf(
                "Average time: %.2f ms, Average size: %.2f, Average links: %.2f, Average cuts: %.2f, Average trees: %.2f%n",
                (timeSum / 20), (sizeSum / 20), (linkSum / 20), (cutSum / 20), (treeSum / 20));
    }

    public static void ThirdExperiment(int n) {
        double timeSum = 0, sizeSum = 0, linkSum = 0, cutSum = 0, treeSum = 0;
        for (int j = 1; j <= 20; j++) {
            Instant start = Instant.now();
            FibonacciHeap heap = new FibonacciHeap();
            Integer[] keys = new Integer[n];
            for (int i = 1; i <= n; i++) {
                keys[i - 1] = i;
            }
            List<Integer> keysList = Arrays.asList(keys);
            Collections.shuffle(keysList);
            FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[n];
            for (Integer k : keysList) {
                nodes[k - 1] = heap.insert(k, String.valueOf(k));
            }
            heap.deleteMin();
            for (int k = nodes.length; k > (Math.pow(2, 5)); k--) {
                heap.delete(nodes[k - 1]);
            }
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            timeSum += duration.toMillis();
            sizeSum += heap.size;
            linkSum += heap.totalLinks();
            cutSum += heap.totalCuts();
            treeSum += heap.numTrees();
        }
        System.out.printf(
                "Average time: %.2f ms, Average size: %.2f, Average links: %.2f, Average cuts: %.2f, Average trees: %.2f%n",
                (timeSum / 1), (sizeSum / 1), (linkSum / 1), (cutSum / 1), (treeSum / 1));
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            int n = (int) (Math.pow(3, i + 7) - 1);
            System.out.println("************************************************************************");
            System.out.println( "i = "+i+", n = "+ n);
            System.out.println("First:");
            FirstExperiment(n);
            System.out.println("Second:");
            SecondExperiment(n);
             System.out.println("Third:");
             ThirdExperiment(n);
        }
    }
}
