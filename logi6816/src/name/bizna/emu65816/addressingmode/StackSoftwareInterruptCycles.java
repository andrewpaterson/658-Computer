package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.interrupt.InterruptVector;

import static name.bizna.emu65816.AddressingMode.StackInterruptSoftware;

public class StackSoftwareInterruptCycles
    extends InstructionCycles
{
  public StackSoftwareInterruptCycles(InterruptVector interruptVector)
  {
    super(StackInterruptSoftware,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), IO(), PC_pp()),
          new BusCycle(Address(S()), Write_PBR(), SP_mm()),
          new BusCycle(Address(S()), Write_PCH(), SP_mm()),
          new BusCycle(Address(S()), Write_PCL(), SP_mm()),
          new BusCycle(Address(S()), Write_PS(), SP_mm(), new Execute1()),
          new BusCycle(Address(InterruptAddress(interruptVector)), Read_AAL()),
          new BusCycle(Address(InterruptAddress(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA())));
  }
}

