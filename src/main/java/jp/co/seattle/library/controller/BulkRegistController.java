package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Handles requests for the application home page.
 */

@Controller //APIの入り口
public class BulkRegistController {
    final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

    @Autowired //クラス名 //インスタンス名
    private BooksService booksService;

    /**
     * @param model
     * @return
     */
    //home.jspからここに飛んでくる
    @RequestMapping(value = "/bulkRegistration", method = RequestMethod.GET) //value＝actionで指定したパラメータ

    //RequestParamでname属性を取得
    public String login(Model model) {
        return "bulkRegist"; //bulkRegist.jspへ遷移
    }

    /**
    * bulkRegistBookメソッド
    * csvファイルを読み込み、書籍の一括登録をする
    * @param locale
    * @param csvFile
    * @param model
    * @return  
    * */
    @Transactional //bulkRegist.jspからここに飛んでくる
    @RequestMapping(value = "/bulkRegist", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    public String bulkRegistBook(Locale locale,
            @RequestParam("csvFile") MultipartFile csvFile,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);
        //bookData本棚（リスト）　bulkは本一冊（配列）
        //取り込んだデータ（配列）を格納するためのリスト　
        List<String[]> bookData = new ArrayList<String[]>();
        //エラーを格納するためのリスト
        List<String> errorList = new ArrayList<String>();

        String line;

        boolean flag = false;

        //Maltipartfileを読み込み
        try (InputStream stream = csvFile.getInputStream();
                Reader reader = new InputStreamReader(stream);
                BufferedReader buf = new BufferedReader(reader);) {

            //csvファイルの行がない時の処理
            if (csvFile.isEmpty()) {
                errorList.add("CSVファイルが空です。値を入力してください");
                flag = true;
            }

            int a = 0; //a行目でエラー

            //読み込んだファイルに行がある時の処理  //本の情報がなくなるまで繰り返す
            while ((line = buf.readLine()) != null) {
                String[] bulk = line.split(",", -1);
                a++;

                //必須項目があるかチェック
                if ((bulk[0].isEmpty()) || bulk[0] == null || (bulk[1].isEmpty()) || bulk[1] == null
                        || (bulk[2].isEmpty()) || bulk[2] == null || (bulk[3].isEmpty()) || bulk[3] == null) {
                    errorList.add(a + "行目の必須項目が入力されていません");
                    flag = true;

                }

                //出版日バリデーションチェック
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                    df.setLenient(false); // 日付解析を厳密に行う設定
                    df.parse(bulk[3]);
                } catch (ParseException e) {
                    errorList.add(a + "行目の出版日は半角数字のYYYYMMDD形式で入力してください");
                    flag = true;
                }

                //ISBNバリデーションチェック
                if ((bulk[4].isEmpty()) || !bulk[4].matches("[0-9]{10}|[0-9]{13}")) {
                    errorList.add(a + "行目のISBNの桁数が10か13ではありません。もしくは半角英数字ではありません");
                    flag = true;
                }

                //リストに配列（ファイルの中にある本の情報）を追加
                bookData.add(bulk);

            } //ここまで繰り返し

            //リストに格納されたエラーを表示
            if (flag) {
                model.addAttribute("errorList", errorList);
                return "bulkRegist";
            }

            //書籍を登録
            for (int i = 0; i < bookData.size(); i++) {

                BookDetailsInfo bookInfo = new BookDetailsInfo();
                bookInfo.setTitle(bookData.get(i)[0]);
                bookInfo.setAuthor(bookData.get(i)[1]);
                bookInfo.setPublisher(bookData.get(i)[2]);
                bookInfo.setPublishDate(bookData.get(i)[3]);
                bookInfo.setIsbn(bookData.get(i)[4]);
                bookInfo.setDescription(bookData.get(i)[5]);

                booksService.registBook(bookInfo);
            }
            //登録完了を表示
            model.addAttribute("registComplete", "一括登録完了");

            return "bulkRegist";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("readError", "ファイル読み込みに失敗しました");
            return "bulkRegist";
        }

    }
}
