package animores.serverapi.to_do.entity.vo;

import animores.serverapi.profile.domain.vo.ProfileVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ToDoInstanceVo(
        Long id,
        ToDoVo toDo,
        LocalDate date,
        LocalTime time,
        ProfileVo completeProfile,
        LocalDateTime completeTime
) {
}
