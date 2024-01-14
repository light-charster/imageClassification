package org.example.design.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.core.http.HttpHeaders;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.RequestConfiguration;
import darabonba.core.client.ClientOverrideConfiguration;
import org.example.design.service.PhoneService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.example.design.domain.Phone;
import org.example.design.domain.phoneCode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class PhoneServiceImpl implements PhoneService{
    private final RedisTemplate<String, String> redisTemplate;
    public PhoneServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean send(String phone) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(Phone.AccessKeyIdAliyun)
                .accessKeySecret(Phone.AccessKeySecretAliyun)
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
        String jsonString = JSONObject.toJSONString(new phoneCode());
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String code = (String)jsonObject.get("code");
        System.out.println(code);
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("校园信息服务平台")
                .templateCode("123123123")
                .phoneNumbers(phone)
                .templateParam(jsonString)
                .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp;
        redisTemplate.opsForValue().set(phone,code,120, TimeUnit.SECONDS);
        try {
            resp = response.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Gson().toJson(resp));
        client.close();
        return true;
    }

}
