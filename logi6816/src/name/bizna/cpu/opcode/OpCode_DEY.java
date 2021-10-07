package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_DEY
    extends OpCode
{
  public OpCode_DEY(int mCode, InstructionCycles cycles)
  {
    super("DEY", "Decrement Index Y by One", mCode, cycles);
  }
}

