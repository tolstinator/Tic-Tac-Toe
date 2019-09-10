package ru.job4j.ttt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Memory {
    private final char[][] matrix = new char[3][3];
    private final List<Integer> btnsFree = new LinkedList<>();
    private final List<Integer> answers = new ArrayList<>();

    public Memory(List<Integer> btns) {
        btnsFree.addAll(btns);
    }

    public boolean isFree(Integer id) {
        return btnsFree.contains(id);
    }

    public boolean isFinish() {
        return btnsFree.size() == 0;
    }

    public List<Integer> winX() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 'X' && matrix[i][1] == 'X' && matrix[i][2] == 'X') {
                if (i == 0) {
                    result.add(R.id.button);
                    result.add(R.id.button4);
                    result.add(R.id.button9);
                } else if (i == 1) {
                    result.add(R.id.button2);
                    result.add(R.id.button5);
                    result.add(R.id.button8);
                } else {
                    result.add(R.id.button3);
                    result.add(R.id.button6);
                    result.add(R.id.button7);
                }
                break;
            }
        }
        if (result.size() == 0) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[0][i] == 'X' && matrix[1][i] == 'X' && matrix[2][i] == 'X') {
                    if (i == 0) {
                        result.add(R.id.button);
                        result.add(R.id.button2);
                        result.add(R.id.button3);
                    } else if (i == 1) {
                        result.add(R.id.button4);
                        result.add(R.id.button5);
                        result.add(R.id.button6);
                    } else {
                        result.add(R.id.button9);
                        result.add(R.id.button8);
                        result.add(R.id.button7);
                    }
                    break;
                }
            }
        }
        if (result.size() == 0 && matrix[0][0] == 'X' && matrix[1][1] == 'X' && matrix[2][2] == 'X') {
            result.add(R.id.button);
            result.add(R.id.button5);
            result.add(R.id.button7);
        }
        if (result.size() == 0 && matrix[0][2] == 'X' && matrix[1][1] == 'X' && matrix[2][0] == 'X') {
            result.add(R.id.button9);
            result.add(R.id.button5);
            result.add(R.id.button3);
        }
        return result;
    }

    public List<Integer> winO() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] == 'O' && matrix[i][1] == 'O' && matrix[i][2] == 'O') {
                if (i == 0) {
                    result.add(R.id.button);
                    result.add(R.id.button4);
                    result.add(R.id.button9);
                } else if (i == 1) {
                    result.add(R.id.button2);
                    result.add(R.id.button5);
                    result.add(R.id.button8);
                } else {
                    result.add(R.id.button3);
                    result.add(R.id.button6);
                    result.add(R.id.button7);
                }
                break;
            }
        }
        if (result.size() == 0) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[0][i] == 'O' && matrix[1][i] == 'O' && matrix[2][i] == 'O') {
                    if (i == 0) {
                        result.add(R.id.button);
                        result.add(R.id.button2);
                        result.add(R.id.button3);
                    } else if (i == 1) {
                        result.add(R.id.button4);
                        result.add(R.id.button5);
                        result.add(R.id.button6);
                    } else {
                        result.add(R.id.button9);
                        result.add(R.id.button8);
                        result.add(R.id.button7);
                    }
                    break;
                }
            }
        }
        if (result.size() == 0 && matrix[0][0] == 'O' && matrix[1][1] == 'O' && matrix[2][2] == 'O') {
            result.add(R.id.button);
            result.add(R.id.button5);
            result.add(R.id.button7);
        }
        if (result.size() == 0 && matrix[0][2] == 'O' && matrix[1][1] == 'O' && matrix[2][0] == 'O') {
            result.add(R.id.button9);
            result.add(R.id.button5);
            result.add(R.id.button3);
        }
        return result;
    }

    public void setBusy(Integer id, int x, int y, char player) {
        btnsFree.remove(id);
        matrix[x][y] = player;

    }

    public Integer rnd() {
        Random rn = new Random();
        return btnsFree.get(rn.nextInt(btnsFree.size()));
    }
}
