import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadExample {

    public static void main(String[] args) {
        Vault vault = new Vault(545);
        List<Thread> threads = new ArrayList<>();

        Thread threading = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });


        threads.add(new PoliceClass());
        threads.add(new AscendingThread(vault));
        threads.add(new DescendingThread(vault));

        for (Thread thread : threads){
            thread.start();
        }

    }


    private static class PoliceClass extends Thread {
        @Override
        public void run(){
            for (int i = 10; i >= 1 ; i-- ){
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("You are done !!! ");
        }

    }

    private static abstract class HackerThread extends Thread{
        Vault vault;
        public HackerThread(Vault vault){
            this.vault = vault;
        }
    }

    private static class AscendingThread extends HackerThread{
        public AscendingThread(Vault vault){
            super(vault);
        }


        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                try {
                    if(vault.guessPassword(i)){
                        System.out.println("The password has been detected by thread "+this.getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class DescendingThread extends HackerThread{
        public DescendingThread(Vault vault){
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 1000; i > 0 ; i--) {
                try {
                    if (vault.guessPassword(i)) {
                        System.out.println("The password has been detected by thread " + this.getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static class Vault {
        private int password;
        public Vault(int password){
            this.password = password;
        }

        public boolean guessPassword(int guess) throws InterruptedException {
            Thread.sleep(5);
            return guess == password;
        }
    }
}
