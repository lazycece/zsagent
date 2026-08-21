package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryCommandFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryCreateRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryDeleteRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryMoveRequest;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryRenameRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryCreateResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryDeleteResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryMoveResult;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryRenameResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 目录命令控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/directory")
public class DirectoryCommandController implements DirectoryCommandFacade{

    private final DirectoryCommandFacade directoryCommandFacade;

    public DirectoryCommandController(DirectoryCommandFacade directoryCommandFacade) {
        this.directoryCommandFacade = directoryCommandFacade;
    }

    /**
     * 创建目录。
     */
    @Override
    @PostMapping("/create")
    public RespData<DirectoryCreateResult> create(
            @Validated @RequestBody DirectoryCreateRequest request) {
        return directoryCommandFacade.create(request);
    }

    /**
     * 重命名目录。
     */
    @Override
    @PostMapping("/rename")
    public RespData<DirectoryRenameResult> rename(
            @Validated @RequestBody DirectoryRenameRequest request) {
        return directoryCommandFacade.rename(request);
    }

    /**
     * 移动目录。
     */
    @Override
    @PostMapping("/move")
    public RespData<DirectoryMoveResult> move(
            @Validated @RequestBody DirectoryMoveRequest request) {
        return directoryCommandFacade.move(request);
    }

    /**
     * 删除目录。
     */
    @Override
    @PostMapping("/delete")
    public RespData<DirectoryDeleteResult> delete(
            @Validated @RequestBody DirectoryDeleteRequest request) {
        return directoryCommandFacade.delete(request);
    }
}
