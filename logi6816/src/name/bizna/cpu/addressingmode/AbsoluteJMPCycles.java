package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.Absolute;

public class AbsoluteJMPCycles
    extends InstructionCycles
{
  //1b
  public AbsoluteJMPCycles()
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_NewPCH(), PC_inc(), PC_e(PBR(), New_PC()), DONE()));
  }
}

