package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXRMWCycles
    extends InstructionCycles
{
  //6b
  public AbsoluteIndexedWithXRMWCycles()
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_pp()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(false)),
          new BusCycle(Address(DBR(), AA(), X()), Read_DataLow(false)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Read_DataHigh(false)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), IO(false)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), new Execute2(), new WriteDataHigh(false)),
          new BusCycle(Address(DBR(), AA(), X()), new Execute1(), new WriteDataLow(false)));
  }
}

