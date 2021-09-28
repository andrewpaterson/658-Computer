package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPageIndirectIndexedWithY;

public class DirectPageIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public DirectPageIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectPageIndirectIndexedWithY,
          new BusCycle(new ProgramCounterAddress(0), new OpCodeData()),
          new BusCycle(new ProgramCounterAddress(1), new DirectPageOffset(true)),
          new BusCycle(new ProgramCounterAddress(1), new DirectPageLowNotZero(true)),
          new BusCycle(new DirectPageAddress(0), new AbsoluteAddressLow(true)),
          new BusCycle(new DirectPageAddress(1), new AbsoluteAddressHigh(true)),
          new BusCycle(new DirectPageAddressPlusYLow(), new InternalIgnored(true)),
          new BusCycle(new DirectPageAddressPlusY(0), new ExecuteLow(true, read)),
          new BusCycle(new DirectPageAddressPlusY(1), new ExecuteHigh(true, read)));
  }
}
