package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectCycles
    extends InstructionCycles
{
  public DirectCycles(boolean read)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(DirectPage(), D0()), ExecuteLow(read, true)),
          new BusCycle(Address(DirectPage(), D0(), o(1)), ExecuteHigh(read, true)));
  }
}

