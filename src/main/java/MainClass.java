import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainClass {

    public static final int CARS_COUNT = 4;

    //здесь предполагаем, что машина после прохождения этапа не сразу едет на следующий и там готовится, затем
    // проходит его по готовности
    private static Semaphore roadBarrier = new Semaphore(CARS_COUNT);

    //старт одновременный у всех машин и есть еще общий этап подготовки, использую один и тот же барьер 2 раза
    private static CyclicBarrier startcDL = new CyclicBarrier(CARS_COUNT);

    //проверяю наличие хоть одного победителя
    private static AtomicBoolean isWinnerExist = new AtomicBoolean(false);

    private static AtomicBoolean isRaceStarted = new AtomicBoolean(false);

    //проверяю, что гонка началась, данное значение вычисляется в while(true){} ниже


    // проверяю что гонка закончилась
    private static boolean isRaceStopped = false;


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT / 2), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), startcDL, roadBarrier, isRaceStarted,
                    isWinnerExist);
            Thread tI = new Thread(cars[i]);
            tI.start();
        }



        while (true) {
            if (!isRaceStarted.get()) {
                int numberOfReadyCars = 0;
                for (Car car : cars) {
                    if (car.isCarReady()) {
                        numberOfReadyCars++;
                    }
                }
                if (numberOfReadyCars == CARS_COUNT) {
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                    isRaceStarted.set(true);
                }
            }


            if (!isRaceStopped) {
                int numberOfCarsAtFinish = 0;
                for (Car car : cars) {
                    if (car.getPassedStages() == 3) {
                        numberOfCarsAtFinish++;
                    }
                }
                if (numberOfCarsAtFinish == CARS_COUNT) {
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
                    isRaceStopped = true;
                    break;
                }
            }
        }


    }
}
