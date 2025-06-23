package com.vfl.webservice;

import com.vfl.util.ApplicationEnvironment;
import com.vfl.util.Util;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CreateTemplate {

    public CreateTemplate() {
    }

//    Path workPath = Paths.get("Copy string literal text to the clipboard");
    @PostMapping(path = "/template")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTemplate(@RequestBody Case myCase) {
        Environment env = ApplicationEnvironment.environment;
        Map<String, String> map = Util.createTemplateFolderNFile(myCase);
//        Util.startExternalProcess("Explorer.exe", map.get("folder"));
        String fileExplorerApp = env.getProperty("app.file.explorer.application");
        Util.startExternalProcess(fileExplorerApp, map.get("folder"));
        // Sleep for 1 second to allow Explorer to be opened and present in UI as notepad++.exe opens faster than explorer.exe. The requirement is to open explore.exe and then notepad++.exe
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        Util.startExternalProcess("notepad++.exe", map.get("file"));
        String fileEditorApp = env.getProperty("app.file.editor.application");
        Util.startExternalProcess(fileEditorApp, map.get("file"));
        return "Template created successfully with input." + myCase;
//        return "Template created successfully with input." + map.get("jsonString");
    }
}
