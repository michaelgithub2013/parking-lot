package mwang.parkinglot.interfaces.impl;

import mwang.parkinglot.interfaces.Payable;
/**
 * @author xd_wa
 *
 */

public class PayableImpl implements Payable{
	float payableAmount ;
	
	public PayableImpl(float amount){
		payableAmount = amount;
		
	}

	public float getPayableAmount() {
		return payableAmount;
	}

	public void pay(float amount) {
		payableAmount = payableAmount - amount;
	}

}
