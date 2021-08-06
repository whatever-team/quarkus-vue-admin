package xyz.easyboot.web.common.service;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.handler.OssConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujiawei
 * @see
 * @since 2021/8/6 9:33 上午
 */
@Slf4j
@Transactional
@ApplicationScoped
public class ExcelService {
    
    @Inject
    OssConfig ossConfig;
    
    @Scheduled(every = "60s", identity = "OssTempStoreCleaner")
    void run() {
        FileUtil.clean(ossConfig.tempStorePath());
    }
    
    /**
     * 导出
     * @param fileName 文件名称，不需要后缀
     * @param head 类Class
     * @param data 导出数据
     * @return
     */
    public File doExport(String fileName, Class head, List data) {
        String filePath = ossConfig.tempStorePath() + "/" + fileName +".xlsx";
        File target = FileUtil.touch(filePath);
        EasyExcel.write(target, head)
                .sheet("sheet1")
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20))
                .doWrite(data);
        return target;
    }
    
    /**
     * 导入
     * @param inputStream
     * @param head 列头类
     * @param importHandler 数据导入处理器
     * @param <Head>
     */
    public <Head> void doImport(InputStream inputStream, Class<Head> head, ImportHandler<Head> importHandler) {
        EasyExcel.read(inputStream, head, new ExcelService.ImportDataListener<Head>(importHandler)).sheet().doRead();
    }
    
    /**
     * 数据导入处理器
     * @param <Head>
     */
    @FunctionalInterface
    public static interface ImportHandler<Head> {
        void handle(List<Head> list);
    }
    
    /**
     * 通用的数据导入监听器
     * @param <Head>
     * @param <Entity>
     */
    public static class ImportDataListener<Head> extends AnalysisEventListener<Head> {
    
        private static final int BATCH_COUNT = 3000;
        List<Head> list = new ArrayList<>();
        
        private final ImportHandler<Head> importHandler;
        public ImportDataListener(ImportHandler<Head> importHandler) {
            this.importHandler = importHandler;
        }
        
        @Override
        public void invoke(Head data, AnalysisContext context) {
            list.add(data);
            if (list.size() >= BATCH_COUNT) {
                importHandler.handle(list);
                list.clear();
            }
        }
    
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            importHandler.handle(list);
            log.info("所有数据解析完成");
        }
    }
    
}
