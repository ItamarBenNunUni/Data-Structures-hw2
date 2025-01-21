import java.time.Duration;
import java.time.Instant;

public class FibonaccoHeapExperiment {

    public static void FirstExperiment(int n) {
        Instant start = Instant.now();
        // Code here
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Elapsed time in milliseconds: " + duration.toMillis());
    }

    public static void SecondExperiment(int n) {
        Instant start = Instant.now();
        // Code here
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Elapsed time in milliseconds: " + duration.toMillis());
    }

    public static void ThirdExperiment(int n) {
        Instant start = Instant.now();
        // Code here
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Elapsed time in milliseconds: " + duration.toMillis());
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            int n = (int) (Math.pow(3, i + 7) - 1);
            FirstExperiment(n);
            SecondExperiment(n);
            ThirdExperiment(n);
        }
    }
}
