package com.spring.springselenium.Configuraion.service;

import com.github.javafaker.Faker;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Lazy
@Service
public class ScreenshotService {

    @Autowired
    private ApplicationContext ctx;

    @Value("${screenshot.path}")
    private Path path;

    @Autowired
    private Faker faker;

    public String takeScreenShot() throws IOException {
        File sourceFile = this.ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        FileCopyUtils.copy(sourceFile, this.path.resolve(faker.name().firstName() + ".png").toFile());
        return sourceFile.toString();
    }
    public String browser_TakeScreenShot() throws IOException {
        String destination = null;
        String imgPath = null;
        String dateName = new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        byte[] imag=this.ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
        ByteArrayInputStream bais = new ByteArrayInputStream(imag);
        BufferedImage image = ImageIO.read(bais);
        imgPath = "\\Screenshots\\" + "screenShot" + dateName + ".png";
        destination = System.getProperty("user.dir") + imgPath;
        File finalDestination = new File(destination);
        finalDestination.getParentFile().mkdir();
        ImageIO.write(image, "png", finalDestination);
        return imgPath;
    }
    public byte[] getScreenshot(){
        return this.ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.BYTES);
    }

}
