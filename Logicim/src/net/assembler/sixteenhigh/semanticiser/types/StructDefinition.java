package net.assembler.sixteenhigh.semanticiser.types;

import java.util.ArrayList;
import java.util.List;

public class StructDefinition
    extends TypeDefinition
{
  protected List<TypeDefinition> members;

  public StructDefinition()
  {
    this.members = new ArrayList<>();
  }
}

