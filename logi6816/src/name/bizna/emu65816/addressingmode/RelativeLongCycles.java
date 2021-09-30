package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.RelativeLong;

public class RelativeLongCycles
    extends InstructionCycles
{
  public RelativeLongCycles()
  {
    super(RelativeLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new FetchRelativeOffsetLow(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new FetchRelativeOffsetHigh()),
          new BusCycle(Address(PBR(), PC()), new Execute1()));
  }
}

