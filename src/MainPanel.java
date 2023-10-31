package src;

import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import src.JDBC.JDBC;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JFrame {
    JButton searchBtn, updateBtn, deleteBtn, insertBtn;
    JLabel sumLabel;
    JCheckBox[] items;
    final String[] category = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    final String[] item = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    final String[] sexs = {"M", "F"};
    String[] departments = {"Research", "Administration", "Headquarters"};
    DefaultTableModel model = new DefaultTableModel(0, 0);
    JPanel categoryPanel, itemPanel, employeePanel,headcountPanel, updatePanel, deletePanel, insertPanel;
    JPanel topPanel, btmPanel, bottomPanel;

    // 사용자에 따라 id, password 변경
    private static final String dbacct = "root";
    private static final String passwrd = "junhee1202";
    private static final String dbname = "company";
    JDBC jdbc = new JDBC(dbacct, passwrd, dbname);

    MainPanel() {
        searchBtn = new JButton("검색");
        sumLabel = new JLabel("뭘 선택?");
        updateBtn = new JButton("update");
        deleteBtn = new JButton("선택한 데이터 삭제");
        insertBtn = new JButton("직원 등록");
        items = new JCheckBox[8];

        categoryPanel = setCategory();
        itemPanel = setItems();
        employeePanel = setEmployees();
        headcountPanel = setHeadCounts();
        updatePanel = setUpdate();
        deletePanel = setDelete();
        insertPanel = setInsert();
        topPanel = setTop(categoryPanel, itemPanel);
        btmPanel = halfBottom(headcountPanel, updatePanel, deletePanel, insertPanel);
        bottomPanel = setBottom(btmPanel, employeePanel);

        jdbc.connectJDBC();

    }

    public JPanel getTopPanel(){
        return this.topPanel;
    }
    public JPanel getBottomPanel(){
        return this.bottomPanel;
    }

    public JPanel setCategory(){
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
                if (selectedIndex == 5) { //Sex
                    text.setVisible(false);
                    sex.setVisible(true);
                    department.setVisible(false);
                }
                else if(selectedIndex == 8){ //Department
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

        return categoryPanel;
    }

    public JPanel setItems(){
        JPanel itemPanel = new JPanel();
        //MyItemListener listener = new MyItemListener();

        itemPanel.add(new JLabel("검색항목  "));
        for(int i=0;i<item.length; i++){
            items[i] = new JCheckBox(item[i]);
            itemPanel.add(items[i]);
            //items[i].addItemListener(listener);
        }
        itemPanel.add(searchBtn);

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int val = 1;
                for(int i = 0; i < items.length; i++){
                    if(items[i].isSelected()) val *= 0;
                }
                if(val == 1) JOptionPane.showMessageDialog(null, "하나 이상의 범위를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
        itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        searchBtn.addActionListener(this::actionPerformed);
        return itemPanel;
    }

    public JPanel setTable(){
        JPanel tablePanel = new JPanel();
        JTable table = new JTable(this.model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(950, 300));
        tablePanel.add(scrollPane);
        return tablePanel;
    }


    public JPanel setEmployees(){
        JPanel selectedEmployeePanel = new JPanel();

        JLabel selected = new JLabel("선택한 직원 :  ");
        String employee = ""; // 나중에 입력
        JLabel employees = new JLabel(employee);

        Font font = new Font("SansSerif", Font.BOLD, 20);
        selected.setFont(font);
        employees.setFont(font);

        selectedEmployeePanel.add(selected);
        selectedEmployeePanel.add(employees);

        selectedEmployeePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return selectedEmployeePanel;
    }

    public JPanel setHeadCounts(){
        JPanel headCountPanel = new JPanel();

        JLabel employeeHeadCount = new JLabel("인원수 :  ");
        JLabel count = new JLabel();

        headCountPanel.add(employeeHeadCount);
        headCountPanel.add(count);

        headCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return headCountPanel;
    }

    public JPanel setUpdate(){
        JPanel updatePanel = new JPanel();

        JLabel modify = new JLabel("수정");
        JComboBox<String> updateCombo = new JComboBox<String>(item);

        JTextField modifyText = new JTextField(20);
        JComboBox sex = new JComboBox<String>(sexs);
        JComboBox department = new JComboBox<String>(departments);

        updatePanel.add(modify);
        updatePanel.add(updateCombo);
        updatePanel.add(modifyText);
        updatePanel.add(sex);
        updatePanel.add(department);
        updatePanel.add(updateBtn);

        modifyText.setVisible(true);
        sex.setVisible(false);
        department.setVisible(false);

        updateCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = updateCombo.getSelectedIndex();
                if (selectedIndex == 4) { //Sex
                    modifyText.setVisible(false);
                    sex.setVisible(true);
                    department.setVisible(false);
                }
                else if(selectedIndex == 7){ //Department
                    modifyText.setVisible(false);
                    sex.setVisible(false);
                    department.setVisible(true);
                }
                else{
                    modifyText.setVisible(true);
                    sex.setVisible(false);
                    department.setVisible(false);
                }
            }
        });

        updatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        updateBtn.addActionListener(this::actionPerformed);

        return updatePanel;
    }

    public JPanel setDelete(){
        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteBtn.addActionListener(this::actionPerformed);
        return deletePanel;
    }

    public JPanel setInsert(){
        JPanel insertPanel = new JPanel();
        insertPanel.add(insertBtn);
        insertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertBtn.addActionListener(this::actionPerformed);
        return insertPanel;
    }

    public JPanel setTop(JPanel categoryPanel, JPanel itemPanel){
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); // 패널 세로정렬
        top.add(categoryPanel);
        top.add(itemPanel);
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        return top;
    }

    public JPanel halfBottom(JPanel headCountPanel, JPanel updatePanel, JPanel deletePanel, JPanel insertPanel){
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(headCountPanel);
        bottomPanel.add(updatePanel);
        bottomPanel.add(deletePanel);
        bottomPanel.add(insertPanel);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        return bottomPanel;
    }

    public JPanel setBottom(JPanel bottomPanel, JPanel selectedEmployeePanel){
        JPanel bottom = new JPanel();
        bottom.add(selectedEmployeePanel);
        bottom.add(bottomPanel);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        return bottom;
    }


    SubFrame sf;
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == searchBtn) {
            //검색 버튼 누르면 jdbc 연결 후 보고서 출력 후 연결 해제
            jdbc.connectJDBC();
            model = jdbc.printReport(model, items); // 모델이 반환됨
            //showTable(model);
            jdbc.disconnectJDBC();
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
}

