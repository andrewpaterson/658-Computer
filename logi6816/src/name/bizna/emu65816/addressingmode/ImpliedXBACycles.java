package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImpliedCycles
    extends InstructionCycles
{
  public ImpliedCycles()
  {
    super(Immediate,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)));
  }
}

