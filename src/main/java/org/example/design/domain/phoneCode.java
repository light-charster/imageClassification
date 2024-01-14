package org.example.design.domain;

import lombok.Data;

@Data
public class phoneCode {
    public String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, 5)));
}
