package mwang.parkinglot;

import junit.framework.TestCase;
import mwang.parkinglot.interfaces.ParkingBill;
import mwang.parkinglot.interfaces.ParkingToken;
import mwang.parkinglot.interfaces.Payable;
import mwang.parkinglot.interfaces.impl.ParkingTokenImpl;
import mwang.parkinglot.interfaces.impl.PayableImpl;

public class AppUnitTest extends TestCase{
	ParkingLot parkingLot;
	
	@Override
	public void setUp() throws Exception{
		 String[] entries = { "South Entry", "North Entry" };
		 String[] exits = { "Under Ground Exit", "Ground Exit" };
		 int spaceNumber = 1;
		parkingLot = new ParkingLotImpl(entries, exits, spaceNumber);
		super.setUp();
		
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Passed when parking lot not full
	 * Bloked when parking lot is full
	 * @throws Exception
	 */
	public void testEnterParkingLot() throws Exception {
		assertTrue(parkingLot.enterParkingLot("South Entry").getValue() instanceof ParkingToken);
		assertFalse(parkingLot.enterParkingLot("South Entry").getValue() instanceof ParkingToken);

	}
	/**
	 * Generated a bill from parking token.
	 * the bill is unpaid, the amount of the bill is 10.0f
	 */
	public void testGenerateBill() {
		assertTrue(parkingLot.generateBill(new ParkingTokenImpl()).getBillAmount() == 10.0f);
		assertFalse((parkingLot.generateBill(new ParkingTokenImpl())).isPaid());
	}
	
	/**
	 * Pay an unpaid 10.0f bill with 10.0 payables, the bill is paid.
	 * Pay an unpaid 10.0f bill with 2.0 payables, the bill is still unpaid.
	 */
	public void testPayBill() {
		ParkingToken token = new ParkingTokenImpl();
		Payable payable1 = new PayableImpl(2.0f);
		Payable payable2 = new PayableImpl(8.0f);

		
		ParkingBill bill = parkingLot.generateBill(token);
		Payable[] payables = { payable1, payable2 };
		bill = parkingLot.payBill(bill, payables);
		assertTrue(bill.isPaid());
		
		bill = parkingLot.generateBill(token);
		Payable[] payables2 = { payable2 };
		bill = parkingLot.payBill(bill, payables2);
		assertFalse(bill.isPaid());


	}
	
	/**
	 * exit parking lot with unpaid bill get bloked. 
	 * exit parking lot with paid bill get passed.
	 * @throws InterruptedException
	 */
	public void testExitParkinglot() throws InterruptedException {
		ParkingToken token = new ParkingTokenImpl();
		Payable payable1 = new PayableImpl(10.0f);
		Payable[] payables = { payable1 };
		ParkingBill bill = parkingLot.generateBill(token);
		assertTrue("Bloked".equals((parkingLot.exitParkingLot(bill, "Ground Exit").getKey())));
		bill = parkingLot.payBill(bill, payables);
		assertTrue("Passed".equals(parkingLot.exitParkingLot(bill, "Ground Exit").getKey()));
		
	}


	
}
