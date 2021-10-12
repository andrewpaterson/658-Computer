package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.interrupt.InterruptVector;

import static name.bizna.cpu.AddressingMode.StackInterruptHardware;

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

