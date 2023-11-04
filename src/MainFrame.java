package src;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    public MainFrame(){
        // 메인 프레임 기본 configuration
        JFrame frame = new JFrame("직원 검색 시스템");
        frame.setVisible(true); // default가 false이므로 보여지기 위해서 선언

        // 프레임 첫 등장 좌표
        frame.setLocation(100, 50);


        // 메인프레임 window size apply
        setSize(1000, 500);

        // 종료
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 메인 panel 생성
        MainPanel mainPanel = new MainPanel();
        JPanel top = mainPanel.getTopPanel();
        JPanel middle = mainPanel.getTablePanel();
        JPanel bottom = mainPanel.getBottomPanel();

        // 컨텐츠 container에 top/bottom panel 삽입
        Container container = frame.getContentPane();
        container.add("North", top);
        container.add( middle);
        container.add("South", bottom);

        frame.pack(); // 위 contents들이 메인프레임에 들어오게 하는 목적인 걸로 보임 (but, deprecated)
    }
}

