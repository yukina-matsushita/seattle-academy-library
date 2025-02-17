package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        //書籍ID、書籍名,著者名、出版社名、出版日、サムネイル画像
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id,title,author,publisher,publish_date,thumbnail_url from books order by title",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    public int getBookId() {
        //最新のIDを取得   
        String sql = "SELECT MAX(id) FROM books";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books "
                + "(title, author,publisher,publish_date,thumbnail_name,thumbnail_url,reg_date,upd_date,isbn,description) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "',"
                + "sysdate(),"
                + "sysdate(),"
                + "'" + bookInfo.getIsbn() + "',"
                + "'" + bookInfo.getDescription() + "')";

        jdbcTemplate.update(sql);
    }

    /**
     * 削除
     * @param bookId
     */
    //書籍を削除 
    public void deleteBook(int bookId) {
        //SQL文
        String sql = "DELETE FROM books WHERE id = " + bookId;
        //SQL文を実行
        jdbcTemplate.update(sql);

    }

    /**
     * 書籍情報を更新
    * @param bookInfo 書籍情報
    */
    public void editBook(BookDetailsInfo bookInfo) {
        //"は文字列を認識させたい時に使う、開始と終了で
        //＋は認識させたい変数の前と後ろ　文字列の中で認識させたい時に使う
        //’はSQLで必要なもの　”はJavaで必要なもの 
        String sql = "UPDATE books SET title = '" + bookInfo.getTitle() + "', author ='" + bookInfo.getAuthor() + "', "
                + "publisher = '" + bookInfo.getPublisher() + "' ,"
                + "publish_date = '" + bookInfo.getPublishDate() + "' ,"
                + "thumbnail_name = '" + bookInfo.getThumbnailName() + "' ,"
                + "thumbnail_url = '" + bookInfo.getThumbnailUrl() + "' ,"
                + "isbn ='" + bookInfo.getIsbn() + "' ,"
                + "description = '" + bookInfo.getDescription() + "',"
                + "upd_date = sysdate() "
                + "WHERE ID = " + bookInfo.getBookId() + ";";
        //SQL文実行
        jdbcTemplate.update(sql);
    }

    /**
     * 
     * @param bookId
     * @return
     */
    //ボタンの活性、非活性を決めるメソッド
    //レコードの数を数えて０か１かを返す
    public int countRecord(int bookId) {
        //レコードがあるかないかを調べるsql文
        String sql = "SELECT COUNT(*) FROM rental WHERE bookId=" + bookId + ";";
        //カウントの結果を返す
        return jdbcTemplate.queryForObject(sql, Integer.class);

    }

    //タイトル部分一致検索  
    /**
     * @param searchWord
     * @return 検索結果
     */
    //ユーザーが入力した文字列は変数"searchWord"の中に入る
    public List<BookInfo> getSearchBookList(String searchWord) {

        //入力された文字列が含まれるタイトルの本の情報を取得し、リストに格納
        List<BookInfo> searchBookList = jdbcTemplate.query(
                "SELECT * FROM books WHERE TITLE LIKE '%" + searchWord + "%'",
                new BookInfoRowMapper());

        return searchBookList;
    }

}
