package net.logicim.domain.common;

import java.util.ArrayList;
import java.util.List;

public class Timeline
{
  protected List<Pins> tickables;
  protected long tickCount;

  public Timeline()
  {
    this.tickables = new ArrayList<>();
    this.tickCount = 0;
  }

  public void add(Pins tickable)
  {
    tickables.add(tickable);
  }

  public void run()
  {
    tickCount++;
  }
}

