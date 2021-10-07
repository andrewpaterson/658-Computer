package name.bizna.bus.common;

public class Single
{
  protected boolean value;

  public Single()
  {
    this.value = false;
  }

  public boolean get()
  {
    return value;
  }

  public void set(boolean value)
  {
    this.value = value;
  }
}

