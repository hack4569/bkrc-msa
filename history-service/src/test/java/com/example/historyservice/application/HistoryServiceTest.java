package com.example.historyservice.application;

import com.example.common.ErrorCode;
import com.example.common.Snowflake;
import com.example.common.exception.BusinessException;
import com.example.historyservice.entity.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private Snowflake snowflake;

    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        historyService = new HistoryService(historyRepository, snowflake);
    }

    @Test
    void savesHistory() {
        given(snowflake.nextId()).willReturn(1L);
        given(historyRepository.saveAndFlush(any(History.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        assertThatCode(() -> historyService.saveHistory(123, 10L))
                .doesNotThrowAnyException();
    }

    @Test
    void duplicateHistoryReturnsConflictBusinessError() {
        given(snowflake.nextId()).willReturn(1L);
        given(historyRepository.saveAndFlush(any(History.class)))
                .willThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> historyService.saveHistory(123, 10L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.HISTORY_ALREADY_EXISTS);
    }
}
