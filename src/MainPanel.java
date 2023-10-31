package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JFrame {
    final String[] SEARCH_RANGES = {"전체", "이름", "Ssn", "생년월일", "주소", "성별", "연봉", "상사", "부서"};
    final String[] SEARCH_ITEMS = {"Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Department"};
    final String[] SEX = {"M", "F"};

    JButton searchBtn, updateBtn, deleteBtn, insertBtn;
    String[] departmentStrings = {"Research", "Administration", "Headquarters"};
    JPanel categoryPanel, itemPanel, employeePanel,headcountPanel, updatePanel, deletePanel, insertPanel;
    JPanel topPanel, btmPanel, bottomPanel;

    MainPanel() {
        searchBtn = new JButton("검색");
        updateBtn = new JButton("update");
        deleteBtn = new JButton("선택한 데이터 삭제");
        insertBtn = new JButton("직원 등록");

        // 메인 패널에 추가될 sub Panels
        categoryPanel = getSearchRangePanel();
        itemPanel = getSearchItemPanel();
        employeePanel = getSelectedEmpPanel();
        headcountPanel = getHeadCounts();
        updatePanel = getUpdateItemPanel();
        deletePanel = getDeleteItemPanel();
        insertPanel = getInsertItemPanel();

        // 구역별 sub Panels 위치
        topPanel = setTop(categoryPanel, itemPanel);
        btmPanel = halfBottom(headcountPanel, updatePanel, deletePanel, insertPanel);
        bottomPanel = setBottom(btmPanel, employeePanel);
    }

    public JPanel getTopPanel(){
        return this.topPanel;
    }
    public JPanel getBottomPanel(){
        return this.bottomPanel;
    }


    // 컴색 범위 텍스트 + 드랍박스 injection
    public JPanel getSearchRangePanel(){
        JPanel searchRangePanel = new JPanel();
        JComboBox<String> searchRangeComboBox = new JComboBox<>(SEARCH_RANGES);
        searchRangePanel.add(new JLabel("검색범위  "));
        searchRangePanel.add(searchRangeComboBox);
        searchRangePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JTextField searchTextBox = new JTextField(20);
        JComboBox<String> sexComboBox = new JComboBox<>(this.SEX);
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

        for (String searchItem : SEARCH_ITEMS) {
            searchItemPanel.add(new Checkbox(searchItem));
        }

        searchItemPanel.add(searchBtn);

        searchItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchBtn.addActionListener(this::actionPerformed);

        return searchItemPanel;
    }

    /* ----- 231031 16:39 검토 완료 ----- */

    /*
    * 입력시 이벤트 관리 필요
    * */
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

    /*
     * 입력시 이벤트 관리 필요
     * */
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

        JComboBox<String> updateItemComboBox = new JComboBox<>(SEARCH_ITEMS);

        JTextField updateTextBox = new JTextField(20);
        JComboBox<String> sexComboBox = new JComboBox<>(this.SEX);
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
}

