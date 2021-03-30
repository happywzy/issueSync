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
public class GiteeComment {
    private String access_token;
    private String owner;
    private String repo;
    private String number;
    private String body;
}
