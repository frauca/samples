package test.auctionsniper;

import auctionsniper.SniperState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuctionStateTest {

    @Test
    public void findByValue(){
        SniperState expected = SniperState.BIDDING;
        SniperState result = SniperState.findByValue(expected.text());

        assertEquals(expected,result);
    }
}