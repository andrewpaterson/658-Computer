package net.logicim.domain.common;

import java.util.ArrayList;
import java.util.List;

public class Tickables
{
  protected List<TickablePins> tickables;
  protected long tickCount;

  public Tickables()
  {
    this.tickables = new ArrayList<>();
    this.tickCount = 0;
  }

  public void add(TickablePins tickable)
  {
    tickables.add(tickable);
  }

  public void run()
  {
    tickCount++;
  }
}

