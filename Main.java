import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame implements ActionListener{
    private JButton searchBtn = new JButton("검색");
    private JLabel sumLabel = new JLabel("뭘 선택?");
    private JButton updateBtn = new JButton("update");
    private JButton deleteBtn = new JButton("선택한 데이터 삭제");
    private JButton insertBtn = new JButton("직원 등록");
    private final String[] category = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    private final String[] modifyList = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};

    private final String[] sexs = {"M", "F"};
    private final String[] departments = {"Research", "Administration", "Headquarters"};
    private JCheckBox[] items = new JCheckBox[7];
    private final String[] item = {"Name", "Ssn", "Address", "Sex", "Salary", "Supervisor", "Department"};
    public Main() {
        Dimension dim = new Dimension(1000, 500);

        JFrame frame = new JFrame("직원 검색 시스템");
        frame.setLocation(100, 50);
        frame.setPreferredSize(dim);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();

        JPanel categoryPanel = new JPanel();

        JComboBox<String> categoryCombo = new JComboBox<String>(category);
        categoryPanel.add(new JLabel("검색범위  "));
        categoryPanel.add(categoryCombo);
        categoryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextField text = new JTextField(20);
        JComboBox sex = new JComboBox<String>(sexs);
        JComboBox department = new JComboBox<String>(departments);
        categoryPanel.add(text);
        categoryPanel.add(sex);
        categoryPanel.add(department);
        text.setVisible(false);
        sex.setVisible(false);
        department.setVisible(false);

        categoryCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = categoryCombo.getSelectedIndex();
                if (selectedIndex == 5) {
                    text.setVisible(false);
                    sex.setVisible(true);
                    department.setVisible(false);
                }
                else if(selectedIndex == 8){
                    text.setVisible(false);
                    sex.setVisible(false);
                    department.setVisible(true);
                }
                else{
                    text.setVisible(true);
                    sex.setVisible(false);
                    department.setVisible(false);
                }
            }
        });

        JPanel itemPanel = new JPanel();
        MyItemListener listener = new MyItemListener();
        itemPanel.add(new JLabel("검색항목  "));
        for(int i=0;i<item.length; i++){
            items[i] = new JCheckBox(item[i]);
            itemPanel.add(items[i]);
            items[i].addItemListener(listener);
        }
        itemPanel.add(sumLabel);
        itemPanel.add(searchBtn);
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchBtn.addActionListener(this);


        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); // 패널 세로정렬
        top.add(categoryPanel);
        top.add(itemPanel);
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        container.add("North", top);

        JPanel selectedEmployeePanel = new JPanel();
        JLabel selected = new JLabel("선택한 직원 :  ");
        JLabel employees = new JLabel();
        String employee = ""; // 나중에 입력
        Font font = new Font("SansSerif", Font.BOLD, 20);
        selected.setFont(font);
        employees.setFont(font);
        selectedEmployeePanel.add(selected);
        selectedEmployeePanel.add(employees);
        selectedEmployeePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel headCountPanel = new JPanel();
        JLabel employeeHeadCount = new JLabel("인원수 :  ");
        JLabel count = new JLabel();
        headCountPanel.add(employeeHeadCount);
        headCountPanel.add(count);
        headCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel updatePanel = new JPanel();
        JLabel modify = new JLabel("수정");
        JComboBox<String> modifyCombo = new JComboBox<String>(modifyList);
        JTextField modifyText = new JTextField(20);
        updatePanel.add(modify);
        updatePanel.add(modifyCombo);
        updatePanel.add(modifyText);
        updatePanel.add(updateBtn);
        updatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        updateBtn.addActionListener(this);

        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteBtn.addActionListener(this);

        JPanel insertPanel = new JPanel();

        insertPanel.add(insertBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertBtn.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(headCountPanel);
        bottomPanel.add(updatePanel);
        bottomPanel.add(deletePanel);
        bottomPanel.add(insertPanel);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JPanel bottom = new JPanel();
        bottom.add(selectedEmployeePanel);
        bottom.add(bottomPanel);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        container.add("South", bottom);

        JPanel tablePanel = new JPanel();
        JTable table = new JTable();

        frame.pack();
        frame.setVisible(true);

    }

    SubFrame sf;

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            //검색 버튼 누르면
        } else if (e.getSource() == updateBtn) {
            //업데이트 버튼 누르면
        } else if (e.getSource() == deleteBtn) {
            // 삭제 버튼 누르면
        } else if (e.getSource() == insertBtn) {
            if (sf == null) {
                sf = new SubFrame();
            } else {
                sf.dispose();
                sf = new SubFrame();
            }
        }
    }

    class MyItemListener implements ItemListener {
        int sum = 0; // 선택 과일들의 합계

        // 체크 박스 선택이 될 때, 얘가 이벤트가 발생함을 알아차림
        @Override
        public void itemStateChanged(ItemEvent e) {
            // 체크가 되었을 때
            if(e.getStateChange() == ItemEvent.SELECTED) {
                if(e.getItem() == items[0]) 		sumLabel.setText(item[0] + "선택");
                else if(e.getItem() == items[1]) 		sumLabel.setText(item[1] + "선택");
                else if(e.getItem() == items[2]) 		sumLabel.setText(item[2] + "선택");
                else if(e.getItem() == items[3]) 		sumLabel.setText(item[3] + "선택");
                else if(e.getItem() == items[4]) 		sumLabel.setText(item[4] + "선택");
                else if(e.getItem() == items[5]) 		sumLabel.setText(item[5] + "선택");
                else if(e.getItem() == items[6]) 		sumLabel.setText(item[6] + "선택");
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
