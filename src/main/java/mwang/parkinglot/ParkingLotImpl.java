package mwang.parkinglot;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;

import mwang.parkinglot.interfaces.LotEntry;
import mwang.parkinglot.interfaces.LotExit;
import mwang.parkinglot.interfaces.ParkingBill;
import mwang.parkinglot.interfaces.ParkingToken;
import mwang.parkinglot.interfaces.Payable;
import mwang.parkinglot.interfaces.impl.LotEntryImpl;
import mwang.parkinglot.interfaces.impl.LotExitImpl;
import mwang.parkinglot.interfaces.impl.ParkingBillImpl;
import mwang.parkinglot.interfaces.impl.ParkingTokenImpl;

public class ParkingLotImpl implements ParkingLot {
	Logger logger = Logger.getLogger(this.getClass());

	private final Map<String, LotEntry> lotEntries;
	private final Map<String, LotExit> lotExits;
	public final int totalParkingSpaces;
	
	private int totalVehicles;
	private boolean isParkinglotFull;
	
	private Semaphore available = new Semaphore(1, true);


	public ParkingLotImpl(String[] entryNames, String[] exitNames, int totalParkingSpaces) throws Exception {
		this.totalParkingSpaces = totalParkingSpaces;

		lotEntries = new ConcurrentHashMap<>();
		for (int i = 0; i < entryNames.length; i++) {
			if (null != lotEntries.put(entryNames[i], new LotEntryImpl())) {
				String errMsg = "Duplicated parkinglot entrance name: " + entryNames[i];
				throw new Exception(errMsg);
			}
		}

		lotExits = new ConcurrentHashMap<>();
		for (int i = 0; i < exitNames.length; i++) {
			if (null != lotExits.put(exitNames[i], new LotExitImpl())) {
				String errMsg = "Duplicated parkinglot exit name: " + exitNames[i];
				throw new Exception(errMsg);
			}
		}

	}

	public SimpleEntry<String, Object> enterParkingLot(String entranceName) throws InterruptedException {

		if (!lotEntries.get(entranceName).isOpen()) {
			logger.debug("A vehicle is blocked at " + entranceName + " because gate is closed");
			return new SimpleEntry<String, Object>("Bloked", "The gate is closed");
		}

		available.acquire();
		if (isParkinglotFull) {
			available.release();
			logger.debug("A vehicle is blocked because parkinglot is full");
			available.release();
			return new SimpleEntry<String, Object>("Bloked", "The parkinglot is full");

		} else {
			if (++totalVehicles >= totalParkingSpaces) {
				isParkinglotFull = true;
			}
			available.release();
			logger.debug("A vehicle is entering from entrnace: " + entranceName);
			return new SimpleEntry<String, Object>("Passed", new ParkingTokenImpl());
		}
	}

	public SimpleEntry<String, Object> exitParkingLot(ParkingBill bill, String exitName) throws InterruptedException {

		if (!lotExits.get(exitName).isOpen()) {
			logger.debug("A vehicle is blocked at " + exitName + " because  gate is closed");
			return new SimpleEntry<String, Object>("Bloked", "The gate is closed");
		}

		if (!bill.isPaid()) {
			logger.debug("A vehicle is blocked at " + exitName + " because parking fee is unpaid ");
			return new SimpleEntry<String, Object>("Bloked", "The parking bill is unpaid");
		} else {
			available.acquire();
			totalVehicles--;
			if (isParkinglotFull) {
				isParkinglotFull = false;
			}
			available.release();
			logger.debug("A vehicle is leaving through " + exitName);
			return new SimpleEntry<String, Object>("Passed", "leaving through " + exitName);
		}

	}

	public ParkingBill generateBill(ParkingToken token) {
		return new ParkingBillImpl(token);
	}

	public ParkingBill payBill(ParkingBill bill, Payable[] payables) {
		if (bill.isPaid())
			return bill;
		float totalPaidAmount = 0.0f;
		for (Payable payable : payables) {
			totalPaidAmount = totalPaidAmount + payable.getPayableAmount();
		}
		if (totalPaidAmount == bill.getBillAmount()) {
			for (Payable payable : payables) {
				payable.pay(payable.getPayableAmount());
			}
			bill.setToPaid();
		}
		return bill;
	}

	public void openExit(String exitName) {
		lotExits.get(exitName).openExit();
		
	}

	public void closeExit(String exitName) {
		lotExits.get(exitName).closeExit();
		
	}

	public void openEntry(String entry) {
		lotEntries.get(entry).openEntry();
		
	}

	public void closeEntry(String entry) {
		lotEntries.get(entry).closeEntry();
	}



}