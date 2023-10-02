package com.yagieottae_back_end.Controller;

import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pill")
@RestController
@RequiredArgsConstructor
public class PillController
{
    private final PillService pillService;

    @GetMapping("/getPill")
    public ResponseEntity<ResponseDto> getPill(@RequestParam String itemName, Pageable page)
    {
        return ResponseEntity.ok(pillService.getPill(itemName, page));
    }
}