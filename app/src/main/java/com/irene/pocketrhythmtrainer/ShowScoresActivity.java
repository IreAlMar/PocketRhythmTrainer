package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowScoresActivity extends Activity {

    private RoundsAdapter roundsAdapter;
    private static final String TAG = ShowScoresActivity.class.getSimpleName();
    private ArrayList<Round> arrayOfRounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);

        showList();
        super.registerForContextMenu(findViewById(R.id.lv_scores));
    }

    private void showList() {
        arrayOfRounds = Round.listAll();
        roundsAdapter = new RoundsAdapter(this, arrayOfRounds);
        ListView listView = (ListView) findViewById(R.id.lv_scores);
        listView.setAdapter(roundsAdapter);
    }

    private void clearList(){

        roundsAdapter.clear();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.round_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_item:
                Log.e(TAG, String.valueOf(info.id));
                Round auxRound = arrayOfRounds.get(Integer.valueOf((int) info.id));
                Round.delete(auxRound.getId());
                showList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
