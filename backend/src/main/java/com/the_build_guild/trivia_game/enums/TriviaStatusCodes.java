package com.the_build_guild.trivia_game.enums;

public enum TriviaStatusCodes {
    SUCCESS(0, "Returned results successfully."),
    NO_RESULTS(1, "Could not return results. The API doesn't have enough questions for your query."),
    INVALID_PARAMETER(2, "Contains an invalid parameter. Arguments passed in aren't valid."),
    TOKEN_NOT_FOUND(3, "Session Token does not exist."),
    TOKEN_EMPTY(4, "Session Token has returned all possible questions for the specified query. Resetting the Token is necessary."),
    RATE_LIMIT(5, "Too many requests have occurred. Each IP can only access the API once every 5 seconds.");

    private final int code;
    private final String message;

    //constructor 
    TriviaStatusCodes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public static TriviaStatusCodes fromCode(int code){
        for(TriviaStatusCodes statusCode : TriviaStatusCodes.values()){
            if(statusCode.getCode() == code){
                return statusCode;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
