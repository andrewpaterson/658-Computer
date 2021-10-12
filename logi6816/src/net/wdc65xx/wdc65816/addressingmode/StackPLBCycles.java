package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

public class StackPLBCycles
    extends InstructionCycles
{
  //22b
  public StackPLBCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), SP_inc(), Read_DataLow(), E(Cpu65816::PLB), DONE()));
  }
}

