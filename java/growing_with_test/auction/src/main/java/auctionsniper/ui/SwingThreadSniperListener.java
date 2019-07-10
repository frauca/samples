package auctionsniper.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;

import javax.swing.*;

public class SwingThreadSniperListener implements SniperListener {
    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {

        this.snipers = snipers;
    }

    public void sniperStateChanged(final SniperSnapshot snapshot) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                snipers.sniperStateChanged(snapshot);
            }
        });
    }
}

