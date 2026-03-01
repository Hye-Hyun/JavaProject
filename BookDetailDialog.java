package swubook;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BookDetailDialog extends JDialog{

	public BookDetailDialog(JFrame parent, Book book) {
		super(parent, "도서 상세 정보", true);
		setSize(400, 400);
		setLocationRelativeTo(parent);
		setLayout(new BorderLayout());
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		content.setBackground(new Color(225, 211, 192));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBackground(new Color(225, 211, 192));

		try {
		    ImageIcon icon = new ImageIcon(new URL(book.getCoverUrl()));
		    JLabel cover = new JLabel(resize(icon, 120, 180));
		    cover.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
		    topPanel.add(cover);
		} catch (Exception e) {
		    topPanel.add(new JLabel("이미지 없음"));
		}

		// 오른쪽 텍스트 패널
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBackground(new Color(225, 211, 192));

		infoPanel.add(makeLabel("제목", book.getTitle()));
		infoPanel.add(makeLabel("저자", book.getAuthor()));
		infoPanel.add(makeLabel("출판사", book.getPublisher()));
		infoPanel.add(makeLabel("카테고리", book.getCategory()));

		topPanel.add(infoPanel);

		// content에 추가
		
		JButton writeBtn = new JButton("이 책 기록하기");
		
		writeBtn.setBackground(new Color(80, 47, 3));
		writeBtn.setForeground(new Color(253, 246, 236));
		writeBtn.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

		writeBtn.setBorder(null);
		writeBtn.setFocusPainted(false);
		writeBtn.setOpaque(true);
		writeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		writeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		writeBtn.setPreferredSize(new Dimension(100, 30));
		writeBtn.setMaximumSize(new Dimension(100, 30));  // ⭐ 이게 핵심
		writeBtn.setMinimumSize(new Dimension(100, 30));

		writeBtn.addActionListener(e -> {
		    if (parent instanceof Main) {
		        Main main = (Main) parent;
		        main.showWritePanel(); // 내 서재 패널로 이동
		        dispose();             // 다이얼로그 닫기
		    }
		});
		
		content.add(Box.createVerticalGlue());
		content.add(topPanel);
		content.add(Box.createVerticalGlue());
		content.add(writeBtn);
		content.add(Box.createVerticalGlue());
		
		setContentPane(content);
	}
	
	private JLabel makeLabel(String title, String value) {
		JLabel label = new JLabel(
				"<html><b>"+title+"  |  </b> "+value+"</html>");
		label.setFont(new Font("Noto Sans Kr", Font.PLAIN, 12));		
		label.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		return label;
	}
	
	private ImageIcon resize(ImageIcon icon, int w, int h) {
		Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}
}
