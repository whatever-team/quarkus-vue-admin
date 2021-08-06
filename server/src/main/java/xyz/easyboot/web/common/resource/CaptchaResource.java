package xyz.easyboot.web.common.resource;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.date.DateUnit;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/26 9:51 下午
 */
@Slf4j
@Path("captcha")
public class CaptchaResource {
    
    public static final TimedCache<String, String> TIMED_CACHE = CacheUtil.newTimedCache(4);
    
    /**
     * 为每个验证码code分配一个主键codeId。
     * 前端请求验证码code时，将codeId在前端生成并发送给后端；
     * 后端生成code及图形验证码后，并与codeId关联保存；
     * 前端填写好code后，将code和codeId一并提交到后端，后端对code和codeId进行比较，完成验证
     * @param timestamp
     * @return
     */
    @GET
    @Produces(value = MediaType.APPLICATION_OCTET_STREAM)
    public byte[] captcha(@QueryParam("t") String timestamp) {
        AbstractCaptcha captcha = CaptchaUtil.createGifCaptcha(116, 40);
        captcha.setGenerator(new RandomGenerator("qwertyupasdfghjkzxcvbnm23456789".toUpperCase(), 4));
        String code = captcha.getCode();
        TIMED_CACHE.put(timestamp, code, DateUnit.MINUTE.getMillis() * 3);
        return captcha.getImageBytes();
    }
    
}
