package auctionsniper.ui;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperCollector;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import com.objogate.exception.Defect;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.STARTING);
    private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
    private final ArrayList<AuctionSniper> notToBeGCd = new ArrayList<>();

    @Override
    public void addSniper(AuctionSniper sniper) {
        notToBeGCd.add(sniper);
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }


    public void addSniperSnapshot(SniperSnapshot snapshot) {
        int row = snapshots.size();
        snapshots.add(snapshot);
        fireTableChanged(new TableModelEvent(this, row));
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public int getRowCount() {
        return snapshots.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        SniperSnapshot snapshot = snapshots.get(rowIndex);
        return Column.at(columnIndex).valueIn(snapshot);
    }

    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + snapshot);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

}
