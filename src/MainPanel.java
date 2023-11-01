package src;

import src.JDBC.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainPanel extends JFrame implements MouseListener {
    private static final int CHECKBOX_NUM = 8;
    final String[] searchRanges = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    final String[] searchItems = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    final String[] sexStrings = {"M", "F"};

    JButton searchBtn, updateBtn, deleteBtn, insertBtn;
    String[] departmentStrings = {"Research", "Administration", "Headquarters"};
    DefaultTableModel model = new DefaultTableModel(0, 0); // edited
    JTable table = new JTable(this.model); // edited
    JCheckBox[] items; // edited
    JPanel categoryPanel, itemPanel, employeePanel,headcountPanel, updatePanel, deletePanel, insertPanel;
    JPanel topPanel, halfBottomPanel, halfBottom;

    JTextField text;
    JComboBox sex;
    JComboBox department;
    JComboBox<String> categoryCombo;

    // 사용자에 따라 id, password 변경
    private static final String ID = "root";
    private static final String PW = "junhee1202";
    private static final String DB_NAME = "company";
    JDBC jdbc = new JDBC(ID, PW, DB_NAME);

    MainPanel() {
        searchBtn = new JButton("검색");
        updateBtn = new JButton("update");
        deleteBtn = new JButton("선택한 데이터 삭제");
        insertBtn = new JButton("직원 등록");
        items = new JCheckBox[CHECKBOX_NUM];

        // 메인 패널에 추가될 sub Panels
        categoryPanel = getSearchRangePanel();
        itemPanel = getSearchItemPanel();
        employeePanel = getSelectedEmpPanel();
        headcountPanel = getHeadCounts();
        updatePanel = getUpdateItemPanel();
        deletePanel = getDeleteItemPanel();
        insertPanel = getInsertItemPanel();

        // 구역별 sub Panels 위치
        ArrayList<JPanel> topPanels = new ArrayList<>();
        topPanels.add(categoryPanel);
        topPanels.add(itemPanel);
        topPanel = setTop(topPanels);

        ArrayList<JPanel> halfBottomPanels = new ArrayList<>();
        halfBottomPanels.add(headcountPanel);
        halfBottomPanels.add(updatePanel);
        halfBottomPanels.add(deletePanel);
        halfBottomPanels.add(insertPanel);
        halfBottom = setHalfBottom(halfBottomPanels);


        ArrayList<JPanel> bottomPanels = new ArrayList<>();
        bottomPanels.add(halfBottom);
        bottomPanels.add(employeePanel);

        halfBottomPanel = setBottom(bottomPanels);

        jdbc.connectJDBC();

    }

    public JPanel getTopPanel(){
        return this.topPanel;
    }
    public JPanel getBottomPanel(){
        return this.halfBottom;
    }


    // 컴색 범위 텍스트 + 드랍박스 injection
    public JPanel getSearchRangePanel(){
        JPanel searchRangePanel = new JPanel();
        JComboBox<String> searchRangeComboBox = new JComboBox<>(searchRanges);
        searchRangePanel.add(new JLabel("검색범위  "));
        searchRangePanel.add(searchRangeComboBox);
        searchRangePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField searchTextBox = new JTextField(20);
        JComboBox<String> sexComboBox = new JComboBox<>(this.sexStrings);
        JComboBox<String> departmentComboBox = new JComboBox<>(departmentStrings);

        searchRangePanel.add(searchTextBox);
        searchRangePanel.add(sexComboBox);
        searchRangePanel.add(departmentComboBox);

        searchTextBox.setVisible(false);
        sexComboBox.setVisible(false);
        departmentComboBox.setVisible(false);

        searchRangeComboBox.addActionListener(e -> {
            int selectedIndex = searchRangeComboBox.getSelectedIndex();
            if (selectedIndex == 0) { // 전체
                searchTextBox.setVisible(false);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(false);
            } else if (selectedIndex == 5) { //Sex
                searchTextBox.setVisible(false);
                sexComboBox.setVisible(true);
                departmentComboBox.setVisible(false);
            } else if(selectedIndex == 8){ //Department
                searchTextBox.setVisible(false);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(true);
            } else{
                searchTextBox.setVisible(true);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(false);
            }
        });

        return searchRangePanel;
    }

    public JPanel getSearchItemPanel(){
        JPanel searchItemPanel = new JPanel();
        JLabel searchItemLabel = new JLabel("검색항목  ");

        searchItemPanel.add(searchItemLabel);

        for (int i = 0; i < CHECKBOX_NUM; i++) {
            items[i] = new JCheckBox(searchItems[i]);
            searchItemPanel.add(items[i]);
        }


        searchItemPanel.add(searchBtn);
        searchItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchBtn.addActionListener(this::actionPerformed);

        return searchItemPanel;
    }

    public JPanel setTable(){
        JPanel tablePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(this.table);
        table.addMouseListener(this);
        scrollPane.setPreferredSize(new Dimension(950, 300));
        tablePanel.add(scrollPane);
        return tablePanel;
    }
    public JPanel getSelectedEmpPanel(){
        JPanel selectedEmployeePanel = new JPanel();

        JLabel selectedEmpLabel = new JLabel("선택한 직원 :  ");
        String employee = ""; // 나중에 입력
        JLabel selectedEmps = new JLabel(employee);

        // apply font
        Font font = new Font("SansSerif", Font.BOLD, 20);
        selectedEmpLabel.setFont(font);
        selectedEmps.setFont(font);

        //
        selectedEmployeePanel.add(selectedEmpLabel);
        selectedEmployeePanel.add(selectedEmps);

        selectedEmployeePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return selectedEmployeePanel;
    }

    public JPanel getHeadCounts(){
        JPanel headCountPanel = new JPanel();

        JLabel headCountLabel = new JLabel("인원수 :  ");
        JLabel headCounts = new JLabel();

        headCountPanel.add(headCountLabel);
        headCountPanel.add(headCounts);

        headCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return headCountPanel;
    }

    public JPanel getUpdateItemPanel(){
        JPanel updateItemPanel = new JPanel();
        JLabel updateItemLabel = new JLabel("수정 ");

        JComboBox<String> updateItemComboBox = new JComboBox<>(searchItems);

        JTextField updateTextBox = new JTextField(20);
        JComboBox<String> sexComboBox = new JComboBox<>(this.sexStrings);
        JComboBox<String> departmentComboBox = new JComboBox<>(departmentStrings);

        updateItemPanel.add(updateItemLabel);
        updateItemPanel.add(updateItemComboBox);
        updateItemPanel.add(updateTextBox);
        updateItemPanel.add(sexComboBox);
        updateItemPanel.add(departmentComboBox);
        updateItemPanel.add(updateBtn);

        updateTextBox.setVisible(true);
        sexComboBox.setVisible(false);
        departmentComboBox.setVisible(false);

        updateItemComboBox.addActionListener(e -> {
            int selectedIndex = updateItemComboBox.getSelectedIndex();

            if (selectedIndex == 4) { //Sex
                updateTextBox.setVisible(false);
                sexComboBox.setVisible(true);
                departmentComboBox.setVisible(false);
            }
            else if(selectedIndex == 7){ //Department
                updateTextBox.setVisible(false);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(true);
            }
            else{
                updateTextBox.setVisible(true);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(false);
            }
        });

        updateItemPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        updateBtn.addActionListener(this::actionPerformed);

        return updateItemPanel;
    }

    public JPanel getDeleteItemPanel(){
        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteBtn.addActionListener(this::actionPerformed);
        return deletePanel;
    }

    public JPanel getInsertItemPanel(){
        JPanel insertPanel = new JPanel();
        insertPanel.add(insertBtn);
        insertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertBtn.addActionListener(this::actionPerformed);
        return insertPanel;
    }

    public JPanel setTop(ArrayList<JPanel> panels){
        JPanel top = new JPanel();
        for (JPanel panel : panels) {
            top.add(panel);
        }
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); // 패널 세로정렬
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        return top;
    }

    public JPanel setHalfBottom(ArrayList<JPanel> panels){
        JPanel bottomPanel = new JPanel();
        for (JPanel panel : panels) {
            bottomPanel.add(panel);
        }
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        return bottomPanel;
    }

    public JPanel setBottom(ArrayList<JPanel> panels){
        JPanel bottomPanel = new JPanel();
        for (JPanel panel : panels) {
            bottomPanel.add(panel);
        }
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        return bottomPanel;
    }

    //MouseListener override 함수
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        for (int i = 0; i < table.getColumnCount(); i++) {
            System.out.print(table.getModel().getValueAt(row, i )+"\t"); //삭제할 때 필요한 record 정보
        } }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }


    SubFrame sf;

    /*
    * 버튼 이벤트리스너 관리
    * */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            boolean isSelected = false;
            for(JCheckBox item : items){
                if(item.isSelected()){
                    isSelected = true;
                }
            }
            if (!isSelected) {
                JOptionPane.showMessageDialog(null, "하나 이상의 범위를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            } else {
                //검색 버튼 누르면 jdbc 연결 후 보고서 출력 후 연결 해제
                jdbc.connectJDBC();
                model = jdbc.printReport(model, items); // 모델이 반환됨
                //showTable(model);
                jdbc.disconnectJDBC();
            }
        } else if (e.getSource() == updateBtn) {
            //업데이트 버튼 누르면
        } else if (e.getSource() == deleteBtn) {
            boolean[] boolArray = {false, false, false};
            boolArray[0] = text.isVisible();
            boolArray[1] = sex.isVisible();
            boolArray[2] = department.isVisible();

            // 삭제 버튼 누르면
            jdbc.connectJDBC();
            if(jdbc.deleteEmployee(text, sex, department, boolArray, categoryCombo))
                JOptionPane.showMessageDialog(this, "직원 정보 삭제 성공");
            else JOptionPane.showMessageDialog(this, "직원 정보 삭제 실패");
            jdbc.disconnectJDBC();
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
