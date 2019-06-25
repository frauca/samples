package test.auctionsniper;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.Mocked;
import mockit.Verifications;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;
    @Injectable
    private AuctionEventListener listener;
    private AuctionMessageTranslator translator;

    @BeforeEach
    public void setUp(){
        translator = new AuctionMessageTranslator(listener);
    }

    @Test
    public void notfiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");
        translator.processMessage(UNUSED_CHAT, message);

        new Verifications(){{
            listener.auctionClosed(); times =1;
        }};
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {


        Message message = new Message();
        message.setBody(
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"
        );
        translator.processMessage(UNUSED_CHAT, message);

        new Verifications(){{
            listener.currentPrice(192,7); times =1;
        }};
    }
}