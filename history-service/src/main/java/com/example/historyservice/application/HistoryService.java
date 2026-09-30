package com.example.historyservice.application;

import com.example.common.ErrorCode;
import com.example.common.Snowflake;
import com.example.common.exception.BusinessException;
import com.example.historyservice.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final Snowflake snowflake;

    public List<HistoryResponse> getHistoryByMemberId(Long memberId) {
        return historyRepository.findAllByMemberId(memberId).stream()
                .map(HistoryResponse::from)
                .toList();
    }

    @Transactional
    public long deleteHistoryByMemberId(Long memberId) {
        return historyRepository.deleteByMemberId(memberId);
    }

    @Transactional
    public void saveHistory(Integer itemId, Long memberId) {
        try {
            historyRepository.saveAndFlush(History.create(itemId, memberId, snowflake.nextId()));
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(ErrorCode.HISTORY_ALREADY_EXISTS, exception);
        }
    }
}
