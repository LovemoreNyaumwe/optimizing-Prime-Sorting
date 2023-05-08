import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelPrimes {

    // Constant variable for the team name
    public static final String TEAM_NAME = "Three Amigos";
    // Constant variable for the maximum value to check for primes
    public static final int MAX_VALUE = Integer.MAX_VALUE;
     // Constant variable for the number of threads available to the program
    public static final int N_THREADS = Runtime.getRuntime().availableProcessors();

    // Method to generate a list of optimized primes
    public static void optimizedPrimes(int[] primes) {
        // Get a list of small primes
        int[] smallPrimes = Primes.getSmallPrimes();


        int nPrimes = primes.length;

        // Copy the small primes to the result array
        int count = 0;
        int minSize = Math.min(nPrimes, smallPrimes.length);
        for (; count < minSize; count++) {
            primes[count] = smallPrimes[count];
        }

        // If the result array is already filled, return
        if (nPrimes == minSize) {
            return;
        }

        // Create a thread pool to calculate primes in parallel
        ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
        List<Future<int[]>> results = new ArrayList<>();

        // Divide the search range into blocks and submit each block to the thread pool
        for (long curBlock = Primes.ROOT_MAX; curBlock < MAX_VALUE; curBlock += Primes.ROOT_MAX*20) {
            results.add(pool.submit(new parallelPrimeTask(curBlock)));
        }

        // Collect the prime numbers calculated by the tasks
        try {
            for (Future<int[]> result : results) {
                for(int res : result.get()) {
                    if(count < nPrimes){
                        primes[count++] = res;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Shut down the thread pool
        pool.shutdown();
    }
}
