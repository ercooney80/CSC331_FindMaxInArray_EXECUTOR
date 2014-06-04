package findmaxinarray;

/**
 * @author: Edward Cooney 
 * File: Prog1.java 
 * Section: CSC331 
 * Date: 03/14/2014
 * Notes: Use a series of thread pools to perform three step operation to find the
 * maximum value in an array. 
 * 1. Use n threads to initial a flag array to all 1's 
 * 2. Use n * (n-1) / 2 threads to compare two elements of the integer array
 * placing a 0 at the index of the corresponding flag array that holds the
 * lesser integer value. 
 * 3. Use n threads to determine which index of the flag array holds the 
 * remaining 1 value. Print the index value and the value that corresponds to 
 * that index in the integer array.
 * 
 * Sample Input: FindMaxInArray.java 9 3 1 7 4 11 5 9 20 13
 * Sample Output: Maximum = 20
 *                Location = 7
 */

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FindMaxInArray {



    /**
     * Run n threads to initialize the token array to all 1's
     *
     * @param flagArray Uninitialized token array
     * @throws InterruptedException
     * @throws ExecutionException
     */
    static int[] initializeFlagArray(final int[] inputValues)
            throws InterruptedException {
        /* Build the thread pool with the n flagArrayInitializer Objects */
        final int[] flagArray = new int[inputValues.length];
        final ExecutorService executor = Executors.newFixedThreadPool(flagArray.length);
        for (int i = 0; i < flagArray.length; i++) {
            final int index = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    //System.out.println(Thread.currentThread());
                    flagArray[index] = 1;
                }
            });
            
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        return flagArray;
    }


    /**
     * Run y Threads to perform y comparisons of two unique elements from the
     * array Places a value of 0 in the token array at the index of the smaller
     * value
     *
     * @param inputValues The array of input values
     * @param flagArray The initialized token array
     * @throws InterruptedException
     * @throws ExecutionException
     */
    static void findMaximumInArray(final int[] inputValues, final int[] flagArray)
            throws InterruptedException {
        /* Build the thread pool with the n TokenArrayInitializer Objects */
        final ExecutorService executor = Executors.newFixedThreadPool((inputValues.length * (inputValues.length - 1) / 2));
        int end = 1;
        for (int i = 0; i < inputValues.length - 1; i++) {
            for (int j = end; j < inputValues.length; j++) {
                final int indexI = i;
                final int indexJ = j;
                executor.submit(new Runnable() {
                    
                     @Override
                    public void run() {
                        //System.out.println(Thread.currentThread());
                        int lesser;
                        if (Integer.compare(inputValues[indexI], inputValues[indexJ]) < 0) {
                            flagArray[indexI] = 0;
                            lesser = indexI;
                        } else {
                            flagArray[indexJ] = 0;
                            lesser = indexJ;
                        }
                        System.out.printf("Thread T(%d,%d) compares x[%1$d] = %d "
                                + "and x[%2$d] = %d, and writes 0 into w[%d]\n", 
                                indexI, indexJ, flagArray[indexI], 
                                flagArray[indexJ], lesser);
                    }
                });
            }
            ++end;
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    /**
     * Run n threads to print the maximum number in array and corresponding
     * index
     *
     * @param inputValues Input value array
     * @param flagArray Token holding array - Single location 1 value marks max
     * @throws InterruptedException
     * @throws ExecutionException
     */
    static void printMaximumInArray(final int[] inputValues, final int[] flagArray)
            throws InterruptedException {
        /* Build the thread pool with the n TokenArrayInitializer Objects */
        final ExecutorService executor = Executors.newFixedThreadPool(flagArray.length);
        for (int i = 0; i < flagArray.length; i++) {
            final int index = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    //System.out.println(Thread.currentThread());
                    if (flagArray[index] == 1) {
                        System.out.println("Maximum                = " + inputValues[index]);
                        System.out.println("Location               = " + index);
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

     /**
     * Parse the command line args String[] into an int[]
     *
     * @param input Command Line args
     * @return int[] Array one less than the input (First element is num of
     * args)
     */
    static int[] parseArguments(String[] input) {
        int[] inputValues = new int[input.length - 1];
        
        for (int i = 0; i < inputValues.length; i++) {
            try {
                inputValues[i] = Integer.parseInt(input[i + 1]);
            } catch (NumberFormatException nfe) {
                System.out.println("Conversion Error: " + nfe.getMessage());
                return null;
            }
        }
        return inputValues;
    }
    
    /**
     * Print a message to user for any instance where input does not match 
     * parameters set forth in assignment.
     * @return A message describing to the user how to proceed with command
     * line input
     */
    static String printProgramOperation() {
        return "This program uses command line input in the form:\n"
                    + "   > Prog1.java n x0 x1 ... xn\n"
                    + "Where n is the number of elements and x0 to xn are unique integers "
                    + "that make up\nthe arrayfrom which the user wants to find maximum.  "
                    + "Comparison array size\nshould be between 3 and 10.\n";
    }

    /**
     * Format arrays for output to console
     *
     * @param array The array to be formatted
     * @return String of array elements comma separated
     */
    static String arrayToString(int[] array) {
        return Arrays.toString(array).replaceAll("[\\[\\],]", "");
    }

    /**
     * Main method to control flow of thread creation and output
     *
     * @param args Command Line arguments
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        if ((args == null) || (args.length < 4) || (args.length > 11)) {
            System.out.println(printProgramOperation());
            return;
        }

        int[] inputValues = parseArguments(args);
        if (inputValues == null) {
            System.out.println(printProgramOperation());
            return;
        }
        
        if (!(inputValues.length == Integer.parseInt(args[0]))){
            System.out.println(printProgramOperation());
            return;
        }
        
        System.out.println("Number of input values = " + inputValues.length);
        System.out.println("Input values         x = " + arrayToString(inputValues));

        int[] flagArray = initializeFlagArray(inputValues);
        System.out.println("After initialization w = " + arrayToString(flagArray));
        findMaximumInArray(inputValues, flagArray);
        System.out.println("After step 2         w = " + arrayToString(flagArray));
        printMaximumInArray(inputValues, flagArray);
        System.out.println("All Done!");
    }

}
