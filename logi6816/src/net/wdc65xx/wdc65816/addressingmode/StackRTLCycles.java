package net.wdc65xx.wdc65816.addressingmode;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

public class StackRTLCycles
    extends InstructionCycles
{
  //22i
  public StackRTLCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_NewPCL(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_NewPCH(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_NewPBR(), SP_inc(), PC_e(New_PBR(), New_PC()), DONE()));
  }
}

