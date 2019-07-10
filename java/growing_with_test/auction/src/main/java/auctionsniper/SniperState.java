package auctionsniper;

import com.objogate.exception.Defect;

import java.util.Objects;

public enum SniperState {
    JOINING("joining"){
        @Override
        public SniperState whenAuctionClosed() {
            return LOST;
        }
    },
    BIDDING("binding"){
        @Override
        public SniperState whenAuctionClosed() {
            return LOST;
        }
    }
    ,
    WINNING("wining"){
        @Override
        public SniperState whenAuctionClosed() {
            return WON;
        }
    },
    LOST("lost"),
    WON("won"),
    STARTING("starting");

    private final String text;

    SniperState(String text){
        this.text = text;
    }

    public String text(){
        return this.text;
    }

    public SniperState whenAuctionClosed() {
        throw new Defect("Auction is already closed");
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public static SniperState findByValue(final String value){
        SniperState result=null;
        for(SniperState state : values()){
            if(state.text().equalsIgnoreCase(value)){
                result = state;
            }
        }
        return Objects.requireNonNull(result,String.format("%s is not a valid value state",value));
    }
}
