package test.auctionsniper.ui;

import auctionsniper.AuctionSniper;
import auctionsniper.SniperSnapshot;
import auctionsniper.ui.Column;
import auctionsniper.ui.SnipersTableModel;
import mockit.Mocked;
import mockit.Verifications;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnipersTableModelTest {

    @Mocked
    private TableModelListener listener;
    private SnipersTableModel model = new SnipersTableModel();

    @BeforeEach
    public void attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test
    public void hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(Column.values().length));
    }

    @Test
    public void setsSniperValuesInColumns() {
        AuctionSniper sniper = new AuctionSniper("item id",null);
        SniperSnapshot bidding = sniper.getSnapshot().bidding(555, 666);

        model.addSniper(sniper);
        model.sniperStateChanged(bidding);

        assertRowMatchesSnapshot(0, bidding);
        new Verifications() {{
            listener.tableChanged(withInstanceOf(TableModelEvent.class)); times = 2;
        }};
    }

    @Test
    public void notifiesListenersWhenAddingASniper() {
        AuctionSniper sniper = new AuctionSniper("item id",null);

        assertEquals(0, model.getRowCount());
        model.addSniper(sniper);
        assertEquals(1, model.getRowCount());
        assertRowMatchesSnapshot(0, sniper.getSnapshot());
        new Verifications() {{
            listener.tableChanged(withInstanceOf(TableModelEvent.class)); times = 1;
        }};
    }

    @Test
    public void holdsSnipersInAdditionOrder() {
        model.addSniper(new AuctionSniper("item 0",null));
        model.addSniper(new AuctionSniper("item 0",null));
        assertEquals("item 0", cellValue(0, Column.ITEM_IDENTIFIER));
        assertEquals("item 1", cellValue(1, Column.ITEM_IDENTIFIER));
    }


    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

    private Matcher<TableModelEvent> aRowChangedEvent() {
        return samePropertyValuesAs(new TableModelEvent(model, 0));
    }

    private Matcher<TableModelEvent> anInsertionAtRow(int row) {
        return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
        assertEquals(snapshot.itemId, cellValue(row, Column.ITEM_IDENTIFIER));
        assertEquals(snapshot.lastPrice, cellValue(row, Column.LAST_PRICE));
        assertEquals(snapshot.lastBid, cellValue(row, Column.LAST_BID));
        assertEquals(snapshot.state.text(), cellValue(row, Column.SNIPER_STATE));
    }

    private Object cellValue(int rowIndex, Column column) {
        return model.getValueAt(rowIndex, column.ordinal());
    }

}
