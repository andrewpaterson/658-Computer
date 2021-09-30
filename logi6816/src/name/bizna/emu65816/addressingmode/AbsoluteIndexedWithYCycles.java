package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithY;

public class AbsoluteIndexedWithYCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYCycles(boolean read)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), new NoteFour(true, read)),
          new BusCycle(Address(DBR(), AA(), Y()), ExecuteLow(read, true)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), ExecuteHigh(read, true)));
  }

}

