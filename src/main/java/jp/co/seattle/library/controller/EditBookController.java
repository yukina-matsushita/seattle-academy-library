/* package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.service.BooksService;  */

/**
 * 編集コントローラー
 */

/* @Controller //APIの入り口
public class EditBookController {
   final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);
   @Autowired
   private BooksService booksService; */

    /**
     * 対象書籍を編集する
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
/* @Transactional //編集ボタン押すとここに来る
@RequestMapping(value = "/editBook", method = RequestMethod.POST)
public String editBook(
        Locale locale,
        @RequestParam("bookId") Integer bookId,
        Model model) {
    logger.info("Welcome edit! The client locale is {}.", locale);

    booksService.editBook(bookId);
    model.addAttribute("bookInfo", booksService.getBookList());
    return "detail";  

}

} */

