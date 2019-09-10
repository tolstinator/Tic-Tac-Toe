package ru.job4j.ttt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Memory memory = new Memory(
            Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9)
    );

    public Executor pool = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        Integer buttonId = view.getId();
        if (memory.isFree(buttonId)) {
            pool.execute(animation(Collections.singletonList(buttonId), "X"));
            memory.setBusy(buttonId, seekX(buttonId), seekY(buttonId), 'X');
            List<Integer> winX = memory.winX();
            if (winX.size() != 0) {
                pool.execute(animationWin(winX));
                clearFields();
            } else {
                if (memory.isFinish()) {
                    msg("Game over!");
                    clearFields();
                } else {
                    Integer buttonIdAI = memory.rnd();
                    pool.execute(animation(Collections.singletonList(buttonIdAI), "O"));
                    memory.setBusy(buttonIdAI, seekX(buttonIdAI), seekY(buttonIdAI), 'O');
                    List<Integer> winO = memory.winO();
                    if (winO.size() != 0) {
                        pool.execute(animationWin(winO));
                        clearFields();
                    }
                }
            }
        }
    }

    private void clearFields() {
        List<Integer> btns = Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9);
        pool.execute(animation(btns, ""));
        memory = new Memory(btns);
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
