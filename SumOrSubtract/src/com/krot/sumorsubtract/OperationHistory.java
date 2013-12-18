package com.krot.sumorsubtract;

import java.util.List;
import java.util.ArrayList;
//import android.util.Log;

public class OperationHistory {
	public static final String ADDITION = "+";
	public static final String SUBTRACTION = "-";
	
	protected List<HistoryItem> items = new ArrayList<HistoryItem>();
	protected int currentItemIdx = 0; // ID of the last requested valid item
	protected int invalidItemIdx = 0; // ID of the last requested invalid item
	protected boolean requestIsInvalid = false;
	
	public void addItem(String operationName, int operationResult) {
		HistoryItem item = new HistoryItem(operationName, operationResult);
		items.add(item);
	}
	
	public String getOperation() {
		return getCurrentItem().getOperation();
	}

	public int getResult() {
		return getCurrentItem().getResult();
	}

	public int getHistoryItemId() {
		return currentItemIdx;
	}
	
	public int size() {
		return items.size();
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	public void goToPreviousItem() {
		resetInvalidItem();
		if (currentItemIdx > 0)
			currentItemIdx --;
		else
			setInvalidItem(currentItemIdx-1);
	}
	
	public void goToNextItem() {
		resetInvalidItem();
		if (currentItemIdx < this.size()-1)
			currentItemIdx ++;
		else
			setInvalidItem(currentItemIdx+1);
	}
	
	public void goToItemAt(int idx) {
		resetInvalidItem();
		if (idx > 0 && idx < this.size()-1)
			currentItemIdx = idx;
		else
			setInvalidItem(idx);
	}
	
	public HistoryItem getCurrentItem() {
		return items.get(currentItemIdx);
	}
	
	public boolean mostRecentQueryWasInvalid() {
		return requestIsInvalid;
	}

	protected void setInvalidItem(int val) {
		requestIsInvalid = true;
		invalidItemIdx = val;
	}

	protected void resetInvalidItem() {
		requestIsInvalid = false;
	}
	
	private class HistoryItem {
		private String operation;
		private int result;
		
		HistoryItem(String opName, int opResult) {
			operation = opName;
			result = opResult;
		}
		
		public String getOperation() {
			return operation;
		}
		
		public int getResult() {
			return result;
		}
	}
}
