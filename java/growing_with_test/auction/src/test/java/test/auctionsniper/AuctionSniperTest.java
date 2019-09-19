package test.auctionsniper;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.AuctionSniper;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import mockit.Mocked;
import mockit.Verifications;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperTest {

    @Mocked
    Auction auction;
    @Mocked
    SniperListener listener;
    private AuctionSniper sniper;
    private static final String ITEM_ID = "item-id";

    @BeforeEach
    public void setUp() {
        sniper = new AuctionSniper(ITEM_ID, auction, listener);
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        sniper.auctionClosed();

        new Verifications() {{
            SniperSnapshot snapshot;
            listener.sniperStateChanged(snapshot = withCapture());
            assertThat(snapshot, hasASniperThatIs(SniperState.LOST));
        }};
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {


        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        new Verifications() {{
            SniperSnapshot snapshot;
            listener.sniperStateChanged(snapshot = withCapture());
            assertThat(snapshot, hasASniperThatIs(SniperState.LOST));
        }};
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {

        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        sniper.auctionClosed();

        new Verifications() {{
            SniperSnapshot snapshot;
            listener.sniperStateChanged(snapshot = withCapture());
            assertThat(snapshot, hasASniperThatIs(SniperState.WON));
        }};
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        int bid = increment + price;

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

        new Verifications() {{
            SniperSnapshot snaptshot;
            auction.bid(bid);
            times = 1;
            listener.sniperStateChanged(snaptshot = withCapture());
            assertThat(snaptshot, hasASniperThatIs(SniperState.BIDDING));
        }};
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {

        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, PriceSource.FromSniper);
        new Verifications() {{
            List<SniperSnapshot> snapshots = new ArrayList<>();
            listener.sniperStateChanged(withCapture(snapshots));

            assertThat(snapshots.get(0), hasASniperThatIs(SniperState.BIDDING));
            assertThat(snapshots.get(1), hasASniperThatIs(SniperState.WINNING));

        }};
    }

    private Matcher<SniperSnapshot> hasASniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was") {
            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }

}
