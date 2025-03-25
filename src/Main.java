import model.Monitor;
import model.Student;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class Main {
    public static void main(String[] args) {
        System.out.println("enter the number of students");
        int students = Integer.parseInt(System.console().readLine());

        Thread monitor = new Thread(new Monitor());
        monitor.start();

        for (int i = 1; i <= students; i++) {
            Thread student = new Thread(new Student(i));
            student.start();
        }
        
    }
}