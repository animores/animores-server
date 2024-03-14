package animores.serverapi.to_do.entity.vo;


import animores.serverapi.pet.type.Tag;

import java.time.LocalDate;
import java.time.LocalTime;

public record ToDoVo(
Long id,
LocalDate date,
LocalTime time,
boolean isAllDay,
String content,
Tag tag,
String color,
boolean isUsingAlarm
) {

}
