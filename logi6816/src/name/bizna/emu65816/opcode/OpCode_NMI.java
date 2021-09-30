package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackHardwareInterruptCycles;
import name.bizna.emu65816.interrupt.NMIVector;

public class OpCode_NMI
    extends OpCode
{
  public OpCode_NMI()
  {
    super("NMI", "Non-maskable interrupt.", -1, new StackHardwareInterruptCycles(new NMIVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.nonMaskableInterrupt();
  }
}

