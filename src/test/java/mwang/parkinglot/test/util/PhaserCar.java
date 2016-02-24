package mwang.parkinglot.test.util;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;

import mwang.parkinglot.ParkingLot;
import mwang.parkinglot.interfaces.ParkingBill;
import mwang.parkinglot.interfaces.ParkingToken;
import mwang.parkinglot.interfaces.Payable;
import mwang.parkinglot.interfaces.impl.PayableImpl;

/**
 * The callable implementation simulates a journey of car:
 * - wait for group ready
 * - enter a parking lot
 * - stay for a while
 * - get a bill
 * - pay bill
 * - exit the parking lot
 * 
 * It is used to test Parkinglot
 * @author xd_wa
 * Phaser is a concurrent thread control object.
 * 
 * as a lock, it set the lock number.
 * the lock number can be set through constructor,
 * or increase by phaser.register()
 * decrease by phaser.arriveAndDeregister();
 * 
 * two ways to release the lock: 
 * way1: the arrival number of threads reach the lock number
 * way2: release the lock immediately forceTermination() 
 * 
 * thread can use the phaser in three ways
 * way1: phaser.arriveAndAwaitAdvance().
 *  it reports one arrival and wait for release.
 * way2: phaser.AwaitAdvance(int Phase).
 *  don't know how this work yet
 * way3: phaser.arrive().
 * it reports one arrival but does not wait.
 *
 */

public class PhaserCar implements Callable<PhaserCar> {


	String carName;
	String entranceName;
	String exitName;
	ParkingLot parkinglot;
	Phaser phaser;
	List<String> journeys;

	

	public  PhaserCar(String carName, ParkingLot parkinglot, String entranceName, String exitName,  Phaser phaser) {
		this.entranceName = entranceName;
		this.exitName = exitName;
		this.parkinglot = parkinglot;
		this.carName = carName;
		this.phaser = phaser;
		this.journeys = new ArrayList<>();
	}

	public String getLastJourney(){
		if (journeys == null || journeys.size() == 0){
			return null;
		}else {
			return journeys.get(journeys.size()-1);
		}
	}
	public PhaserCar call() throws InterruptedException  {
		phaser.arriveAndAwaitAdvance();
//		phaser.arriveAndDeregister();
		StringBuilder status = new StringBuilder();
		status.append(":: " + carName + "-> " + entranceName + " timestamp:" + new Date().getTime());
		SimpleEntry<String,Object> result = parkinglot.enterParkingLot(entranceName);
		if (!(result.getValue() instanceof ParkingToken)) {
			status.append("-> " + result.getKey() + " timestamp:" + new Date().getTime());
			this.journeys.add(Thread.currentThread().getName() + status.toString());
//			Thread.sleep(stayTime);
			return this;
		}
		// set stay time between 1000 to 2000;
		int stayTime = new Random().nextInt(1000) + 1000;
		status.append("-> entered and stays for " + stayTime + " millis");
		Thread.sleep(stayTime);
		ParkingToken token = (ParkingToken) result.getValue();
		ParkingBill bill = parkinglot.generateBill(token);
		Payable[] payables = { new PayableImpl(10.0f) };
		bill = parkinglot.payBill(bill, payables);
		status.append("-> " + parkinglot.exitParkingLot(bill, exitName).getKey() + " timestamp:" + new Date().getTime());
		this.journeys.add(Thread.currentThread().getName() + status.toString());
//		Thread.sleep(stayTime/2);
		return this;
	}


}
