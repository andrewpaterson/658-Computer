package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.interrupt.InterruptVector;

import static net.wdc65xx.wdc65816.AddressingMode.StackInterruptHardware;

public class StackResetCycles
    extends InstructionCycles
{
  //22a
  public StackResetCycles(InterruptVector interruptVector, Executor<Cpu65816> consumer)
  {
    super(StackInterruptHardware,
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, true, true)),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), IO(), SP_dec()),
          new BusCycle(Address(S()), IO(), SP_dec()),
          new BusCycle(Address(S()), IO(), SP_dec(), E(consumer)),
          new BusCycle(Address(VA(interruptVector)), Read_AAL()),
          new BusCycle(Address(VA(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA()), DONE()));
  }
}

