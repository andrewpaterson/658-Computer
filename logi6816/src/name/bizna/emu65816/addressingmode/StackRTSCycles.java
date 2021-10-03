package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackRTSCycles
    extends InstructionCycles
{
  //22h
  public StackRTSCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPCL(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPCH()),
          new BusCycle(Address(S()), IO(), PC_e(PBR(), New_PC()), DONE()));
  }
}

