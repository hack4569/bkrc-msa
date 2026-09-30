package com.example.historyservice.application;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import com.example.common.security.GatewayMemberAuthenticationFilter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/history-service")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/v1/history")
    @ResponseStatus(HttpStatus.OK)
    public void saveHistory(
            @Parameter(hidden = true)
            @RequestHeader(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER) Long memberId,
            @RequestParam @NotNull(message = "itemId는 필수입니다.") Integer itemId
    ) {
        historyService.saveHistory(itemId, memberId);
    }

    @GetMapping("/internal/v1/histories/{memberId}")
    public List<HistoryResponse> getHistories(@PathVariable Long memberId) {
        return historyService.getHistoryByMemberId(memberId);
    }

    @DeleteMapping("/internal/v1/histories/{memberId}")
    public long deleteHistories(@PathVariable Long memberId) {
        return historyService.deleteHistoryByMemberId(memberId);
    }
}
