package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteRMWCycles
    extends InstructionCycles
{
  //1d
  public AbsoluteRMWCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_pp()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), IO(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteHigh(false, RMW)),
          new BusCycle(Address(DBR(), AA()), ExecuteLow(false, RMW)));
  }
}

