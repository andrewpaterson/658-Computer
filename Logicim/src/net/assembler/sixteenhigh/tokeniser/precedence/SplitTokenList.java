package net.assembler.sixteenhigh.tokeniser.precedence;

import net.assembler.sixteenhigh.tokeniser.statment.expression.TokenExpression;

import java.util.ArrayList;
import java.util.List;

public class SplitTokenList
{
  protected List<TokenExpression> left;
  protected List<TokenExpression> right;

  public SplitTokenList(List<TokenExpression> expressions, int splitIndex)
  {
    left = new ArrayList<>();
    right = new ArrayList<>();

    for (int i = 0; i < expressions.size(); i++)
    {
      TokenExpression expression = expressions.get(i);
      if (i < splitIndex)
      {
        left.add(expression);
      }
      else if (i > splitIndex)
      {
        right.add(expression);
      }
    }
  }
}

