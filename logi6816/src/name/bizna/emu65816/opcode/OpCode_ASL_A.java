package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.is16bitValueNegative;
import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift ", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Accumulator:
      {
        if (cpu.isMemory8Bit())
        {
          int accumulator = cpu.getA();
          boolean carry = is8bitValueNegative(accumulator);
          accumulator = toByte(accumulator << 1);
          cpu.setCarryFlag(carry);
          cpu.setA(accumulator);
        }
        break;
      }
      case ASL_Absolute:
      case ASL_DirectPage:
      case ASL_AbsoluteIndexedWithX:
      case ASL_DirectPageIndexedWithX:
      {
        if (cpu.isMemory8Bit())
        {
          int operand = cpu.getData();
          boolean newCarry = is8bitValueNegative(operand);
          operand = toByte(operand << 1);
          cpu.getCpuStatus().setCarryFlag(newCarry);
          cpu.setData(operand);
        }
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Accumulator:
      {
        if (cpu.isMemory16Bit())
        {
          int accumulator = cpu.getA();
          boolean carry = is16bitValueNegative(accumulator);
          accumulator = toShort(accumulator << 1);
          cpu.setCarryFlag(carry);
          cpu.setA(accumulator);
        }
        break;
      }
      case ASL_Absolute:
      case ASL_DirectPage:
      case ASL_AbsoluteIndexedWithX:
      case ASL_DirectPageIndexedWithX:
      {
        if (cpu.isMemory16Bit())
        {
          int operand = cpu.getData();
          boolean newCarry = (operand & 0x8000) != 0;
          operand = toShort(operand << 1);
          cpu.getCpuStatus().setCarryFlag(newCarry);
          cpu.setData(operand);
        }
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

