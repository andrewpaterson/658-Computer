package net.assembler.sixteenhigh.semanticiser.expression;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

public class Variables
{
  protected Map<String, VariableDefinition> variables;
  protected VariableScope scope;
  protected String description;

  public Variables(VariableScope scope, String description)
  {
    this.variables =  new LinkedHashMap<>();
    this.scope = scope;
    this.description = description;
  }

  public VariableDefinition get(String name)
  {
    return variables.get(name);
  }

  public VariableDefinition create(String name)
  {
    VariableDefinition variable = get(name);
    if (variable != null)
    {
      throw new SimulatorException("%s Variable Definition [%s] already exists.", description, name);
    }
    variable = new VariableDefinition(name, scope);
    variables.put(name, variable);
    return variable;
  }
}

