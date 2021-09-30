package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXCycles
    extends InstructionCycles
{
  public AbsoluteIndexedWithXCycles(boolean read)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteAddressHigh(), new AbsoluteAddressLowPlusX(), new NoteFour(true, read)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new XIndex(), new ExecuteLow(read, true)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new XIndex(), new Offset(1), new ExecuteHigh(read, true)));
  }
}

