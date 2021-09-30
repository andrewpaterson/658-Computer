package name.bizna.emu65816.opcode;

import name.bizna.emu65816.BCDResult;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.bcdSum8Bit;
import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ADC
    extends OpCode
{
  public OpCode_ADC(int mCode, InstructionCycles cycles)
  {
    super("ADC", "Add memory, carry and A; result in A and update NZC.", mCode, cycles);
  }

  protected void execute8BitADC(Cpu65816 cpu)
  {
    int operand = cpu.getData();
    int accumulator = cpu.getA();
    int carryValue = (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    int result16Bit = toShort(accumulator + operand + carryValue);

    // Is there a carry out of the penultimate bit, redo the sum with 7 bits value and find out.
    accumulator &= 0x7F;
    operand &= 0x7F;
    int partialResult = toByte(accumulator + operand + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = is8bitValueNegative(partialResult);

    // Is there a carry out of the last bit, check bit 8 for that
    boolean carryOutOfLastBit = (result16Bit & 0x0100) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);
    cpu.setA(toByte(result16Bit));
  }

  protected void execute16BitADC(Cpu65816 cpu)
  {
    int operand = cpu.getData();
    int accumulator = cpu.getA();
    int carryValue = (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    int result32Bit = accumulator + operand + carryValue;

    // Is there a carry out of the penultimate bit, redo the sum with 15 bits value and find out.
    accumulator &= 0x7FFF;
    operand &= 0x7FFF;
    int partialResult = toShort(accumulator + operand + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = (partialResult & 0x8000) != 0;

    // Is there a carry out of the last bit, check bit 16 for that
    boolean carryOutOfLastBit = (result32Bit & 0x010000) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);
    cpu.setA(toShort(result32Bit));
  }

  protected void execute8BitBCDADC(Cpu65816 cpu)
  {
    int operand = cpu.getDataLow();
    int accumulator = cpu.getA();

    BCDResult bcd8BitResult = bcdSum8Bit(operand, accumulator, cpu.getCpuStatus().carryFlag());
    cpu.getCpuStatus().setCarryFlag(bcd8BitResult.carry);
    cpu.setA(bcd8BitResult.value);
  }

  protected void execute16BitBCDADC(Cpu65816 cpu)
  {
    int operand = cpu.getData();
    int accumulator = cpu.getA();

    BCDResult bcd16BitResult;
    bcd16BitResult = Binary.bcdSum16Bit(operand, accumulator, cpu.getCpuStatus().carryFlag());
    cpu.getCpuStatus().setCarryFlag(bcd16BitResult.carry);
    cpu.setA(bcd16BitResult.value);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      if (!cpu.getCpuStatus().decimalFlag())
      {
        execute8BitADC(cpu);
      }
      else
      {
        execute8BitBCDADC(cpu);
      }
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
    {
      if (!cpu.getCpuStatus().decimalFlag())
      {
        execute16BitADC(cpu);
      }
      else
      {
        execute16BitBCDADC(cpu);
      }
    }
  }
}

