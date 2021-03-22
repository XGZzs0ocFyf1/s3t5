import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Road extends Stage {


    CyclicBarrier cb = new CyclicBarrier(4);


    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }
    @Override
    public void go(Car c) {
        try {
            cb.await();
            System.out.println(c.getName() + " начал этап: " + description);
            Thread.sleep(length / c.getSpeed() * 1000);


            cb.await();
            synchronized (this){
                if (!c.getIsWin().get() && c.getPassedStages() == c.getNumberOfStages()-1){
                    System.out.println(c.getName() + " закончил этап: " + description);
                    System.out.println(c.getName() + " WIN");
                    c.getIsWin().set(true);
                }else{
                    System.out.println(c.getName() + " закончил этап: " + description);
                }
            }

            //этап пройден, увеличиваем счетчик этапов на машине
            c.passStage();

        } catch (InterruptedException | BrokenBarrierException e) {
            //что тут делать если выскочит исключение, например прервался поток, т.е. машине не едет
            //это нужно как то обработать ?
            e.printStackTrace();
        }
    }
}
