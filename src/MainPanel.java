package src;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    JPanel categoryPanel, itemPanel, employeePanel,headcountPanel, updatePanel, deletePanel, insertPanel;
    JPanel topPanel, btmPanel, bottomPanel;

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
    }

    public JPanel getTopPanel(){
        return this.topPanel;
    }
    public JPanel getBottomPanel(){
        return this.bottomPanel;
    }

    class MyItemListener implements ItemListener {
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

        searchBtn.addActionListener(this::actionPerformed);
        return itemPanel;
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

