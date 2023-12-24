package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.statment.TokenStatement;

public abstract class DirectiveTokenStatement
    extends TokenStatement
{
  public DirectiveTokenStatement(TokenUnit statements, int index)
  {
    super(statements, index);
  }

  @Override
  public boolean isDirective()
  {
    return true;
  }
}

