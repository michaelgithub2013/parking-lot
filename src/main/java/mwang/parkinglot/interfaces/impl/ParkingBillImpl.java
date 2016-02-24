package mwang.parkinglot.interfaces.impl;

import java.util.Date;

import mwang.parkinglot.interfaces.ParkingBill;
import mwang.parkinglot.interfaces.ParkingToken;

public class ParkingBillImpl implements ParkingBill{
	boolean paid;
	float billAmount;
	
	public ParkingBillImpl(ParkingToken token){
		if (token.getEnterTime() instanceof Date){
			billAmount = 10.0f;
		}
	}
	
	public boolean isPaid() {
		return paid;
	}

	public float getBillAmount() {
		return billAmount;
	}

	public void setToPaid() {
		
		paid = true;
	}

}
