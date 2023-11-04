package src;

import src.JDBC.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import static src.LoginFrame.*;
import static src.Main.*;

public class MainPanel extends JPanel {

    SubFrame subFrame;


    // 컴포넌트 판넬
    JPanel searchRangePanel, searchItemPanel, selectedEmpPanel, headcountPanel, updatePanel, deletePanel, insertPanel;
    // 레이아웃 판넬
    JPanel searchPanel, contextPanel, midPanel, bottomPanel;
    // 액션 버튼
    JButton searchBtn, updateBtn, deleteBtn, insertBtn;


    // search filter
    private static final int CHECKBOX_NUM = 8;
    private final String[] searchRanges = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    private final String[] searchItems = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    private final String[] sexStrings = {"M", "F"};
    private final String[] departmentStrings = {"Research", "Administration", "Headquarters"};

    JCheckBox[] items;

    JTextField text;
    JComboBox<String> sex;
    JComboBox<String> department;
    JComboBox<String> category;

    Set<String> ssnList = new HashSet();


    // table
    JPanel tablePanel;
    //    JPanel tablePanel = new JPanel();
    JScrollPane scrollPane;
    DefaultTableModel model = new DefaultTableModel(0, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column > 0) {
                return false;
            } else {
                return true;
            }
        }
    };
    JTable table = new JTable(this.model) {
        @Override
        public Class getColumnClass(int column) {
            if (column == 0) {
                return Boolean.class;
            } else
                return String.class;
        }
    };

    public static Map<String, Integer> columnIdxMap = new HashMap<>();

    boolean actionAfterCommand = false;


    // 업데이트 해줘야 하는 필드는 공통으로 관리
    public JLabel headCountNumber = new JLabel();
    public JLabel selectedEmpStrings = new JLabel();


    // update시에 변수로 넘겨주기 위해 전역변수로 뺐습니다.
    JComboBox<String> updateItemComboBox;
    JTextField updateTextBox;
    JComboBox<String> sexComboBox;
    JComboBox<String> departmentComboBox;

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

        // 구역별 panels 위치
        searchPanel = setSearchPanel(searchRangePanel, searchItemPanel);
        midPanel = setMidPanel(selectedEmpPanel);
        bottomPanel = setBottomPanel(headcountPanel, updatePanel, deletePanel, insertPanel);
        contextPanel = setContextPanel(midPanel, bottomPanel);
    }

    public JPanel getSearchPanel() {
        return this.searchPanel;
    }

    public JPanel getContextPanel() {
        return this.contextPanel;
    }


    // 컴색 범위 텍스트 + 드랍박스 injection
    public JPanel getSearchRangePanel() {
        JPanel searchRangePanel = new JPanel();
        JComboBox<String> searchRangeComboBox = new JComboBox<>(searchRanges);
        searchRangePanel.add(new JLabel("검색범위  "));
        searchRangePanel.add(searchRangeComboBox);
        searchRangePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        text = new JTextField(20);
        sex = new JComboBox<>(this.sexStrings);
        department = new JComboBox<>(departmentStrings);
        category = searchRangeComboBox;

        searchRangePanel.add(text);
        searchRangePanel.add(sex);
        searchRangePanel.add(department);

        text.setVisible(false);
        sex.setVisible(false);
        department.setVisible(false);

        searchRangeComboBox.addActionListener(e -> {
            int selectedIndex = searchRangeComboBox.getSelectedIndex();
            if (selectedIndex == 0) { // 전체
                text.setVisible(false);
                sex.setVisible(false);
                department.setVisible(false);
            } else if (selectedIndex == 5) { //Sex
                text.setVisible(false);
                sex.setVisible(true);
                department.setVisible(false);
            } else if (selectedIndex == 8) { //Department
                text.setVisible(false);
                sex.setVisible(false);
                department.setVisible(true);
            } else {
                text.setText("");
                text.setVisible(true);
                sex.setVisible(false);
                department.setVisible(false);
            }
        });

        return searchRangePanel;
    }

    public JPanel getSearchItemPanel() {
        JPanel searchItemPanel = new JPanel();
        JLabel searchItemLabel = new JLabel("검색항목  ");

        searchItemPanel.add(searchItemLabel);

        for (int i = 0; i < CHECKBOX_NUM; i++) {
            items[i] = new JCheckBox(searchItems[i], true);
            searchItemPanel.add(items[i]);
            columnIdxMap.put(searchItems[i], -1);
        }


        searchItemPanel.add(searchBtn);
        searchItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchBtn.addActionListener(this::click);

        return searchItemPanel;
    }

    public JPanel getTablePanel() {
        tablePanel = new JPanel();
        scrollPane = new JScrollPane(this.table);
        table.getModel().addTableModelListener(new modelEventListener());
        scrollPane.setPreferredSize(new Dimension(1000, 300));
        tablePanel.add(scrollPane);
        super.add(tablePanel, BorderLayout.CENTER);
        super.revalidate();

        return tablePanel;
    }

    public JPanel getSelectedEmpPanel() {
        JPanel selectedEmployeePanel = new JPanel();
        JLabel selectedEmpLabel = new JLabel("선택한 직원 :  ");

        Font font = new Font("SansSerif", Font.BOLD, 11);
        selectedEmpLabel.setFont(font);
        selectedEmpStrings.setFont(font);

        selectedEmployeePanel.add(selectedEmpLabel);
        selectedEmployeePanel.add(selectedEmpStrings);
        selectedEmployeePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return selectedEmployeePanel;
    }

    public JPanel getHeadCounts() {
        JPanel headCountPanel = new JPanel();

        JLabel headCountLabel = new JLabel("인원수 :  ");

        headCountPanel.add(headCountLabel);
        headCountPanel.add(headCountNumber);

        headCountPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return headCountPanel;
    }

    public JPanel getUpdateItemPanel() {
        JPanel updateItemPanel = new JPanel();
        JLabel updateItemLabel = new JLabel("수정 ");

        updateItemComboBox = new JComboBox<>(searchItems);

        updateTextBox = new JTextField(20);
        sexComboBox = new JComboBox<>(this.sexStrings);
        departmentComboBox = new JComboBox<>(departmentStrings);

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
            } else if (selectedIndex == 7) { //Department
                updateTextBox.setVisible(false);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(true);
            } else {
                updateTextBox.setText("");
                updateTextBox.setVisible(true);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(false);
            }
        });

        updateItemPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        updateBtn.addActionListener(this::click);

        return updateItemPanel;
    }

    public JPanel getDeleteItemPanel() {
        JPanel deletePanel = new JPanel();
        deletePanel.add(deleteBtn);
        deletePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        deleteBtn.addActionListener(this::click);
        return deletePanel;
    }

    public JPanel getInsertItemPanel() {
        JPanel insertPanel = new JPanel();
        insertPanel.add(insertBtn);
        insertPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        insertBtn.addActionListener(this::click);
        return insertPanel;
    }

    public JPanel setSearchPanel(JPanel searchRangePanel, JPanel searchItemPanel) {
        JPanel top = new JPanel();
        top.add(searchRangePanel);
        top.add(searchItemPanel);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS)); // 패널 세로정렬
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        return top;
    }

    public JPanel setMidPanel(JPanel selectedEmpPanel) {
        JPanel midPanel = new JPanel();
        midPanel.add(selectedEmpPanel);
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.X_AXIS));
        return midPanel;
    }

    public JPanel setBottomPanel(JPanel headcountPanel, JPanel updatePanel, JPanel deletePanel, JPanel insertPanel) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(headcountPanel);
        bottomPanel.add(updatePanel);
        bottomPanel.add(deletePanel);
        bottomPanel.add(insertPanel);

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        return bottomPanel;
    }


    public JPanel setContextPanel(JPanel midPanel, JPanel bottomPanel) {
        JPanel contextPanel = new JPanel();

        contextPanel.add(midPanel);
        contextPanel.add(bottomPanel);

        contextPanel.setLayout(new BoxLayout(contextPanel, BoxLayout.Y_AXIS));
        contextPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        return contextPanel;
    }



    /*
     * 버튼 이벤트리스너 관리
     * */
    public void click(ActionEvent e) {

        if (actionAfterCommand) {
            super.remove(tablePanel);
            super.revalidate();
        }

        if (e.getSource().equals(searchBtn)) {

            if (hasSelectAttribute()) {
                jdbc.connectJDBC();
                getTableView();
                jdbc.disconnectJDBC();
            } else {
                JOptionPane.showMessageDialog(null, "하나 이상의 항목를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            }

        } else if (e.getSource().equals(updateBtn)) {
            System.out.println("updateItemComboBox = " + updateItemComboBox.getSelectedItem());
            System.out.println("updateTextBox.getText() = " + updateTextBox.getText());
            System.out.println("sexComboBox = " + sexComboBox.getSelectedItem());
            System.out.println("departmentComboBox = " + departmentComboBox.getSelectedItem());
            jdbc.connectJDBC();
//            try {
//                //jdbc.updateEmployeeDate(updateItemComboBox, updateTextBox, sexComboBox, departmentComboBox);
//            } catch (SQLException sqlException){
//                System.out.println("오류..");
//            }
            jdbc.disconnectJDBC();

        } else if (e.getSource().equals(deleteBtn)) {
            if (hasSsnAttribute()) {

                jdbc.connectJDBC();
                try {
                    jdbc.deleteEmployee(ssnList);
                    JOptionPane.showMessageDialog(this, "직원 정보 삭제 성공");

                    ssnList = new HashSet<>(); // reset ssnList
                    getTableView(); // refresh table
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(this, "직원 정보 삭제 실패");
                } finally {
                    jdbc.disconnectJDBC();
                }
            } else {
                JOptionPane.showMessageDialog(null, "삭제를 위해선 Ssn을 반드시 조회해야 합니다!", "경고", JOptionPane.WARNING_MESSAGE);
            }

        } else if (e.getSource().equals(insertBtn)) {
            // 삽입 버튼 누르면
            if (subFrame != null) {
                subFrame.dispose();
            }
            subFrame = new SubFrame();

        }

    }

    private void getTableView() {
        model = jdbc.printReport(model, items, category, text, sex, department); // 모델이 반환됨
        actionAfterCommand = true;
        table = new JTable(model) {
            @Override
            public Class getColumnClass(int column) {
                if (column == 0) {
                    return Boolean.class;
                } else
                    return String.class;
            }
        };
        selectedEmpStrings.setText(" ");
        int rowCount = model.getRowCount();
        headCountNumber.setText(String.valueOf(rowCount));
        getTablePanel();
    }

    private boolean isCheckedRow(int i) {
        return table.getValueAt(i, 0).equals(true);
    }

    private boolean hasSelectAttribute() {
        boolean isSelected = false;
        for (JCheckBox item : items) {
            if (item.isSelected()) {
                isSelected = true;
            }
        }
        return isSelected;
    }

    private boolean hasSsnAttribute() {
        return !ssnList.isEmpty();
    }

    private class modelEventListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0) {
                TableModel model = (TableModel) e.getSource();
                String firstColumn = model.getColumnName(1);
                String secondColumn = model.getColumnName(2);

                Boolean isChecked = (Boolean) model.getValueAt(row, 0);

                if (firstColumn.equals("Name")) {
                    StringBuilder empNames = new StringBuilder();
                    if (isChecked) {
                        if (secondColumn.equals("Ssn")) {
                            ssnList.add(model.getValueAt(row, 2).toString());
                        }
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (isCheckedRow(i)) {
                                empNames.append(table.getValueAt(i, 1)).append("    ");
                            }
                        }
                        selectedEmpStrings.setText(empNames.toString());
                    } else {
                        if (secondColumn.equals("Ssn")) {
                            ssnList.remove(model.getValueAt(row, 2).toString());
                        }
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (isCheckedRow(i)) {
                                empNames.append(table.getValueAt(i, 1)).append("    ");

                            }
                        }
                        selectedEmpStrings.setText(empNames.toString());
                    }
                }
                if (firstColumn.equals("Ssn")) {
                    if (isChecked) {
                        ssnList.add(model.getValueAt(row, 1).toString());
                    } else {
                        ssnList.remove(model.getValueAt(row, 1).toString());
                    }
                }
            }
        }
    }
}



