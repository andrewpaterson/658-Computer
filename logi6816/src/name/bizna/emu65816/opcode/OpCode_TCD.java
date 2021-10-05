package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TCD
    extends OpCode
{
  public OpCode_TCD(int mCode, InstructionCycles cycles)
  {
    super("TCD", "Transfer C Accumulator to Direct Register", mCode, cycles);
  }
}

