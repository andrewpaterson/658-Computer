package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongCycles
    extends InstructionCycles
{
  public AbsoluteLongCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressBank(true)),
          new BusCycle(new AddressBank(), new AbsoluteAddress(), new ExecuteLow(read, true)),
          new BusCycle(new AddressBank(), new AbsoluteAddress(), new Offset(1), new ExecuteHigh(read, true)));
  }
}

