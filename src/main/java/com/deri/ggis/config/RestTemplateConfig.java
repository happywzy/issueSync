package com.deri.ggis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: RestTemplateConfig
 * @Description: restful客户端
 * @Author: wuzhiyong
 * @Time: 2020/5/8 11:04
 * @Version: v1.0
 **/
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory;
        factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(600000);
        factory.setReadTimeout(600000);
        return factory;
    }

}
