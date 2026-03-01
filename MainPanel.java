package swubook;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import swubook.Book;
import swubook.BookDAO;

public class MainPanel extends JPanel {
	
    public MainPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(225, 211, 192));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(225, 211, 192));

        // 왼쪽 메뉴바 기준 20px margin
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 5, 0));

        // 추천 도서, 인기 도서 패널 추가
        contentPanel.add(new BookSectionPanel("추천 도서", "recommend"));
        contentPanel.add(new BookSectionPanel("인기 도서", "popular"));

        add(contentPanel, BorderLayout.CENTER);
    }
}

class BookSectionPanel extends JPanel {

    public BookSectionPanel(String title, String type) {
        setLayout(new BorderLayout());
        setBackground(new Color(225, 211, 192));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Noto Sans Kr", Font.PLAIN, 32));
        titleLabel.setForeground(new Color(80, 47, 3));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(titleLabel, BorderLayout.NORTH);
        add(new BookPagePanel(type), BorderLayout.CENTER);
    }
}

class BookPagePanel extends JPanel {
	private JPanel createBookItem(Book book) {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBackground(new Color(225, 211, 192));

	    JButton btn = new JButton();
	    btn.setContentAreaFilled(false);
	    btn.setBorderPainted(false);
	    btn.setFocusPainted(false);

	    try {
	        ImageIcon rawIcon = new ImageIcon(new URL(book.getCoverUrl()));
	        ImageIcon resized = resizeIcon(rawIcon, 160, 240);
	        btn.setIcon(resized);
	    } catch (Exception e) {
	        btn.setText("이미지 없음");
	    }
	    
	    btn.addActionListener(e->{
	    	JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
	    	new BookDetailDialog(frame, book).setVisible(true);
	    });
	    
	    /*btn.addActionListener(e->{
        	try {
        		BookDAO.increaseViewCount(book.getId());
        	}catch(Exception ex) {
        		ex.printStackTrace();
        	}
        });*/

	    JLabel titleLabel = new JLabel(
	        "<html><center>" + book.getTitle() + "</center></html>"
	    );
	    titleLabel.setFont(new Font("Noto Sans Kr", Font.PLAIN, 13));
	    titleLabel.setForeground(new Color(80, 47, 3));
	    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

	    panel.add(btn, BorderLayout.CENTER);
	    panel.add(titleLabel, BorderLayout.SOUTH);

	    return panel;
	}
	
	private List<Book> books;
	private String type;
	
	public static ImageIcon resizeIcon(ImageIcon icon, int maxW, int maxH) {
		int w = icon.getIconWidth();
	    int h = icon.getIconHeight();

	    double ratio = Math.min((double) maxW / w, (double) maxH / h);
	    int newW = (int) (w * ratio);
	    int newH = (int) (h * ratio);

	    Image srcImg = icon.getImage();
	    BufferedImage resized = new BufferedImage(
	        newW, newH, BufferedImage.TYPE_INT_ARGB
	    );

	    Graphics2D g2 = resized.createGraphics();
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	                        RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                        RenderingHints.VALUE_ANTIALIAS_ON);

	    g2.drawImage(srcImg, 0, 0, newW, newH, null);
	    g2.dispose();

	    return new ImageIcon(resized);
    }
	
    private JPanel bookDisplayPanel;
    private int currentPage = 0;

    private JButton btnPrev;
    private JButton btnNext;
    
    public BookPagePanel(String type) {
    	this.type = type;
    	
    	try {
    		if(type.equals("recommend")) {
    			books=BookDAO.findRecommend(15);
    		} else if(type.equals("popular")) {
    			books=BookDAO.findPopular(15);
    		} else {
    			books=BookDAO.findAll();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    		books=List.of();
    	}
    	
    	
        setLayout(new BorderLayout());
        setBackground(new Color(225, 211, 192));

        // 왼쪽 화살표
        ImageIcon rawPrev = new ImageIcon(getClass().getResource("/image/btnPrev.png"));
        ImageIcon imagePrev = resizeIcon(rawPrev, 25, 300);
        btnPrev = new JButton(imagePrev);
        btnPrev.setContentAreaFilled(false);
	    btnPrev.setBorderPainted(false);
	    //btnPrev.setFocusPainted(false);
        

        // 오른쪽 화살표
        ImageIcon rawNext = new ImageIcon(getClass().getResource("/image/btnNext.png"));
        ImageIcon imageNext = resizeIcon(rawNext, 25, 300);
        btnNext = new JButton(imageNext);
        btnNext.setContentAreaFilled(false);
	    btnNext.setBorderPainted(false);
        
	    
	    //방향 버튼 추가
	    JPanel left = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	    left.setBackground(new Color(225, 211, 192));
	    left.add(btnPrev);
	    
	    JPanel right = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	    right.setBackground(new Color(225, 211, 192));
	    //right.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
	    right.add(btnNext);
	    
	    add(left, BorderLayout.WEST);
	    add(right, BorderLayout.EAST);

        // 실제 책 버튼 표시 영역
        bookDisplayPanel = new JPanel(null);
        bookDisplayPanel.setPreferredSize(new Dimension(1200, 500));
        bookDisplayPanel.setBackground(new Color(225, 211, 192));

        add(bookDisplayPanel, BorderLayout.CENTER);
        updatePage();

        add(bookDisplayPanel, BorderLayout.CENTER);

        // 이전 페이지 이동
        btnPrev.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updatePage();
            }
        });

        // 다음 페이지 이동
        btnNext.addActionListener(e -> {
            if (currentPage < 2) { 
                currentPage++;
                updatePage();
            }
        });
        
    }

    // 현재 페이지의 5개 버튼 표시
    private void updatePage() {
        bookDisplayPanel.removeAll();

        int start = currentPage * 5;
        int x;
        if (currentPage==0)
        	x=65;
        else 
        	x=10;
        
        for (int i = start; i < start + 5&&i<books.size(); i++) {
        	Book book = books.get(i);

            JPanel bookItem = createBookItem(book);
            bookItem.setBounds(x, 0, 160, 280); 
            bookDisplayPanel.add(bookItem);

            x += 220;
        }
        
        if (currentPage == 0) {
            btnPrev.setVisible(false);  
            btnNext.setVisible(true);
        } else if (currentPage == 2) {
            btnPrev.setVisible(true);
            btnNext.setVisible(false);
        } else {
            btnPrev.setVisible(true);
            btnNext.setVisible(true);
        }

        revalidate();
        repaint();
    }
}