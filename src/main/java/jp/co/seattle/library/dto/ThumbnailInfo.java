package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * サムネイルに関する情報格納DTO
 */
/**
 * @author user
 *
 */
@Configuration
@Data
public class ThumbnailInfo {

    private String thumbnailName;
    private String thumbnailUrl;

    public ThumbnailInfo() {

    }

    // コンストラクタ
    /**
     * @param thumbnailName
     * @param thumbnailUrl
     */
    public ThumbnailInfo(String thumbnailName, String thumbnailUrl) {
        this.thumbnailName = thumbnailName;
        this.thumbnailUrl = thumbnailUrl;

    }

}