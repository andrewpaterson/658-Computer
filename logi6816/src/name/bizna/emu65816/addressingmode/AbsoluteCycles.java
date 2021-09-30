package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteCycles
    extends InstructionCycles
{
  //1a
  public AbsoluteCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(DBR(), AA()), ExecuteLow(read, true)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteHigh(read, true)));
  }
}

