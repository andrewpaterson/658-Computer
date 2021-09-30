package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndirect;

public class AbsoluteIndirectJMPCycles
    extends InstructionCycles
{
  //3b
  public AbsoluteIndirectJMPCycles()
  {
    super(AbsoluteIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(AA()), Read_NewPCL()),
          new BusCycle(Address(AA(), o(1)), Read_NewPCH(), new SetProgramCounter(PBR(), New_PC())));
  }
}

