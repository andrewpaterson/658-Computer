package net.simulation.memory;

import net.common.Snapshot;

public class MemorySnapshot
    implements Snapshot
{
  protected boolean propagateWroteMemory;
  protected long oldAddress;
  protected long oldValue;

  public MemorySnapshot()
  {
    propagateWroteMemory = false;
  }
}

