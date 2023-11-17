package net.assembler.sixteenhigh.semanticiser.types;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeDefinition
{
  public List<Long> arrayMatrix;
  public int pointerCount;

  public TypeDefinition()
  {
    this.arrayMatrix = new ArrayList<>();
    this.pointerCount = 0;
  }
}

