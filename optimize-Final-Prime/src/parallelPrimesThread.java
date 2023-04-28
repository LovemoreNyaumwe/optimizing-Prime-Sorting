import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class parallelPrimesThread implements Runnable {
    int[] primes;
    int[] smallPrimes;
    int nPrimes;
    int start;
    long end;

    int count;


    public parallelPrimesThread(int[] primes, int[] smallPrimes, int nPrimes, int start, long end) {
        this.primes = primes;
        this.smallPrimes = smallPrimes;
        this.nPrimes = nPrimes;
        this.start = start;
        this.end = end;
        this.count = start;
    }

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

    @Override
    public void run() {
        // Apply the sieve of Eratosthenes to find primes. This
        // procedure partitions the sieving task up into several
        // blocks, where each block isPrime stores boolean values
        // associated with ROOT_MAX consecutive numbers. Note that
        // partitioning the problem in this way is necessary because
        // we cannot create a boolean array of size MAX_VALUE.
        boolean[] isPrime = new boolean[Primes.ROOT_MAX];
        for (long curBlock = start; curBlock < end; curBlock += Primes.ROOT_MAX) {
            primeBlock(isPrime, smallPrimes, (int) curBlock);
            for (int i = 0; i < isPrime.length && count < end - start; i++) {
                if (isPrime[i]) {
                    primes[count++] = (int) curBlock + i;
                }
            }
        }
    }
}
