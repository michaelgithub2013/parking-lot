package mwang.parkinglot.interfaces.impl;

import mwang.parkinglot.interfaces.LotEntry;

public class LotEntryImpl implements LotEntry {

	private boolean open;
	
	public LotEntryImpl(){
		open = true;
	}
	
	
	public void setOpen(boolean open){
		this.open = open;
	}
	
	public boolean isOpen() {
		return open;
	}


	public void closeEntry() {
		this.open = false;
		
	}


	public void openEntry() {
		this.open = true;
		
	}
}
