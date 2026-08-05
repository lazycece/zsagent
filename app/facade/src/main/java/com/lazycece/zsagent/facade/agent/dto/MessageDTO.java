package com.lazycece.zsagent.facade.agent.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息DTO
 *
 * @author lazycece
 */
@Getter
@Setter
public class MessageDTO {

    /** 消息唯一标识 */
    private String messageId;

    /** 消息角色：USER / ASSISTANT / SYSTEM */
    private String role;

    /** 消息内容 */
    private String content;

    /** 来源引用列表 */
    private List<SourceReferenceDTO> sources;

    /** 反馈状态：USEFUL / NOT_USEFUL */
    private String feedback;

    /** 创建时间 */
    private LocalDateTime createTime;
}
