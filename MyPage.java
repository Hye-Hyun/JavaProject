package swubook;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyPage extends JPanel {

    private final Color BG = new Color(230, 220, 200);
    private final Color BROWN = new Color(90, 60, 20);
    private static final int FORM_WIDTH = 420;

    public MyPage() {
        setBackground(BG);
        
        setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setBackground(BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(60, 0, 60, 0));

        center.setPreferredSize(new Dimension(FORM_WIDTH, 800));
        center.setMaximumSize(new Dimension(FORM_WIDTH, Integer.MAX_VALUE));

        center.add(title());
        center.add(Box.createVerticalStrut(40));
        center.add(profileImage());
        center.add(Box.createVerticalStrut(20));
        center.add(nickname());
        center.add(Box.createVerticalStrut(40));
        center.add(inputField("아이디"));
        center.add(Box.createVerticalStrut(25));
        center.add(inputField("이메일"));
        center.add(Box.createVerticalStrut(40));
        center.add(editButton());
        center.add(Box.createVerticalStrut(20));
        center.add(logoutText());

        add(center, BorderLayout.CENTER);
    }

    private JLabel title() {
        JLabel title = new JLabel("북슬북슬");
        title.setFont(new Font("Noto Sans KR", Font.BOLD, 36));
        title.setForeground(BROWN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JPanel profileImage() {
        JPanel img = new JPanel();
        img.setPreferredSize(new Dimension(120, 120));
        img.setMaximumSize(new Dimension(120, 120));
        img.setBackground(Color.WHITE);
        img.setBorder(BorderFactory.createLineBorder(BROWN, 2));
        img.setAlignmentX(Component.CENTER_ALIGNMENT);
        return img;
    }

    private JLabel nickname() {
        JLabel name = new JLabel("사용자 닉네임");
        name.setFont(new Font("Noto Sans KR", Font.BOLD, 18));
        name.setForeground(BROWN);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        return name;
    }

    private JPanel inputField(String labelText) {
    	JPanel wrapper = new JPanel();
        wrapper.setBackground(BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setMaximumSize(new Dimension(FORM_WIDTH, 90));

        wrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Noto Sans KR", Font.PLAIN, 14));
        label.setForeground(BROWN);

        label.setPreferredSize(new Dimension(FORM_WIDTH, 20));
        label.setMaximumSize(new Dimension(FORM_WIDTH, 20));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("Noto Sans KR", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(BROWN, 2));

        field.setPreferredSize(new Dimension(FORM_WIDTH, 45));
        field.setMaximumSize(new Dimension(FORM_WIDTH, 45));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(8));
        wrapper.add(field);

        return wrapper;
    }

    private JButton editButton() {
    	RoundedButton btn = new RoundedButton("회원정보 수정");
        btn.setFont(new Font("Noto Sans KR", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BROWN);
        btn.setPreferredSize(new Dimension(FORM_WIDTH, 55));
        btn.setMaximumSize(new Dimension(FORM_WIDTH, 55));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JLabel logoutText() {
        JLabel logout = new JLabel("로그아웃");
        logout.setFont(new Font("Noto Sans KR", Font.PLAIN, 13));
        logout.setForeground(BROWN);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        return logout;
    }
    static class RoundedButton extends JButton {

    	public RoundedButton(String text) {
    		super(text);
    		setContentAreaFilled(false);
    		setFocusPainted(false);
    		setBorderPainted(false);
    		setOpaque(false);
    	}

    	@Override
    	protected void paintComponent(Graphics g) {
    		Graphics2D g2 = (Graphics2D) g.create();
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

    		g2.setColor(getBackground());
    		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

    		super.paintComponent(g2);
    		g2.dispose();
    	}
    }
}


