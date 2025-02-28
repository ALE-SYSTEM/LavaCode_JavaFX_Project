package br.edu.ifsc.fln.exception;


public class ExceptionLavacao extends Exception {


    public ExceptionLavacao() {
    }


    public ExceptionLavacao(String msg) {
        super(msg);
    }

    public ExceptionLavacao(Exception cause) {
        super(cause);
    }

    public ExceptionLavacao(String message, Exception cause) {
        super(message, cause);
    }
}


