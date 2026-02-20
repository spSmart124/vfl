package com.vfl.webservice;

import com.vfl.dto.Case;
import com.vfl.dto.Engagement;
import com.vfl.util.Util;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Template {

    public Template() {
    }

//    Path workPath = Paths.get("Copy string literal text to the clipboard");
    @PostMapping(path = "/template/case")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCase(@RequestBody Case myCase) {
        return Util.createTemplate(myCase);
    }

    @PostMapping(path = "/template/engagement")
    @ResponseStatus(HttpStatus.CREATED)
    public String createEngagement(@RequestBody Engagement myEngagement) {
        return Util.createTemplate(myEngagement);
    }
}
