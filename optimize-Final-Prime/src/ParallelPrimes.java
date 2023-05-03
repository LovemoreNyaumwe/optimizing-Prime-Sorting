import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelPrimes {

    // replace this string with your team name
    public static final String TEAM_NAME = "Three Amigos";
    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int N_THREADS = Runtime.getRuntime().availableProcessors();

    // Apply the sieve of Eratosthenes to find primes. This
    // procedure partitions the sieving task up into several
    // blocks, where each block isPrime stores boolean values
    // associated with ROOT_MAX consecutive numbers. Note that
    // partitioning the problem in this way is necessary because
    // we cannot create a boolean array of size MAX_VALUE.
    public static void optimizedPrimes(int[] primes) {
        // compute small prime values
        int[] smallPrimes = Primes.getSmallPrimes();
        int nPrimes = primes.length;


        // write small primes to primes
        int count = 0;
        int minSize = Math.min(nPrimes, smallPrimes.length);
        for (; count < minSize; count++) {
            primes[count] = smallPrimes[count];
        }

        // check if we've already filled primes, and return if so
        if (nPrimes == minSize) {
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
        int i = 0;
        List<Future<boolean[]>> results = new ArrayList<>();
        for (long curBlock = Primes.ROOT_MAX; curBlock < MAX_VALUE; curBlock += Primes.ROOT_MAX) {
            results.add(pool.submit(new parallelPrimeTask(i, curBlock)));
            i++;
        }

        try {
            long curBlock = Primes.ROOT_MAX;
            for (Future<boolean[]> result : results) {
                int index = 0;
                for(boolean res : result.get()) {
                    if(res && count < nPrimes) {
                        primes[count++] = (int) curBlock + index;
                    }
                    index++;
                }
                curBlock += Primes.ROOT_MAX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pool.shutdown();

    }

}