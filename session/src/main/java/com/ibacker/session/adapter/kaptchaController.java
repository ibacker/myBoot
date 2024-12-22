package com.ibacker.session.adapter;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ibacker.session.infrastructure.bean.ResultObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/session")
public class kaptchaController {

    @Resource
    private DefaultKaptcha captchaProducer;


    @GetMapping("/getVerificationCodePhoto")
    public void getVerificationCodePhoto(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码
            String verifyCode = captchaProducer.createText();
            //验证码字符串保存到session中
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            //设置写出图片的格式
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


    @GetMapping("/getVerificationCode")
    public ResultObject getVerificationCode(HttpServletRequest request) {
        ResultObject result = new ResultObject();
        String verifyCode = captchaProducer.createText();
        request.getSession().setAttribute("verifyCode", verifyCode);
        result.setResult(verifyCode);
        result.setMessage(request.getSession().getId());
        return result;

    }
}
