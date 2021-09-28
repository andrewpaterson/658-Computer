package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPageIndirectLong;

public class DirectPageIndirectLongCycles
    extends InstructionCycles
{
  public DirectPageIndirectLongCycles(boolean read)
  {
    super(DirectPageIndirectLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true)),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPageAddress(0), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPageAddress(0), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DirectPageAddress(0), new FetchAbsoluteAddressBank(true)),
          new BusCycle(new DirectPageAddress(0), new ExecuteLow(true, read)),
          new BusCycle(new DirectPageAddress(1), new ExecuteHigh(true, read)));
  }
}

