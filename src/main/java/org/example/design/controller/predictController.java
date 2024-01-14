package org.example.design.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.example.design.common.R;
import org.example.design.domain.Relic;
import org.example.design.service.impl.RelicServiceImpl;
import org.example.design.util.HttpClientUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/predict")
public class predictController {

    private final HttpClientUtil httpClientUtil;
    private final RelicServiceImpl relicService;

    public predictController(HttpClientUtil httpClientUtil, RelicServiceImpl relicService) {
        this.httpClientUtil = httpClientUtil;
        this.relicService = relicService;
    }

    @PostMapping()
        public R<String> pre(MultipartFile image) throws UnirestException, IOException {
        String imgName = UUID.randomUUID().toString().replace("_", "")+".jpg";
        String imgFilePath="python\\"+"end.jpg";
        OutputStream out = new FileOutputStream(imgFilePath);
        out.write(image.getBytes());
        out.flush();
        out.close();
        String predict = httpClientUtil.predict();
        imgFilePath="python\\"+predict+"\\"+imgName;
        out = new FileOutputStream(imgFilePath);
        out.write(image.getBytes());
        out.flush();
        out.close();
        Relic relic = new Relic();
        relic.setName(imgName);
        relic.setTime(LocalDateTime.now());
        relic.setType(predict);
        relicService.save(relic);
        return R.success(predict);
    }
}
