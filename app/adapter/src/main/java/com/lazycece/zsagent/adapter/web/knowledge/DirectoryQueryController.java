package com.lazycece.zsagent.adapter.web.knowledge;

import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryQueryFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 目录查询控制器
 *
 * @author lazycece
 */
@RestController
@RequestMapping("/api/v1/directory")
public class DirectoryQueryController {

    private final DirectoryQueryFacade directoryQueryFacade;

    public DirectoryQueryController(DirectoryQueryFacade directoryQueryFacade) {
        this.directoryQueryFacade = directoryQueryFacade;
    }

    /**
     * 查询子目录列表。
     */
    @GetMapping("/list-children")
    public RespData<DirectoryListResult> listChildren(
            @Validated DirectoryChildrenQueryRequest request) {
        return directoryQueryFacade.listChildren(request);
    }

    /**
     * 查询完整目录树。
     */
    @GetMapping("/tree")
    public RespData<DirectoryListResult> tree() {
        return directoryQueryFacade.tree();
    }
}
