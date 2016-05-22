package com.irene.pocketrhythmtrainer;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by Irene on 22/05/2016.
 */
public class SaveRoundDialogFragment extends DialogFragment {
    EditText editTextPlayer;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_save_round, null));
        //editTextPlayer = (EditText) findViewById(R.id.player_name);

        builder.setTitle(R.string.save_title);
        builder.setMessage(R.string.save_message)
                .setPositiveButton(R.string.save_title, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //player = editTextPlayer.getText().toString();
//
//                        if(player.isEmpty()){
//                            //Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        showEndOfExercise();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showEndOfExercise();
                    }
                });
        return builder.create();
    }

    private void showEndOfExercise() {
        Intent intent = new Intent(getContext(), EndOfExerciseActivity.class);
        startActivity(intent);
    }
}
