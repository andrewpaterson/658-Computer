package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.semanticiser.expression.VariableDefinitions;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class VariableDefinition
{
  protected String name;
  protected Scope scope;
  protected TypeDefinition type;
  protected VariableDefinitions variables;
  protected boolean auto;

  public VariableDefinition(String name, Scope scope, TypeDefinition type, VariableDefinitions variables, boolean auto)
  {
    this.name = name;
    this.scope = scope;
    this.type = type;
    this.variables = variables;
    this.auto = auto;
  }

  public String getName()
  {
    return name;
  }

  public TypeDefinition getType()
  {
    return type;
  }

  public Scope getScope()
  {
    return scope;
  }

  public VariableDefinitions getVariables()
  {
    return variables;
  }

  public boolean isAuto()
  {
    return auto;
  }

  public String print()
  {
    return "(" + type.print() + ")" + name;
  }
}

