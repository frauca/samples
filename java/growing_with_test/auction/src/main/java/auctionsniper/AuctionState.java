package auctionsniper;

public enum AuctionState {
    JOINING("joining"),
    BINDING("binding"),
    WINING("wining"),
    LOST("lost");

    private final String value;

    AuctionState(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

    @Override
    public String toString() {
        return value();
    }
}
