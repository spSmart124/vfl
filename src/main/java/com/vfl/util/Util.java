package com.vfl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vfl.webservice.Case;
import org.apache.commons.exec.*;
import org.springframework.core.env.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static void startExternalProcess(String executableName, String arg) {
        CommandLine cmdLine = new CommandLine(executableName);
        cmdLine.addArgument("${file}");
        HashMap<String, File> map = new HashMap();
        map.put("file", new File(arg));
        cmdLine.setSubstitutionMap(map);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = ExecuteWatchdog.builder().setTimeout(Duration.ofMillis(2000)).get();
        Executor executor = DefaultExecutor.builder().get();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);
        try {
            executor.execute(cmdLine, resultHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

// some time later the result handler callback was invoked so we
// can safely request the exit value
        try {
            resultHandler.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int exitValue = resultHandler.getExitValue();
    }

    public static Map<String, String> createTemplateFolderNFile(Case case1) {
//        String workPathString = "C:\\Users\\srpradhan\\Desktop\\Work\\CAI\\\\" + case1.getCaseNo();
        Environment env = ApplicationEnvironment.environment;
        String workPathString = env.getProperty("app.work.path") + case1.getCaseNo();
        System.out.println("workPathString: " + workPathString);
        Path workPath = Path.of(workPathString);
        String templateFileName = env.getProperty("template.file.start") + env.getProperty("template.file.delimiter") + case1.getCaseNo() + env.getProperty("template.file.delimiter") + env.getProperty("template.file.end") + env.getProperty("template.file.extension");
        Path templateFile = Path.of(workPath + "\\", templateFileName);
        if ((Files.exists(workPath))) {
            System.out.println(workPath + " file is already present in FileSystem.");
        } else {
            try {
                Files.createDirectory(workPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        String testString = "";
        if ((Files.exists(templateFile))) {
            System.out.println(templateFile + " file is already present in FileSystem.");
        } else {
            ObjectMapper om = new ObjectMapper();
            try (BufferedWriter writer = Files.newBufferedWriter(templateFile)) {
//                ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).registerModule(new JavaTimeModule());
//                String jsonString = case1.toString();
                String jsonString = om.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(case1);
                System.out.println(jsonString);
//                jsonString = om.writeValueAsString(case1);
//                om.writeValue(new File(String.valueOf(templateFile)), case1);
                writer.write(jsonString, 0, jsonString.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        Map<String, String> map = new HashMap<>();
        map.put("folder", workPath.toString());
        map.put("file", templateFile.toString());
//        map.put("jsonString", testString);

        return map;
    }
}
