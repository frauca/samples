package auctionsniper.ui;

import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0,SniperState.STARTING);
    private SniperSnapshot snapshot = STARTING_UP;

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return 1;
    }

     public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSniperState) {
        snapshot = newSniperState;
        fireTableRowsUpdated(0, 0);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }
}
