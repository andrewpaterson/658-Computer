package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteRMWCycles
    extends InstructionCycles
{
  //1d
  public AbsoluteRMWCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_inc()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(RMW), new NoteOne()),
          new BusCycle(Address(DBR(), AA(), o(1)), IO(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteRMWHigh()),
          new BusCycle(Address(DBR(), AA()), ExecuteRMWLow_Done()));
  }
}

