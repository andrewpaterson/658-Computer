package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImpliedXBACycles
    extends InstructionCycles
{
  public ImpliedXBACycles()
  {
    super(Immediate,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(true)));
  }
}

