package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;
import static name.bizna.emu65816.AddressingMode.Immediate;

public class AbsoluteCycles
    extends InstructionCycles
{
  public AbsoluteCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(new PCRelativeAddress(0), new OpCodeData()),
          new BusCycle(new PCRelativeAddress(1), new AbsoluteAddressLow()),
          new BusCycle(new PCRelativeAddress(2), new AbsoluteAddressHigh()),
          new BusCycle(new AbsoluteAddress(0), new ExecuteLow(read, true)),
          new BusCycle(new AbsoluteAddress(1), new ExecuteHigh(read, true)));
  }
}

