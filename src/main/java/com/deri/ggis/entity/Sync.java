package com.deri.ggis.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    public boolean valid() {
        if (StringUtils.isEmpty(githubOwner) || StringUtils.isEmpty(githubRepo) || StringUtils.isEmpty(githubToken) ||
                StringUtils.isEmpty(giteeOwner) || StringUtils.isEmpty(giteeRepo) || StringUtils.isEmpty(giteeToken) ) {
            return false;
        }
        return true;
    }
}
