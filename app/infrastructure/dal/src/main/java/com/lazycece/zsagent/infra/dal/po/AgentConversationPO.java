package com.lazycece.zsagent.infra.dal.po;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * agent_conversation 表 PO
 *
 * @author lazycece
 */
@Getter
@Setter
public class AgentConversationPO {

    /** 主键 */
    private Long id;
    /** 对话唯一标识 */
    private String conversationId;
    /** 用户ID */
    private String userId;
    /** 对话标题 */
    private String title;
    /** 状态: ACTIVE / ARCHIVED */
    private String status;
    /** 创建人 */
    private String creator;
    /** 更新人 */
    private String updater;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 逻辑删除 */
    private Boolean deleted;
}
