package animores.serverapi.to_do.entity.vo;


import animores.serverapi.pet.type.Tag;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;

import java.util.List;


public record ToDoVo(
Long id,
boolean isAllDay,
String content,
Tag tag,
String color,
boolean isUsingAlarm,
List<PetToDoRelationship>petToDoRelationships
) {
    public static ToDoVo fromToDo(ToDo toDo) {
        return new ToDoVo(toDo.getId(),
                toDo.isAllDay(),
                toDo.getContent(),
                toDo.getTag(),
                toDo.getColor(),
                toDo.isUsingAlarm(),
                toDo.getPetToDoRelationships());
    }
}
