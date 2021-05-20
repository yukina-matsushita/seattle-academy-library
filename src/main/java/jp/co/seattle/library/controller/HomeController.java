package jp.co.seattle.library.controller;

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
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HomeController {
    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String transitionHome(Model model) {
        model.addAttribute("bookList", booksService.getBookList());
        return "home";
    }

    //検索窓に文字が入り、検索ボタンが押されるとここに飛んでくる
    /**
     * @param model
     * @param searchWord
     * @return ホーム画面
     */

    @Transactional
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchTitle(Model model,
            @RequestParam("searchWord") String searchWord) {
        //該当する書籍を画面に表示
        model.addAttribute("searchBookList", booksService.getSearchBookList(searchWord));
        return "home";

    }

}


