package com.mooo.devforin.appservice.controller.todo;

import com.mooo.devforin.appservice.config.global.ResponseDTO;
import com.mooo.devforin.appservice.config.global.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
public class TodoController {

    @GetMapping("/get-list")
    public ResponseDTO<Object> getList(){
        log.info("get List Controller.");
      return ResponseUtil.SUCCESS(200, "Get List !", null);
    }

    @GetMapping("/test-log")
    public String testLog() {
        log.info("✅ INFO 로그 출력 확인");
        log.warn("⚠️ WARN 로그 출력 확인");
        log.error("❌ ERROR 로그 출력 확인");
        return "로그 확인 완료!";
    }
}
