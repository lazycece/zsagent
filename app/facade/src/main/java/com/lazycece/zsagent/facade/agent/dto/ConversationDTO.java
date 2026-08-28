package com.lazycece.zsagent.facade.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对话DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class ConversationDTO {

    /** 对话唯一标识 */
    private String conversationId;

    /** 对话标题 */
    private String title;

    /** 消息列表（详情接口返回，列表接口不返回） */
    private List<MessageDTO> messages;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后更新时间 */
    private LocalDateTime updatedAt;
}
