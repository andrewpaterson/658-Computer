package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackRelativeCycles
    extends InstructionCycles
{

  public StackRelativeCycles(boolean read)
  {
    super(StackRelative,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchStackPointerOffset(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new StackPointer(), new StackOffset(), new ExecuteLow(true, read)),
          new BusCycle(new StackPointer(), new StackOffset(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

