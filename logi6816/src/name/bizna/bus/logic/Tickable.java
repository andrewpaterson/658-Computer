package name.bizna.bus.logic;

public interface Tickable
{
  boolean propagate();

  default void donePropagation()
  {
  }
}

