package auctionsniper;

import com.google.common.eventbus.Subscribe;

import java.util.EventListener;

public interface UserRequestListener extends EventListener {

    @Subscribe
    void joinAuction(String itemId);
}
