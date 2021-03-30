package com.deri.ggis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.deri.ggis.entity.*;
import com.deri.ggis.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SyncService
 * @Description: TODO
 * @Author: wuzhiyong
 * @Time: 2021/3/29 16:42
 * @Version: v1.0
 **/
@Service
@Slf4j
public class SyncService {
    @Autowired
    RestTemplate restTemplate;

    public void syncIssue(Sync sync) throws InterruptedException {
        Map<Integer, CompleteIssue> issueMap = getCompleteIssues(sync.getGithubOwner(), sync.getGithubRepo(), sync.getGithubToken());
        log.info("在Github-{}-{}上共收集{}了条issues.", sync.getGithubOwner(), sync.getGithubRepo(), issueMap.size());
        for (CompleteIssue ci : issueMap.values()) {
            GiteeIssue giteeIssue = new GiteeIssue();
            giteeIssue.setAccess_token(sync.getGiteeToken());
            giteeIssue.setOwner(sync.getGiteeOwner());
            giteeIssue.setRepo(sync.getGiteeRepo());
            giteeIssue.setBody(generateIssueBody(ci));
            giteeIssue.setTitle(ci.getIssue().getTitle());
            giteeIssue.setLabels(generateLabels(ci));
            String number = addIssue(giteeIssue);
            if (ci.getIssue().getComments() > 0) {
                for (Comment comment : ci.getComments()) {
                    GiteeComment giteeComment = new GiteeComment();
                    giteeComment.setAccess_token(sync.getGiteeToken());
                    giteeComment.setNumber(number);
                    giteeComment.setOwner(sync.getGiteeOwner());
                    giteeComment.setRepo(sync.getGiteeRepo());
                    giteeComment.setBody(generateCommentBody(comment));
                    addComment(giteeComment);
                }
            }
            if ("closed".equalsIgnoreCase(ci.getIssue().getState())) {
                giteeIssue.setNumber(number);
                giteeIssue.setState("closed");
                closeIssue(giteeIssue);
            }
        }
        log.info("issues同步到Gitee完成.");
    }

    private String generateLabels(CompleteIssue ci) {
        StringBuilder builder = new StringBuilder("");
        for (Label label : ci.getIssue().getLabels()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(label.getName());
        }
        if (builder.length() <= 0) return null;
        else return builder.toString();
    }


    private String generateIssueBody(CompleteIssue ci) {
        StringBuilder builder = new StringBuilder("---------------- ");
        builder.append("[原issue作者：" + ci.getIssue().getUser().getLogin() + "](");
        builder.append(ci.getIssue().getUser().getHtml_url() + ") ---------------- \n\n ");
        builder.append(ci.getIssue().getBody());
        builder.append(" \n\n ");
        builder.append("---------------- [原issue地址](" + ci.getIssue().getHtml_url() + ") ----------------");
        return builder.toString();
    }

    private String generateCommentBody(Comment comment) {
        StringBuilder builder = new StringBuilder("---------------- ");
        builder.append("[原评论作者：" + comment.getUser().getLogin() + "](");
        builder.append(comment.getUser().getHtml_url() + ") ---------------- \n\n ");
        builder.append(comment.getBody());
        builder.append(" \n\n ");
        builder.append("---------------- [原评论地址](" + comment.getHtml_url() + ") ----------------");
        return builder.toString();
    }

    public void closeIssue(GiteeIssue giteeIssue) {
        String url = Utils.GITEE_API_URL_PREDIX + giteeIssue.getOwner() + "/issues/" + giteeIssue.getNumber();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPatch httpPatch = new HttpPatch(url);
            addEntity(httpPatch, JSON.toJSONString(giteeIssue));
            httpClient.execute(httpPatch);
        } catch (Exception e) {
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ioe) {
                }
            }
        }
    }

    public HttpEntityEnclosingRequestBase addEntity(HttpEntityEnclosingRequestBase http, String json) throws UnsupportedEncodingException {
        http.addHeader("Content-Type", "application/json;charset=UTF-8");
        http.addHeader("Accept", "application/json");
        http.addHeader("User-Agent", "ggis");
        StringEntity entity = new StringEntity(json, "utf-8");
        http.setEntity(entity);
        return http;
    }

    public void addComment(GiteeComment giteeComment) {
        String url = Utils.GITEE_API_URL_PREDIX + giteeComment.getOwner() + "/" + giteeComment.getRepo() + "/issues/" +
                giteeComment.getNumber() + "/comments";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            addEntity(httpPost, JSON.toJSONString(giteeComment));
            httpClient.execute(httpPost);
        } catch (Exception e) {
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ioe) {
                }
            }
        }
    }


    public String addIssue(GiteeIssue giteeIssue) {
        String url = Utils.GITEE_API_URL_PREDIX + giteeIssue.getOwner() + "/issues";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String number = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            addEntity(httpPost, JSON.toJSONString(giteeIssue));
            response = httpClient.execute(httpPost);
            String tmp = EntityUtils.toString(response.getEntity(), "UTF-8");
            number = JSON.parseObject(tmp, GiteeAddIssueResponse.class).getNumber();
        } catch (Exception e) {
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ioe) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ioe) {
                }
            }
        }
        return number;
    }

    public Map<Integer, CompleteIssue> getCompleteIssues(String owner, String repo, String token) throws InterruptedException {
        Map<Integer, CompleteIssue> completeIssues = new HashMap<>();
        List<Issue> issues = getIssues(owner, repo, token);
        for (Issue issue : issues) {
            CompleteIssue completeIssue = new CompleteIssue();
            completeIssue.setIssue(issue);
            if (issue.getComments() > 0) {
                completeIssue.setComments(getComment(owner, repo, token, issue.getNumber()));
            }
            completeIssues.put(issue.getNumber(), completeIssue);
//            Thread.sleep(100);
        }
        return completeIssues;
    }

    public List<Comment> getComment(String owner, String repo, String token, int issue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.github.v3+json");
        ResponseEntity<String> response = restTemplate.exchange(generateCommentUri(owner, repo, token, issue),
                HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        List<Comment> comments = JSON.parseObject(response.getBody(), new TypeReference<List<Comment>>() {
        });
        return comments;
    }

    public List<Issue> getIssues(String owner, String repo, String token) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.github.v3+json");
        List<Issue> issues = new ArrayList<>();
        int page = 1;
        while (true) {
            ResponseEntity<String> response = restTemplate.exchange(generateIssuesUri(owner, repo, token, page),
                    HttpMethod.GET, new HttpEntity<String>(headers), String.class);
            List<Issue> tmp = JSON.parseObject(response.getBody(), new TypeReference<List<Issue>>() {
            });
            if (tmp.isEmpty()) {
                break;
            } else {
                page++;
                issues.addAll(tmp);
            }
//            Thread.sleep(1000);
        }
        return issues;
    }

    private String generateCommentUri(String owner, String repo, String token, int issue) {
        String uri = Utils.GITHUB_API_URL_PREDIX + owner + "/" + repo + "/issues/" + issue + "/comments";
        if (token != null) {
            uri = uri + "?access_token=" + token;
        }
        return uri;
    }

    private String generateIssuesUri(String owner, String repo, String token, int page) {
        String uri = Utils.GITHUB_API_URL_PREDIX + owner + "/" + repo + "/issues?state=all&per_page=100&page=" + page;
        if (token != null) {
            uri = uri + "&access_token=" + token;
        }
        return uri;
    }
}
