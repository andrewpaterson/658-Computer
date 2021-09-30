package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJMPCycles
    extends InstructionCycles
{
  //1b
  public AbsoluteJMPCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_pp(), new SetProgramCounter(PBR(), New_PC())));
  }
}

