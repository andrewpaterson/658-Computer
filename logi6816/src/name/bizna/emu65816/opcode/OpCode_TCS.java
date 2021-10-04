package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TCS
    extends OpCode
{
  public OpCode_TCS(int mCode, InstructionCycles cycles)
  {
    super("TCS", "Transfer C Accumulator to Stack Pointer", mCode, cycles);
  }
}

