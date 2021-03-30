package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Comment
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:53
 * @Version: v1.0
 **/
@Data
@NoArgsConstructor
public class Comment {
    private String html_url;
    private User user;
    private String body;
}
