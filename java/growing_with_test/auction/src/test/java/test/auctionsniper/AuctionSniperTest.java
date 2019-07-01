package test.auctionsniper;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperTest {

    @Mocked
    Auction auction;
    @Mocked
    SniperListener listener;
    private AuctionSniper sniper;

    @BeforeEach
    public void setUp() {
        sniper = new AuctionSniper(auction, listener);
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        sniper.auctionClosed();

        new Verifications() {{
            listener.sniperLost();
            times = 1;
        }};
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {


        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        new Verifications() {{
            listener.sniperLost();
            times = 1;
        }};
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {

        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        sniper.auctionClosed();

        new Verifications() {{
            listener.sniperWon();
            times = 1;
        }};
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

        new Verifications() {{
            auction.bid(price + increment);
            times = 1;
            listener.sniperBidding();
            minTimes = 1;
        }};
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {

        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        new Verifications() {{
            listener.sniperWinning();
            minTimes = 1;
        }};
    }
}
