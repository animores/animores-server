package animores.serverapi.diary.entity;

import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;

public enum DiaryMediaType {
    I, V;

    public static DiaryMediaType checkType(String type) {
        return switch (type) {
            case "image/png", "image/jpeg" -> I;
            case "video/mp4" -> V;
            default -> throw new CustomException(ExceptionCode.UNSUPPORTED_TYPE);
        };
    }

}
