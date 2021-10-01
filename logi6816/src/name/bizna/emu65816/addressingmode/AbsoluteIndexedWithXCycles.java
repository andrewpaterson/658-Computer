package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXCycles(boolean read)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), new NoteFourX(read)),
          new BusCycle(Address(DBR(), AA(), X()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

