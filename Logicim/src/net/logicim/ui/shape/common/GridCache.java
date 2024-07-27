package net.logicim.ui.shape.common;

public class GridCache
{
  protected boolean valid;

  public GridCache()
  {
    valid = false;
  }

  public void invalidate()
  {
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }

  protected void update()
  {
    valid = true;
  }
}

