package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.interrupt.InterruptVector;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.StackInterruptHardware;

public class StackResetCycles
    extends InstructionCycles
{
  //22a
  public StackResetCycles(InterruptVector interruptVector, Consumer<Cpu65816> consumer)
  {
    super(StackInterruptHardware,
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, true, true)),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(S()), IO(), E(consumer)),
          new BusCycle(Address(VA(interruptVector)), Read_AAL()),
          new BusCycle(Address(VA(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA()), DONE()));
  }
}

