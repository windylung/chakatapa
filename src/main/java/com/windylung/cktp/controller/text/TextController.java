package com.windylung.cktp.controller.text;

import com.windylung.cktp.dto.text.TextRequest;
import com.windylung.cktp.dto.text.TextResponse;
import com.windylung.cktp.service.text.TextService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")  // React 앱의 주소
public class TextController {

    private TextService textService = new TextService();
    @GetMapping("/convert")
    TextResponse translateText(@RequestParam String text){
        return textService.translateText(text);
    }
}
