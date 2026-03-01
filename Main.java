package swubook;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.*;

public class Main extends JFrame{
	
	private JLabel menuBar;
	private JButton logo = new JButton("북슬북슬");
	private String[] menuItems = {"홈", "찾아보기", "내 서재", "마이페이지"};
	private JButton[] menuBtn = new JButton[menuItems.length];
	
	//fn panel
	private JPanel fnPanel;
	private JPanel home;
	private JPanel search;
	private JPanel write;
	private JPanel myPage;
	private CardLayout cardlayout = new CardLayout();
	
	//로그인 상태
	private boolean isLoggedIn = false;
	
	//프로필 ui
	private JLabel profileImgLabel;
	private JLabel nicknameLabel;
	
	//button style
	private void styleMenuButton(JButton button) {
	    button.setAlignmentX(Component.LEFT_ALIGNMENT);
	    button.setForeground(new Color(253, 246, 236));
	    button.setContentAreaFilled(false);
	    button.setBorderPainted(false);
	    button.setFocusPainted(false);
	}

	public Main() {
		setTitle("북슬북슬");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(new Color(225, 211, 192));
		
		//menu panel
		ImageIcon menuPanel = new ImageIcon(getClass().getResource("/image/menuBar.png"));
		menuBar = new JLabel(menuPanel);
		
		menuBar.setBackground(new Color(253, 246, 236));
		menuBar.setSize(247, 1024);
		menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.Y_AXIS));
		menuBar.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		
		int menuwidth = 226;
		menuBar.setPreferredSize(new Dimension(menuwidth, 0));
		
		//menu item
		try {
			Font notoLight = Font.createFont(Font.TRUETYPE_FONT, 
					new java.io.File("C:\\WINDOWS\\FONTS\\NOTOSANSKR-VF.TTF"))
					.deriveFont(34f);
			
			logo.setFont(notoLight);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		styleMenuButton(logo);
		logo.addActionListener(e->{
			cardlayout.show(fnPanel, "home");
		});
		menuBar.add(logo);
		
		menuBar.add(Box.createRigidArea(new Dimension(0, 15)));
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/image/menuLine.png"));
		JLabel label = new JLabel(icon);
		menuBar.add(label);
		
		menuBar.add(Box.createRigidArea(new Dimension(0, 15)));
		
		for(int i=0; i<menuItems.length; i++) {
			menuBtn[i] = new JButton(menuItems[i]);
			styleMenuButton(menuBtn[i]);
			menuBtn[i].setFont(logo.getFont().deriveFont(Font.BOLD, 24));
			menuBtn[i].addActionListener(new MyActionListener());
			menuBar.add(menuBtn[i]);
			menuBar.add(Box.createRigidArea(new Dimension(0, 10)));
		}
	
		// 공간 채우기용
		menuBar.add(Box.createVerticalGlue());

		// ===== 프로필 영역 =====
		JPanel profilePanel = new JPanel();
		profilePanel.setOpaque(false);
		profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.X_AXIS));
		profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
		profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		// 기본 프로필 이미지
		ImageIcon defaultProfile = new ImageIcon(
		        getClass().getResource("/image/defaultProfile.png")
		);
		Image scaledImg = defaultProfile.getImage()
		        .getScaledInstance(40, 40, Image.SCALE_SMOOTH);

		profileImgLabel = new JLabel(new ImageIcon(scaledImg));
		profileImgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// 닉네임 / 로그인 텍스트
		nicknameLabel = new JLabel("로그인 하기");
		nicknameLabel.setForeground(new Color(253, 246, 236));
		nicknameLabel.setFont(logo.getFont().deriveFont(Font.PLAIN, 14));
		nicknameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// 클릭 이벤트
		MouseAdapter loginClickListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        goToLoginPage();
		    }
		};

		profileImgLabel.addMouseListener(loginClickListener);
		nicknameLabel.addMouseListener(loginClickListener);

		// 가로 배치
		profilePanel.add(profileImgLabel);
		profilePanel.add(Box.createRigidArea(new Dimension(10, 0))); // 이미지-텍스트 간격
		profilePanel.add(nicknameLabel);

		menuBar.add(profilePanel);

		// 모든 구성 끝난 후 메뉴바 추가
		add(menuBar, BorderLayout.WEST);
		
		//fn panel
		fnPanel = new JPanel(cardlayout);
		home = new MainPanel();
		search = new SearchPanel();
		//write = new WritePanel();
		myPage = new MyPage();
		
		fnPanel.add(home, "home");
		fnPanel.add(search, "search");
		//fnPanel.add(write, "write");
		fnPanel.add(myPage, "myPage");
		
		add(fnPanel,BorderLayout.CENTER);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	
	class MyActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd) {
				
			case "홈":
				cardlayout.show(fnPanel, "home");
				break;
			
			case "찾아보기":
				cardlayout.show(fnPanel, "search");
				break;
			/*
			case "내 서재":
				cardlayout.show(fnPanel, "write");
				break;
			*/
				
			case "마이페이지":
				cardlayout.show(fnPanel, "myPage");
				break;
				
			}	
		}
	}
	public void showWritePanel() {
	    cardlayout.show(fnPanel, "write");
	}
	
	private void goToLoginPage() {
		cardlayout.show(fnPanel, "myPage");
    }
	
	public static void main(String[] args) {
		new Main();
	}

}