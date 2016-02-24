package mwang.parkinglot;

import java.util.AbstractMap.SimpleEntry;

import mwang.parkinglot.interfaces.ParkingBill;
import mwang.parkinglot.interfaces.ParkingToken;
import mwang.parkinglot.interfaces.Payable;
/**
 *  
 * @author xd_wa
 *The SimpleEntry has status code as key
 *value could be ParkingToken or description 
 */
public interface ParkingLot {
	SimpleEntry<String, Object> enterParkingLot(String entranceName) throws InterruptedException;
	SimpleEntry<String, Object> exitParkingLot(ParkingBill bill, String exitName) throws InterruptedException;
	ParkingBill generateBill(ParkingToken token);
	ParkingBill payBill(ParkingBill bill, Payable[] payables);
	void openExit(String exitName);
	void closeExit(String exitName);
	void openEntry(String entry);
	void closeEntry(String entry);
}
