package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.StackHardwareInterruptCycles;
import name.bizna.emu65816.interrupt.AbortVector;

public class OpCode_ABORT
    extends OpCode
{
  public OpCode_ABORT()
  {
    super("ABORT",
          "Stop the current instruction and return processor status to what it was prior to the current instruction.",
          -1,
          new StackHardwareInterruptCycles(new AbortVector()));
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.abort();
  }
}

