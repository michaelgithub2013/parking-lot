package mwang.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeoutException;

import junit.framework.TestCase;
import mwang.parkinglot.test.util.PhaserCar;

/**
 * 
 * @author mwang
 * 
 *         Description of the Multithreading use case
 * 
 *         It demonstrates concurrent access of resource, thread execution,
 *         thread lock and result gathering.
 *
 *         Prepare:
 *         
 *         A parking lot is constructed with 2 entries, 2 exits and 3 parking
 *         spaces. Each entry or exit allows multiple cars passing at same time.
 *         Once parking lot is full, cars are not allowed to enter
 * 
 *         A car behaves like this: Wait until a group signal is ready, enter
 *         the parking lot, stay inside for a while, pay bill, leave and save
 *         journey.
 *
 *         Test case:
 *          
 *         Send the car group to the parking lot. Once a car
 *         is back, print its journey and send it to the parking lot again.
 *         Repeat this three times.
 * 
 *         Result:
 *         
 *         Running the case, We expect to see the 4 cars group went to the 3
 *         spaces parking lot three times. In each time, they started at the
 *         same time. There is always one car blocked. When coming back, first
 *         car got its journey printed first.
 * 
 * 
 */
public class AppMultithreadingUseCaseTest extends TestCase {
	private ParkingLot parkingLot;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		String[] entries = { "South Entry", "North Entry" };
		String[] exits = { "Under Ground Exit", "Ground Exit" };
		int spaceNumber = 3;
		parkingLot = new ParkingLotImpl(entries, exits, spaceNumber);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPhaserCar() throws InterruptedException, ExecutionException, TimeoutException {

		List<Callable<PhaserCar>> cars = new ArrayList<>();
		int carNumberInGroup = 4;
		Phaser phaser = new Phaser(carNumberInGroup);
		for (int i = 0; i < carNumberInGroup; i++)
			cars.add(new PhaserCar("car_" + (i + 1), parkingLot, "North Entry", "Ground Exit", phaser));

		Executor executor = Executors.newFixedThreadPool(9);
		CompletionService<PhaserCar> completionService = new ExecutorCompletionService<PhaserCar>(executor);

		// send the cars
		System.out.println("*** start ***");
		for (Callable<PhaserCar> car : cars)
			completionService.submit(car);

		// gather result

		int received = 0;
		while (received < carNumberInGroup * 3) {
			received++;
			// blocks if none available
			Future<PhaserCar> resultFuture = completionService.take();

			PhaserCar aCar = resultFuture.get();
			System.out.println(aCar.getLastJourney());
			completionService.submit(aCar);

		}

	}
}
