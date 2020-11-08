package com.example.master.models;

public class SortingViewModel {

    public int[][] getRows() {
        return rows;
    }

    public void setRows(int[][] rows) {
        this.rows = rows;
    }

    public void updateRow(int index, int[] row) {
        for (int i = 0; i < row.length; i++) {
            this.rows[index][i] = row[i];
        }
    }

    private int[][] rows;

}
