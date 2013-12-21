package com.krot.sumorsubtract;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

//import android.util.Log;

public class OperationHistory implements Parcelable {
    public static final String ADDITION = "+";
    public static final String SUBTRACTION = "-";

    protected List<HistoryItem> items = new ArrayList<HistoryItem>();
    protected int currentItemIdx = 0; // ID of the last requested valid item
    protected int invalidItemIdx = 0; // ID of the last requested invalid item
    protected boolean requestIsInvalid = false;
    protected boolean hasValidRequests = false;

    OperationHistory() {

    }

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

    public int getInvalidItemId() {
        return invalidItemIdx;
    }

    public int size() {
        return items.size();
    }

    public boolean hadValidQueries() {
        return hasValidRequests;
    }

    public void goToPreviousItem() {
        if (currentItemIdx > 0)
            setValidItem(currentItemIdx - 1);
        else
            setInvalidItem(currentItemIdx - 1);
    }

    public void goToNextItem() {
        if (currentItemIdx < size() - 1)
            setValidItem(currentItemIdx + 1);
        else
            setInvalidItem(currentItemIdx + 1);
    }

    public void goToItemAt(int idx) {
        if (idx > -1 && idx < size())
            setValidItem(idx);
        else
            setInvalidItem(idx);
    }

    public HistoryItem getCurrentItem() {
        return items.get(currentItemIdx);
    }

    public boolean wasMostRecentQueryInvalid() {
        return requestIsInvalid;
    }

    protected void setValidItem(int val) {
        resetInvalidItem();
        currentItemIdx = val;
        hasValidRequests = true;
    }

    protected void setInvalidItem(int val) {
        requestIsInvalid = true;
        invalidItemIdx = val;
    }

    protected void resetInvalidItem() {
        requestIsInvalid = false;
    }

    /*
     * make it Parcelable
     */

    public static final Parcelable.Creator<OperationHistory> CREATOR =
            new Parcelable.Creator<OperationHistory>() {
                public OperationHistory createFromParcel(Parcel in) {
                    return new OperationHistory(in);
                };

                public OperationHistory[] newArray(int size) {
                    return new OperationHistory[size];
                };
            };

    public OperationHistory(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        items = in.readArrayList(HistoryItem.class.getClassLoader());
        currentItemIdx = in.readInt();
        invalidItemIdx = in.readInt();
        requestIsInvalid = in.readByte() != 0;
        hasValidRequests = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag) {
        out.writeList(items);
        out.writeInt(currentItemIdx);
        out.writeInt(invalidItemIdx);
        out.writeByte((byte) (requestIsInvalid ? 1 : 0));
        out.writeByte((byte) (hasValidRequests ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private class HistoryItem implements Parcelable {
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

        /*
         * Make it Parcelable
         */

        protected HistoryItem(Parcel in) {
            readFromParcel(in);
        }

        protected void readFromParcel(Parcel in) {
            operation = in.readString();
            result = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeString(operation);
            out.writeInt(result);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }
}
