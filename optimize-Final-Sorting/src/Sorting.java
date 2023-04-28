import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class Sorting {
    // replace with your 
    public static final String TEAM_NAME = "baseline";
    
    /**
     * Sorts an array of doubles in increasing order. This method is a
     * single-threaded baseline implementation.
     *
     * @param data   the array of doubles to be sorted
     */
    public static void baselineSort (float[] data) {
	Arrays.sort(data, 0, data.length);
    }

    /**
     * Sorts an array of doubles in increasing order. This method is a
     * multi-threaded optimized sorting algorithm. For large arrays (e.g., arrays of size at least 1 million) it should be significantly faster than baselineSort.
     *
     * @param data   the array of doubles to be sorted
     */
    // public static void parallelSort (float[] data) {

	// // replace this with your method!
	
	// baselineSort(data);
	
    // }

// This method sorts a float array using parallel quicksort algorithm

public static void parallelSort(float[] data) {
    // Create a new ForkJoinPool
    ForkJoinPool pool = new ForkJoinPool();
    // Invoke the SortTask to sort the data array
    pool.invoke(new SortTask(data, 0, data.length - 1));
}


private static class SortTask extends RecursiveAction {
    private static final int THRESHOLD = 10000; // threshold for parallelism
    private float[] data;
    private int low;
    private int high;

    public SortTask(float[] data, int low, int high) {
        this.data = data;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (high - low < THRESHOLD) {
            Arrays.sort(data, low, high + 1);
        } else {
            int pivotIndex = lomutoPartition(data, low, high);
            SortTask left = new SortTask(data, low, pivotIndex - 1);
            SortTask right = new SortTask(data, pivotIndex + 1, high);
            invokeAll(left, right);
        }
    }

    private static int lomutoPartition(float[] data, int low, int high) {
        int pivotIndex = ThreadLocalRandom.current().nextInt(low, high + 1);
        float pivot = data[pivotIndex];
        swap(data, pivotIndex, high); // move pivot to the end
        int i = low; // index of last element smaller than pivot
        for (int j = low; j < high; ++j) {
            if (data[j] < pivot) {
                swap(data, i, j);
                ++i;
            }
        }
        swap(data, i, high); // move pivot to its final position
        return i;
    }

    private static void swap(float[] data, int i, int j) {
        float temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}


    /**
     * Determines if an array of doubles is sorted in increasing order.
     *
     * @param   data  the array to check for sortedness
     * @return        `true` if the array is sorted, and `false` otherwise
     */
    public static boolean isSorted (float[] data) {
	double prev = data[0];

	for (int i = 1; i < data.length; ++i) {
	    if (data[i] < prev) {
		return false;
	    }

	    prev = data[i];
	}

	return true;
    }
}
