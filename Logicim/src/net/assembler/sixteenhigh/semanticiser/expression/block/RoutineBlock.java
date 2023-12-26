package net.assembler.sixteenhigh.semanticiser.expression.block;

import net.assembler.sixteenhigh.semanticiser.RoutineDefinition;

public class RoutineBlock
    extends Block
{
  protected RoutineDefinition definition;

  public RoutineBlock(RoutineDefinition definition)
  {
    this.definition = definition;
  }
}

