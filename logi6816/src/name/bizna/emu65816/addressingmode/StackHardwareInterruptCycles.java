package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.interrupt.InterruptVector;

import static name.bizna.emu65816.AddressingMode.StackInterruptHardware;

public class StackHardwareInterruptCycles
    extends InstructionCycles
{
  //22a
  public StackHardwareInterruptCycles(InterruptVector interruptVector)
  {
    super(StackInterruptHardware,
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, true, true)),
          new BusCycle(Address(PBR(), PC()), IO(), PC_inc()),
          new BusCycle(Address(S()), Write_PBR(), SP_dec(), PBR_e(0), new NoteSeven()),
          new BusCycle(Address(S()), Write_PCH(), SP_dec()),
          new BusCycle(Address(S()), Write_PCL(), SP_dec()),
          new BusCycle(Address(S()), Write_PS(), SP_dec(), new Execute1()),
          new BusCycle(Address(VA(interruptVector)), Read_AAL()),
          new BusCycle(Address(VA(interruptVector), o(1)), Read_AAH(), PC_e(AA()), DONE()));
  }
}

