package src;

import src.JDBC.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import static src.Main.*;

public class MainPanel extends JFrame {
    MainFrame frame;

    // 컴포넌트 판넬
    JPanel searchRangePanel, searchItemPanel, selectedEmpPanel, headcountPanel, updatePanel, deletePanel, insertPanel;
    // 레이아웃 판넬
    JPanel searchPanel, contextPanel, midPanel, bottomPanel;
    // 액션 버튼
    JButton searchBtn, updateBtn, deleteBtn, insertBtn;


    // search filter
    private static final int CHECKBOX_NUM = 8;
    final String[] searchRanges = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    final String[] searchItems = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    final String[] sexStrings = {"M", "F"};
    final String[] departmentStrings = {"Research", "Administration", "Headquarters"};

    JCheckBox[] items;

    JTextField text;
    JComboBox<String> sex;
    JComboBox<String> department;
    JComboBox<String> category;


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




    JDBC jdbc = new JDBC(ID, PW, DB_NAME);

    MainPanel(MainFrame frame) {
        this.frame = frame;
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
        table.getModel().addTableModelListener(new CheckboxModelListener());
        scrollPane.setPreferredSize(new Dimension(1000, 300));
        tablePanel.add(scrollPane);
        frame.add(tablePanel, BorderLayout.CENTER);
        frame.revalidate();

        return tablePanel;
    }

    public JPanel getSelectedEmpPanel() {
        JPanel selectedEmployeePanel = new JPanel();
        JLabel selectedEmpLabel = new JLabel("선택한 직원 :  ");

        Font font = new Font("SansSerif", Font.BOLD, 20);
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
            } else if (selectedIndex == 7) { //Department
                updateTextBox.setVisible(false);
                sexComboBox.setVisible(false);
                departmentComboBox.setVisible(true);
            } else {
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


    SubFrame sf;

    /*
     * 버튼 이벤트리스너 관리
     * */
    public void click(ActionEvent e) {
        jdbc.connectJDBC();

        if (actionAfterCommand) {
            frame.remove(tablePanel);
            frame.revalidate();
        }

        if (e.getSource().equals(searchBtn)) {
            if (!hasSelectAttribute()) {
                JOptionPane.showMessageDialog(null, "하나 이상의 항목를 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            } else {
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

        } else if (e.getSource() == updateBtn) {
            //업데이트 버튼 누르면

        } else if (e.getSource() == deleteBtn) {
            // 삭제 버튼 누르면
            boolean[] boolArray = {false, false, false};
            boolArray[0] = text.isVisible();
            boolArray[1] = sex.isVisible();
            boolArray[2] = department.isVisible();

            if (jdbc.deleteEmployee(text, sex, department, boolArray, category))
                JOptionPane.showMessageDialog(this, "직원 정보 삭제 성공");
            else JOptionPane.showMessageDialog(this, "직원 정보 삭제 실패");
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
        model = jdbc.printReport(model, items, category, text, sex, department); // 모델이 반환됨
        //showTable(model);
        jdbc.disconnectJDBC();
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

    public class CheckboxModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0) {
                TableModel model = (TableModel) e.getSource();
                String columnName = model.getColumnName(1);
                Boolean checked = (Boolean) model.getValueAt(row, column);
                if (columnName.equals("Name")) {
                    StringBuilder empNames = new StringBuilder();
                    if (checked) {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                empNames.append((String) table.getValueAt(i, 1)).append("    ");
                            }
                        }
                        selectedEmpStrings.setText(empNames.toString());
                    } else {
                        for (int i = 0; i < table.getRowCount(); i++) {
                            if (table.getValueAt(i, 0) == Boolean.TRUE) {
                                empNames.append((String) table.getValueAt(i, 1)).append("    ");

                            }
                        }
                        selectedEmpStrings.setText(empNames.toString());
                    }
                }
            }
        }
    }
}



