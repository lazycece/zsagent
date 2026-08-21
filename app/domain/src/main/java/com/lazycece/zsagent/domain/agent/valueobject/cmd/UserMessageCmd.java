package com.lazycece.zsagent.domain.agent.valueobject.cmd;

import lombok.Getter;
import lombok.Setter;

/**
 * 记录用户提问命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UserMessageCmd {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 问题内容 */
    private String content;
}
