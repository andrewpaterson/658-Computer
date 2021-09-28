package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;

public class AbsoluteAddress
    extends AddressCycle
{
  private final boolean dataBank;
  private final int offset;

  public AbsoluteAddress(int offset)
  {
    this(true, offset);
  }

  public AbsoluteAddress(boolean dataBank, int offset)
  {
    if (offset > 1)
    {
      throw new EmulatorException("AbsoluteAddress offsets may only be 1 or 0.");
    }

    this.dataBank = dataBank;
    this.offset = offset;
  }

  @Override
  public Address getAddress(Cpu65816 cpu)
  {
    if (!dataBank)
    {
      return new Address(cpu.getAddress()).offset(offset, false);
    }
    else
    {
      return new Address(cpu.getDataBank(), cpu.getAddress().getOffset()).offset(offset, false);
    }
  }
}

