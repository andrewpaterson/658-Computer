package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.is16bitValueNegative;
import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ASL_A
    extends OpCode
{
  public OpCode_ASL_A(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift accumulator left one bit; update NZC.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      int accumulator = cpu.getA();
      boolean carry = is8bitValueNegative(accumulator);
      accumulator = toByte(accumulator << 1);
      cpu.setCarryFlag(carry);
      cpu.setA(accumulator);
    }
    else
    {
      if (cpu.isMemory16Bit())
      {
        int accumulator = cpu.getA();
        boolean carry = is16bitValueNegative(accumulator);
        accumulator = toShort(accumulator << 1);
        cpu.setCarryFlag(carry);
        cpu.setA(accumulator);
      }
    }
  }
}

