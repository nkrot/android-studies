package com.krot.sumorsubtract;

import java.util.List;
import java.util.ArrayList;
//import android.util.Log;

public class OperationHistory {
	public static final String ADDITION = "+";
	public static final String SUBTRACTION = "-";
	
	protected List<HistoryItem> items = new ArrayList<HistoryItem>();
	protected int currentItemIdx = 0;
	
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
	
	public void goToPreviousItem() {
		if (currentItemIdx > 0)
			currentItemIdx --;
	}
	
	public void goToNextItem() {
		if (currentItemIdx < this.size()-1)
			currentItemIdx ++;
	}
	
	public void goToItemAt(int idx) {
		if (idx > 0 && idx < this.size()-1)
			currentItemIdx = idx;
	}
	
	public HistoryItem getCurrentItem() {
		return items.get(currentItemIdx);
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
