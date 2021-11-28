package lclang.exceptions;

import lclang.Caller;

public class LCLangTypeErrorException extends LCLangRuntimeException {
    public LCLangTypeErrorException(String message, Caller caller) {
        super("Type Error", message, caller);
    }
}
