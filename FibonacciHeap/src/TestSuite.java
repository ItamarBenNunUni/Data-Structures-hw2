public class TestSuite {
	public final static String ANSI_RED = "\u001B[31m";
	public final static String ANSI_GREEN = "\u001B[32m";

	public static void colorPrint(String message, String color) {
		final String ANSI_RESET = "\u001B[0m";
		System.err.println(color + message + ANSI_RESET);
	}

	private int testsFailed = 0;
	private int totalTests = 0;

	public TestSuite() {
		this.testsFailed = 0;
		this.totalTests = 0;
	}

	public int getFailedCount() {
		return testsFailed;
	}

	public void test(boolean predicate, String name) {
		totalTests++;
		if (!predicate) {
			colorPrint("!!! FAILED ----- Test: " + name + " ----- FAILED !!!", ANSI_RED);
			testsFailed++;
			return;
		}
		colorPrint("Passed >>> Test: " + name, ANSI_GREEN);
	}

	public int summary() {
		System.out.println();
		System.out.println("-------------- Summary --------------");
		if (testsFailed > 0) {
			colorPrint(testsFailed + "/" + totalTests + " tests failed.", ANSI_RED);
		} else {
			colorPrint("Passed all " + totalTests + " tests.", ANSI_GREEN);
		}
		return testsFailed;
	}
}
