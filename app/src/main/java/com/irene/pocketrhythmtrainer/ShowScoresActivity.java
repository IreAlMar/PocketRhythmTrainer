package com.irene.pocketrhythmtrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowScoresActivity extends AppCompatActivity {

    private static final String TAG = ShowScoresActivity.class.getSimpleName();

    private RoundsAdapter roundsAdapter;
    private ArrayList<Round> arrayOfRounds;
    private boolean orderByRound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores);

        showList();
        super.registerForContextMenu(findViewById(R.id.lv_scores));
    }

    private void showList() {
        if (orderByRound)
            arrayOfRounds = Round.selectOrderedByRound();
        else
            arrayOfRounds = Round.selectOrderedByPlayer();
        if (arrayOfRounds.isEmpty()){
            TextView textEmpty = (TextView) findViewById(R.id.empty);
            textEmpty.setText(R.string.empty);
        }
        roundsAdapter = new RoundsAdapter(this, arrayOfRounds);
        ListView listView = (ListView) findViewById(R.id.lv_scores);
        listView.setAdapter(roundsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "Creating options menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scores_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.help: {
                Toast.makeText(this, getString(R.string.help), Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.delete: {
                clearList();
                return true;
            }
            case R.id.order: {
                orderByRound = !orderByRound;
                showList();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

   @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (orderByRound) {
            MenuItem item = menu.findItem(R.id.order);
            item.setTitle(getString(R.string.order_player));
        } else {
            MenuItem item = menu.findItem(R.id.order);
            item.setTitle(getString(R.string.order_round));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void clearList(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle(R.string.clear_title);
        dialog.setMessage(R.string.clear);

        dialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        roundsAdapter.clear();
                        Round.deleteAll();
                        showList();
                    }
                });
        dialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
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
