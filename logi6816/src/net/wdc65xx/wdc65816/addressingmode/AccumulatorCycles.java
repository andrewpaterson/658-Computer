package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Accumulator;

public class AccumulatorCycles
    extends InstructionCycles
{
  //8
  public AccumulatorCycles(Executor<Cpu65816> operation)
  {
    super(Accumulator,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), E(operation), DONE()));
  }
}

