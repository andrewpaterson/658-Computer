package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_XBA
    extends OpCode
{
  public OpCode_XBA(int mCode, InstructionCycles cycles)
  {
    super("XBA", "Exchange B and A Accumulator", mCode, cycles);
  }
}

