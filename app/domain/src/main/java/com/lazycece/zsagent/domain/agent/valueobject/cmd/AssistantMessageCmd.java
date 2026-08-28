package com.lazycece.zsagent.domain.agent.valueobject.cmd;

import com.lazycece.zsagent.domain.agent.valueobject.SourceReference;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 记录助手回答命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class AssistantMessageCmd {

    /** 用户ID */
    private String userId;
    /** 对话ID */
    private String conversationId;
    /** 回答内容 */
    private String content;
    /** 来源引用列表 */
    private List<SourceReference> sources;
}
