package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackSoftwareInterruptCycles;
import name.bizna.emu65816.interrupt.COPVector;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(int code)
  {
    super("COP", "Force software interrupt.", code, new StackSoftwareInterruptCycles(new COPVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.coprocessor();
  }
}

