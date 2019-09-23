package auctionsniper;

import java.util.EventListener;

public interface SniperPortfolio extends EventListener {
    void sniperAdded(AuctionSniper sniper);
}
