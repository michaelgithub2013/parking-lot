package mwang.parkinglot.interfaces.impl;

import mwang.parkinglot.interfaces.LotExit;

public class LotExitImpl implements LotExit{
	private boolean open;
	
	public LotExitImpl(){
		open = true;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}

	public void closeExit() {
		this.open = false;
		
	}

	public void openExit() {
		this.open = true;
		
	}
}
