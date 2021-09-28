package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.EmulatorException;

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

