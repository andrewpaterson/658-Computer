package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectRMWCycles
    extends InstructionCycles
{
  public DirectRMWCycles()
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new FetchDirectOffset(false), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new DirectPageLowZero(false)),
          new BusCycle(Address(DirectPage(), D0(), Read_DataLow(false)),
          new BusCycle(Address(DirectPage(), D0(), o(1), Read_DataHigh(false)),
          new BusCycle(Address(DirectPage(), D0(), o(1), IO(false)),
          new BusCycle(Address(DirectPage(), D0(), o(1), new Execute2(), new WriteDataHigh(false)),
          new BusCycle(Address(DirectPage(), D0(), new Execute1(), new WriteDataLow(false)));
  }
}

