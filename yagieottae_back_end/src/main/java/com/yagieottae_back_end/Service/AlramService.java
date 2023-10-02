package com.yagieottae_back_end.Service;

import com.yagieottae_back_end.Dto.AlramDto;
import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Entity.Alram;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.AlramRepository;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import com.yagieottae_back_end.Util.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlramService
{
    private final AlramRepository alramRepository;
    private final UserRepository userRepository;
    private final PillRepository pillRepository;
    private final ModelMapper modelMapper;

    //새로운 알람을 저장한다
    public ResponseDto saveAlram(AlramDto.Request alramDto)
    {
        ResponseDto returnValue;

        try
        {
            User user = SessionUtil.getUserFromDB();
            Alram existingAlram = new Alram();

            if (alramDto.getId() == 0) //알람 신규 등록시 알람 중복 검사
            {
                if (alramRepository
                        .findExsistingAlram(user.getId(), alramDto.getPillId())
                        .isPresent())
                {
                    throw new CustomBadRequestException("기존에 설정해둔 알람이 존재합니다. 알람은 약 하나당 한개씩만 설정 가능합니다.");
                }
            }
            else
            {
                existingAlram = alramRepository
                        .findById(alramDto.getId())
                        .orElseThrow(() -> new CustomBadRequestException("기존 알람 정보가 존재하지 않습니다!"));
            }

            Pill pill = pillRepository
                    .findById(alramDto.getPillId())
                    .orElseThrow(() -> new CustomBadRequestException("저장하시려는 약을 찾을 수 없습니다.")); //pill의 pk값으로 조회

            Alram alram = Alram.builder() //Alram 객체 생성
                    .id(alramDto.getId() == 0 ? 0 : existingAlram.getId())
                    .alramTime(alramDto.getAlramTime())
                    .days(alramDto.getDays())
                    .beforeMeal(alramDto.getBeforeMeal())
                    .dosingTime(alramDto.getDosingTime())
                    .regDate(alramDto.getId() == 0 ? new Date() : existingAlram.getRegDate())
                    .editDate(new Date())
                    .user(user)
                    .pill(pill)
                    .build();

            alramRepository.save(alram); //알람 저장

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message(String.format("%s의 알람이 저장되었습니다!", pill.getItemName()))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e) //Unhandled Exception
        {
            log.error("saveAlram Unhandled Exception occured", e);
            throw new CustomBadRequestException("알람 저장에 실패하였습니다!");
        }

        return returnValue;
    }

    //알람 목록을 가져온다
    public ResponseDto getAlrams(Boolean getToday)
    {
        ResponseDto returnValue;
        List<Alram> alramList;

        try
        {
            User user = SessionUtil.getUserFromDB();

            if (getToday) //오늘 날짜의 알람 목록들만 가져와야 한다면
            {
                LocalDate date = LocalDate.now(); //현재 시간
                int today = date
                        .getDayOfWeek()
                        .getValue(); //현재 시간의 요일 (1:월, 2:화,...,6:토,7:일)
                alramList = alramRepository
                        .findTodayAlrams(user.getId(), today)
                        .get();
            }
            else //현재 요일에 상관없이 알람 목록을 가져와야 한다면
            {
                alramList = alramRepository
                        .findAllAlrams(user.getId())
                        .get(); //전체 알람 목록을 DB에서 조회
            }

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("알람 조회 성공")
                    .body(JsonUtil.ObjectToJsonObject("alrams", alramList))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e) //Unhandled Exception
        {
            log.error("Unhandled Exception occured", e);
            throw new CustomBadRequestException("알람 조회에 실패하였습니다!");
        }
        return returnValue;
    }

    //알람 삭제
    public ResponseDto deleteAlram(Long alramId)
    {
        ResponseDto returnValue;

        try
        {
            if (alramRepository
                    .findById(alramId)
                    .isEmpty()) //삭제 대상 알람이 존재하지 않는다면
            {
                throw new CustomBadRequestException("삭제 대상 알람이 존재하지 않습니다.");
            }

            alramRepository.deleteById(alramId); //알람 삭제

            returnValue = ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("알람이 삭제되었습니다.")
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("deleteAlram Unhandled Exception occured", e);
            throw new CustomBadRequestException("알람 삭제에 실패하였습니다.");
        }

        return returnValue;
    }
}
