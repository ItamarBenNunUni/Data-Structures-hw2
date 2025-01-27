import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

public class FibonacciHeapTest {
	private static Map<String, Integer> methodNames = new HashMap<>();

	private static String fibHeapToString(FibonacciHeap heap) {
		Integer minKey = heap.findMin() == null ? null : heap.findMin().key;
		return "FibonacciHeap(" + "minKey=" + minKey + ", size=" + heap.size() + ", numTrees=" + heap.numTrees()
				+ ", totalCuts=" + heap.totalCuts() + ", totalLinks=" + heap.totalLinks() + ")";
	}

	private static void heapStateTest(TestSuite suite, FibonacciHeap heap, Integer minKey, int size,
			int numTrees, int totalCuts, int totalLinks) {
		String callingFunctionName = Thread.currentThread().getStackTrace()[2].getMethodName();
		methodNames.putIfAbsent(callingFunctionName, 0);
		methodNames.put(callingFunctionName, methodNames.get(callingFunctionName) + 1);

		String testName = callingFunctionName + "[" + methodNames.get(callingFunctionName) + "]";

		System.out.println();
		int currentFailures = suite.getFailedCount();
		if (null == minKey) {
			suite.test(heap.findMin() == null, testName + " - min key");
		} else {
			suite.test(heap.findMin().key == minKey, testName + " - min key");
		}
		suite.test(heap.size() == size, testName + " - size");
		suite.test(heap.numTrees() == numTrees, testName + " - numTrees");
		suite.test(heap.totalCuts() == totalCuts, testName + " - totalCuts");
		suite.test(heap.totalLinks() == totalLinks, testName + " - totalLinks");

		if (suite.getFailedCount() > currentFailures) {
			System.out.println("Expected: minKey=" + minKey + ", size=" + size + ", numTrees=" + numTrees
					+ ", totalCuts=" + totalCuts + ", totalLinks=" + totalLinks);
			System.out.println("Got: " + fibHeapToString(heap) + "\n------");
		}
	}

	private static void testEmptyHeapMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		suite.test(heap.findMin() == null, "Min is null when empty");
		suite.test(heap.size() == 0, "Empty heap - size");
		suite.test(heap.numTrees() == 0, "Empty heap - numTrees");
	}

	private static void testInsertOnce(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		suite.test(heap.findMin().key == 10, "Insert once - min");
		suite.test(heap.size() == 1, "Insert once - size");
		suite.test(heap.numTrees() == 1, "Insert once - numTrees");
	}

	private static void testInsertChangesMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		heap.insert(5, "");
		suite.test(heap.findMin().key == 5, "Insert changes min");
		suite.test(heap.size() == 2, "Insert changes min - size");
		suite.test(heap.numTrees() == 2, "Insert changes min - numTrees");
	}

	private static void testInsertNotChangesMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(5, "");
		heap.insert(10, "");
		suite.test(heap.findMin().key == 5, "Insert does not change min");
	}

	private static void testInsertSameKeyMultipleTimes(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(5, "");
		heap.insert(10, "");
		heap.insert(5, "other 5");
		heap.insert(5, "yet another 5");
		suite.test(heap.findMin().key == 5, "Insert same key multiple times - numTrees");
		suite.test(heap.numTrees() == 4, "Insert same key multiple times - numTrees");
		suite.test(heap.size() == 4, "Insert same key multiple times - numTrees");
	}

	private static void testDeleteMinEmptyHeap(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		// heap.deleteMin();
		heapStateTest(suite, heap, null, 0, 0, 0, 0);
	}

	private static void testDeleteMinSingleNodeHeap(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(1, "");
		heap.deleteMin();
		heapStateTest(suite, heap, null, 0, 0, 0, 0);
	}

	private static void testDeleteMinNoLinkingNeededNoChildrenToMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(1, "");
		heap.insert(2, "");
		heap.deleteMin();
		heapStateTest(suite, heap, 2, 1, 1, 0, 0);
	}

	private static void testDeleteMinWithLinkingNeededNoChildrenToMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1; i <= 3; i++) {
			heap.insert(i, "");
		}
		heap.deleteMin();
		heapStateTest(suite, heap, 2, 2, 1, 0, 1);
	}

	private static void testDeleteMinWithLotsOfLinkingNeededNoChildrenToMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1; i <= 10; i++) {
			heap.insert(i, "");
		}
		heap.deleteMin();
		heapStateTest(suite, heap, 2, 9, 2, 0, 7);
	}

	private static void testDeleteMinWithLotsOfReveresedLinkingNeededNoChildrenToMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 10; i >= 1; i--) {
			heap.insert(i, "");
		}
		heap.deleteMin();
		heapStateTest(suite, heap, 2, 9, 2, 0, 7);
	}

	private static void testDeleteMinDuplicateMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(5, "");
		heap.insert(5, "");
		heap.insert(5, "");
		heap.insert(5, "");
		heap.insert(5, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 5, 4, 1, 0, 3);
		heap.deleteMin();
		heapStateTest(suite, heap, 5, 3, 2, 2, 3);
		heap.deleteMin();
		heap.deleteMin();
		suite.test(heap.findMin().key == 5, "Duplicate min deletion keeps the min same");
		heap.deleteMin();
		suite.test(heap.findMin() == null, "Duplicate min deletion finish all duplications");
	}

	private static void testEmptyHeapFrom3Nodes(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1; i <= 3; i++) {
			heap.insert(i, "");
		}
		for (int i = 1; i <= 3; i++) {
		}
		heap.deleteMin();
		heapStateTest(suite, heap, 2, 2, 1, 0, 1);
		heap.deleteMin();
		heapStateTest(suite, heap, 3, 1, 1, 1, 1);
		heap.deleteMin();
		heapStateTest(suite, heap, null, 0, 0, 1, 1);
	}

	private static void testMultipleDeleteMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1; i <= 10; i++) {
			heap.insert(i, "");
		}
		heap.deleteMin();
		suite.test(heap.findMin().child.key == 6, "Child points to biggest child from all of its children");
		heap.deleteMin();
		suite.test(heap.findMin().child.key == 10, "Child points to biggest child from all of its children");
		heap.deleteMin();
		suite.test(heap.findMin().child.key == 5, "Child points to biggest child from all of its children");
		heap.deleteMin();
		suite.test(heap.findMin().child.key == 10, "Child points to biggest child from all of its children");
		heapStateTest(suite, heap, 5, 6, 2, 7, 11);
	}

	private static void testMultipleDeleteMinReverse(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		for (int i = 1; i <= 10; i++) {
			heap.insert(i, "");
		}
		for (int i = 1; i <= 4; i++) {
			heap.deleteMin();
		}
		heapStateTest(suite, heap, 5, 6, 2, 7, 11);// TODO: note
	}

	private static void testDecreaseKeyMaxNoViolation(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(1, "");
		heap.insert(2, "");
		heap.insert(3, "");
		heap.insert(10, "");
		FibonacciHeap.HeapNode node50 = heap.insert(50, "");

		heap.deleteMin();
		suite.test(heap.min.child.child == node50, "Check most left child");

		heap.decreaseKey(node50, 20);
		heapStateTest(suite, heap, 2, 4, 1, 0, 3);
	}

	private static void testDecreaseKeyMaxViolation(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(1, "");
		heap.insert(2, "");
		heap.insert(3, "");
		heap.insert(10, "");
		FibonacciHeap.HeapNode node50 = heap.insert(50, "");

		heap.deleteMin();
		heap.decreaseKey(node50, 45);
		heapStateTest(suite, heap, 2, 4, 2, 1, 3);
	}

	private static void testDecreaseKeyDoubleViolation(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(1, "");
		heap.insert(2, "");
		FibonacciHeap.HeapNode node3 = heap.insert(3, "");
		heap.insert(10, "");
		heap.insert(50, "");

		heap.deleteMin();
		heap.decreaseKey(heap.min.child.child, 45);
		heapStateTest(suite, heap, 2, 4, 2, 1, 3);
		suite.test(heap.min.child.next == node3, "Check min next child");

		heap.decreaseKey(node3, 2);
		heapStateTest(suite, heap, 1, 4, 3, 2, 3);
	}

	private static void testDecreaseKeyMultipleMark(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(1, "");
		heap.insert(3, "");
		heap.insert(4, "");
		heap.insert(11, "");
		heap.insert(51, "");

		FibonacciHeap.HeapNode node61 = heap.insert(61, "");
		heap.insert(81, "");
		FibonacciHeap.HeapNode node91 = heap.insert(91, "");
		heap.insert(101, "");

		heap.deleteMin();

		heap.decreaseKey(node61, 59);
		heapStateTest(suite, heap, 2, 8, 2, 1, 7);

		heap.decreaseKey(node91, 90);
		heapStateTest(suite, heap, 1, 8, 3, 2, 7);
	}

	private static void testDecreaseKeyMultipleMarkNotMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(0, "");

		heap.insert(10, "");
		FibonacciHeap.HeapNode node20 = heap.insert(20, "");
		heap.insert(30, "");
		heap.insert(40, "");
		heap.insert(50, "");
		heap.insert(60, "");
		heap.insert(70, "");
		heap.insert(80, "");

		heap.insert(110, "");
		FibonacciHeap.HeapNode node120 = heap.insert(120, "");
		heap.insert(130, "");
		heap.insert(140, "");
		heap.insert(150, "");
		FibonacciHeap.HeapNode node160 = heap.insert(160, "");
		FibonacciHeap.HeapNode node170 = heap.insert(170, "");
		heap.insert(180, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 10, 16, 1, 0, 15);

		heap.decreaseKey(node20, 15);
		heapStateTest(suite, heap, 5, 16, 2, 1, 15);
		heap.decreaseKey(node120, 15);
		heapStateTest(suite, heap, 5, 16, 3, 2, 15);
		heap.decreaseKey(node160, 15);
		heapStateTest(suite, heap, 5, 16, 4, 3, 15);

		// Cuscading Cuts
		heap.decreaseKey(node170, 35);
		heapStateTest(suite, heap, 5, 16, 7, 6, 15);

	}

	private static void testDecreaseKeyNewMinimum(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(1, "");
		heap.insert(4, "");
		heap.insert(5, "");
		heap.insert(10, "");
		FibonacciHeap.HeapNode node53 = heap.insert(53, "");

		heap.deleteMin();
		suite.test(heap.min.child.child == node53, "given 4 elements, max is most left child");

		heap.decreaseKey(node53, 50);
		heapStateTest(suite, heap, 3, 4, 2, 1, 3);
	}

	private static void testDecreaseKeyNewMinimumNoParent(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(4, "");
		heap.insert(5, "");
		heap.insert(10, "");
		FibonacciHeap.HeapNode node53 = heap.insert(53, "");
		heapStateTest(suite, heap, 4, 4, 4, 0, 0);

		heap.decreaseKey(node53, 50);
		heapStateTest(suite, heap, 3, 4, 4, 0, 0);
	}

	private static void testDecreaseKeyCutWithSubTree(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();

		heap.insert(0, "");

		heap.insert(10, "");
		heap.insert(20, "");
		FibonacciHeap.HeapNode node30 = heap.insert(30, "");
		heap.insert(40, "");
		heap.insert(50, "");
		heap.insert(60, "");
		FibonacciHeap.HeapNode node70 = heap.insert(70, "");
		heap.insert(80, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 10, 8, 1, 0, 7);

		heap.decreaseKey(node70, 25);
		heapStateTest(suite, heap, 10, 8, 2, 1, 7);

		heap.decreaseKey(node30, 25);
		heapStateTest(suite, heap, 5, 8, 3, 2, 7);
	}

	private static void testDeleteSingleElement(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.delete(heap.insert(10, ""));
		heapStateTest(suite, heap, null, 0, 0, 0, 0);
	}

	private static void testDeleteBeforeDeleteMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		heap.insert(20, "");
		FibonacciHeap.HeapNode node30 = heap.insert(30, "");
		heap.insert(40, "");

		heap.delete(node30);
		heapStateTest(suite, heap, 10, 3, 3, 0, 0);
		heap.deleteMin();
		heapStateTest(suite, heap, 20, 2, 1, 0, 1);
		heap.deleteMin();
		heapStateTest(suite, heap, 40, 1, 1, 1, 1);
		heap.deleteMin();
		heapStateTest(suite, heap, null, 0, 0, 1, 1);
	}

	private static void testDeleteMinElement(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		FibonacciHeap.HeapNode minNode = heap.insert(5, "");
		heap.insert(20, "");

		heap.delete(minNode);

		heapStateTest(suite, heap, 10, 2, 1, 0, 1);
	}

	private static void testDeleteNonMinElement(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		FibonacciHeap.HeapNode middleNode = heap.insert(10, "");
		heap.insert(5, "");
		heap.insert(20, "");

		heap.delete(middleNode);

		heapStateTest(suite, heap, 5, 2, 2, 0, 0);
	}

	private static void testDeleteWithChildrenTheMinimum(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		FibonacciHeap.HeapNode gonnaBeMinNode = heap.insert(5, "");
		heap.insert(20, "");
		heap.insert(3, "three");

		heap.deleteMin();

		heap.delete(gonnaBeMinNode);
		heapStateTest(suite, heap, 10, 2, 1, 1, 2);
	}

	private static void testDeleteWithLotsOfChildrenTheMinimum(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(0, "");

		FibonacciHeap.HeapNode gonnaBeMinNode = heap.insert(10, "");
		heap.insert(20, "");
		heap.insert(30, "");
		heap.insert(40, "");
		heap.insert(50, "");
		heap.insert(60, "");
		heap.insert(70, "");
		heap.insert(80, "");
		heap.insert(90, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 10, 9, 2, 0, 7);

		heap.delete(gonnaBeMinNode);
		heapStateTest(suite, heap, 20, 8, 1, 3, 10);
	}

	private static void testDeleteWithLotsOfChildrenMiddleNode(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(0, "");

		heap.insert(10, "");
		heap.insert(20, "");
		heap.insert(30, "");
		heap.insert(40, "");
		FibonacciHeap.HeapNode middleNode = heap.insert(50, "");
		heap.insert(60, "");
		heap.insert(70, "");
		heap.insert(80, "");
		heap.insert(90, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 10, 9, 2, 0, 7);

		heap.delete(middleNode);
		heapStateTest(suite, heap, 10, 8, 4, 3, 7);
	}

	private static void testDeleteMaxAllTheWay(TestSuite suite) {
		Stack<FibonacciHeap.HeapNode> nodes = new Stack<>();
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(0, "");

		nodes.add(heap.insert(10, ""));
		nodes.add(heap.insert(20, ""));
		nodes.add(heap.insert(30, ""));
		nodes.add(heap.insert(40, ""));
		nodes.add(heap.insert(50, ""));
		nodes.add(heap.insert(60, ""));
		nodes.add(heap.insert(70, ""));
		nodes.add(heap.insert(80, ""));
		nodes.add(heap.insert(90, ""));

		heap.deleteMin();
		heapStateTest(suite, heap, 10, 9, 2, 0, 7);

		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 8, 1, 0, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 7, 1, 1, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 6, 1, 2, 7);

		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 5, 2, 4, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 4, 1, 4, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 3, 1, 5, 7);

		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 2, 1, 6, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, 10, 1, 1, 7, 7);
		heap.delete(nodes.pop());
		heapStateTest(suite, heap, null, 0, 0, 7, 7);
	}

	private static void testDeleteWithChildrenNeverMin(TestSuite suite) {
		FibonacciHeap heap = new FibonacciHeap();
		heap.insert(10, "");
		heap.insert(5, "");
		FibonacciHeap.HeapNode node20 = heap.insert(20, "");
		heap.insert(3, "");
		heap.insert(30, "");
		heap.insert(90, "");

		heap.deleteMin();
		heapStateTest(suite, heap, 5, 5, 2, 0, 3);

		heap.delete(node20);
		heapStateTest(suite, heap, 5, 4, 3, 2, 3);
	}

	private static void testDeleteMin(TestSuite suite) {
		testDeleteMinEmptyHeap(suite);
		testDeleteMinSingleNodeHeap(suite);
		testDeleteMinNoLinkingNeededNoChildrenToMin(suite);
		testDeleteMinWithLinkingNeededNoChildrenToMin(suite);
		testDeleteMinWithLotsOfLinkingNeededNoChildrenToMin(suite);
		testDeleteMinWithLotsOfReveresedLinkingNeededNoChildrenToMin(suite);
		testDeleteMinDuplicateMin(suite);
		testEmptyHeapFrom3Nodes(suite);
		testMultipleDeleteMin(suite);
		testMultipleDeleteMinReverse(suite);
	}

	private static void testInsert(TestSuite suite) {
		testInsertOnce(suite);
		testInsertChangesMin(suite);
		testInsertNotChangesMin(suite);
		testInsertSameKeyMultipleTimes(suite);
	}

	private static void testDecreaseKey(TestSuite suite) {
		testDecreaseKeyMaxNoViolation(suite);
		testDecreaseKeyMaxViolation(suite);
		// testDecreaseKeyDoubleViolation(suite);
		testDecreaseKeyMultipleMark(suite);
		testDecreaseKeyMultipleMarkNotMin(suite);
		testDecreaseKeyNewMinimum(suite);
		testDecreaseKeyNewMinimumNoParent(suite);
		testDecreaseKeyCutWithSubTree(suite);
	}

	private static void testDelete(TestSuite suite) {
		testDeleteSingleElement(suite);
		testDeleteBeforeDeleteMin(suite);
		testDeleteMinElement(suite);
		testDeleteNonMinElement(suite);
		testDeleteWithChildrenTheMinimum(suite);
		testDeleteWithLotsOfChildrenTheMinimum(suite);
		testDeleteWithLotsOfChildrenMiddleNode(suite);
		testDeleteMaxAllTheWay(suite);
		testDeleteWithChildrenNeverMin(suite);
	}

	private static void testMeldTwoEmptyHeaps(TestSuite suite) {
		FibonacciHeap heap1 = new FibonacciHeap();
		FibonacciHeap heap2 = new FibonacciHeap();
		heap1.meld(heap2);
		heapStateTest(suite, heap1, null, 0, 0, 0, 0);
	}

	private static void testMeldEmptyAndFullHeap(TestSuite suite) {
		FibonacciHeap heap1 = new FibonacciHeap();
		FibonacciHeap heap2 = new FibonacciHeap();
		for (int i = 1; i <= 10; i++) {
			heap2.insert(i, "");
		}
		heap1.meld(heap2);
		heapStateTest(suite, heap1, 1, 10, 10, 0, 0);
	}

	private static void testMeldFullAndEmptyHeap(TestSuite suite) {
		FibonacciHeap heap1 = new FibonacciHeap();
		FibonacciHeap heap2 = new FibonacciHeap();
		for (int i = 1; i <= 10; i++) {
			heap1.insert(i, "");
		}
		heap1.meld(heap2);
		heapStateTest(suite, heap1, 1, 10, 10, 0, 0);
	}

	private static void testMeldTwoFullHeaps(TestSuite suite) {
		FibonacciHeap heap1 = new FibonacciHeap();
		FibonacciHeap heap2 = new FibonacciHeap();
		for (int i = 6; i <= 10; i++) {
			heap1.insert(i, "");
		}
		heap1.deleteMin();
		for (int i = 1; i <= 5; i++) {
			heap2.insert(i, "");
		}
		heap1.meld(heap2);
		heapStateTest(suite, heap1, 1, 9, 6, 0, 3);
	}

	private static void testMeld(TestSuite suite) {
		testMeldTwoEmptyHeaps(suite);
		testMeldEmptyAndFullHeap(suite);
		testMeldFullAndEmptyHeap(suite);
		testMeldTwoFullHeaps(suite);
	}

	public static void tests(TestSuite suite) {
		testEmptyHeapMin(suite);
		testInsert(suite);
		testDeleteMin(suite);
		testDecreaseKey(suite);
		testDelete(suite);
		testMeld(suite);
	}

	public static void main(String[] args) {
		TestSuite suite = new TestSuite();

		try {
			tests(suite);
		} catch (Exception e) {
			suite.test(false, "One test raised an exception");
			e.printStackTrace();
		} finally {
			System.exit(suite.summary());
		}
	}
}
