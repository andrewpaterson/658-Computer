package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.RelativeLong;

public class RelativeLongCycles
    extends InstructionCycles
{
  //21
  public RelativeLongCycles()
  {
    super(RelativeLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_RL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_RH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_e(PBR(), PC(), R()), DONE()));
  }
}

