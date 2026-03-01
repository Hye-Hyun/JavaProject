package swubook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class SearchPanel extends JPanel {

    private JTextField search = new JTextField();
    private JButton result = new JButton("검색");

    private static final String PLACEHOLDER = "책 제목, 저자로 검색";
    private JPanel resultPanel = new JPanel();

    public SearchPanel() {

        setLayout(new BorderLayout());
        setBackground(new Color(225, 211, 192));

        // 정렬용 wrapper
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(new Color(225, 211, 192));
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // 검색창, 검색 버튼
        JPanel searchBox = new JPanel(new BorderLayout(10, 0));
        searchBox.setBackground(new Color(225, 211, 192));

        searchBox.setPreferredSize(new Dimension(500, 40));

        // 검색 textfield
        search.setText(PLACEHOLDER);
        search.setForeground(Color.GRAY);
        search.setFont(new Font("Noto Sans KR", Font.PLAIN, 14));

        search.setPreferredSize(new Dimension(0, 40));
        search.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals(PLACEHOLDER)) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText(PLACEHOLDER);
                    search.setForeground(Color.GRAY);
                }
            }
        });
        
        result.setBackground(new Color(80, 47, 3));
        result.setForeground(new Color(253, 246, 236));
        result.setFont(new Font("Noto Sans KR", Font.BOLD, 14));

        result.setBorder(null);
        result.setFocusPainted(false);
        result.setOpaque(true);
        result.setCursor(new Cursor(Cursor.HAND_CURSOR));

        result.setPreferredSize(new Dimension(60, 40));
        
        result.addActionListener(e->doSearch());
        search.addActionListener(e -> doSearch());

        searchBox.add(search, BorderLayout.CENTER);
        searchBox.add(result, BorderLayout.EAST);

        wrapper.add(searchBox);

        resultPanel.setBackground(new Color(225, 211, 192));
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));

        add(wrapper, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
    }
    
    private void doSearch() {

        String keyword = search.getText().trim();

        // placeholder거나 비어 있으면 검색 안 함
        if (keyword.isEmpty() || keyword.equals(PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "검색어를 입력하세요");
            return;
        }
        resultPanel.removeAll();

        // DB 검색
        BookDAO dao = new BookDAO();
        java.util.List<Book> list = dao.search(keyword);

        if (list.isEmpty()) {
        	resultPanel.setLayout(new GridBagLayout());
            JLabel empty = new JLabel("일치하는 검색 결과가 없습니다.");
            empty.setFont(new Font("Noto Sans KR", Font.PLAIN, 24));
            empty.setForeground(new Color(253, 246, 236));
            resultPanel.add(empty, new GridBagConstraints());
        } 
        else {
        	resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 25));
            for (Book book : list) {
                resultPanel.add(new BookCard(book));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

}

class BookCard extends JPanel {

    public BookCard(Book book) {
        setPreferredSize(new Dimension(120, 230));
        setBackground(new Color(225, 211, 192));
        setLayout(new BorderLayout());
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //표지 이미지
        JLabel coverLabel;

        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(book.getCoverUrl()));
            Image img = icon.getImage().getScaledInstance(120, 170, Image.SCALE_SMOOTH);
            coverLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            coverLabel = new JLabel("이미지 없음", SwingConstants.CENTER);
            coverLabel.setPreferredSize(new Dimension(120, 170));
            coverLabel.setOpaque(true);
            coverLabel.setBackground(Color.LIGHT_GRAY);
        }

        //제목, 작가 영역
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(225, 211, 192));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //제목
        JLabel titleLabel = new JLabel(
            "<html><div style='text-align:center;'>" + book.getTitle() + "</div></html>",
            SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Noto Sans KR", Font.BOLD, 14));
        titleLabel.setForeground(new Color(80, 47, 3));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //작가
        String cleanAuthor = book.getAuthor()
        	    .replaceAll("\\s*\\(.*?\\)", ""); // (지은이), (옮긴이) 제거

        	JLabel authorLabel = new JLabel(cleanAuthor, SwingConstants.CENTER);

        authorLabel.setFont(new Font("Noto Sans KR", Font.PLAIN, 10));
        authorLabel.setForeground(new Color(80, 47, 3));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(authorLabel);

        add(coverLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                JFrame parentFrame =
                    (JFrame) SwingUtilities.getWindowAncestor(BookCard.this);

                BookDetailDialog dialog =
                    new BookDetailDialog(parentFrame, book);

                dialog.setVisible(true);
            }
        });
    }
}
