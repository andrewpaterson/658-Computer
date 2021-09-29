package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackInterruptSoftware;

public class WaitForInterruptCycles
    extends InstructionCycles
{
  public WaitForInterruptCycles()
  {
    super(StackInterruptSoftware,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true), new ExecuteLow(true, true)));
  }
}

