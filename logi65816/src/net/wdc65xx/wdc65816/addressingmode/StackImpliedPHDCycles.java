package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

public class StackImpliedPHDCycles
    extends InstructionCycles
{
  //22c
  public StackImpliedPHDCycles(Executor<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), E(consumer), Write_DataHigh(), SP_dec()),
          new BusCycle(Address(S()), Write_DataLow(), SP_dec(), DONE()));
  }
}

