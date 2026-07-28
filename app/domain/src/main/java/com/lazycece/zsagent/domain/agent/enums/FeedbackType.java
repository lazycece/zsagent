package com.lazycece.zsagent.domain.agent.enums;

import com.lazycece.rapidf.domain.model.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 反馈类型枚举
 *
 * @author lazycece
 */
@Getter
@AllArgsConstructor
public enum FeedbackType implements BaseEnum<String> {

    USEFUL("useful", "有用"),
    NOT_USEFUL("not_useful", "无用"),
    ;

    private final String code;
    private final String desc;
}
