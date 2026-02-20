package com.vfl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vfl.dto.ITemplate;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.heading.Heading;
import org.apache.commons.exec.*;
import org.springframework.core.env.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Util {
    private static final Environment env = ApplicationEnvironment.environment;

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

    public static Map<String, String> createTemplateFolderNFile(ITemplate template, Path workPath, Path templateFile, FileFormat fileFormat) {
        if ((Files.exists(workPath))) {
            System.out.println(workPath + " file is already present in FileSystem.");
        } else {
            try {
                Files.createDirectory(workPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if ((Files.exists(templateFile))) {
            System.out.println(templateFile + " file is already present in FileSystem.");
        } else {
            ObjectMapper om = new ObjectMapper();
            try (BufferedWriter writer = Files.newBufferedWriter(templateFile)) {
//                String fileFormat = env.getProperty("template.file.format");
                String formattedString = null;
                assert fileFormat != null;
                if (fileFormat.equals(FileFormat.JSON)) {
                    formattedString = createJsonFormattedTemplate(template);
                } else if (fileFormat.equals(FileFormat.MARKDOWN)) {
                    formattedString = createMarkdownFormattedTemplate(template);
                } else {
                    // Using default format
                    formattedString = createJsonFormattedTemplate(template);
                }

                writer.write(formattedString, 0, formattedString.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        Map<String, String> map = new HashMap<>();
        map.put("folder", workPath.toString());
        map.put("file", templateFile.toString());

        return map;
    }

    public static String getTemplateName(ITemplate template) {
        Class<?> myClass = template.getClass();
        return myClass.getSimpleName();
    }

    public static String createTemplate(ITemplate template) {
//        Environment env = ApplicationEnvironment.environment;
        String workPathString = env.getProperty("app.work.path") + template.getNo();
        System.out.println("workPathString: " + workPathString);

        FileFormat fileFormat;
        String appFileFormat = env.getProperty("template.file.format");
        assert appFileFormat != null;
        if (appFileFormat.equals(FileFormat.JSON.getFormat())) {
            fileFormat = FileFormat.JSON;
        } else if (appFileFormat.equals(FileFormat.MARKDOWN.getFormat())) {
            fileFormat = FileFormat.MARKDOWN;
        } else {
            // Using default format as JSON
            fileFormat = FileFormat.JSON;
        }

        Path workPath = Path.of(workPathString);
//        String templateFileName = getLowerCaseTemplateName(template) + env.getProperty("template.file.delimiter") + template.getNo() + env.getProperty("template.file.delimiter") + env.getProperty("template.file.end") + env.getProperty("template.file.extension");
        String templateFileName = getTemplateName(template).toLowerCase() + env.getProperty("template.file.delimiter") + template.getNo() + env.getProperty("template.file.delimiter") + env.getProperty("template.file.end") + fileFormat.getFileExtension();
        Path templateFile = Path.of(workPath + "\\", templateFileName);

        Map<String, String> map = createTemplateFolderNFile(template, workPath, templateFile, fileFormat);
        String fileExplorerApp = env.getProperty("app.file.explorer.application");
        Util.startExternalProcess(fileExplorerApp, map.get("folder"));
        // Sleep for 1 second to allow Explorer to be opened and present in UI as notepad++.exe opens faster than explorer.exe. The requirement is to open explore.exe and then notepad++.exe
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String fileEditorApp = env.getProperty("app.file.editor.application");
        Util.startExternalProcess(fileEditorApp, map.get("file"));
        return "Template created successfully with input." + template;
    }

    public static String createJsonFormattedTemplate(ITemplate template) {
        ObjectMapper om = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = om.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(template).replace("\"no\" :", "\"" + getTemplateName(template).toLowerCase() + "No\" :");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(jsonString);
        return jsonString;
    }

    public static String createMarkdownFormattedTemplate(ITemplate template) {
        StringBuilder sb = createHeadingLevelledMarkdownFormatTemplate(template, 1);

        return sb.toString();
    }

    private static StringBuilder createHeadingLevelledMarkdownFormatTemplate(ITemplate template, int level) {
        StringBuilder sb = new StringBuilder();
//        Class<?> cls = template.getClass();
//
//        sb.append(new Heading(getTemplateName(template) + " " + template.getNo() + " Notes", level)).append("\n");
//
//        Method[] methods = cls.getMethods();
//        for (Method method: methods) {
//            String methodPrefix = "get";
//            if (method.getName().startsWith(methodPrefix) && !method.getName().equals("getClass")) {
////                System.out.println(method.getName());
//
//                try {
//                    sb.append(new BoldText(method.getName().substring(methodPrefix.length()) + ":")).append(" ").append(method.invoke(template)).append("\n\n");
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//        }

        sb = createHeadingLevelledMarkdownFormat(template);

//        sb.append("\n");
        return sb;
    }

    private static String getMethodNameWithoutPrefix(Method method, String methodPrefix) {
        return method.getName().substring(methodPrefix.length());
    }

    private static boolean isMethodPrefixed(Method method, String methodPrefix) {
        return method.getName().startsWith(methodPrefix);
    }

    private static boolean isGetClassMethod(Method method) {
        return method.getName().equals("getClass");
    }

    private static StringBuilder createHeadingLevelledMarkdownFormat(ITemplate template) {
        StringBuilder sb = new StringBuilder();
        sb.append(new Heading(getTemplateName(template) + " " + template.getNo() + " Notes", 1)).append("\n");

        Class<?> cls = template.getClass();

        String methodPrefix = "get";
//        Method[] filteredMethods = cls.getMethods();
        // Appending getNo method field
        sb.append(new BoldText(getTemplateName(template).toLowerCase() + "No:")).append(" ").append(template.getNo()).append("\n\n");

        Stream<Method> filteredMethods = Arrays.stream(cls.getMethods()).filter(x->isMethodPrefixed(x, methodPrefix) && !isGetClassMethod(x) && !x.getName().equals("getNo"));

        for (Method method: filteredMethods.toList()) {
                try {
                    sb.append(new BoldText(getMethodNameWithoutPrefix(method, methodPrefix) + ":")).append(" ").append(method.invoke(template)).append("\n\n");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
        }
        sb.append("\n");

        for (Class myCls: template.findStructures()) {
            sb.append(createHeadingLevelledMarkdownFormat(myCls));
        }

        return sb;
    }

    private static String createHeadingLevelledMarkdownFormat(Class cls) {
        StringBuilder sb = new StringBuilder();
        sb.append(new Heading(cls.getSimpleName(), 2)).append("\n");

//        Method[] methods = cls.getMethods();
        String methodPrefix = "get";
        Stream<Method> filteredMethods = Arrays.stream(cls.getMethods()).filter(x->isMethodPrefixed(x, methodPrefix) && !isGetClassMethod(x));
        for (Method method: filteredMethods.toList()) {
            sb.append(new BoldText(getMethodNameWithoutPrefix(method, methodPrefix) + ":")).append(" ").append("\n\n");
        }
        sb.append("\n");

        return sb.toString();
    }
}
