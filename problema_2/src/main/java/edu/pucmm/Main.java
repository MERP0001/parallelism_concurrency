package edu.pucmm;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int QUEUE_CAPACITY = 10;
    private static final int PRODUCER_COUNT = 2;
    private static final int CONSUMER_COUNT = 2;
    private static final int PRODUCE_COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> cola = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        AtomicInteger multiplicacion = new AtomicInteger(1);
        for (int i = 0; i < PRODUCER_COUNT; i++) {
            new Thread(new Producer(cola, PRODUCE_COUNT)).start();
        }
        long startTime = System.currentTimeMillis();


        for (int i = 0; i < CONSUMER_COUNT; i++) {
            new Thread(new Consumer(cola, multiplicacion)).start();
        }
//        Thread.sleep(1000);
        long endTime = System.currentTimeMillis();
        System.out.println("La multiplicacion de los números es: " + multiplicacion.get());
        System.out.println("Tiempo búsqueda paralela: " + (endTime - startTime) + "ms");
    }

    static class Producer implements Runnable {
        private final BlockingQueue<Integer> cola;
        private final int numero;

        Producer(BlockingQueue<Integer> cola, int produceCount) {
            this.cola = cola;
            this.numero = produceCount;
        }
        @Override
        public void run() {
            for (int i = 0; i < numero; i++) {
                try {
                    Random random = new Random();
                    int num = random.nextInt(100);
                    cola.put(num);
                    System.out.println("Produciendo: " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> cola;
        private final AtomicInteger multiplicacion;

        Consumer(BlockingQueue<Integer> cola, AtomicInteger multiplicacion) {
            this.cola = cola;
            this.multiplicacion = multiplicacion;
        }
        @Override
        public void run() {
            int i = 0;
            int limit = PRODUCE_COUNT ;
            while (i < limit){
                try {
                    int num = cola.take();
                    multiplicacion.set(multiplicacion.get() * num + 1);
                    System.out.println("Consumiendo: " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    }
}