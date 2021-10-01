package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongJMPCycles
    extends InstructionCycles
{
  //4b
  public AbsoluteLongJMPCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPBR(), PC_inc(), PC_e(PBR(), New_PC()), DONE()));
  }
}

