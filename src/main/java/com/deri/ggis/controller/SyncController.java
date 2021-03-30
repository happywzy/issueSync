package com.deri.ggis.controller;

import com.deri.ggis.entity.Comment;
import com.deri.ggis.entity.CompleteIssue;
import com.deri.ggis.entity.Issue;
import com.deri.ggis.entity.Sync;
import com.deri.ggis.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

/**
 * @ClassName: SyncController
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:04
 * @Version: v1.0
 **/
@RestController
public class SyncController {
    @Autowired
    SyncService syncService;

    @GetMapping("/test1")
    Map<Integer, CompleteIssue> test1() throws InterruptedException {
        return syncService.getCompleteIssues("happywzy", "happywzy.github.io", "0c8b33af25804af157123aaf3b81e242eaabffeb");
    }

    @GetMapping("/test2")
    List<Comment> test2() {
        return syncService.getComment("apache", "rocketmq-externals", null, 570);
    }

    @GetMapping("/test3")
    List<Issue> test3() throws InterruptedException {
        return syncService.getIssues("happywzy", "happywzy.github.io", "0c8b33af25804af157123aaf3b81e242eaabffeb");
    }

    @PostMapping("/test4")
    void test4(@RequestBody Sync sync) throws InterruptedException {
        syncService.syncIssue(sync);
    }
}
