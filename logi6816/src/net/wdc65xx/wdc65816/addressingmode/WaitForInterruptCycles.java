package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.WaitForInterrupt;

public class WaitForInterruptCycles
    extends InstructionCycles
{
  public WaitForInterruptCycles(Executor<Cpu65816> consumer)
  {
    super(WaitForInterrupt,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), new WaitOperation(), E(consumer), DONE()));
  }
}

