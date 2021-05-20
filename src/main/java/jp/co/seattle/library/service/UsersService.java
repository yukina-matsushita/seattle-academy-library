package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.rowMapper.UserCountRowMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
//APIの入り口 APIとは、他のソフトウェアが外部から自分のソフトウェアへアクセスし利用できるようにしたもの
//ソフトウェアコンポーネントが互いにやりとりするのに使用するインタフェースの仕様
public class UsersService {
    final static Logger logger = LoggerFactory.getLogger(UsersService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ユーザー情報を登録する
     * @param userInfo ユーザー情報
     */
    public void registUser(UserInfo userInfo) {

        // SQL生成
        String sql = "INSERT INTO users (email, password,reg_date,upd_date) VALUES ('"
                + userInfo.getEmail()
                + "','"
                + userInfo.getPassword()
                + "',sysdate(),sysdate()" + ")";

        jdbcTemplate.update(sql);
    }

    /**
     * ユーザー情報取得
     * @param email メールアドレス
     * @param password パスワード
     * @return ユーザー情報
     */
    public UserInfo selectUserInfo(String email, String password) {
        // SQL生成
        // SELECT文

        String sql = "SELECT ID,EMAIL,PASSWORD FROM users WHERE PASSWORD="
                + "'"
                + password
                + "'AND email = '"
                + email
                + "'";

            // 検索実行、mapで取得した値をselectUserInfoクラスのインスタンスにセット

        try {
        UserInfo selectedUserInfo = jdbcTemplate.queryForObject(sql, new UserCountRowMapper());
        return selectedUserInfo;

    } catch (EmptyResultDataAccessException e) {
        return null;
    }

    }

}
