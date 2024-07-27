package net.logicim.domain.common.state;

public class Stateless
    extends State
{
  public Stateless()
  {
    super();
  }

  @Override
  public boolean isStateless()
  {
    return true;
  }
}

