package com.arms.appserver.controller.wap;

import com.aliyuncs.exceptions.ClientException;
import com.arms.common.exception.BizException;
import com.arms.common.sms.AliSmsUtil;
import com.arms.common.util.ImgVerifyCode;
import com.arms.common.util.NumberUtil;
import com.arms.common.util.VerifyUtil;
import com.arms.common.vo.JsonVO;
import com.arms.service.model.SmsCode;
import com.arms.service.service.SmsCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class SmsController {

    @Resource
    private SmsCodeService    smsCodeService;

    private static final int  SMS_CODE_LENGTH = 6;

    /*
     * 两次获取校验码的时间间隔(单位分钟)
     */
    private static final long DURATION        = 1;

    /**
     * 响应验证码页面
     * @return
     */
    @RequestMapping(value="/wap/common/get_img_verify_code")
    public String getImgVerifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();

        ImgVerifyCode vCode = new ImgVerifyCode(120,40,5,100);
        session.setAttribute("imgVerifyCode", vCode.getCode());
        vCode.write(response.getOutputStream());
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/wap/common/get_sms_code", method = { RequestMethod.GET,
            RequestMethod.POST })
    public String postSMSCheckCode(HttpServletRequest request,
                                   @RequestParam String cellphone,
                                   @RequestParam(required = false) String imgVerifyCode) throws Exception {
        JsonVO json = new JsonVO();

        HttpSession session = request.getSession();
        String imgCode = (String) session.getAttribute("imgVerifyCode");
        if (imgVerifyCode == null || !imgVerifyCode.equalsIgnoreCase(imgCode)){
            json.setIsSuccess(0);
            json.setMsg("图形验证码错误");
            return json.toString();
        }

        if (StringUtils.isBlank(cellphone)) {
            json.setIsSuccess(0);
            json.setMsg("手机号码不能为空！");
            return json.toString();
        }

        if (!VerifyUtil.checkCellphoneNumber(cellphone)) {
            json.setIsSuccess(0);
            json.setMsg("手机号码格式不正确！");
            return json.toString();
        }

        //判断用户获取校验码是否过于频繁
        SmsCode smsCode = smsCodeService.getLast(cellphone);
        if (smsCode != null) {
            String generateTimeMillisStr = smsCode.getSystemTimeMillis();
            if (StringUtils.isNotBlank(generateTimeMillisStr)) {
                long generateTimeMillis = Long.parseLong(generateTimeMillisStr);
                long remainingMillis = System.currentTimeMillis() - generateTimeMillis;
                if (remainingMillis < DURATION * 60 * 1000) {
                    json.setIsSuccess(0);
                    json.setData(remainingMillis);
                    json.setMsg(DURATION + "分钟之内只能获取一次校验码！");
                    return json.toString();
                }
            }
        }

        long phoneNumber = Long.parseLong(cellphone);
        int checkCode = NumberUtil.getRandomIntByLength(SMS_CODE_LENGTH);
        long generateTime = System.currentTimeMillis();
//        MnsUtil.getInstence().postSMS(MnsSignName.Taotie, phoneNumber, checkCode + "");
        //        MnsUtil.getInstence().postSMS(MnsSignName.TheAngryClub, phoneNumber, checkCode + "");

        try {
            AliSmsUtil.sendSms(phoneNumber + "",checkCode + "");
        } catch (ClientException e) {
            json.setIsSuccess(0);
            json.setMsg("验证码发送失败");
            return json.toString();
        }
        SmsCode newSmsCode = new SmsCode();
        newSmsCode.setSmsCode(checkCode + "");
        newSmsCode.setCellphone(cellphone);
        newSmsCode.setSystemTimeMillis(System.currentTimeMillis() + "");
        smsCodeService.add(newSmsCode);

        json.setIsSuccess(1);
        json.setMsg("校验码已发送！");
        return json.toString();
    }

}
