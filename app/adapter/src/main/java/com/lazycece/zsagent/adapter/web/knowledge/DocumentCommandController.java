package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.api.DocumentCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DocumentDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRestoreRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentRollbackRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateContentRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUpdateMetadataRequest;
import com.lazycece.zsagent.facade.knowledge.request.DocumentUploadRequest;
import com.lazycece.zsagent.facade.knowledge.result.DocumentDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRestoreResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentRollbackResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateContentResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUpdateMetadataResult;
import com.lazycece.zsagent.facade.knowledge.result.DocumentUploadResult;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 文档命令控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/document")
public class DocumentCommandController {

    private final DocumentCommandFacade documentCommandFacade;

    public DocumentCommandController(DocumentCommandFacade documentCommandFacade) {
        this.documentCommandFacade = documentCommandFacade;
    }

    /**
     * 上传文档。
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespData<DocumentUploadResult> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam String userId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String directoryId,
            @RequestParam(required = false) List<String> tags,
            @RequestParam String visibility,
            @RequestParam(required = false) List<String> visibleTo) throws IOException {
        DocumentUploadRequest request = new DocumentUploadRequest();
        request.setUserId(userId);
        request.setTitle(title);
        request.setDirectoryId(directoryId);
        request.setTags(tags);
        request.setVisibility(visibility);
        request.setVisibleTo(visibleTo);
        request.setFileContent(file.getBytes());
        request.setOriginalFilename(file.getOriginalFilename());
        return documentCommandFacade.upload(request);
    }

    /**
     * 更新文档元数据（不产生新版本）。
     */
    @PostMapping("/update-metadata")
    public RespData<DocumentUpdateMetadataResult> updateMetadata(
            @Validated @RequestBody DocumentUpdateMetadataRequest request) {
        return documentCommandFacade.updateMetadata(request);
    }

    /**
     * 更新文档内容（产生新版本并触发 ETL）。
     */
    @PostMapping(value = "/update-content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespData<DocumentUpdateContentResult> updateContent(
            @RequestPart("file") MultipartFile file,
            @RequestParam String userId,
            @RequestParam String documentId,
            @RequestParam(required = false) String changeLog) throws IOException {
        DocumentUpdateContentRequest request = new DocumentUpdateContentRequest();
        request.setUserId(userId);
        request.setDocumentId(documentId);
        request.setChangeLog(changeLog);
        request.setFileContent(file.getBytes());
        request.setOriginalFilename(file.getOriginalFilename());
        return documentCommandFacade.updateContent(request);
    }

    /**
     * 删除文档（移入回收站）。
     */
    @PostMapping("/delete")
    public RespData<DocumentDeleteResult> delete(
            @Validated @RequestBody DocumentDeleteRequest request) {
        return documentCommandFacade.delete(request);
    }

    /**
     * 恢复文档。
     */
    @PostMapping("/restore")
    public RespData<DocumentRestoreResult> restore(
            @Validated @RequestBody DocumentRestoreRequest request) {
        return documentCommandFacade.restore(request);
    }

    /**
     * 回滚到指定历史版本。
     */
    @PostMapping("/rollback")
    public RespData<DocumentRollbackResult> rollback(
            @Validated @RequestBody DocumentRollbackRequest request) {
        return documentCommandFacade.rollback(request);
    }
}
