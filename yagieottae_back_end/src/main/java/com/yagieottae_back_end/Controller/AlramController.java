package com.yagieottae_back_end.Controller;

import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Service.AlramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alram")
@RequiredArgsConstructor
public class AlramController
{
    private final AlramService alramService;

    //알람 저장
    @PostMapping("/save")
    public ResponseEntity<ResponseDto> saveAlram(@Valid @RequestBody AlramDto.Request alramDTO)
    {
        return ResponseEntity.ok(alramService.saveAlram(alramDTO));
    }

    //알람 목록을 가져온다
    @GetMapping("/getAlrams")
    public ResponseEntity<ResponseDto> getAlrams(@RequestParam Boolean getToday)
    {
        return ResponseEntity.ok(alramService.getAlrams(getToday));
    }

    /**
     * 기능: 알람을 삭제한다
     * 파라미터:
     * userId: 유저 아이디
     * alramID: 알람 아이디
     * 작성자: kor1du
     * 수정자: kor1du
     * 반환 값: 삭제 결과
     **/
    @DeleteMapping("/deleteAlram")
    public ResponseEntity<ResponseDto> deleteAlram(@RequestParam Long alramId)
    {
        return ResponseEntity.ok(alramService.deleteAlram(alramId));
    }
}
