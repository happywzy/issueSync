package com.deri.ggis.controller;

import com.deri.ggis.entity.Sync;
import com.deri.ggis.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @ClassName: SyncController
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:04
 * @Version: v1.0
 **/
@Controller
public class SyncController {
    @Autowired
    SyncService syncService;

    @GetMapping("/sync")
    String sync(Model model) {
        return "sync";
    }

    @PostMapping("/sync/start")
    @ResponseBody
    int start(@RequestBody Sync sync) {
        return syncService.syncIssue(sync);
    }

}
