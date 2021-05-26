package jp.co.seattle.library.controller;

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

import jp.co.seattle.library.service.BooksService;

/**
 * 削除コントローラー
 */
@Controller //APIの入り口
public class DeleteBookController {
    final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);
    @Autowired
    private BooksService booksService;

    /**
     * 対象書籍を削除する
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */

    @Transactional //削除ボタン押すとここに来る
    @RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
    public String deleteBook(
            Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        //レコードがあるかないかを調べるメソッド（rentBook）の実行結果を入れる変数をcountとして定義
        //レコードの数(0か1)をcountに入れる
        int count = booksService.countRecord(bookId);
        //countが0の時は借りれる状態→削除できる
        if (count == 0) {
            booksService.deleteBook(bookId);
            model.addAttribute("returnDisabled", "disabled");
            model.addAttribute("lendingStatus", "貸し出し可");
            model.addAttribute("bookList", booksService.getBookList());
            return "home";

            //借りれない状態→削除できない
        } else {
            model.addAttribute("deleteDisabled", "disabled");
            model.addAttribute("lendingStatus", "貸し出し不可");
            model.addAttribute("editError", "貸出中のため編集・削除できません");
            model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
            //詳細画面に戻る
            return "details";

        }

    }

}
