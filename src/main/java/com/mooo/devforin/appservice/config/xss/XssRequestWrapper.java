package com.mooo.devforin.appservice.config.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    // 생성자
    public XssRequestWrapper(HttpServletRequest request){
        super(request);
    }

    // 파라미터 값 필터링 
    @Override
    public String getParameter(String content){
        String value = super.getParameter(content);
        return sanitize(value);
    }

    // 파라미터 값이 배열인 경우의 필터링
    @Override
    public String[] getParameterValues(String content){
        String[] values = super.getParameterValues(content);
        if(values == null){
            return null;
        }

        String[] sanitizedValues = new String[values.length];
        for(int i = 0; i < values.length; i++){
            sanitizedValues[i] = sanitize(values[i]);
        }
        return sanitizedValues;
    }

    // XSS 공격 방지를 위한 특정 HTML 태그 및 js 코드 치환
    private String sanitize(String value){
        if(value != null){
            // XSS 공격을 방지하기 위한 간단한 치환 (여기서 태그들을 치환)
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            value = value.replaceAll("'", "&#39;");
            value = value.replaceAll("eval\\((.*)\\)", "");
            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replaceAll("script", "");
        }
        return value;
    }


}
