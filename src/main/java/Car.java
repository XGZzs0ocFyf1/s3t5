import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private final Race race;
    private final int speed;
    private final String name;

    private final CyclicBarrier startBarrier;
    private final Semaphore roadCounter;
    private final AtomicBoolean isRaceStarted;
    private final AtomicBoolean isWin;
    private boolean isCarReady = false;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    private int numberOfStages = 0;
    private int passedStages = 0;



    public Car(Race race, int speed, CyclicBarrier startBarrier, Semaphore road,
               AtomicBoolean isRaceStarted, AtomicBoolean isWin) {
        this.race = race;
        this.speed = speed;
        this.startBarrier = startBarrier;
        this.roadCounter = road;
        this.isWin = isWin;
        this.isRaceStarted = isRaceStarted;



        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {

            startBarrier.await();
            System.out.println(this.name + " готовится");
            startBarrier.await();
            System.out.println(this.name + " готов ");
            isCarReady = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        while(true){
            //проверяем что получен сигнал начала гонки
            if (isRaceStarted.get()) {
                System.out.println(this.name + " начал гонку");
                numberOfStages = race.getStages().size();
                for (int i = 0; i < race.getStages().size(); i++) {

                    try {
                        roadCounter.acquire();
                        race.getStages().get(i).go(this);
                        passedStages++;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        roadCounter.release();
                    }


                }
                //выходим из цикла
                break;
            }


        }


    }

    public void passStage(){
        passedStages++;
    }



    public AtomicBoolean getIsWin() {
        return isWin;
    }

    public int getPassedStages() {
        return passedStages;
    }

    public int getNumberOfStages() {
        return numberOfStages;
    }

    public boolean isCarReady() {
        return isCarReady;
    }
}
