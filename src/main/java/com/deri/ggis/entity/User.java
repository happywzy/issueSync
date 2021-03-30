package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: User
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:09
 * @Version: v1.0
 **/
@Data
@NoArgsConstructor
public class User {
    private String login;
    private String html_url;
    private String type;
    private boolean site_admin;
}
