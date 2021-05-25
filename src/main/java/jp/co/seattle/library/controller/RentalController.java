package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalService;

@Controller //APIの入り口
public class RentalController {
        final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

@Autowired //クラス名 //インスタンス名
private RentalService rentalService;

@Autowired //クラス名 //インスタンス名
private BooksService booksService;

//details.jspからここに飛ぶ POSTで揃える
/**
 * @param model
 * @param bookId
 * @return
 */
@RequestMapping(value = "/rentBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ

//RequestParamでname属性を取得
public String rent(Model model,
        @RequestParam("bookId") int bookId) {

    //countRecordメソッド呼び出し
    rentalService.countRecord(bookId);

    //第一引数に"変数名"、第二引数に　”送りたい文字列”を書く jspに送る
    model.addAttribute("disabled", "disabled");
    model.addAttribute("lendingStatus", "貸し出し不可");
    model.addAttribute("editError", "貸出中のため編集・削除できません");

    //情報を渡してあげる
    model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
    //詳細画面に戻る
    return "details";

    //借りるボタンを押すと、” 貸し出し不可”に変わる　返すボタンが効く　借りるボタン押しても効かないように
    //返すボタンを押すと、”貸し出し可”に変わる　借りるボタンが効く 返すボタンが効かないように

}

/**
 * @param model
 * @param bookId
 * @return
 */
@RequestMapping(value = "/returnBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
public String back(Model model,
        @RequestParam("bookId") int bookId) {

    //returnBookメソッド呼び出し
    rentalService.returnBook(bookId);

    //第一引数に"変数名"、第二引数に”送りたい文字列”を書く jspに送る
    model.addAttribute("returnDisabled", "disabled");
    model.addAttribute("lendingStatus", "貸し出し可");

    //情報を渡してあげる
    model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
    //詳細画面に戻る
    return "details";

    //借りるボタンを押すと、” 貸し出し不可”に変わる　返すボタンが効く　借りるボタン押しても効かないように
    //返すボタンを押すと、”貸し出し可”に変わる　借りるボタンが効く 返すボタンが効かないように

}

}
