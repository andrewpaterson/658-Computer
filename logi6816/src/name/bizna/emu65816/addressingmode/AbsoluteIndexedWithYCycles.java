package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithY;

public class AbsoluteIndexedWithYCycles
    extends InstructionCycles
{
  //7
  public AbsoluteIndexedWithYCycles(boolean read)
  {
    super(AbsoluteIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), new NoteFourY(read)),
          new BusCycle(Address(DBR(), AA(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

