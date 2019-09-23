package test.integration.auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.junit.jupiter.api.Test;
import test.endtoend.auctionsniper.ApplicationRunner;
import test.endtoend.auctionsniper.FakeAuctionServer;

import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    XMPPConnection connection = new XMPPConnection(FakeAuctionServer.XMPP_HOSTNAME);

    @Test
    public void
    receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        Auction auction = new XMPPAuction(connection, auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();
        assertTrue(auctionWasClosed.await(2, SECONDS), "should have been closed");
    }

    private AuctionEventListener
    auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            public void currentPrice(int price, int increment, PriceSource priceSource) {
// not implemented
            }
        };
    }
}
