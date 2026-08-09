package com.lazycece.zsagent.infra.acl.repository;

import com.lazycece.rapidf.domain.model.Pagination;
import com.lazycece.zsagent.domain.agent.model.AgentConversation;
import com.lazycece.zsagent.domain.agent.repository.AgentConversationRepository;
import com.lazycece.zsagent.infra.acl.converter.AgentInfraConverter;
import com.lazycece.zsagent.infra.dal.mapper.udf.AgentConversationUdfMapper;
import com.lazycece.zsagent.infra.dal.mapper.udf.AgentMessageUdfMapper;
import com.lazycece.zsagent.infra.dal.po.AgentConversationPO;
import com.lazycece.zsagent.infra.dal.po.AgentMessagePO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对话仓储 MyBatis 实现。
 *
 * @author lazycece
 */
@Repository
public class AgentConversationRepositoryImpl implements AgentConversationRepository {

    private static final Logger log = LoggerFactory.getLogger(AgentConversationRepositoryImpl.class);

    private final AgentConversationUdfMapper conversationMapper;
    private final AgentMessageUdfMapper messageMapper;

    public AgentConversationRepositoryImpl(
            AgentConversationUdfMapper conversationMapper,
            AgentMessageUdfMapper messageMapper) {
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(AgentConversation conversation) {
        AgentConversationPO convPO = AgentInfraConverter.toConversationPO(conversation);
        conversationMapper.insert(convPO);

        if (conversation.getMessages() != null && !conversation.getMessages().isEmpty()) {
            List<AgentMessagePO> messagePOs = conversation.getMessages().stream()
                    .map(AgentInfraConverter::toMessagePO)
                    .collect(Collectors.toList());
            messageMapper.insertBatch(messagePOs);
        }

        log.debug("新建对话: conversationId={}, 消息数={}",
                conversation.getConversationId(),
                conversation.getMessages() != null ? conversation.getMessages().size() : 0);
        return conversation.getConversationId();
    }

    @Override
    public AgentConversation findByConversationId(String userId, String conversationId) {
        AgentConversationPO convPO = conversationMapper
                .selectByUserIdAndConversationId(userId, conversationId);
        if (convPO == null) {
            return null;
        }
        List<AgentMessagePO> messagePOs = messageMapper
                .selectByConversationId(conversationId);
        return AgentInfraConverter.toConversation(convPO, messagePOs);
    }

    @Override
    public List<AgentConversation> findByUserId(String userId, Pagination pagination) {
        long total = conversationMapper.countByUserId(userId);
        pagination.setCount(total);

        int offset = (pagination.getPage() - 1) * pagination.getSize();
        List<AgentConversationPO> convPOs = conversationMapper
                .selectByUserId(userId, offset, pagination.getSize());

        return convPOs.stream()
                .map(po -> AgentInfraConverter.toConversation(po, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AgentConversation conversation) {
        // 更新对话记录
        AgentConversationPO convPO = AgentInfraConverter.toConversationPO(conversation);
        conversationMapper.update(convPO);

        // 消息处理：逻辑删除旧消息，批量插入当前消息列表
        if (conversation.getMessages() != null) {
            messageMapper.deleteByConversationId(conversation.getConversationId());
            if (!conversation.getMessages().isEmpty()) {
                List<AgentMessagePO> messagePOs = conversation.getMessages().stream()
                        .map(AgentInfraConverter::toMessagePO)
                        .collect(Collectors.toList());
                messageMapper.insertBatch(messagePOs);
            }
        }

        log.debug("更新对话: conversationId={}, 消息数={}",
                conversation.getConversationId(),
                conversation.getMessages() != null ? conversation.getMessages().size() : 0);
    }
}
