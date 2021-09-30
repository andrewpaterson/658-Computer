package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.*;

public class AbsoluteIndirectJMLCycles
    extends InstructionCycles
{
  //3a
  public AbsoluteIndirectJMLCycles()
  {
    super(AbsoluteIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(AA()), Read_NewPCL()),
          new BusCycle(Address(AA(), o(1)), Read_NewPCH()),
          new BusCycle(Address(AA(), o(2)), Read_NewPBR(), new SetProgramCounter(New_PBR(), New_PC())));
  }
}

