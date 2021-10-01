package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackResetCycles;
import name.bizna.emu65816.interrupt.ResetVector;

public class OpCode_RES
    extends OpCode
{
  public OpCode_RES()
  {
    super("RES",
          "Reset the CPU.",
          -1,
          new StackResetCycles(new ResetVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.reset();
  }
}

