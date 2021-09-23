package name.bizna.emu65816.opcode;

import name.bizna.emu65816.*;

public class OpCode_ADC
    extends OpCode
{
  public OpCode_ADC(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(dataAddress);
    byte accumulator = Binary.lower8BitsOf(cpu.getA());
    byte carryValue = (byte) (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    short result16Bit = (short) (accumulator + value + carryValue);

    // Is there a carry out of the penultimate bit, redo the sum with 7 bits value and find out.
    accumulator &= 0x7F;
    value &= 0x7F;
    byte partialResult = (byte) (accumulator + value + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = (partialResult & 0x80) != 0;

    // Is there a carry out of the last bit, check bit 8 for that
    boolean carryOutOfLastBit = (result16Bit & 0x0100) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    if (overflow)
    {
      cpu.getCpuStatus().setOverflowFlag();
    }
    else
    {
      cpu.getCpuStatus().clearOverflowFlag();
    }

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);

    byte result8Bit = Binary.lower8BitsOf(result16Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result8Bit);
    // Store the 8 bit result in the accumulator
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result8Bit));
  }

  protected void execute16BitADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(dataAddress);
    short accumulator = cpu.getA();
    short carryValue = (short) (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    int result32Bit = accumulator + value + carryValue;

    // Is there a carry out of the penultimate bit, redo the sum with 15 bits value and find out.
    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    short partialResult = (short) (accumulator + value + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = (partialResult & 0x8000) != 0;

    // Is there a carry out of the last bit, check bit 16 for that
    boolean carryOutOfLastBit = (result32Bit & 0x010000) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    if (overflow)
    {
      cpu.getCpuStatus().setOverflowFlag();
    }
    else
    {
      cpu.getCpuStatus().clearOverflowFlag();
    }

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);

    byte result16Bit = (byte) Binary.lower16BitsOf(result32Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result16Bit);
    // Store the 16 bit result in the accumulator
    cpu.setA(result16Bit);
  }

  protected void execute8BitBCDADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    byte value = cpu.readByte(dataAddress);
    byte accumulator = Binary.lower8BitsOf(cpu.getA());

    byte result = 0;
    BCD8BitResult bcd8BitResult = Binary.bcdSum8Bit(value, accumulator, result, cpu.getCpuStatus().carryFlag());
    boolean carry = bcd8BitResult.carry;
    result = bcd8BitResult.value;
    cpu.getCpuStatus().setCarryFlag(carry);

    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
  }

  protected void execute16BitBCDADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    short value = cpu.readTwoBytes(dataAddress);
    short accumulator = cpu.getA();

    short result = 0;
    BCD16BitResult bcd16BitResult = Binary.bcdSum16Bit(value, accumulator, result, cpu.getCpuStatus().carryFlag());
    boolean carry = bcd16BitResult.carry;
    result = bcd16BitResult.value;
    cpu.getCpuStatus().setCarryFlag(carry);

    cpu.setA(result);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue((byte) result);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute8BitBCDADC(cpu);
      }
      else
      {
        execute8BitADC(cpu);
      }
    }
    else
    {
      if (cpu.getCpuStatus().decimalFlag())
      {
        execute16BitBCDADC(cpu);
      }
      else
      {
        execute16BitADC(cpu);
      }
      cpu.addToCycles(1);
    }

    switch (getCode())
    {
      case (0x69):                 // ADC Immediate
      {
        if (cpu.accumulatorIs16BitWide())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(2);
        break;
      }
      case (0x6D):                 // ADC Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case (0x6F):                 // ADC Absolute Long
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case (0x65):                 // ADC Direct Page
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case (0x72):                 // ADC Direct Page Indirect
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case (0x67):                 // ADC Direct Page Indirect Long
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case (0x7D):                 // ADC Absolute Indexed, X
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case (0x7F):                 // ADC Absolute Long Indexed, X
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case (0x79):                 // ADC Absolute Indexed Y
      {
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case (0x75):                 // ADC Direct Page Indexed, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case (0x61):                 // ADC Direct Page Indexed Indirect, X
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case (0x71):                 // ADC Direct Page Indirect Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        if (cpu.opCodeAddressingCrossesPageBoundary(getAddressingMode()))
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case (0x77):                 // ADC Direct Page Indirect Long Indexed, Y
      {
        if (Binary.lower8BitsOf(cpu.getD()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case (0x63):                 // ADC Stack Relative
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case (0x73):                 // ADC Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

