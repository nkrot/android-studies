package com.krot.sumorsubtract;

import com.krot.sumorsubtract.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class InvalidHistoryItemDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder.setMessage(R.string.invalid_history_item_id)
		       .setPositiveButton(R.string.gotyou, new DialogInterface.OnClickListener() {
				   @Override
				   public void onClick(DialogInterface dialog, int which) {
					   // TODO Auto-generated method stub
				   }
		        });
		return builder.create();
	}
}
