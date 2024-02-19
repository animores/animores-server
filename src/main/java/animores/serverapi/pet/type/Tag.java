package animores.serverapi.pet.type;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Tag {

    산책,
    밥주기,
    목욕,
    미용;


    private static final Map<String,Tag> tagMap =
        Stream.of(Tag.values())
            .collect(Collectors.toMap(Tag::name, Function.identity()));


    @JsonCreator
    public static Tag fromString(String value) {
        return tagMap.get(value.toUpperCase());
    }
}
