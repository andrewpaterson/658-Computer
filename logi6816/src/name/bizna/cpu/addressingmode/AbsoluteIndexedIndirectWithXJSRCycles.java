package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.AbsoluteIndexedIndirectWithX;

public class AbsoluteIndexedIndirectWithXJSRCycles
    extends InstructionCycles
{
  //2b
  public AbsoluteIndexedIndirectWithXJSRCycles()
  {
    super(AbsoluteIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(S()), Write_PCH(), SP_dec()),
          new BusCycle(Address(S()), Write_PCL(), SP_dec()),
          new BusCycle(Address(PBR(), PC()), Read_AAH()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), AA(), X()), Read_NewPCL()),
          new BusCycle(Address(PBR(), AA(), X(), o(1)), Read_NewPCH(), PC_e(PBR(), New_PC()), DONE()));
  }
}

