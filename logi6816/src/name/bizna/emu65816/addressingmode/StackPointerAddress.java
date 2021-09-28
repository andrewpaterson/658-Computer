package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;

public class StackPointerAddress
    extends BaseAddressCycle
{
  protected int offset;

  public StackPointerAddress(int offset)
  {
    if (offset > 1)
    {
      throw new EmulatorException("StackPointerAddress offsets may only be 1 or 0.");
    }
    this.offset = offset;
  }

  @Override
  public Address getAddress(Cpu65816 cpu)
  {
    return new Address(0x00, cpu.getStackPointer()).offset(offset, false);  //@todo - does this wrap;
  }
}

