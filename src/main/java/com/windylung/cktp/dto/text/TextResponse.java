package com.windylung.cktp.dto.text;

public class TextResponse {
    private String data;
    private Integer saliva;

    public TextResponse(String data, Integer saliva) {
        this.data = data;
        this.saliva = saliva;
    }

    public String getData() {
        return data;
    }

    public Integer getSaliva() {
        return saliva;
    }
}
