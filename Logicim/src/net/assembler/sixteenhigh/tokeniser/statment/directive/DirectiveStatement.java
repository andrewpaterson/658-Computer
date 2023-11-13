package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.Statement;

public abstract class DirectiveStatement
    extends Statement
{
  public DirectiveStatement(Statements statements, int index)
  {
    super(statements, index);
  }

  @Override
  public boolean isDirective()
  {
    return true;
  }
}

