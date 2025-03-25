package model;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Monitor implements Runnable{

    public static Semaphore semaphoreMonitor = new Semaphore(0);
    public static Semaphore semaphoreOffice = new Semaphore(1);
    public static Semaphore semaphoreChair = new Semaphore(0);
    public static Queue<Integer> queueStudents = new LinkedList<>();
    public static final Object lock = new Object();
    public static boolean monitorSleeping = true;

    @Override
    public void run() {
        while(true){
            try{
                System.out.println("Monitor is sleeping");
                semaphoreMonitor.acquire();

                synchronized(lock){
                    monitorSleeping = false;
                    System.out.println("Monitor is awake and ready to help students");
                }

                int studentId = -1;
                while(true){
                    synchronized(lock){
                        if(queueStudents.isEmpty()){
                            monitorSleeping = true;
                            break;
                        }
                        studentId = queueStudents.poll();
                        semaphoreChair.release();
                    }
                }

                // atender estudiante
                System.out.println("Monitor is helping student " + studentId);
                Thread.sleep(new Random().nextInt(5000) + 1000);
                System.out.println("Monitor finished helping student " + studentId);
                semaphoreOffice.release();

            
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    
}
