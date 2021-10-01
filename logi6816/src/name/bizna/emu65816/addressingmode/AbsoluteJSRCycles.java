package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJSRCycles
    extends InstructionCycles
{
  //1c
  public AbsoluteJSRCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), Write_PCH(), SP_dec()),
          new BusCycle(Address(S()), Write_PCL(), SP_dec(), PC_e(PBR(), New_PC()), DONE()));
  }
}

