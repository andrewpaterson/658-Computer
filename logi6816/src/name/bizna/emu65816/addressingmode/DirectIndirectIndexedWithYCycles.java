package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class DirectIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public DirectIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteHighOffset(), new AbsoluteLowPlusYOffset(), new NoteFour(true, read)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new YIndex(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new YIndex(), new Offset(1), new ExecuteLow(true, read)));
  }
}

