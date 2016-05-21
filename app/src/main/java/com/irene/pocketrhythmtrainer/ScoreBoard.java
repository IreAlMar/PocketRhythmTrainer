package com.irene.pocketrhythmtrainer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class ScoreBoard extends ListActivity {
    private ItemDbAdapter dbAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        dbAdapter = new ItemDbAdapter(this);
        dbAdapter.open();
        showList();
        //super.registerForContextMenu(super.getListView()); ¿?
    }

    private void showList() {
        cursor = dbAdapter.selectAllOrdered();
        super.startManagingCursor(cursor);
        CursorAdapter list = new CursorAdapter(this, cursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.row, parent, false);
                bindView(view, context, cursor);
                return view;
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView textViewPlayer =
                        (TextView) view.findViewById(R.id.textPlayer);
                TextView textViewGame =
                        (TextView) view.findViewById(R.id.textGame);
                TextView textViewPoints =
                        (TextView) view.findViewById(R.id.textPoints);
                String nameP =
                        cursor.getString(cursor.getColumnIndex(ItemDbAdapter.COL_PLAYER));
                String nameG =
                        cursor.getString(cursor.getColumnIndex(ItemDbAdapter.COL_GAME));
                float points =
                        cursor.getFloat(cursor.getColumnIndex(ItemDbAdapter.COL_POINTS));
                int color = Color.HSVToColor(new float[]{
                        points / 5.0f * 120f, 1f, 1f
                });
                textViewPlayer.setTextColor(color);
                textViewPoints.setTextColor(color);
                textViewPlayer.setText(nameP);
                textViewGame.setText(nameG);
                textViewPoints.setText(String.valueOf(points));
            }
        };
        super.setListAdapter(list);
    }

    private void clearList() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.clear_title);
        dialog.setMessage(R.string.clear);
        dialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbAdapter.deleteAll();
                        showList();
                    }
                });
        dialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    //Demás métodos para cuando se hace click sobre algún elemento de la lista y el menu
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }
}
