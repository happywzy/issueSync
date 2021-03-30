package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: Sync
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/30 14:54
 * @Version: v1.0
 **/
@Data
@NoArgsConstructor
public class Sync {
    private String githubOwner;
    private String githubRepo;
    private String githubToken;

    private String giteeOwner;
    private String giteeRepo;
    private String giteeToken;
}
