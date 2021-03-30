package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: Issue
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:06
 * @Version: v1.0
 **/
@Data
@NoArgsConstructor
public class Issue {
    private int number;
    private String title;
    private User user;
    private List<Label> labels;
    private String state;
    private boolean locked;
    private int comments;
    private String body;
    private String html_url;
}
