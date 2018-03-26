package com.arms.appserver.controller.util;

import com.arms.core.configure.ConfigResolver;
import com.arms.core.configure.DefaultConfigResolver;
import com.arms.service.exception.AppPushException;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangchaojie on 2017/5/9.
 */
public class AppPushUtil {

    private static Logger logger       = LoggerFactory.getLogger(AppPushUtil.class);

    private static String ENV_MODE     = "";

    //定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
    private static String appId        = "";
    private static String appKey       = "";
    private static String masterSecret = "";
    private static String url          = "http://sdk.open.api.igexin.com/apiex.htm";

    static {
        ConfigResolver configResolver = new DefaultConfigResolver();
        Configuration configuration = null;
        try {
            configuration = configResolver.getAppConfig();
            ENV_MODE = configuration.getString("app.env");
        } catch (ConfigurationException e) {
            logger.error("", e);
        } catch (MalformedURLException e) {
            logger.error("", e);
        }
        if (!ENV_MODE.equals("release")) {
            appId = "GNtO17w1VVAmoxucYmoYw7";
            appKey = "4tJmFJjnfe9oWU0tNhSgG7";
            masterSecret = "zrnweWXgBO73OEGacYt2m9";
        } else {
            appId = "";
            appKey = "";
            masterSecret = "";
        }
    }

    /**
     * zhangchaojie
     * ios单用户推送
     * @param cid
     * @param pushMessage
     * @throws AppPushException
     */
    public static void iosSinglePush(String cid, String pushMessage, String transmissionMessage)
                                                                                                throws AppPushException {
        if (StringUtils.isBlank(cid) || StringUtils.isBlank(pushMessage)) {
            throw new AppPushException("Illegal parameter !! [cid: " + cid + "], [pushMessage: "
                                       + pushMessage + "], [transmissionMessage: "
                                       + transmissionMessage + "]");
        }
        IGtPush push = new IGtPush(url, appKey, masterSecret);

        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        TransmissionTemplate transmissionTemplate = getTemplate(pushMessage, transmissionMessage);

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmissionTemplate);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cid);
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            logger.info(ret.getResponse().toString());
        } else {
            logger.info("服务器响应异常");
        }
    }

    public static TransmissionTemplate getTemplate(String pushMessage, String transmissionMessage) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        //        template.setTransmissionContent("透传内容");
        template.setTransmissionContent(transmissionMessage);
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");

        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(pushMessage));

        template.setAPNInfo(payload);
        return template;
    }

    public static NotificationTemplate getNotificationTemplate (String pushMessage, String transmissionMessage) {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        //        template.setTransmissionContent("透传内容");
        //template.setTransmissionContent(transmissionMessage);
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory(transmissionMessage);

        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(pushMessage));

        template.setAPNInfo(payload);
        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle(pushMessage);
        style.setText(transmissionMessage);
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);
        return template;
    }

    /**
     * zhangchaojie
     * ios全体推送
     * @param pushMessage
     * @throws AppPushException
     */
    public static void iosAppPush(String pushMessage, String transmissionMessage)
                                                                                 throws AppPushException {
//        if (StringUtils.isBlank(pushMessage)) {
//            throw new AppPushException("Illegal parameter !! [pushMessage: " + pushMessage
//                    + "], [transmissionMessage: " + transmissionMessage + "]");
//        }
        IGtPush push = new IGtPush(url, appKey, masterSecret);

        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        TransmissionTemplate transmissionTemplate = getTemplate(pushMessage, transmissionMessage);

        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
        AppMessage message = new AppMessage();
        message.setData(transmissionTemplate);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(message);
        //        logger.info(ret.getResponse().toString());
    }

    //    public static void main(String[] args) throws IOException {
    //        AppPushUtil appPush = new AppPushUtil();
    //        //                appPush.testIosPush();
    //        appPush.testIosSinglePush();
    //    }

    //    private void testAndroidPush() {
    //        IGtPush push = new IGtPush(url, appKey, masterSecret);
    //
    //        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
    //        LinkTemplate template = new LinkTemplate();
    //        template.setAppId(appId);
    //        template.setAppkey(appKey);
    //        template.setTitle("欢迎使用个推!");
    //        template.setText("这是一条推送消息~");
    //        template.setUrl("http://getui.com");
    //
    //        List<String> appIds = new ArrayList<String>();
    //        appIds.add(appId);
    //
    //        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
    //        AppMessage message = new AppMessage();
    //        message.setData(template);
    //        message.setAppIdList(appIds);
    //        message.setOffline(true);
    //        message.setOfflineExpireTime(1000 * 600);
    //
    //        IPushResult ret = push.pushMessageToApp(message);
    //        System.out.println(ret.getResponse().toString());
    //    }

    //    private void testIosPush() {
    //        IGtPush push = new IGtPush(url, appKey, masterSecret);
    //
    //        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
    //        TransmissionTemplate transmissionTemplate = getTemplate();
    //
    //        List<String> appIds = new ArrayList<String>();
    //        appIds.add(appId);
    //
    //        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
    //        AppMessage message = new AppMessage();
    //        message.setData(transmissionTemplate);
    //        message.setAppIdList(appIds);
    //        message.setOffline(true);
    //        message.setOfflineExpireTime(1000 * 600);
    //
    //        IPushResult ret = push.pushMessageToApp(message);
    //        System.out.println(ret.getResponse().toString());
    //    }

    //    private void testIosSinglePush() {
    //        IGtPush push = new IGtPush(url, appKey, masterSecret);
    //
    //        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
    //        TransmissionTemplate transmissionTemplate = getTemplate();
    //
    //        List<String> appIds = new ArrayList<String>();
    //        appIds.add(appId);
    //
    //        //        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
    //        //        AppMessage message = new AppMessage();
    //        //        message.setData(transmissionTemplate);
    //        //        message.setAppIdList(appIds);
    //        //        message.setOffline(true);
    //        //        message.setOfflineExpireTime(1000 * 600);
    //        //
    //        //        IPushResult ret = push.pushMessageToApp(message);
    //        //        System.out.println(ret.getResponse().toString());
    //
    //        SingleMessage message = new SingleMessage();
    //        message.setOffline(true);
    //        // 离线有效时间，单位为毫秒，可选
    //        message.setOfflineExpireTime(24 * 3600 * 1000);
    //        message.setData(transmissionTemplate);
    //        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
    //        message.setPushNetWorkType(0);
    //        Target target = new Target();
    //        target.setAppId(appId);
    //        //        target.setClientId(CID);
    //        target.setClientId("74310fe08f1df78f7202b6f8310f48eb");
    //        //target.setAlias(Alias);
    //        IPushResult ret = null;
    //        try {
    //            ret = push.pushMessageToSingle(message, target);
    //        } catch (RequestException e) {
    //            e.printStackTrace();
    //            ret = push.pushMessageToSingle(message, target, e.getRequestId());
    //        }
    //        if (ret != null) {
    //            System.out.println(ret.getResponse().toString());
    //        } else {
    //            System.out.println("服务器响应异常");
    //        }
    //
    //    }

    //    public static TransmissionTemplate getTemplate() {
    //        TransmissionTemplate template = new TransmissionTemplate();
    //        template.setAppId(appId);
    //        template.setAppkey(appKey);
    //        template.setTransmissionContent("透传内容");
    //        template.setTransmissionType(2);
    //        APNPayload payload = new APNPayload();
    //        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
    //        payload.setAutoBadge("+1");
    //        payload.setContentAvailable(1);
    //        payload.setSound("default");
    //        payload.setCategory("$由客户端定义");
    //
    //        //简单模式APNPayload.SimpleMsg
    //        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("测试iOS端个推Single推送!!"));
    //
    //        //字典模式使用APNPayload.DictionaryAlertMsg
    //        //payload.setAlertMsg(getDictionaryAlertMsg());
    //
    //        // 添加多媒体资源
    //        payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
    //            .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4").setOnlyWifi(true));
    //
    //        template.setAPNInfo(payload);
    //        return template;
    //    }

    //    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
    //        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
    //        alertMsg.setBody("body");
    //        alertMsg.setActionLocKey("ActionLockey");
    //        alertMsg.setLocKey("LocKey");
    //        alertMsg.addLocArg("loc-args");
    //        alertMsg.setLaunchImage("launch-image");
    //        // iOS8.2以上版本支持
    //        alertMsg.setTitle("Title");
    //        alertMsg.setTitleLocKey("TitleLocKey");
    //        alertMsg.addTitleLocArg("TitleLocArg");
    //        return alertMsg;
    //    }

    public static void pushToList(String pushMessage, String transmissionMessage, List<Target> targetList){
        IGtPush push = new IGtPush(url, appKey, masterSecret);
        // 定义"点击链接打开通知模板"，并设置标题、内容、链接
        NotificationTemplate transmissionTemplate = getNotificationTemplate(pushMessage, transmissionMessage);

        ListMessage message = new ListMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(transmissionTemplate);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targetList);
        if (ret != null) {
            logger.info("推送结果:" + ret.getResponse().toString());
        } else {
            logger.info("服务器响应异常");
        }
    }
}
