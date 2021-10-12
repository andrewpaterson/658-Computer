package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Stack;

public class StackPLDCycles
    extends InstructionCycles
{
  //22b
  public StackPLDCycles(Executor<Cpu65816> consumer)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc()),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), E(consumer), DONE()));
  }
}

