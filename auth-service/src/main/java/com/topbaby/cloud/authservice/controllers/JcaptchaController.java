package com.topbaby.cloud.authservice.controllers;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.topbaby.cloud.authservice.util.RedisConstants;
import com.topbaby.data.model.MessageDTO;
import com.topbaby.data.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

/**
 * <p>图形验证码 Controller</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-22
 * @since 1.0.0
 */
@RestController
public class JcaptchaController {

    /**
     * 验证码图形格式
     */
    private static final String CAPTCHA_IMAGE_FORMAT = "jpeg";

    @Autowired
    private DefaultManageableImageCaptchaService imageCaptchaService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取图形验证码
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            handleOutputStream(jpegOutputStream, request.getSession().getId(), request.getLocale());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/" + CAPTCHA_IMAGE_FORMAT);
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 图形验证码校验
     * 更新redis中该手机号发送验证码需要弹出图形验证码的标志
     *
     * @param request HttpServletRequest
     * @param phone   手机号
     * @param code    图形验证码数值
     * @return MessageDTO
     */
    @GetMapping("/captcha/check")
    public MessageDTO check(HttpServletRequest request, String phone, String code) {
        try {
            if (!imageCaptchaService.validateResponseForID(request.getSession().getId(), code)) {
                return new MessageDTO(MessageType.ERROR, "验证码输入有误");
            }
        } catch (CaptchaServiceException e) {
            return new MessageDTO(MessageType.ERROR, "验证码输入有误");
        }

        //消除短信验证码发送限制
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(RedisConstants.SMS_FOR_SHOW_VERIFICATION_CODE_FLAG_PREFIX + phone, phone);
        return new MessageDTO(MessageType.SUCCESS, "验证通过");
    }

    /**
     * 处理验证码图片
     * TODO 创建细节注释待补充
     *
     * @param jpegOutputStream 字节输出流
     */
    private void handleOutputStream(ByteArrayOutputStream jpegOutputStream, String responseOutputStream, Locale locale) throws IOException, CaptchaServiceException {
        BufferedImage challenge = imageCaptchaService.getImageChallengeForID(responseOutputStream, locale);

        Graphics2D graphics2D = challenge.createGraphics();
        Random ran = new Random();
        graphics2D.setColor(this.getRandColor(ran, 110, 255));
        graphics2D.setBackground(this.getRandColor(ran, 10, 255));

        int i = 1;
        while (true) {
            if (i >= 4) {
                graphics2D.dispose();
                ImageIO.write(challenge, CAPTCHA_IMAGE_FORMAT, jpegOutputStream);
                break;
            }

            int x = ran.nextInt(challenge.getWidth());
            int y = ran.nextInt(challenge.getHeight());
            int x1 = ran.nextInt(challenge.getWidth());
            int y1 = ran.nextInt(challenge.getHeight());
            if (i % 2 == 0) {
                graphics2D.drawLine(x, y, x1, y1);
            }

            graphics2D.drawOval(x1, y1, x, y);
            ++i;
        }
    }

    private Color getRandColor(Random ran, int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }

        if (bc > 255) {
            bc = 255;
        }

        int r = fc + ran.nextInt(bc - fc);
        int g = fc + ran.nextInt(bc - fc);
        int b = fc + ran.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
