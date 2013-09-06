/*
 * Copyright (c) 2012.
 * Todos os direitos reservados à HR Gestão da Informação Ltda.
 * A cópia não autorizada, irá gerar punições previstas em lei.
 */

package org.brevleq.crud.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.util.List;
import java.util.Map;

public class StoredProcedureRunnerImpl implements StoredProcedureRunner {


    private List<String> argumentosDeEntrada;
    private List valoresDeEntrada;
    private String mensagem;
    private SimpleJdbcCall jdbcCall;
    private SqlParameterSource parametros;
    private Map parametrosSaida;

    public StoredProcedureRunnerImpl(SimpleJdbcCall jdbcCall) {
        this.jdbcCall = jdbcCall;
    }

    public boolean run(String storedProcedureName) {
        jdbcCall.withProcedureName(storedProcedureName);
        parametros = carregarParametros();
        parametrosSaida = jdbcCall.execute(parametros);
        mensagem = recuperarMensagem();
        Integer resultado = getResult();
        if (resultado != null)
            return resultado != 1;
        else
            return true;
    }

    private String recuperarMensagem() {
        if (parametrosSaida.containsKey("MSGERRO"))
            return (String) parametrosSaida.get("MSGERRO");
        else
            return "";
    }

    public Integer getResult() {
        if (parametrosSaida.containsKey("RESULTADO"))
            return (Integer) parametrosSaida.get("RESULTADO");
        else
            return 0;
    }

    private SqlParameterSource carregarParametros() {
        MapSqlParameterSource mapa = new MapSqlParameterSource();
        for (int indice = 0; indice < valoresDeEntrada.size(); indice++) {
            mapa.addValue(argumentosDeEntrada.get(indice), valoresDeEntrada.get(indice));
        }
        return mapa;
    }

    public void setInArguments(List<String> inArguments) {
        this.argumentosDeEntrada = inArguments;
    }

    @SuppressWarnings("rawtypes")
    public void setInValues(List inValues) {
        this.valoresDeEntrada = inValues;
    }

    public String getMessage() {
        return mensagem;
    }

    @Override
    public Object getOutValue(String key) {
        return parametrosSaida.get(key);
    }
}
