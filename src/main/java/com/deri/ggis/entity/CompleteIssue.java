package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: CompleteIssue
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 17:01
 * @Version: v1.0
 **/
@Data
@NoArgsConstructor
public class CompleteIssue {
    private Issue issue;
    private List<Comment> comments;
}
