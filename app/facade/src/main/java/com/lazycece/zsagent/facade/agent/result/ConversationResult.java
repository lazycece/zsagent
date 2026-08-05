package com.lazycece.zsagent.facade.agent.result;

import com.lazycece.zsagent.facade.agent.dto.ConversationDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 对话查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class ConversationResult implements Serializable {

    /** 对话详情 */
    private ConversationDTO conversation;
}
