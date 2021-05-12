package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class RentalService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍を借りる
     * @param bookId
     */
    public void countRecord(int bookId) {
        //bookIdをrentalテーブルに追加するSQL文    
        String sql = "INSERT INTO rental (bookId) VALUE " + "(" + bookId + ")";

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を返す
     * @param bookId
     */
    public void returnBook(int bookId) {
        //bookIdをrentalテーブルから削除するSQL文    
        String sql = "DELETE FROM rental WHERE bookId = " + bookId + ";";

        jdbcTemplate.update(sql);
    }
}
