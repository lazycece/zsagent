package com.lazycece.zsagent.domain.agent.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对话状态枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum ConversationStatus implements BaseEnum<String> {

    ACTIVE("active", "活跃"),
    ARCHIVED("archived", "已归档"),
    ;

    private final String code;
    private final String desc;
}
