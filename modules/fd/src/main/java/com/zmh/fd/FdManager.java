package com.zmh.fd;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author skymhzhang on 2019-11-08.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "WeakerAccess"})
public class FdManager {
    private final static String TAG = "zmh_FDManager";

    public static void dumpFdCount() {
        File fdDir = new File(getFdPath());
        File[] fdFiles = fdDir.listFiles();
        if (fdFiles == null) {
            return;
        }
        Log.v(TAG, "fd_count = " + fdFiles.length + " thread_count = " + Thread.getAllStackTraces().size());
    }

    public static String getFdInfo() {
        File fdFile = new File(getFdPath());
        File[] files = fdFile.listFiles();
        if (files == null) {
            return "";
        }
        List<String> list = new ArrayList<>();
        try {
            for (File f : files) {
                list.add(f.getCanonicalPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder fdInfoStr = new StringBuilder("fd_count = " + list.size() + "\n");
        for (String info : list) {
            fdInfoStr.append(info).append("\n");
        }
        return fdInfoStr.toString();
    }


    public static String getThreadName() {
        Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
        StringBuilder info = new StringBuilder("count = " + threadMap.size() + " list = \n");
        for (Map.Entry<Thread, StackTraceElement[]> entry : threadMap.entrySet()) {
            Thread thread = entry.getKey();
            info.append("name = ").append(thread.getName()).append("\n");
        }
        return info.toString();
    }

    private static String getThreadInfo() {
        Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
        StringBuilder info = new StringBuilder();
        for (Map.Entry<Thread, StackTraceElement[]> entry : threadMap.entrySet()) {
            Thread thread = entry.getKey();
            StackTraceElement[] stackElements = entry.getValue();
            info.append("thread name:").append(thread.getName()).append(" id:").append(thread.getId()).append(" thread:").append(thread.getPriority()).append("\n");
            for (StackTraceElement stackElement : stackElements) {
                String stringBuilder = "            " + stackElement.getClassName() + "." +
                        stackElement.getMethodName() + "(" +
                        stackElement.getFileName() + ":" +
                        stackElement.getLineNumber() + ")";
                info.append(stringBuilder).append("\n");
            }
        }
        return info.toString();
    }

    public static void saveAllInfo2File(Context context) {
        String rootPath = getRootPath(context);
        FileUtils.deleteFile(new File(rootPath));
        dumpFdCount();
        String threadName = getThreadName();
        String threadInfo = getThreadInfo();
        String fdInfo = getFdInfo();
        String parentPath = rootPath + "/" + "fd_" + TimeUtils.getCurrentDate();
        File parentFile = new File(parentPath);
        FileUtils.deleteFile(parentFile);
        parentFile.mkdirs();
        FileUtils.write2File(threadName, parentPath + "/" + "threadName.txt");
        FileUtils.write2File(fdInfo, parentPath + "/" + "fdInfo.txt");
        FileUtils.write2File(threadInfo, parentPath + "/" + "threadInfo.txt");
        Log.w(TAG, "saveAllInfo end~");
    }

    private static String getFdPath() {
        return "/proc/" + Process.myPid() + "/fd";
    }

    private static String getRootPath(Context context) {
        File file = context.getExternalFilesDir("/fd");
        if (file == null) {
            throw new RuntimeException(TAG);
        }
        String rootPath = file.getAbsolutePath();
        Log.v(TAG, "rootPath = " + rootPath);
        return rootPath;
    }


}

