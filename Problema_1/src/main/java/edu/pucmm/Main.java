package edu.pucmm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_COUNT = 4;
    private static final int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int TARGET = 256; // Número a buscar

    public static void main(String[] args) {
        // Inicializar la matriz con valores aleatorios
        //...
//        Math random = new Math();

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                matrix[i][j] = (int) (Math.random() * 1000);
            }
        }
        // Medir el tiempo de ejecución de la búsqueda secuencial
        //...
        long startTime = System.currentTimeMillis();
        System.out.println("Numero a buscar: " + TARGET);
        long endTime;
        if (sequentialSearch()) {
            endTime = System.currentTimeMillis();
            System.out.println("Tiempo búsqueda secuencial: " + (endTime - startTime) + "ms");
        }


        // Medir el tiempo de ejecución de la búsqueda paralela
        //...
        System.out.println("Resultado búsqueda paralela: " + TARGET);
        startTime = System.currentTimeMillis();
        parallelSearch();
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo búsqueda paralela: " + (endTime - startTime) + "ms");
    }

    private static boolean sequentialSearch() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrix[i][j] == TARGET) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean parallelSearch() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicBoolean numero_existe = new AtomicBoolean(false);

        for (int i = 0; i < THREAD_COUNT; i++) {
            int hilo_final = i;
            int tamanioporhilo = MATRIX_SIZE / THREAD_COUNT;
            executor.submit(() -> {
                for (int j = hilo_final * tamanioporhilo; j < (hilo_final + 1) * tamanioporhilo && !numero_existe.get(); j++) {
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        if (matrix[j][k] == TARGET) {
                            numero_existe.set(true);
                            //para terminar la busqueda
                            break;
                        }
                    }
                }
            });
        }
        //apagar el executor
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return numero_existe.get();
    }
}