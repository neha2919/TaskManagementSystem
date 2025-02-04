package com.neha.TaskManagement.Util;

import com.neha.TaskManagement.Security.LocalAuthStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class RestTemplateWrapper {
    @Autowired
    private RestTemplate template;

    //yet to be completed.
    public <T> T doGet(String uri,Class<T> clazz,Map<String,String> headers){
        HttpHeaders httpHeaders = addBasicHeaders(headers);
        HttpEntity<T> entity = new HttpEntity<>(null,httpHeaders);

        try {
            ResponseEntity<T> response = template.exchange(uri, HttpMethod.GET, entity, clazz);

            return response.getBody();
        }catch (Exception e){
            log.error("Error invoking URL GET : {}",uri);
            return null;
        }
    }

    public <T> T doGet(String uri, ParameterizedTypeReference<T> clazz,Map<String,String> h){
        HttpHeaders httpHeaders = addBasicHeaders(h);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> entity = new HttpEntity<>(null,httpHeaders);

        try{
            ResponseEntity<T> response = template.exchange(uri, HttpMethod.GET, entity, clazz);
            return response.getBody();
        }catch (Exception e){
            log.error("Error invoking URL GET : {}",uri);
            return null;
        }
    }

    public <T,T1> T doPost(String uri, T1 body,Class<T> clazz, Map<String,String> h){
        HttpHeaders httpHeaders = addBasicHeaders(h);
        HttpEntity<T1> entity = new HttpEntity<>(body,httpHeaders);

        try{
            ResponseEntity<T> response = template.exchange(uri,HttpMethod.POST,entity,clazz);
            return response.getBody();
        }catch (Exception e){
            log.error("Error invoking URL POST : {}",uri);
            return null;
        }
    }

    public <T,T1> T doPost(String uri, T1 body, ParameterizedTypeReference<T> clazz, Map<String,String> h){
        HttpHeaders httpHeaders = addBasicHeaders(h);
        HttpEntity<T1> entity = new HttpEntity<>(body,httpHeaders);

        try{
            ResponseEntity<T> response = template.exchange(uri,HttpMethod.POST,entity,clazz);
            return response.getBody();
        }catch (Exception e){
            log.error("Error invoking URL POST : {}",uri);
            return  null;
        }
    }
    private HttpHeaders addBasicHeaders(Map<String,String> h){
        HttpHeaders httpHeaders = new HttpHeaders();
        LocalAuthStore localAuthStore = LocalAuthStore.getLocalAuthStore();
        if (h!=null){
            h.forEach((key,value) -> {
                httpHeaders.add(key,value);
            });
        }
        if (!h.containsKey("Authorization") ||
                (h.containsKey("Authorization") && (h.get("Authorization")!=null || h.get("Authorization").isEmpty()))){
            httpHeaders.setBearerAuth(localAuthStore.getToken());
        }
        return httpHeaders;
    }
}
