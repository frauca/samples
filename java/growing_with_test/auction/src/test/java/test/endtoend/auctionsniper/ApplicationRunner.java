package test.endtoend.auctionsniper;

import auctionsniper.SniperState;
import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
    private AuctionSniperDriver driver;
    private String itemId;

    public void startBiddingIn(final FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        itemId = auction.getItemId();
        driver.hasTitle(MainWindow.MAIN_WINDOW_TITLE);
        driver.hasColumnTitles();
    }

    public void showsSniperHasLostAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SniperState.LOST);
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, SniperState.BIDDING);
    }

    public void hasShownSniperIsWining(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SniperState.WINNING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SniperState.WON);
    }
}