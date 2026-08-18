package com.template.validator;

public interface Validador <T>{
    boolean validar(T valor);
    String getMessagemError();
    T getValor();
}
