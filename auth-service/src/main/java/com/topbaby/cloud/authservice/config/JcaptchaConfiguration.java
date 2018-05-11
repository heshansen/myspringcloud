package com.topbaby.cloud.authservice.config;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * <p>Jcaptcha 配置类</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-22
 * @since 1.0.0
 */
@Configuration
public class JcaptchaConfiguration {
    private String acceptedChars = "1234567890";
    private int minAcceptedWordLength = 4;
    private int maxAcceptedWordLength = 4;
    private Integer width = 100;
    private Integer height = 50;
    private Integer minFontSize = 20;
    private Integer maxFontSize = 20;

    public FontGenerator fontGenerator() {
        Font arial = new Font("Arial", 1, 10);
        Font bellMT = new Font("Bell MT", 1, 10);
        Font creditValley = new Font("Credit valley", 1, 10);
        Font courierNew = new Font("Courier New", 1, 10);

        Font[] fonts = {arial, bellMT, creditValley, courierNew};

        return new RandomFontGenerator(minFontSize, maxFontSize, fonts);
    }

    public BackgroundGenerator backgroundGenerator() {
        return new UniColorBackgroundGenerator(width, height, new Color(255, 255, 255));
    }

    public TextPaster textPaster() {

        Color[] colors = {new Color(0, 0, 0), new Color(0, 0, 255), new Color(23, 170, 27), new Color(220, 34, 11), new Color(23, 67, 162)};
        RandomListColorGenerator randomListColorGenerator = new RandomListColorGenerator(colors);

        TextDecorator[] textDecorators = {new SimpleTextDecorator()};

        return new DecoratedRandomTextPaster(minAcceptedWordLength, maxAcceptedWordLength, randomListColorGenerator, textDecorators);
    }

    public WordGenerator wordGenerator() {
        return new RandomWordGenerator(this.acceptedChars);
    }

    public WordToImage wordToImage() {
        return new ComposedWordToImage(fontGenerator(), backgroundGenerator(), textPaster());
    }

    public ImageCaptchaFactory imageCaptchaFactory() {
        return new GimpyFactory(wordGenerator(), wordToImage());
    }

    public GenericCaptchaEngine imageCaptchaEngine() {
        CaptchaFactory[] factories = {imageCaptchaFactory()};
        return new GenericCaptchaEngine(factories);
    }

    @Bean
    public DefaultManageableImageCaptchaService imageCaptchaService() {
        DefaultManageableImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService();
        imageCaptchaService.setCaptchaEngine(imageCaptchaEngine());
        return imageCaptchaService;
    }
}
