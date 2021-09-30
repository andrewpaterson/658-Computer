package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongJSLCycles
    extends InstructionCycles
{
  //4c
  public AbsoluteLongJSLCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_pp()),
          new BusCycle(Address(S()), Write_PBR(), SP_mm()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(PBR(), PC()), Read_NewPBR(), PC_pp()),
          new BusCycle(Address(S()), Write_PCH(), SP_mm()),
          new BusCycle(Address(S()), Write_PCL(), SP_mm(), PC_e(PBR(), New_PC())));
  }
}

