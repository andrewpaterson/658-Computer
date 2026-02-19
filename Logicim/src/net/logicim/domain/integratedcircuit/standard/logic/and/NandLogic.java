package net.logicim.domain.integratedcircuit.standard.logic.and;

public class NandLogic
    extends AndLogic
{
  protected boolean transformOutput(boolean value)
  {
    return !value;
  }
}

