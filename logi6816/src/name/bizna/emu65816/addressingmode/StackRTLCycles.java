package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackRTLCycles
    extends InstructionCycles
{
  //22i
  public StackRTLCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPCL(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPCH(), SP_inc()),
          new BusCycle(Address(S()), Read_NewPBR(), PC_e(New_PBR(), New_PC()), DONE()));
  }
}

