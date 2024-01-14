package org.example.design.controller;

import cn.dev33.satoken.util.SaResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.example.design.domain.Change;
import org.example.design.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class WeChatController {
    private final String appId = "wxed9954c01bb89b47";
    private final String appSecret = "a7482517235173ddb4083788de60b90e";
    private final String redirectUrl = "http://localhost:8160/oauth/callback";
    private final userController userClint;

    public WeChatController(userController userClint) {
        this.userClint = userClint;
    }

    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }
    @RequestMapping("/callback")
    public Object login(AuthCallback callback) {
        AuthRequest authRequest = getAuthRequest();
        return authRequest.login(callback);
    }
    private AuthRequest getAuthRequest() {
        return new AuthWeChatOpenRequest(AuthConfig.builder()
                .clientId(appId)
                .clientSecret(appSecret)
                .redirectUri(redirectUrl)
                .build());
    }
    @GetMapping("/Login")
    public SaResult Login(String username)
    {
        Change change = new Change();
        change.setMes("微信");

        change.setUsername(username);
        User user = userClint.find(change);
        return SaResult.ok().setData(user);
    }
}