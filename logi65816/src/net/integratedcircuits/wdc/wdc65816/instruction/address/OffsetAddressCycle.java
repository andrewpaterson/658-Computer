package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.util.EmulatorException;

public abstract class OffsetAddressCycle
    extends BaseAddressCycle
{
  protected final int offset;

  public OffsetAddressCycle(int offset)
  {
    if (offset > 1)
    {
      throw new EmulatorException(getClass().getSimpleName() + " offsets may only be 1 or 0.");
    }
    this.offset = offset;
  }
}

