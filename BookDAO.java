package swubook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    //책 저장 (알라딘 API용)
    public static void insert(Book book) throws Exception {

        Connection conn = DBUtil.getConnection();

        String sql =
            "INSERT INTO book (title, author, publisher, category, cover_url, view_count) " +
            "VALUES (?, ?, ?, ?, ?, 0)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, book.getTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getPublisher());
        ps.setString(4, book.getCategory());
        ps.setString(5, book.getCoverUrl());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    //검색
    public List<Book> search(String keyword) {

        List<Book> list = new ArrayList<>();

        String sql =
        	    "SELECT id, title, author, publisher, category, cover_url " +
        	    "FROM book " +
        	    "WHERE title LIKE ? COLLATE utf8mb4_general_ci " +
        	    "OR author LIKE ? COLLATE utf8mb4_general_ci " +
        	    "OR publisher LIKE ? COLLATE utf8mb4_general_ci";

        try (
            Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getString("category"),
                    rs.getString("cover_url")
                ));
            }

            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //전체 조회
    public static List<Book> findAll() throws Exception {

        List<Book> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();

        String sql =
            "SELECT id, title, author, publisher, category, cover_url " +
            "FROM book";

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getString("category"),
                rs.getString("cover_url")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }

    //인기도서, 조회수 기준
    public static List<Book> findPopular(int limit) throws Exception {

    	List<Book> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();

        String sql =
            "SELECT id, title, author, publisher, category, cover_url " +
            "FROM book " +
            "WHERE popular = 'Y' " +
            "ORDER BY RAND() " +
            "LIMIT ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getString("category"),
                rs.getString("cover_url")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }

    //조회수 증가
    public static void increaseViewCount(int bookId) throws Exception {

        Connection conn = DBUtil.getConnection();

        String sql =
            "UPDATE book " +
            "SET view_count = view_count + 1 " +
            "WHERE id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, bookId);

        ps.executeUpdate();

        ps.close();
        conn.close();
    }
    
    //추천도서
    public static List<Book> findRecommend(int limit) throws Exception {

    	List<Book> list = new ArrayList<>();
        Connection conn = DBUtil.getConnection();

        String sql =
            "SELECT id, title, author, publisher, category, cover_url " +
            "FROM book " +
            "WHERE recommend = 'Y' " +
            "ORDER BY RAND() " +
            "LIMIT ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getString("category"),
                rs.getString("cover_url")
            ));
        }

        rs.close();
        ps.close();
        conn.close();

        return list;
    }

    
    
    //테스트용 메인 메서드
//    public static void main(String[] args) {
//
//        BookDAO dao = new BookDAO();
//        String keyword = "해리";
//
//        System.out.println("검색어: " + keyword);
//        List<Book> list = dao.search(keyword);
//        System.out.println("검색 결과 수: " + list.size());
//
//        for (Book b : list) {
//            System.out.println(b.getTitle());
//        }
//    }
}
