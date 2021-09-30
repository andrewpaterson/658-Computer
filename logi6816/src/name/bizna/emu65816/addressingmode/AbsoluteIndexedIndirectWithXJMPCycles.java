package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedIndirectWithX;

public class AbsoluteIndexedIndirectWithXJMPCycles
    extends InstructionCycles
{
  //2a
  public AbsoluteIndexedIndirectWithXJMPCycles()
  {
    super(AbsoluteIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), AA(), X()), Read_NewPCL()),
          new BusCycle(Address(PBR(), AA(), X(), o(1)), Read_NewPCH(), new SetProgramCounter(PBR(), New_PC())));
  }
}

