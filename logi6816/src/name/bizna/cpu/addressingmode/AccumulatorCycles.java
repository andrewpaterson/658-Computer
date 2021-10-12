package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.Accumulator;

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

