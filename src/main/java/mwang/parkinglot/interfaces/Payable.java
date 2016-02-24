package mwang.parkinglot.interfaces;

public interface Payable {
	float getPayableAmount();
	void pay(float amount);
}
