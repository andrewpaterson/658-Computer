package net.logicim.domain.common.state;

import net.logicim.data.common.SaveData;

public abstract class State
    extends SaveData
{
  public State()
  {
  }

  public boolean isStateless()
  {
    return false;
  }

  @Override
  public Object getObject()
  {
    return this;
  }
}

