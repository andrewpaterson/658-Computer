package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.AbsoluteLong;

public class AbsoluteLongJSLCycles
    extends InstructionCycles
{
  //4c
  public AbsoluteLongJSLCycles()
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_inc()),
          new BusCycle(Address(S()), Write_PBR(), SP_dec()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(PBR(), PC()), Read_NewPBR(), PC_inc()),
          new BusCycle(Address(S()), Write_PCH(), SP_dec()),
          new BusCycle(Address(S()), Write_PCL(), SP_dec(), PC_e(PBR(), New_PC()), DONE()));
  }
}

