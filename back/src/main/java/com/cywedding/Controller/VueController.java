package com.cywedding.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VueController {
    
    @RequestMapping("/image/list")
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
