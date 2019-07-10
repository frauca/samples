package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String MAIN_WINDOW_TITLE = "Auction Sniper";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        super(MAIN_WINDOW_TITLE);
        this.snipers = snipers;
        setName(MAIN_WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 200));
        fillContentPane(makeSnipersTable());
        setResizable(true);
        pack();
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPERS_TABLE_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        result.setPreferredSize(new Dimension(150, 40));
        return result;
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }


}
