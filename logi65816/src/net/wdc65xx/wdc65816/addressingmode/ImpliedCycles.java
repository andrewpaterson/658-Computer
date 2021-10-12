package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Implied;

public class ImpliedCycles
    extends InstructionCycles
{
  //19a
  public ImpliedCycles(Executor<Cpu65816> operation)
  {
    super(Implied,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), E(operation), DONE()));
  }
}

