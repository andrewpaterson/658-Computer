package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXRMWCycles
    extends InstructionCycles
{
  //16b
  public DirectIndexedWithXRMWCycles()
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(RMW), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO(RMW)),
          new BusCycle(Address(DP(), D0(), X()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_DataHigh(RMW), new NoteOne()),
          new BusCycle(Address(DP(), D0(), X(), o(1)), IO(RMW)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), ExecuteRMWHigh()),
          new BusCycle(Address(DP(), D0(), X()), ExecuteRMWLow_Done()));
  }
}

