package auctionsniper.ui;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import com.google.common.eventbus.EventBus;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String MAIN_WINDOW_TITLE = "Auction Sniper";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String JOIN_BUTTON_NAME = "join button";
    private final EventBus eventBus = new EventBus();
    SniperPortfolio portfolio;

    public MainWindow(SnipersTableModel snipers) {
        super(MAIN_WINDOW_TITLE);
        setName(MAIN_WINDOW_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 300));
        fillContentPane(makeSnipersTable(snipers), makeControls());
        setResizable(true);
        pack();
        setVisible(true);
    }

    public void addUserRequestListener(UserRequestListener listener) {
        eventBus.register(listener);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPERS_TABLE_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        result.setPreferredSize(new Dimension(150, 40));
        return result;
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable(SnipersTableModel snipers) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);
        return snipersTable;
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(itemIdField);
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);
        joinAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eventBus.post(itemIdField.getText());
            }
        });
        controls.add(joinAuctionButton);
        return controls;
    }


}
