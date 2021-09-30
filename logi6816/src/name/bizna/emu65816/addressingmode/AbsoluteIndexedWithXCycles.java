package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXCycles(boolean read)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), new NoteFour(true, read)),
          new BusCycle(Address(DBR(), AA(), X()), ExecuteLow(read, true)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), ExecuteHigh(read, true)));
  }
}

