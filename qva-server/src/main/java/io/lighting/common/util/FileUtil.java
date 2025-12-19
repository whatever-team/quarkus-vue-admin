package io.lighting.common.util;

import cn.hutool.core.lang.UUID;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author wujiawei
 * @see
 * @since 2021/8/6 2:07 下午
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {
    
    /**
     * 取上传文件的原始文件名（处理编码异常）。
     */
    public static String getFileName(FileUpload upload) {
        if (upload == null || StringUtil.isEmpty(upload.fileName())) {
            return UUID.fastUUID().toString(true);
        }
        return new String(upload.fileName().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    /**
     * 打开上传文件的流。
     */
    public static InputStream openStream(FileUpload upload) throws IOException {
        return Files.newInputStream(upload.uploadedFile());
    }

    /**
     * 将上传的临时文件移动到目标路径。
     */
    public static File moveUpload(FileUpload upload, Path target) throws IOException {
        Files.createDirectories(target.getParent());
        return Files.move(upload.uploadedFile(), target, StandardCopyOption.REPLACE_EXISTING).toFile();
    }

    public static long size(FileUpload upload) {
        return upload == null ? 0 : upload.size();
    }
}
