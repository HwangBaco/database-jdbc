package src;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class MainFrame extends JFrame {
    public MainFrame(){
        setTitle("직원 검색 시스템");
        setVisible(true); // default가 false이므로 보여지기 위해서 선언
        setLocation(100, 50);
        setSize(1000, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 메인 panel 생성
        MainPanel mainPanel = new MainPanel(this);
        JPanel top = mainPanel.getSearchPanel();
        JPanel middle = mainPanel.getTablePanel();
        JPanel bottom = mainPanel.getContextPanel();

        Container container = getContentPane();
        container.add("North", top);
        container.add( middle, BorderLayout.CENTER);
        container.add("South", bottom);

        pack(); // 위 contents들이 메인프레임에 들어오게 하는 목적인 걸로 보임 (but, deprecated)
    }
}

