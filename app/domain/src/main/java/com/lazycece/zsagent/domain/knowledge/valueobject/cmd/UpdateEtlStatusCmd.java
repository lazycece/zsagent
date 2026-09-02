/*
 *    Copyright (C) 2026 lazycece<lazycece@gmail.com>. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lazycece.zsagent.domain.knowledge.valueobject.cmd;

import com.lazycece.zsagent.domain.knowledge.enums.EtlStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 更新 ETL 状态命令
 *
 * @author lazycece
 */
@Getter
@Setter
public class UpdateEtlStatusCmd {

    /** 文档ID */
    private String documentId;
    /** ETL 状态 */
    private EtlStatus status;
    /** 错误信息（仅 FAILED 时有值） */
    private String errorMessage;

    public static UpdateEtlStatusCmd build(
            String documentId, EtlStatus status, String errorMessage) {
        UpdateEtlStatusCmd cmd = new UpdateEtlStatusCmd();
        cmd.setDocumentId(documentId);
        cmd.setStatus(status);
        cmd.setErrorMessage(errorMessage);
        return cmd;
    }
}
