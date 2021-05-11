package jp.co.seattle.library.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class AddBooksController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired //クラス名 //インスタンス名
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/addBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String login(Model model) {
        return "addBook";
    }

    /**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file1 サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/insertBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String insertBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("isbn") String isbn,
            @RequestParam("description") String description,


            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setDescription(description);

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "addBook";
            }
        }



        //登録日付、ISBN　バリデーションチェック
        
        boolean isValidIsbn = isbn.matches("[0-9]{10}|[0-9]{13}|[0-9]{0}");
        boolean flag = false;
        if(!(isValidIsbn)) {
            model.addAttribute("errorIsbn", "ISBNの桁数が10か13ではありません。もしくは半角英数字ではありません");
            flag = true;
        }
        
        //出版日は半角数字のYYYYMMDD形式で入力してください
        // 日付の書式を指定する
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        df.setLenient(false); // 日付解析を厳密に行う設定にする
     
        try {
            df.parse(publishDate);  // 日付妥当性OK
        } catch (ParseException e) {
            // 日付妥当性NG時の処理を記述
            model.addAttribute("publishDateError", "出版日は半角数字のYYYYMMDD形式で入力してください");  
            flag = true;
        }
 
        
        if (flag) {
            return "addBook";
        }

        // 書籍情報を新規登録する   //booksServiceというインスタンスの　registBookというメソッド　bookInfoは引数
        booksService.registBook(bookInfo);

        //本の情報が渡されたら、メソッドが実行される

        // TODO 登録した書籍の詳細情報を表示するように実装   //BooksServiceの中にあるgetBookIdメソッドを呼び出し
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(booksService.getBookId()));

        //  詳細画面に遷移する
        return "details";
    }
}
