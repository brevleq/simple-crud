package org.brevleq.crud.dao;

import java.util.List;

public interface StoredProcedureRunner {

    public boolean run(String storedProcedureName);

    public void setInArguments(List<String> inArguments);

    public void setInValues(List inValues);

    public String getMessage();

    public Object getOutValue(String key);

    public Integer getResult();

}
