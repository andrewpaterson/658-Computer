package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.AbsoluteLong;

public class AbsoluteLongJMLCycles
    extends InstructionCycles
{
  //4b
  public AbsoluteLongJMLCycles()
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPBR(), PC_inc(), PC_e(PBR(), New_PC()), DONE()));
  }
}

