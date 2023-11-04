package src;

import src.JDBC.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import static src.Main.*;

public class MainPanel extends JFrame /*implements MouseListener*/ {
    private static final int CHECKBOX_NUM = 8;
    final String[] searchRanges = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    final String[] searchItems = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    final String[] sexStrings = {"M", "F"};

    JButton searchBtn, updateBtn, deleteBtn, insertBtn;
    String[] departmentStrings = {"Research", "Administration", "Headquarters"};
    DefaultTableModel model = new DefaultTableModel(0, 0); // edited

    private int NAME_COLUMN = 0;
    JTable table = new JTable(this.model){
        @Override
        public Class getColumnClass(int column) {
            if (column == 0) {
                return Boolean.class;
            } else
                return String.class;
        }
    }; // edited
    JCheckBox[] items; // edited
    JPanel searchRangePanel, searchItemPanel, selectedEmpPanel,headcountPanel, updatePanel, deletePanel, insertPanel;
    JPanel topPanel, bottomPanel, leftBottomPanel;

    private JLabel selectedEmpStrings = new JLabel();
    JTextField text;
    JComboBox<String> sex;
    JComboBox<String> department;
    JComboBox<String> categoryCombo;

    JDBC jdbc = new JDBC(ID, PW, DB_NAME);

    MainPanel() {
        searchBtn = new JButton("검색");
        updateBtn = new JButton("update");
        deleteBtn = new JButton("선택한 데이터 삭제");
        insertBtn = new JButton("직원 등록");
        items = new JCheckBox[CHECKBOX_NUM];

        // 메인 패널에 추가될 sub Panels
        searchRangePanel = getSearchRangePanel();
        searchItemPanel = getSearchItemPanel();
        selectedEmpPanel = getSelectedEmpPanel();
        headcountPanel = getHeadCounts();
        updatePanel = getUpdateItemPanel();
        deletePanel = getDeleteItemPanel();
        insertPanel = getInsertItemPanel();

        // 구역별 sub Panels 위치
        ArrayList<JPanel> searchPanels = new ArrayList<>();
        searchPanels.add(searchRangePanel);
        searchPanels.add(searchItemPanel);
        topPanel = setTopPanel(searchPanels);

        ArrayList<JPanel> commandPanels = new ArrayList<>();
        commandPanels.add(headcountPanel);
        commandPanels.add(updatePanel);
        commandPanels.add(deletePanel);
        commandPanels.add(insertPanel);
        leftBottomPanel = setLeftBottomPanel(commandPanels);


        ArrayList<JPanel> bottomPanels = new ArrayList<>();
        bottomPanels.add(leftBottomPanel);
        bottomPanels.add(selectedEmpPanel);

        bottomPanel = setBottom(bottomPanels);

        jdbc.connectJDBC();
    }

    public JPanel getTopPanel(){
        return this.topPanel;
    }
    public JPanel getBottomPanel(){
        return this.leftBottomPanel;
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

        //이부분 지우지 말아주세요! 밑에 변수 네개
        text = searchTextBox;
        sex = sexComboBox;
        department = departmentComboBox;
        categoryCombo = searchRangeComboBox;

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
            items[i] = new JCheckBox(searchItems[i], true);
            searchItemPanel.add(items[i]);
        }


        searchItemPanel.add(searchBtn);
        searchItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchBtn.addActionListener(this::click);

        return searchItemPanel;
    }

    public JPanel getTablePanel(){
        JPanel tablePanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(this.table);
        table.getModel().addTableModelListener(new CheckboxModelListener(selectedEmpPanel));

//        table.addMouseListener(this);
        scrollPane.setSize(950, 300);
        tablePanel.add(scrollPane);
        return tablePanel;
    }
    public JPanel getSelectedEmpPanel(){
        JPanel selectedEmployeePanel = new JPanel();

        JLabel selectedEmpLabel = new JLabel("선택한 직원 :  ");

        // apply font
        Font font = new Font("SansSerif", Font.BOLD, 20);
        selectedEmpLabel.setFont(font);
        selectedEmpStrings.setFont(font);

        //
        selectedEmployeePanel.add(selectedEmpLabel);
        selectedEmployeePanel.add(selectedEmpStrings);

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
        updateBtn.addActionListener(this::click);

        return updateItemPanel;
    }

    public JPanel getDeleteItemPanel(){
        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteBtn.addActionListener(this::click);
        return deletePanel;
    }

    public JPanel getInsertItemPanel(){
        JPanel insertPanel = new JPanel();
        insertPanel.add(insertBtn);
        insertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertBtn.addActionListener(this::click);
        return insertPanel;
    }

    public JPanel setTopPanel(ArrayList<JPanel> panels){
        JPanel top = new JPanel();
        for (JPanel panel : panels) {
            top.add(panel);
        }
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); // 패널 세로정렬
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        return top;
    }

    public JPanel setLeftBottomPanel(ArrayList<JPanel> panels){
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
    /*@Override
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
    }*/


    SubFrame sf;

    /*
    * 버튼 이벤트리스너 관리
    * */
    public void click(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            if (!isSelected()) {
                JOptionPane.showMessageDialog(null, "하나 이상의 범위를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            } else {
                printSelectedItems();
            }

        } else if (e.getSource() == updateBtn) {
            //업데이트 버튼 누르면

        } else if (e.getSource() == deleteBtn) {
            // 삭제 버튼 누르면
            boolean[] boolArray = {false, false, false};
            boolArray[0] = text.isVisible();
            boolArray[1] = sex.isVisible();
            boolArray[2] = department.isVisible();

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

    private void printSelectedItems() {
        //검색 버튼 누르면 jdbc 연결 후 보고서 출력 후 연결 해제
        jdbc.connectJDBC();
        model = jdbc.printReport(model, items); // 모델이 반환됨
        //showTable(model);
        jdbc.disconnectJDBC();
    }

    private boolean isSelected() {
        boolean isSelected = false;
        for(JCheckBox item : items){
            if(item.isSelected()){
                if (item.equals("Name")) {
                    NAME_COLUMN = 1;
                }
                isSelected = true;
            }
        }
        return isSelected;
    }
    public class CheckboxModelListener implements TableModelListener {
        private JPanel selectedEmpPanel;

        public CheckboxModelListener(JPanel selectedEmpPanel) {
            this.selectedEmpPanel = selectedEmpPanel;
        }

        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0) {
                TableModel model = (TableModel) e.getSource();
                String columnName = model.getColumnName(1);
                Boolean checked = (Boolean) model.getValueAt(row, column);
                if (columnName == "NAME") {
                    String dShow;
                    if (checked) {
                        dShow = "";
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                dShow += (String) table.getValueAt(i, NAME_COLUMN) + "    ";

                            }
                        }
                        selectedEmpStrings.setText(dShow);
                    } else {
                        dShow = "";
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                dShow += (String) table.getValueAt(i, 1) + "    ";

                            }
                        }
                        selectedEmpStrings.setText(dShow);
                    }
                }
            }
        }
    }

}
