package animores.serverapi.to_do.dao;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GetTodosPetDao {

    Long id;
    String name;

    @QueryProjection
    public GetTodosPetDao(
        Long id,
        String name
    ) {
        this.id = id;
        this.name = name;
    }
}
