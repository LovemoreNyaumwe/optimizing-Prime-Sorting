import java.util.Arrays;
import java.util.concurrent.Callable;

public class parallelPrimeTask implements Callable<boolean[]> {
    long curBlock;
    int id;

    public parallelPrimeTask(int id, long curBlock){
        this.id = id;
        this.curBlock = curBlock;
    }

    @Override
    public boolean[] call() throws Exception {
        // Apply the sieve of Eratosthenes to find primes. This
        // procedure partitions the sieving task up into several
        // blocks, where each block isPrime stores boolean values
        // associated with ROOT_MAX consecutive numbers. Note that
        // partitioning the problem in this way is necessary because
        // we cannot create a boolean array of size MAX_VALUE.
        boolean[] isPrime = new boolean[Primes.ROOT_MAX];
        primeBlock(isPrime, Primes.getSmallPrimes(), (int) curBlock);
        return isPrime;
    }

    // Compute a block of prime values between start and start +
    // isPrime.length. Specifically, after calling this method
    // isPrime[i] will be true if and only if start + i is a prime
    // number, assuming smallPrimes contains all prime numbers of to
    // sqrt(start + isPrime.length).
    private static void primeBlock(boolean[] isPrime, int[] smallPrimes, int start) {

        // initialize isPrime to be all true
        Arrays.fill(isPrime, true);

        for (int p : smallPrimes) {

            // find the next number >= start that is a multiple of p
            int i = (start % p == 0) ? start : p * (1 + start / p);
            i -= start;

            while (i < isPrime.length) {
                isPrime[i] = false;
                i += p;
            }
        }
    }
}