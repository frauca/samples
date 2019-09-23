package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;
import auctionsniper.ui.SwingThreadSniperListener;
import auctionsniper.xmpp.XMPPAuction;
import com.google.common.eventbus.EventBus;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

public class Main {
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
    public static final String JOIN_COMMAND_FORMAT = "JOIN";
    public static final String CLOSE_FORMAT = "SOLVersion: 1.1; Event: CLOSE;";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    @SuppressWarnings("unused")
    private Collection<Chat> notToBeGCd;
    private MainWindow ui;
    private final SnipersTableModel snipers = new SnipersTableModel();

    public Main() throws Exception {
        startUserInterface();
        notToBeGCd = new ArrayList<>();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        XMPPConnection connection = connectTo(args[ARG_HOSTNAME],
                args[ARG_USERNAME],
                args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);

        main.addUserRequestListenerFor(connection);

        return;
    }

    private static XMPPConnection connectTo(String hostname, String username, String password)
            throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private void addUserRequestListenerFor(final XMPPConnection connection) {
        ui.addUserRequestListener((itemId) ->{
            snipers.addSniper(SniperSnapshot.joining(itemId));
            EventBus auctionEventListeners = new EventBus();
            Auction auction = new XMPPAuction(connection,itemId);
            auctionEventListeners.register(new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));
            auction.join();
        });
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId,
                connection.getServiceName());
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }


}
