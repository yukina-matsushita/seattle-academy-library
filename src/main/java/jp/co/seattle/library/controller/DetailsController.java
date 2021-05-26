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
 * 詳細表示コントローラー
 */
@Controller
public class DetailsController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService booksService;

    /**
     * 詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

        //ボタンの活性、非活性を決めるメソッド(rentBook)の実行結果を入れる変数をcountとして定義
        //レコードの数(0か1)をcountに入れる
        int count = booksService.countRecord(bookId);
        //カウントが0の時は借りれる状態
        if (count == 0) {
            model.addAttribute("returnDisabled", "disabled");
            model.addAttribute("lendingStatus", "貸し出し可");

            //借りれない状態   
        } else {
            model.addAttribute("disabled", "disabled");
            model.addAttribute("lendingStatus", "貸し出し不可");
            model.addAttribute("editError", "貸出中のため編集・削除できません");
        }

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        return "details";

    }

}
