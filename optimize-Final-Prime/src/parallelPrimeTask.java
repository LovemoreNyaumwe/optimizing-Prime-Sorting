import java.util.Arrays;
import java.util.concurrent.Callable;


// A Callable class that calculates prime numbers in a block
class parallelPrimeTask implements Callable<boolean[]> {
    long curBlock;

    // Constructor to set the current block
    public parallelPrimeTask(long curBlock){
        this.curBlock = curBlock;
    }

    // Call method that returns the prime numbers in a boolean array
    @Override
    public boolean[] call() throws Exception {
        boolean[] isPrime = new boolean[Primes.ROOT_MAX];
        primeBlock(isPrime, Primes.getSmallPrimes(), (int) curBlock);
        return isPrime;
    }

    // Method to search for primes in a block of numbers using the Sieve of Eratosthenes algorithm
    private static void primeBlock(boolean[] isPrime, int[] smallPrimes, int start) {
        Arrays.fill(isPrime, true);

        // Mark the non-prime numbers in the block
        for (int p : smallPrimes) {
            int i = (start % p == 0) ? start : p * (1 + start / p);
            i -= start;

            while (i < isPrime.length) {
                isPrime[i] = false;
                i += p;
            }
        }
    }
}
