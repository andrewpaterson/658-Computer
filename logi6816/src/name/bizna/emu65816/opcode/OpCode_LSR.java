package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_LSR
    extends OpCode
{
  public OpCode_LSR(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift one bit Right (Memory or Accumulator)", mCode, cycles);
  }

  void DO_LSR_8_BIT(Cpu65816 cpu, int value)
  {
    boolean newCarry = (value & 0x01) != 0;
    value = toByte(value >> 1);
    cpu.getCpuStatus().setCarryFlag(newCarry);
    cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(value);
  }

  void DO_LSR_16_BIT(Cpu65816 cpu, int value)
  {
    boolean newCarry = (value & 0x0001) != 0;
    value = toShort(value >> 1);
    cpu.getCpuStatus().setCarryFlag(newCarry);
    cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(value);
  }

  protected void executeMemoryLSR(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());

    if (cpu.isMemory8Bit())
    {
      int value = cpu.getDataLow();
      DO_LSR_8_BIT(cpu, value);
      cpu.storeByte(opCodeDataAddress, value);
    }
    else
    {
      int value = cpu.getData();
      DO_LSR_16_BIT(cpu, value);
      cpu.storeTwoBytes(opCodeDataAddress, value);
    }
  }

  protected void executeAccumulatorLSR(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      int value = Binary.getLowByte(cpu.getA());
      DO_LSR_8_BIT(cpu, value);
      cpu.setA(Binary.setLowByte(cpu.getA(), value));
    }
    else
    {
      DO_LSR_16_BIT(cpu, cpu.getA());
    }
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
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
        if (cpu.isMemory16Bit())
        {
          cpu.addToCycles(2);
        }
        cpu.addToProgramAddressAndCycles(3, 6);
        break;
      }
      case LSR_DirectPage:                // LSR Direct Page
      {
        executeMemoryLSR(cpu);
        if (cpu.isMemory16Bit())
        {
          cpu.addToCycles(2);
        }
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddressAndCycles(2, 5);
        break;
      }
      case LSR_AbsoluteIndexedWithX:                // LSR Absolute Indexed, X
      {
        executeMemoryLSR(cpu);
        if (cpu.isMemory16Bit())
        {
          cpu.addToCycles(2);
        }

        cpu.addToProgramAddressAndCycles(3, 7);
        break;
      }
      case LSR_DirectPageIndexedWithX:                // LSR Direct Page Indexed, X
      {
        executeMemoryLSR(cpu);
        if (cpu.isMemory16Bit())
        {
          cpu.addToCycles(2);
        }
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
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

