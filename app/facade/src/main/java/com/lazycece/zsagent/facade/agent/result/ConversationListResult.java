package com.lazycece.zsagent.facade.agent.result;

import com.lazycece.zsagent.facade.agent.dto.ConversationDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 对话列表查询结果
 *
 * @author lazycece
 */
@Getter
@Setter
public class ConversationListResult implements Serializable {

    /** 对话列表 */
    private List<ConversationDTO> list;

    /** 总数 */
    private Long total;

    /** 当前页码 */
    private Integer page;

    /** 每页大小 */
    private Integer size;
}
