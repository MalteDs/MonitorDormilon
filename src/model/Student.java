package model;
import java.util.Random;

public class Student implements Runnable {
    private final Integer id;
    private final Random random = new Random();

    public Student(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while(true){
            try{
                // tiempo programando en sala
                System.out.println("Student " + id + " is programming");
                Thread.sleep(random.nextInt(5000) + 1000);
                // buscar ayuda
                System.out.println("Student " + id + " is going to the monitor");
                
                synchronized(Monitor.lock){
                    if(Monitor.monitorSleeping){
                        // despertar al monitor
                        System.out.println("Student " + id + " wakes up the monitor");
                        Monitor.semaphoreMonitor.release();
                        Monitor.monitorSleeping = false;
                    }
                }

                // intentar entrar a la oficina
                if(Monitor.semaphoreOffice.tryAcquire()){
                    // entrar a la oficina
                    System.out.println("Student " + id + " enters the office");
                    Thread.sleep(random.nextInt(5000) + 1000);
                    System.out.println("Student " + id + " end help, leaves the office");
                    Monitor.semaphoreOffice.release();
                }else{
                    // intentar sentarse
                    if(Monitor.semaphoreChair.tryAcquire()){
                        synchronized(Monitor.lock){
                            Monitor.queueStudents.add(id);
                            System.out.println("Student " + id + " is waiting for help. Students in queue: " + Monitor.queueStudents.size());
                        }
                        // esperar a ser atendido
                        Monitor.semaphoreOffice.acquire();
                        // ya fue atendido, continuar
                        continue;
                    } else {
                        // no hay sillas, salir
                        System.out.println("Student " + id + ": no chairs available, will try later");
                    }   
                }
            }catch(InterruptedException e){
                System.out.println("Error in student " + id);
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
