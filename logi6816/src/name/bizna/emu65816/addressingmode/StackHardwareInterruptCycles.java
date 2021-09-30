package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.interrupt.InterruptVector;

import static name.bizna.emu65816.AddressingMode.StackInterruptHardware;

public class StackHardwareInterruptCycles
    extends InstructionCycles
{
  public StackHardwareInterruptCycles(InterruptVector interruptVector)
  {
    super(StackInterruptHardware,
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, true, true)),
          new BusCycle(Address(PBR(), PC()), IO(), PC_pp()),
          new BusCycle(Address(S()), Write_PBR(), SP_mm()),
          new BusCycle(Address(S()), Write_PCH(), SP_mm()),
          new BusCycle(Address(S()), Write_PCL(), SP_mm()),
          new BusCycle(Address(S()), Write_PS(), SP_mm(), new Execute1()),
          new BusCycle(Address(InterruptAddress(interruptVector)), Read_AAL()),
          new BusCycle(Address(InterruptAddress(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA())));
  }
}

