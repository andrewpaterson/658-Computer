package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Relative;

public class RelativeCycles
    extends InstructionCycles
{
  public RelativeCycles()
  {
    super(Relative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), new FetchRelativeOffsetLow()),
          new BusCycle(Address(PBR(), PC()), new Execute1()),
          new BusCycle(Address(PBR(), PC()), new NoteSix(true)));
  }
}

