package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.interrupt.InterruptVector;

import static name.bizna.emu65816.AddressingMode.StackInterruptHardware;

public class StackResetCycles
    extends InstructionCycles
{
  public StackResetCycles(InterruptVector interruptVector)
  {
    super(StackInterruptHardware,
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, true, true)),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(S()), IO()),
          new BusCycle(Address(S()), IO(), new Execute1()),
          new BusCycle(Address(InterruptAddress(interruptVector)), Read_AAL()),
          new BusCycle(Address(InterruptAddress(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA())));
  }
}

