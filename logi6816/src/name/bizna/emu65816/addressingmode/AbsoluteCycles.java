package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  public ImmediateCycles()
  {
    super(Immediate,
          new BusCycle(new PCRelativeAddress(0), new OpCodeData()),
          new BusCycle(new PCRelativeAddress(1), new ReadImmediateDataLow()),
          new BusCycle(new PCRelativeAddress(2), new ReadImmediateDataHigh()));
  }
}

