package net.assembler.sixteenhigh.semanticiser.statement.block;

import net.assembler.sixteenhigh.semanticiser.RoutineDefinition;

public class RoutineBlock
    extends Block
{
  protected RoutineDefinition routine;

  public RoutineBlock(RoutineDefinition routine)
  {
    this.routine = routine;
  }
}

