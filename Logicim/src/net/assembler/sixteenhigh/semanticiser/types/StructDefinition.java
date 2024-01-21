package net.assembler.sixteenhigh.semanticiser.types;

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
}

