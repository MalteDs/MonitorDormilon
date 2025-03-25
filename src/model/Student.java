package model;

import java.util.Random;

public class Student implements Runnable {
    private final int id;
    private final Random random = new Random();

    public Student(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while(true) {
            try {
                // Tiempo programando
                System.out.println("[Student " + id + "] programming");
                Thread.sleep(random.nextInt(4000) + 1000);
                
                // Buscar ayuda
                System.out.println("[Student " + id + "] needs help");
                
                boolean shouldWakeUp = false;
                synchronized(Monitor.lock) {
                    if(Monitor.isSleeping && !Monitor.isHelping) {
                        shouldWakeUp = true;
                        Monitor.isSleeping = false;
                    }
                }
                
                if(shouldWakeUp) {
                    System.out.println("[Student " + id + "] wakes up the monitor");
                    Monitor.semaphoreMonitor.release();
                }

                // Intentar entrar directamente
                if(Monitor.semaphoreOffice.tryAcquire()) {
                    System.out.println("[Student " + id + "] enters office");
                    Thread.sleep(random.nextInt(3000) + 1000);
                    System.out.println("[Student " + id + "] leaves office");
                    Monitor.semaphoreOffice.release();
                    continue;
                }

                // Intentar sentarse a esperar
                if(Monitor.semaphoreChairs.tryAcquire()) {
                    synchronized(Monitor.lock) {
                        Monitor.waitingStudents.add(id);
                        System.out.println("[Student " + id + "] sits waiting (" + 
                                         Monitor.waitingStudents.size() + " waiting)");
                    }
                    
                    // Esperar turno
                    Monitor.semaphoreOffice.acquire();
                    
                    // Una vez adquirido el permiso, ser atendido
                    System.out.println("[Student " + id + "] being helped");
                    Thread.sleep(random.nextInt(3000) + 1000);
                    System.out.println("[Student " + id + "] finishes help");
                    Monitor.semaphoreOffice.release();
                } else {
                    System.out.println("[Student " + id + "] no chairs available, will return later");
                }
            } catch(InterruptedException e) {
                System.out.println("[Student " + id + "] interrupted");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}