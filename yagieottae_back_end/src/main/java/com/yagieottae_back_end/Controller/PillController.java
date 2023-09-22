package com.yagieottae_back_end.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Service.PillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pill")
@RestController
@RequiredArgsConstructor
public class PillController
{
    private final PillService pillService;

    @GetMapping("/findPill")
    public ResponseEntity<ResponseDto> findPill(@RequestParam String itemName)
    {
        return ResponseEntity.ok(pillService.findPill(itemName));
    }
}
