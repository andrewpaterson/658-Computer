package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectRMWCycles
    extends InstructionCycles
{
  //10b
  public DirectRMWCycles()
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), o(1)), Read_DataHigh(RMW), new NoteOne()),
          new BusCycle(Address(DP(), D0(), o(1)), IO(RMW)),
          new BusCycle(Address(DP(), D0(), o(1)), ExecuteRMWHigh()),
          new BusCycle(Address(DP(), D0()), ExecuteRMWLow_Done()));
  }
}

