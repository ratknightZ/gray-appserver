package com.arms.appserver.controller.api;

import com.arms.appserver.vo.MessageVO;
import com.arms.common.vo.JsonVO;
import com.arms.service.exception.MessageException;
import com.arms.service.model.Message;
import com.arms.service.model.UserReadMessage;
import com.arms.service.service.MessageService;
import com.arms.service.service.UserReadMessageService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuchen
 * @since 2017/12/14
 */
@Controller
public class MessageApiController {

    @Resource
    private MessageService messageService;

    @Resource
    private UserReadMessageService userReadMessageService;

    @ResponseBody
    @RequestMapping(value = "/api/message/my_message",method = RequestMethod.GET)
    public String myMessage(@RequestParam int userId,
                            @RequestParam(required = false,defaultValue = "1") int page,
                            @RequestParam(required = false,defaultValue = "12") int pageSize){
        JsonVO json = new JsonVO();
        if (userId <= 0){
            json.setIsSuccess(0);
            json.setMsg("请求错误");
            json.setMsgForDebug("非法参数，userId: " + userId);
            return json.toString();
        }

        List<Message> messageList = messageService.getMessageList(userId,page,pageSize);
        List<MessageVO> messageVOList = new ArrayList<>();
        for (Message message : messageList){
            if (message == null){
                continue;
            }
            MessageVO messageVO = new MessageVO();
            messageVO.setMessageId(message.getId());
            messageVO.setTitle(message.getTitle());
            messageVO.setContent(message.getContent());
            messageVO.setFromUserId(message.getFromUserId());
            messageVO.setSendTime(new DateTime(message.getGmtCreate().getTime()).toString("yyyy/MM/dd"));
            UserReadMessage userReadMessage = userReadMessageService.get(userId,message.getId());
            if (userReadMessage == null){
                messageVO.setIsRead(0);
            }else {
                messageVO.setIsRead(1);
            }
            messageVOList.add(messageVO);
        }
        json.setData(messageVOList);
        json.setIsSuccess(1);
        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/message/read_message",method = RequestMethod.POST)
    public String readMessage(@RequestParam int userId,
                              @RequestParam long messageId){
        JsonVO json = new JsonVO();
        if (userId <= 0){
            json.setIsSuccess(0);
            json.setMsgForDebug("非法参数，userId: " + userId);
            return json.toString();
        }
        if (messageId <= 0){
            json.setIsSuccess(0);
            json.setMsgForDebug("非法参数，messageId: " + messageId);
            return json.toString();
        }
        try {
            userReadMessageService.add(userId,messageId);
        } catch (MessageException e) {
            json.setIsSuccess(0);
            json.setMsgForDebug(e.getMessage());
            return json.toString();
        }
        json.setIsSuccess(1);
        return json.toString();
    }
}
