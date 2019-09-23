package auctionsniper;

import auctionsniper.util.Announcer;

public class AuctionSniper implements AuctionEventListener {

    private final Auction auction;
    private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);

    private SniperSnapshot snapshot;

    private boolean isWinning = false;

    public AuctionSniper(String itemId, Auction auction) {
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    public void addSniperListener(SniperListener listener) {
        listeners.addListener(listener);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if (isWinning) {
            snapshot = snapshot.winning(price);
        } else {
            int bid = price + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        }
        notifyChange();
    }

    private void notifyChange() {
        listeners.announce().sniperStateChanged(snapshot);
    }
}
