package animores.serverapi.to_do.entity.vo;

import animores.serverapi.profile.domain.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ToDoInstanceVo(
        Long id,
        ToDoVo toDo,
        LocalDate date,
        LocalTime time,
        Profile completeProfile,
        LocalDateTime completeTime
) {
}
