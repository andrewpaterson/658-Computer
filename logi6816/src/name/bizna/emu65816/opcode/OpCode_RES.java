package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.*;

import static name.bizna.emu65816.AddressingMode.StackInterruptHardware;

public class Reset
    extends OpCode
{
  public Reset()
  {
    super("Reset", -1, new InstructionCycles(StackInterruptHardware,
                                             new BusCycle(new ProgramCounter(), new InternalOperation(true, true, true)),
                                             new BusCycle(new ProgramCounter(), new InternalOperation(true), new Execute1()),
                                             new BusCycle(new StackPointer(), new InternalOperation(true)),
                                             new BusCycle(new StackPointer(), new InternalOperation(true)),
                                             new BusCycle(new StackPointer(), new InternalOperation(true)),
                                             new BusCycle(new StackPointer(), new InternalOperation(true)),
                                             new BusCycle(new InterruptVector(0xfffc), new FetchAbsoluteAddressLow(true)),
                                             new BusCycle(new InterruptVector(0xfffc), new Offset(1), new FetchAbsoluteAddressHigh(true), new SetProgramCounter(new AbsoluteAddress()))));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.reset();
  }
}

