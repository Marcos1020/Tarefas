package com.documents.tarefas.exception;

public class TarefaNaoEncontradaException extends RuntimeException {
    
    public TarefaNaoEncontradaException(String message) {
        super(message);
    }
    
    public TarefaNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
