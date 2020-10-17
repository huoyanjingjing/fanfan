package com.jike.study.jvm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try {
            String filePath = "E:\\log\\Hello.xlass";
            String objectName = "Hello";
            Class clazz = new HelloClassLoader().findClass(objectName, filePath);
            Method method = clazz.getMethod("hello");
            method.invoke(clazz.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Class<?> findClass(String name, String filePath) throws ClassNotFoundException {
        byte[] originalBytes = getFileOriginalBytes(filePath);
        if (originalBytes == null || originalBytes.length == 0) {
            throw new ClassNotFoundException("not found file named " + filePath + " or " + filePath + "is empty");
        }
        byte[] decodedBytes = decode(originalBytes);
        return defineClass(name, decodedBytes, 0, decodedBytes.length);
    }

    protected byte[] decode(byte[] originalBytes) {
        for (int i = 0; i < originalBytes.length; i++) {
            originalBytes[i] = (byte) (255 - originalBytes[i]);
        }
        return originalBytes;
    }

    protected byte[] getFileOriginalBytes(String path) {
        try (FileInputStream file = new FileInputStream(path);
             ByteArrayOutputStream bou = new ByteArrayOutputStream()) {
            int x;
            while ((x = file.read()) != -1) {
                bou.write(x);
            }
            return bou.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
