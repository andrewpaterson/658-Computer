package net.assembler.sixteenhigh.semanticiser.types;

import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;
import net.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

public class StructDefinition
    extends TypeDefinition
{
  protected List<TypeDefinition> members;
  protected String name;

  public StructDefinition(String name)
  {
    super();

    this.members = new ArrayList<>();
    this.name = name;
  }

  public List<TypeDefinition> getMembers()
  {
    return members;
  }

  public String getName()
  {
    return name;
  }

  @Override
  public PrimitiveTypeCode getType()
  {
    throw new SimulatorException("StructDefinition cannot answer getType().");
  }

  @Override
  public String print()
  {
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    for (TypeDefinition member : members)
    {
      if (!first)
      {
        builder.append('.');
      }
      else
      {
        first = false;
      }
      builder.append(member.print());
    }
    return builder.toString();
  }
}

