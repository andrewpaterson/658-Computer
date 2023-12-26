package net.assembler.sixteenhigh.semanticiser.expression;

import net.assembler.sixteenhigh.semanticiser.UnitDefinition;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;

public class UnitBlock
    extends Block
{
  private UnitDefinition definition;

  public UnitBlock(UnitDefinition definition)
  {
    this.definition = definition;
  }
}

