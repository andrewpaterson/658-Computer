package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.OpCodeTable.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_LSR
    extends OpCode
{
  public OpCode_LSR(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  void DO_LSR_8_BIT(Cpu65816 cpu, int value)
  {
    boolean newCarry = (value & 0x01) != 0;
    value = toByte(value >> 1);
    cpu.getCpuStatus().setCarryFlag(newCarry);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(value);
  }

  void DO_LSR_16_BIT(Cpu65816 cpu, int value)
  {
    boolean newCarry = (value & 0x0001) != 0;
    value = toShort(value >> 1);
    cpu.getCpuStatus().setCarryFlag(newCarry);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(value);
  }

  protected void executeMemoryLSR(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());

    if (cpu.accumulatorIs8BitWide())
    {
      int value = cpu.readByte(opCodeDataAddress);
      DO_LSR_8_BIT(cpu, value);
      cpu.storeByte(opCodeDataAddress, value);
    }
    else
    {
      int value = cpu.readTwoBytes(opCodeDataAddress);
      DO_LSR_16_BIT(cpu, value);
      cpu.storeTwoBytes(opCodeDataAddress, value);
    }
  }

  protected void executeAccumulatorLSR(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      int value = Binary.lower8BitsOf(cpu.getA());
      DO_LSR_8_BIT(cpu, value);
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), value));
    }
    else
    {
      DO_LSR_16_BIT(cpu, cpu.getA());
    }
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case LSR_Accumulator:                // LSR Accumulator
      {
        executeAccumulatorLSR(cpu);
        cpu.addToProgramAddressAndCycles(1, 2);
        break;
      }
      case LSR_Absolute:                // LSR Absolute
      {
        executeMemoryLSR(cpu);
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case LSR_DirectPage:                // LSR Direct Page
      {
        executeMemoryLSR(cpu);
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case LSR_AbsoluteIndexedWithX:                // LSR Absolute Indexed, X
      {
        executeMemoryLSR(cpu);
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }

        cpu.addToProgramAddressAndCycles(3, 7);
        break;
      }
      case LSR_DirectPageIndexedWithX:                // LSR Direct Page Indexed, X
      {
        executeMemoryLSR(cpu);
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToCycles(2);
        }
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddressAndCycles(2, 6);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

