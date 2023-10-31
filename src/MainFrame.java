package src;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    public MainFrame(){
        Dimension dim = new Dimension(1000, 500);

        JFrame frame = new JFrame("직원 검색 시스템");
        frame.setLocation(100, 50);
        frame.setPreferredSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();

        MainPanel mainPanel = new MainPanel();
        JPanel top = mainPanel.getTopPanel();
        JPanel middle = mainPanel.setTable();
        JPanel bottom = mainPanel.getBottomPanel();

        container.add("North", top);
        container.add( middle);
        container.add("South", bottom);

        frame.pack();
        frame.setVisible(true);
    }
}

