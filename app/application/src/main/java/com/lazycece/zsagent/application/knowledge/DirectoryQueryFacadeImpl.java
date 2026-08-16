package com.lazycece.zsagent.application.knowledge;

import com.lazycece.rapidf.domain.anotation.ApplicationService;
import com.lazycece.rapidf.restful.response.RespData;
import com.lazycece.zsagent.application.knowledge.converter.DirectoryConverter;
import com.lazycece.zsagent.domain.knowledge.model.Directory;
import com.lazycece.zsagent.domain.knowledge.repository.DirectoryRepository;
import com.lazycece.zsagent.facade.knowledge.api.DirectoryQueryFacade;
import com.lazycece.zsagent.facade.knowledge.request.DirectoryChildrenQueryRequest;
import com.lazycece.zsagent.facade.knowledge.result.DirectoryListResult;

import java.util.List;

/**
 * 目录查询门面实现。
 *
 * @author lazycece
 */
@ApplicationService
public class DirectoryQueryFacadeImpl implements DirectoryQueryFacade {

    private final DirectoryRepository directoryRepository;

    public DirectoryQueryFacadeImpl(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    @Override
    public RespData<DirectoryListResult> listChildren(DirectoryChildrenQueryRequest request) {
        List<Directory> directories = directoryRepository.findByParentId(request.getParentId());
        DirectoryListResult result = new DirectoryListResult();
        result.setDirectories(DirectoryConverter.toDirectoryDTOList(directories));
        return RespData.success(result);
    }

    @Override
    public RespData<DirectoryListResult> tree() {
        List<Directory> directories = directoryRepository.findAll();
        DirectoryListResult result = new DirectoryListResult();
        result.setDirectories(DirectoryConverter.toDirectoryTree(directories));
        return RespData.success(result);
    }
}
