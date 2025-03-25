package model;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Monitor implements Runnable {
    public static Semaphore semaphoreMonitor = new Semaphore(0);
    public static Semaphore semaphoreOffice = new Semaphore(1);
    public static Semaphore semaphoreChairs = new Semaphore(3);
    public static Queue<Integer> waitingStudents = new LinkedList<>();
    public static final Object lock = new Object();
    public static boolean isSleeping = true;
    public static boolean isHelping = false;

    @Override
    public void run() {
        while(true) {
            try {
                // Dormir hasta ser despertado
                synchronized(lock) {
                    if(waitingStudents.isEmpty()) {
                        System.out.println("[Monitor] is sleeping");
                        isSleeping = true;
                        isHelping = false;
                    }
                }
                
                semaphoreMonitor.acquire(); // Esperar a ser despertado

                synchronized(lock) {
                    isSleeping = false;
                    System.out.println("[Monitor] is awake and ready to help");
                }

                // Atender estudiantes en cola
                while(true) {
                    int studentId;
                    synchronized(lock) {
                        if(waitingStudents.isEmpty()) {
                            break; // Volver a dormir
                        }
                        studentId = waitingStudents.poll();
                        semaphoreChairs.release(); // Liberar una silla
                        isHelping = true;
                    }

                    // Atender al estudiante
                    System.out.println("[Monitor] helping student " + studentId);
                    Thread.sleep(new Random().nextInt(3000) + 2000);
                    System.out.println("[Monitor] finished with student " + studentId);
                    
                    // Liberar la oficina
                    semaphoreOffice.release();
                    isHelping = false;
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}