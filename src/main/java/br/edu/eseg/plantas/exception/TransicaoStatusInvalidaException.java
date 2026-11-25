package br.edu.eseg.plantas.exception;

public class TransicaoStatusInvalidaException extends RegraNegocioException {
    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }
}
