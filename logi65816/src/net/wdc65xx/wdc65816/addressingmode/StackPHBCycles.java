package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

public class StackPHBCycles
    extends InstructionCycles
{
  //22c
  public StackPHBCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(Cpu65816::PHB), Write_DataLow(), SP_dec(), DONE()));
  }
}

