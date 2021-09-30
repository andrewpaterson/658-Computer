package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXRMWCycles
    extends InstructionCycles
{
  public DirectIndexedWithXRMWCycles()
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new FetchDirectOffset(false), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new DirectPageLowZero(false)),
          new BusCycle(Address(PBR(), PC()), IO(false)),
          new BusCycle(Address(DirectPage(), D0(), X(), Read_DataLow(false)),
          new BusCycle(Address(DirectPage(), D0(), X(), o(1), Read_DataHigh(false)),
          new BusCycle(Address(DirectPage(), D0(), X(), o(1), IO(false)),
          new BusCycle(Address(DirectPage(), D0(), X(), o(1), new Execute2(false, false)),
          new BusCycle(Address(DirectPage(), D0(), X(), new Execute1()));
  }
}

