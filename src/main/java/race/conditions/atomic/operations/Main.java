package race.conditions.atomic.operations;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        Metric metric = new Metric();
        BuisnessLogic buisnessLogic1 = new BuisnessLogic(metric);
        BuisnessLogic buisnessLogic2 = new BuisnessLogic(metric);
        MetricsPrinter metricsPrinter = new MetricsPrinter(metric);

        buisnessLogic1.start();
        buisnessLogic2.start();
        metricsPrinter.start();
    }
    public static class MetricsPrinter extends Thread{
        private Metric metric;

        public MetricsPrinter(Metric metric){
            this.metric = metric;
        }

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(100);
                }catch (InterruptedException ex){

                }
                double currentAverage = metric.getAverage();
                System.out.println("Current average is "+ currentAverage);
            }
        }
    }
    public static class BuisnessLogic extends Thread {
        private Metric metric;
        private Random random = new Random();

        private BuisnessLogic(Metric metric){
            this.metric = metric;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while(true) {
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (Exception ex) {

                }
                long end = System.currentTimeMillis();
                metric.addSample(end - start);
            }
        }
    }
    public static class Metric {
        private long count = 0 ;
        private double average = 0.0;

        public synchronized void addSample(long sample){
            double currentSum = average * count;
            count++;
            average = (currentSum + sample)/count;
        }

        public double getAverage() {
            return average;
        }
    }
}
