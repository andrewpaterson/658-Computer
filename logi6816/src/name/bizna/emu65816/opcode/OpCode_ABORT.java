package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ABORT
    extends OpCode
{
  public OpCode_ABORT(InstructionCycles cycles)
  {
    super("ABORT",
          "Stop the current instruction and return processor status to what it was prior to the current instruction.",
          -1,
          cycles);
  }
}
