package readwritelock;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int HIGHEST_PRICE = 1000;
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                    inventoryDatabase.removeItems(random.nextInt(HIGHEST_PRICE));
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for (int i = 0; i < numberOfReaderThreads; i++) {
            Thread reader = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100000; j++) {
                        int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                        int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                        inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice,upperBoundPrice);

                    }
                }
            });

          //  reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTIme = System.currentTimeMillis();
        for (Thread reader : readers){
            reader.start();
        }

        for (Thread reader:  readers){
            reader.join();
        }

        long endReadingTime = System.currentTimeMillis();
        System.out.println(String.format("Readging time took "+String.valueOf(endReadingTime-startReadingTIme)));
    }

    public static class InventoryDatabase {
        private TreeMap<Integer,Integer> priceToCountMap = new TreeMap<>();
        private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();


        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound){
            readLock.lock();
            try {
                Integer fromKey = priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices = priceToCountMap.subMap(fromKey, true, toKey, true);

                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }
                return sum;
            } finally {
                readLock.unlock();
            }
        }
        public void addItem(int price){
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    priceToCountMap.put(price, 1);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }
            }finally {
                writeLock.unlock();
            }
        }

        public void removeItems(int price){
            writeLock.lock();
            try {
                Integer numberOfItemsForPrice = priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    priceToCountMap.remove(price);
                } else {
                    priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }
            }finally {
                writeLock.unlock();
            }
        }
    }
}
