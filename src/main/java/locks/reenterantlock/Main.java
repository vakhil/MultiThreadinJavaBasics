package locks.reenterantlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Main {

    public static void main(String[] args) {

    }

    public static class PricesContainer {
        private Lock lockObject = new ReentrantLock();
        private double bitcoin;
        private double ether;

        public Lock getLockObject() {
            return lockObject;
        }

        public void setLockObject(Lock lockObject) {
            this.lockObject = lockObject;
        }

        public double getBitcoin() {
            return bitcoin;
        }

        public void setBitcoin(double bitcoin) {
            this.bitcoin = bitcoin;
        }

        public double getEther() {
            return ether;
        }

        public void setEther(double ether) {
            this.ether = ether;
        }
    }

    public static class PriceUpdater extends Thread{
        private PricesContainer pricesContainer;
        private Random random = new Random();
        public PriceUpdater(PricesContainer pricesContainer){
            this.pricesContainer = pricesContainer;
        }

        @Override
        public void run() {
            while (true){
                pricesContainer.getLockObject().lock();
                try {
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException ex){
                    }

                    pricesContainer.setBitcoin(random.nextInt(20000));
                    pricesContainer.setEther(random.nextInt(1000));
                }finally {
                    pricesContainer.getLockObject().unlock();
                }
            }

        }
    }
}
