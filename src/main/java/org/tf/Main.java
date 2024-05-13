package org.tf;

import org.tf.binomialheap.BinomialHeap;
import org.tf.binomialheap.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BinomialHeap<Integer> binomialHeap = new BinomialHeap<>();
        int count1 = 10000;
        int[] array = ArrayGenerator.generate(count1, 0, 1000000);

        List<Node<Integer>> nodes = new ArrayList<>();
        int[] operationsInsertStatistics = new int[count1]; // 10000
        long[] nanoInsertStatistics = new long[count1];
        for (int i = 0; i < array.length; ++i) {
            long start = System.nanoTime();
            nodes.add(binomialHeap.insert(array[i], array[i]));
            long end = System.nanoTime();
            nanoInsertStatistics[i] = end - start;
            operationsInsertStatistics[i] = BinomialHeap.getOperations();
            BinomialHeap.setOperations(0);
        }

        int count2 = 100;
        int[] operationsDeleteMinStatistics = new int[count2]; // 100
        long[] nanoDeleteMinStatistics = new long[count1];
        for (int i = 0; i < count2; ++i) {
            long start = System.nanoTime();
            binomialHeap.deleteMin();
            long end = System.nanoTime();
            nanoDeleteMinStatistics[i] = end - start;
            operationsDeleteMinStatistics[i] = BinomialHeap.getOperations();
            BinomialHeap.setOperations(0);
        }

        int count3 = 1000;
        int[] operationsDeleteStatistics = new int[count3]; // 1000
        long[] nanoDeleteStatistics = new long[count1];
        Random random = new Random();
        for (int i = 0; i < count3; ++i) {
            Node<Integer> randomNode = nodes.get(random.nextInt(0, nodes.size()));
            nodes.remove(randomNode);
            long start = System.nanoTime();
            binomialHeap.delete(randomNode);
            long end = System.nanoTime();
            nanoDeleteStatistics[i] = end - start;
            operationsDeleteStatistics[i] = BinomialHeap.getOperations();
            BinomialHeap.setOperations(0);
        }

        saveToCSV("operations",
                operationsInsertStatistics,
                operationsDeleteMinStatistics,
                operationsDeleteStatistics);

        System.out.println("Средние значения кол-ва операций: " +
                (Arrays.stream(operationsInsertStatistics).sum() / operationsInsertStatistics.length)
                + " " + (Arrays.stream(operationsDeleteMinStatistics).sum() / operationsDeleteMinStatistics.length)
                + " " + (Arrays.stream(operationsDeleteStatistics).sum() / operationsDeleteStatistics.length));

        System.out.println("Средние значения времени: " +
                (Arrays.stream(nanoInsertStatistics).sum() / nanoInsertStatistics.length)
                + " " + (Arrays.stream(nanoDeleteMinStatistics).sum() / nanoDeleteMinStatistics.length)
                + " " + (Arrays.stream(nanoDeleteStatistics).sum() / nanoDeleteStatistics.length));
    }

    public static void saveToCSV(String pathName, int[]... arrays) { // Можно открыть потом в Excel
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathName))) {
            bufferedWriter.write("Кол-во операций добавления;Кол-во операций удаления мин; Кол-во операций удаления");
            bufferedWriter.newLine();

            int i = 0;
            while (true) {
                StringBuilder line = new StringBuilder();
                boolean flag = false;
                for (int j = 0; j < arrays.length - 1; ++j) {
                    if (i < arrays[j].length) {
                        line.append(arrays[j][i]);
                        flag = true;
                    }
                    line.append(";");
                }
                if (i < arrays[arrays.length - 1].length) {
                    line.append(arrays[arrays.length - 1][i]);
                    flag = true;
                }
                if (!flag) break;
                System.out.println(line);
                bufferedWriter.write(line.toString());
                bufferedWriter.newLine();
                ++i;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
