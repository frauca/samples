package test.endtoend.auctionsniper;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();


    @Test
    public void
    sniperWinsAnAuctionByBiddingHigher() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(1000,1098);

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWining(1098);

        auction.announceClosed();
        application.showsSniperHasWonAuction(1098);
    }

    // Additional cleanup
    @AfterEach
    public void stopAuction() {
        auction.stop();
    }

    @AfterEach
    public void stopApplication() {
        application.stop();
    }
}
