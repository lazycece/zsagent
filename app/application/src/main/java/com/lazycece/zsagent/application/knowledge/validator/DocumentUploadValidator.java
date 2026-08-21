package com.lazycece.zsagent.application.knowledge.validator;

import com.lazycece.rapidf.restful.Assert;
import com.lazycece.rapidf.restful.response.RespStatus;
import com.lazycece.rapidf.utils.EnumUtils;
import com.lazycece.zsagent.domain.knowledge.enums.Visibility;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUploadRequest;

/**
 * 文档上传校验器。
 *
 * @author lazycece
 */
public final class DocumentUploadValidator {

    private DocumentUploadValidator() {
    }

    /**
     * 校验上传请求。
     */
    public static void validate(DocumentUploadRequest request) {
        Assert.notNull(request, RespStatus.PARAM_ERROR, "request 不能为 null");
        Assert.notBlank(request.getFilePath(), RespStatus.PARAM_ERROR, "filePath不能为空");

        Visibility visibility = EnumUtils.getEnum(Visibility.class, request.getVisibility());
        Assert.notNull(visibility, RespStatus.PARAM_ERROR, "非法的可见范围: {}", request.getVisibility());
        if (visibility == Visibility.DEPARTMENT || visibility == Visibility.SPECIFIC) {
            Assert.notEmpty(request.getVisibleTo(), RespStatus.PARAM_ERROR,
                    "指定部门/人员可见时必须提供 visibleTo");
        }
    }
}
