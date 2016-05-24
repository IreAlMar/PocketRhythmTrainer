package com.irene.pocketrhythmtrainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Irene on 24/05/2016.
 */
public class RoundsAdapter extends ArrayAdapter<Round> {
    public RoundsAdapter(Context context, ArrayList<Round> rounds) {
        super(context, 0, rounds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Round round = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_round, parent, false);
        }
        TextView nameP = (TextView) convertView.findViewById(R.id.textPlayer);
        TextView nameG = (TextView) convertView.findViewById(R.id.textGame);
        TextView score = (TextView) convertView.findViewById(R.id.textPoints);

        nameP.setText(round.getNameP());
        nameG.setText(round.getNameG());
        score.setText(round.getScore().toString());

        return convertView;
    }
}