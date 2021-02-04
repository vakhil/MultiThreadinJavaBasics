package deadlock;

import java.util.ArrayList;
import java.util.List;

public class GithubDeadlock {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(4);
        list.add(8);
        list.add(10);

        Thread averageCalculator = new AverageCalculator(list);
        Thread listChanger = new ListChanger(list);

        averageCalculator.start();
        listChanger.start();

        averageCalculator.join();
        listChanger.join();
    }


    public static class ListChanger extends Thread{
        List<Integer> list;

        public ListChanger(List<Integer> list){
            this.list = list;
        }

        @Override
        public void run() {
            synchronized (list){
                list.add(30);
                list.add(40);
                list.add(50);
                System.out.println("The elements have been added");
            }

        }
    }

    public static class AverageCalculator extends Thread{
        List<Integer>  list ;

        public AverageCalculator(List<Integer> list){
            this.list = list;
        }
        @Override
        public void run() {
            synchronized (list){
                int sum = 0;

                for (int x : list){
                    sum += x;
                }
                sum = sum/(list.size());
                System.out.println("The average of this elements is " + String.valueOf(sum) + " and elements are "+list.size());

            }
        }
    }
}
