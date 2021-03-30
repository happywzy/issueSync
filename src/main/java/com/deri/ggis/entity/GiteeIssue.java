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
public class GiteeIssue {
    private String access_token;
    private String owner;
    private String repo;
    private String title;
    private String body;
    private String labels;
    private String state;
    private String number;
}
