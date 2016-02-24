package mwang.parkinglot.interfaces.impl;

import java.util.Date;

import mwang.parkinglot.interfaces.ParkingToken;

public class ParkingTokenImpl implements ParkingToken{
	private Date enterDate;
	
	public ParkingTokenImpl(){
		enterDate = new Date();
	}
	
	public Date getEnterTime() {
		return enterDate;
	}
}
