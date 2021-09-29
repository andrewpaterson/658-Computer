package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectLong;

public class DirectIndirectLongCycles
    extends InstructionCycles
{
  public DirectIndirectLongCycles(boolean read)
  {
    super(DirectIndirectLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(2), new FetchAbsoluteAddressBank(true)),
          new BusCycle(new AddressBank(), new AbsoluteAddress(), new ExecuteLow(true, read)),
          new BusCycle(new AddressBank(), new AbsoluteAddress(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

