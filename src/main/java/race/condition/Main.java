package race.condition;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        Thread incrementThread1 = new IncrementThread(inventoryCounter);
        Thread incrementThread2 = new IncrementThread(inventoryCounter);

        incrementThread1.start();
        incrementThread2.start();

        incrementThread2.join();
        incrementThread1.join();
        System.out.println("Count is "+inventoryCounter.getItems());


    }

    public static class IncrementThread extends Thread{
        private InventoryCounter inventoryCounter;

        public IncrementThread(InventoryCounter inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                inventoryCounter.increment();
            }
        }
    }



    public static class DecrementThread extends Thread{
        private InventoryCounter inventoryCounter;

        public DecrementThread(InventoryCounter inventoryCounter){
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                inventoryCounter.decrement();
            }
        }
    }

    private static class InventoryCounter {
        private int items = 0;

        public synchronized void increment(){items++;}

        public synchronized void decrement(){
            items--;
        }

        public int getItems(){
            return items;
        }
    }
}
