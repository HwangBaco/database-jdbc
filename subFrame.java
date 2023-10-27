import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class SubFrame extends JFrame implements ActionListener {
    private final String[] labels = {"First Name","Middle Initial","Last Name","Ssn","Birthdate","Address", "Sex", "Salary","Super_ssn", "Dno"};
    private JTextField[] fields = new JTextField[10];
    private final String[] sex = {"Male", "Female"};
    public SubFrame() {
        super("직원 등록");
        setSize(500, 500);
        setLocation(350, 50);

        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("새로운 직원 정보 추가");
        JLabel empty = new JLabel(" ");
        Font font = new Font("SansSerif", Font.BOLD, 20);
        empty.setFont(font);
        title.setFont(font);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        titlePanel.add(empty);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JComboBox<String> sexCategory = new JComboBox<String>(sex);
        JPanel attributePanel = new JPanel(new GridLayout(11,2,5,10));
        for(int i=0;i<10;i++) {
            attributePanel.add(new JLabel(labels[i]));
            if(labels[i] == "Sex"){
                attributePanel.add(sexCategory);
            }else {
                fields[i] = new JTextField(25);
                attributePanel.add(fields[i]);
            }
        }
        attributePanel.add(new JLabel(" "));

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("정보 추가하기");
        addBtn.addActionListener(this);
        btnPanel.add(addBtn);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel totalPanel = new JPanel();
        totalPanel.add(titlePanel);
        totalPanel.add(attributePanel);
        totalPanel.add(addBtn);
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        add("North", totalPanel);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //디비에 값 넣기
        //pk에 널값이 들어오면 오류 띄우기
        dispose();
    }
}
