package animores.serverapi.to_do.dao;

import animores.serverapi.profile.dao.ProfileVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ToDoInstanceDao(
        Long id,
        ToDoDao toDo,
        LocalDate date,
        LocalTime time,
        ProfileVo completeProfile,
        LocalDateTime completeTime
) {
}
