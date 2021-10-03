package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.interrupt.InterruptVector;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.StackInterruptSoftware;

public class StackSoftwareInterruptCycles
    extends InstructionCycles
{
  //22j
  public StackSoftwareInterruptCycles(InterruptVector interruptVector, Consumer<Cpu65816> operation)
  {
    super(StackInterruptSoftware,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), new InternalOperation(true, false, true), PC_inc()),  //Read and ignore the signature byte.
          new BusCycle(Address(S()), Write_PBR(), SP_dec(), PBR_e(0), new NoteSeven()),
          new BusCycle(Address(S()), Write_PCH(), SP_dec()),
          new BusCycle(Address(S()), Write_PCL(), SP_dec()),
          new BusCycle(Address(S()), Write_PS(), SP_dec(), E(operation)),
          new BusCycle(Address(VA(interruptVector)), Read_AAL()),
          new BusCycle(Address(VA(interruptVector), o(1)), Read_AAH(), PC_e(PBR(), AA()), DONE()));
  }
}

