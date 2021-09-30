package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackSoftwareInterruptCycles;
import name.bizna.emu65816.interrupt.BRKVector;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(int code)
  {
    super("BRK", "Force software interrupt.", code, new StackSoftwareInterruptCycles(new BRKVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.break_();
  }
}

