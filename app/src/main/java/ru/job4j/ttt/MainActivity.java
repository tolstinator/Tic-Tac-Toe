package ru.job4j.ttt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Memory memory = new Memory(
            Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
    );

    private ArrayList<Integer> listXButton = new ArrayList<>();
    private ArrayList<Integer> listOButton = new ArrayList<>();
    private char currentPlayer = 'X';

    private final String LISTXBUTTON = "listXButton";
    private final String LISTOBUTTON = "listOButton";
    private final String CURRENTPLAYER = "currentPlayer";

    public Executor pool = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MainActivity.class.getName(), "onCreate");
        load(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(MainActivity.class.getName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MainActivity.class.getName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.class.getName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(MainActivity.class.getName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.class.getName(), "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(MainActivity.class.getName(), "onSaveInstanceState");
        outState.putIntegerArrayList(LISTXBUTTON, listXButton);
        outState.putIntegerArrayList(LISTOBUTTON, listOButton);
        outState.putChar(CURRENTPLAYER, currentPlayer);
    }

    private void load(Bundle state) {
        if (state != null) {
            for (Integer id : state.getIntegerArrayList(LISTXBUTTON)) {
                ((Button) findViewById(id)).setText("X");
                memory.setBusy(id, seekX(id), seekY(id), 'X');
            }
            for (Integer id : state.getIntegerArrayList(LISTOBUTTON)) {
                ((Button) findViewById(id)).setText("O");
                memory.setBusy(id, seekX(id), seekY(id), 'O');
            }
            currentPlayer = state.getChar(CURRENTPLAYER);
        }
    }

    private Runnable animationWin(List<Integer> ids) {
        return () -> {
            try {
                for (Integer id : ids) {
                    Button btn = findViewById(id);
                    runOnUiThread(() -> btn.setBackgroundColor(0xFFA1F33F));
                }
                Thread.sleep(200);
                for (Integer id : ids) {
                    Button btn = findViewById(id);
                    runOnUiThread(() -> btn.setBackgroundColor(0xFF8BC34A));
                }
                Thread.sleep(200);
                for (Integer id : ids) {
                    Button btn = findViewById(id);
                    runOnUiThread(() -> btn.setBackgroundColor(0xFFA1F33F));
                }
                Thread.sleep(200);
                for (Integer id : ids) {
                    Button btn = findViewById(id);
                    runOnUiThread(() -> btn.setBackgroundColor(0xFF8BC34A));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable animation(List<Integer> ids, String text) {
        return () -> {
            for (Integer id : ids) {
                Button btn = findViewById(id);
                runOnUiThread(() -> btn.setText(text));
            }
        };
    }

    public void answer(View view) {
        boolean sw = ((Switch) findViewById(R.id.switch1)).isChecked();
        int buttonId = view.getId();
        if (memory.isFree(buttonId)) {
            List<Integer> winX = makeMove(buttonId, currentPlayer);
            if (winX.size() != 0) {
                pool.execute(animationWin(winX));
                clearFields();
            } else if (currentPlayer == 'O' && sw) {
                if (memory.isFinish()) {
                    msg("Game over!");
                    clearFields();
                } else {
                    Integer buttonIdAI = memory.rnd();
                    List<Integer> winO = makeMove(buttonIdAI, 'O');
                    if (winO.size() != 0) {
                        pool.execute(animationWin(winO));
                        clearFields();
                    }
                }
            }
        }
    }

    private List<Integer> makeMove(int id, char player) {
        pool.execute(animation(Collections.singletonList(id), player == 'X' ? "X" : "O"));
        listXButton.add(id);
        memory.setBusy(id, seekX(id), seekY(id), player);
        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
        List<Integer> result;
        if (player == 'X') {
            result = memory.winX();
        } else {
            result = memory.winO();
        }
        return result;
    }

    private void clearFields() {
        List<Integer> btns = Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9);
        pool.execute(animation(btns, ""));
        memory = new Memory(btns);
        listXButton = new ArrayList<>();
        listOButton = new ArrayList<>();
        currentPlayer = 'X';
    }

    private int seekX(Integer id) {
        int result = 0;
        Button btn = findViewById(id);
        if (btn.getId() == R.id.button2 || btn.getId() == R.id.button5 || btn.getId() == R.id.button8) {
            result = 1;
        } else if (btn.getId() == R.id.button3 || btn.getId() == R.id.button6 || btn.getId() == R.id.button7) {
            result = 2;
        }
        return result;
    }

    private int seekY(Integer id) {
        int result = 0;
        Button btn = findViewById(id);
        if (btn.getId() == R.id.button4 || btn.getId() == R.id.button5 || btn.getId() == R.id.button6) {
            result = 1;
        } else if (btn.getId() == R.id.button9 || btn.getId() == R.id.button8 || btn.getId() == R.id.button7) {
            result = 2;
        }
        return result;
    }

    private void msg(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Result");
        alertDialogBuilder
                .setMessage(text)
                .setPositiveButton("Ok", (dialog, id) -> {});
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
