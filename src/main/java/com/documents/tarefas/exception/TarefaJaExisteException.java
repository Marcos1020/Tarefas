package com.documents.tarefas.exception;

public class TarefaJaExisteException extends RuntimeException {
    
    public TarefaJaExisteException(String message) {
        super(message);
    }
    
    public TarefaJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}
