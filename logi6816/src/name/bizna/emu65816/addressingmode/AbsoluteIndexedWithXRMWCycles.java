package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXRMWCycles
    extends InstructionCycles
{
  //6b
  public AbsoluteIndexedWithXRMWCycles()
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(RMW)),
          new BusCycle(Address(DBR(), AA(), X()), Read_DataLow(RMW)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Read_DataHigh(RMW), new NoteOne()),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), IO(RMW)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), ExecuteRMWHigh()),
          new BusCycle(Address(DBR(), AA(), X()), ExecuteRMWLow_Done()));
  }
}

