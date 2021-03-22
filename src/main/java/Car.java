import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;

    private CyclicBarrier startBarrier;
    private Semaphore roadCounter;
    private AtomicBoolean isRaceStarted;
    private AtomicBoolean isWin;
    private boolean isCarReady = false;
    private int stagePassed = 0;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }


    private int numberOfStages = 0;
    private int passedStages = 0;


    public void passStage(){
        stagePassed++;
    }

    public int getStagePassed() {
        return stagePassed;
    }



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

    public AtomicBoolean getIsRaceStopped() {
        return isWin;
    }

    public void setIsRaceStopped(AtomicBoolean isRaceStopped) {
        this.isWin = isRaceStopped;
    }

    public static int getCarsCount() {
        return CARS_COUNT;
    }

    public AtomicBoolean getIsWin() {
        return isWin;
    }

    public void setIsWin(AtomicBoolean isWin) {
        this.isWin = isWin;
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
