import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    //    семафор ограничивает количество машин в тоннеле
    private final Semaphore semaphore;

    public Tunnel(int maxCarsInTunnel) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        semaphore = new Semaphore(maxCarsInTunnel);
    }

    @Override
    public void go(Car car) {

        try {
            semaphore.acquire();
            System.out.println(car.getName() + " готовится к этапу(ждет): " + description);
            System.out.println(car.getName() + " начал этап: " + description);

            Thread.sleep(length / car.getSpeed() * 1000);
            System.out.println(car.getName() + " закончил этап: " + description);
            car.passStage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }

    }
}
