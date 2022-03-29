package cn.blogss.core.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainTestTest {
    ThreadPoolExecutor pool;

    @Before
    public void setUp() throws Exception {

        pool = new ThreadPoolExecutor(2, 5, 5,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void serializable() {
        int[] arr = generateRandomArr(30, 200);
        printArr(arr);
        heapSort(arr);
        printArr(arr);
    }

    private int[] generateRandomArr(int maxLen, int maxNum) {
        int len = (int) (Math.random()*(maxLen+1));
        int[] arr = new int[len];
        for (int i=0;i<len;i++){
            arr[i] = (int) (Math.random()*(maxNum+1))-(int) (Math.random()*(maxNum+1));
        }
        return arr;
    }

    private void printArr(int[] arr) {
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private void heapSort(int[] arr){
        for (int i=0;i<arr.length;i++){
            int index = i;
            while (arr[index] > arr[(index-1)/2]){
                swap(arr, index, (index-1)/2);
                index = (index-1)/2;
            }
        }

        System.out.println("建堆完成：");
        printArr(arr);

        for (int i=arr.length-1;i>0;i--){
            swap(arr,0, i);
            heapfy(arr, 0, i);
        }
    }

    private void heapfy(int[] arr, int index, int heapSize) {
        int left = 2*index + 1;
        while(left < heapSize){
            int max = (left+1) < heapSize ? (arr[left] < arr[left+1] ? (left+1):left) : left;
            if(arr[max] < arr[index]){
                break;
            }
            swap(arr, index, max);
            index = max;
            left = 2*index + 1;
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}