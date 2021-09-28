package com.jornah.model.vo;

import java.util.Arrays;

public class TranslateRequest {
    private String text;
    private String sourceLanguage;
    private String[] targetLanguages;
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String[] getTargetLanguages() {
        return targetLanguages;
    }

    public void setTargetLanguages(String[] targetLanguages) {
        this.targetLanguages = targetLanguages;
    }

    @Override
    public String toString() {
        return "TranslateRequest{" +
                "text='" + text + '\'' +
                ", sourceLanguage='" + sourceLanguage + '\'' +
                ", targetLanguages=" + Arrays.toString(targetLanguages) +
                ", secret='" + secret + '\'' +
                '}';
    }
}
