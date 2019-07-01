package auctionsniper.ui;

import auctionsniper.AuctionState;
import auctionsniper.SniperListener;

import javax.swing.*;

public class SniperStateDisplayer implements SniperListener {
    private final MainWindow ui;

    public SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    public void sniperBidding() {
        showStatus(AuctionState.BINDING);
    }

    public void sniperLost() {
        showStatus(AuctionState.LOST);
    }

    public void sniperWinning() {
        showStatus(AuctionState.WINING);
    }

    @Override
    public void sniperWon() {
        showStatus(AuctionState.WON);
    }

    private void showStatus(final AuctionState status) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(status.value());
            }
        });
    }
}

