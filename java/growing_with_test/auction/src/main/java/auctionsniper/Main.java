package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.ui.SwingThreadSniperListener;
import auctionsniper.xmpp.XMPPAuctionHouse;
import org.jivesoftware.smack.XMPPConnection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

public class Main {
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    public static final String JOIN_COMMAND_FORMAT = "JOIN";
    public static final String CLOSE_FORMAT = "SOLVersion: 1.1; Event: CLOSE;";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    @SuppressWarnings("unused")
    private Collection<Auction> notToBeGCd;
    private MainWindow ui;
    private final SnipersTableModel snipers = new SnipersTableModel();

    public Main() throws Exception {
        startUserInterface();
        notToBeGCd = new ArrayList<>();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);

        main.addUserRequestListenerFor(auctionHouse);

        return;
    }


    private void addUserRequestListenerFor(AuctionHouse auctionHouse) {
        SniperLauncher launcher = new SniperLauncher(auctionHouse,snipers);

        ui.addUserRequestListener(launcher::joinAuction);
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }

    private void disconnectWhenUICloses(XMPPAuctionHouse auctionHouse) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }


}
