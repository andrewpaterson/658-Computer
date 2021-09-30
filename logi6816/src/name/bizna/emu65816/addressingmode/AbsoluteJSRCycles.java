package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJSRCycles
    extends InstructionCycles
{
  //1c
  public AbsoluteJSRCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), Write_PCH(), SP_mm()),
          new BusCycle(Address(S()), Write_PCL(), SP_mm(), PC_e(PBR(), New_PC())));
  }
}

