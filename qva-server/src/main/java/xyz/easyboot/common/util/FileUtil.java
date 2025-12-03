package xyz.easyboot.common.util;

import cn.hutool.core.lang.UUID;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author wujiawei
 * @see
 * @since 2021/8/6 2:07 下午
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {
    
    /**
     * 从InputPart中获取输入流
     * @param inputPart
     * @return
     * @throws IOException
     */
    public static InputStream getInputStreamFromInputPart(InputPart inputPart) throws IOException {
        return inputPart.getBody(InputStream.class, null);
    }
    
    /**
     * 从InputPart中获取文件名称
     * @param inputPart
     * @return
     */
    public static String getFileNameFromInputPart(InputPart inputPart) {
        String[] contentDisposition = inputPart.getHeaders().getFirst("Content-Disposition").split(";");
        String fileName = null;
        for (String name : contentDisposition) {
            if (name.trim().startsWith("filename=")) {
                String[] arr = name.split("=");
                fileName = arr[1].trim().replaceAll("\"", "");
                fileName = new String(fileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                break;
            }
        }
        return StringUtil.isEmpty(fileName) ? UUID.fastUUID().toString(true) : fileName;
    }
    
}
