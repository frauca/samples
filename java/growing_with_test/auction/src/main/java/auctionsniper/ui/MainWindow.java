package auctionsniper.ui;

import auctionsniper.AuctionState;
import auctionsniper.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME ="Sniper Status";
    private final JLabel sniperStatus = createLabel(AuctionState.JOINING.value());

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300,400));
        add(sniperStatus);
        setResizable(true);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        result.setPreferredSize(new Dimension(150,40));
        return result;
    }

    public void showStatus(String status) {
        sniperStatus.setText(status);
    }
}
