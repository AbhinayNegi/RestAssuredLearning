package core;

public enum StatusCodeEnum {

    SUCCESS(200, "The request was successful"),
    CREATED(201, "A new resource was created"),
    NOT_FOUND(404, "Cannot find request resources");

    public final int code;
    public final String msg;

    StatusCodeEnum(int code, String msg) {

        this.code = code;
        this.msg = msg;
    }

}
