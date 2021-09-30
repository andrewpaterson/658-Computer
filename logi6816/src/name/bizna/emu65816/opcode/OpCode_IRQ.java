package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackHardwareInterruptCycles;
import name.bizna.emu65816.interrupt.IRQVector;

public class OpCode_IRQ
    extends OpCode
{
  public OpCode_IRQ()
  {
    super("IRQ", "Interrupt request.", -1, new StackHardwareInterruptCycles(new IRQVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.interruptRequest();
  }
}

