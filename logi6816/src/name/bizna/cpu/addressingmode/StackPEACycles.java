package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.StackImmediate;

public class StackPEACycles
    extends InstructionCycles
{
  //22d
  public StackPEACycles()
  {
    super(StackImmediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH()),
          new BusCycle(Address(S()), Write_AAH(), SP_dec()),
          new BusCycle(Address(S()), Write_AAL(), SP_dec(), DONE()));
  }
}

