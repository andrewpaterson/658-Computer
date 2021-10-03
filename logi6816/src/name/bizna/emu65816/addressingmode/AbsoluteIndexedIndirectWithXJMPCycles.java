package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedIndirectWithX;

public class AbsoluteIndexedIndirectWithXJMPCycles
    extends InstructionCycles
{
  //2a
  public AbsoluteIndexedIndirectWithXJMPCycles()
  {
    super(AbsoluteIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), AA(), X()), Read_NewPCL()),
          new BusCycle(Address(PBR(), AA(), X(), o(1)), Read_NewPCH(), PC_e(PBR(), New_PC()), DONE()));
  }
}

