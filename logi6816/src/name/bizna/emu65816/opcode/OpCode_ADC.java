package name.bizna.emu65816.opcode;

import name.bizna.emu65816.*;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ADC
    extends OpCode
{
  public OpCode_ADC(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  protected void execute8BitADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    int accumulator = Binary.getLowByte(cpu.getA());
    int carryValue = (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    int result16Bit = toShort(accumulator + value + carryValue);

    // Is there a carry out of the penultimate bit, redo the sum with 7 bits value and find out.
    accumulator &= 0x7F;
    value &= 0x7F;
    int partialResult = toByte(accumulator + value + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = is8bitValueNegative(partialResult);

    // Is there a carry out of the last bit, check bit 8 for that
    boolean carryOutOfLastBit = (result16Bit & 0x0100) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);

    int result8Bit = Binary.getLowByte(result16Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result8Bit);
    // Store the 8 bit result in the accumulator
    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result8Bit));
  }

  protected void execute16BitADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get16BitData();
    int accumulator = cpu.getA();
    int carryValue = (cpu.getCpuStatus().carryFlag() ? 1 : 0);

    int result32Bit = accumulator + value + carryValue;

    // Is there a carry out of the penultimate bit, redo the sum with 15 bits value and find out.
    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    int partialResult = toShort(accumulator + value + carryValue);
    // Is bit 8 set?
    boolean carryOutOfPenultimateBit = (partialResult & 0x8000) != 0;

    // Is there a carry out of the last bit, check bit 16 for that
    boolean carryOutOfLastBit = (result32Bit & 0x010000) != 0;

    boolean overflow = carryOutOfLastBit ^ carryOutOfPenultimateBit;
    cpu.getCpuStatus().setOverflowFlag(overflow);

    cpu.getCpuStatus().setCarryFlag(carryOutOfLastBit);

    int result16Bit = toShort(result32Bit);
    // Update sign and zero flags
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result16Bit);
    // Store the 16 bit result in the accumulator
    cpu.setA(result16Bit);
  }

  protected void execute8BitBCDADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get8BitData();
    int accumulator = Binary.getLowByte(cpu.getA());

    BCDResult bcd8BitResult = Binary.bcdSum8Bit(value, accumulator, cpu.getCpuStatus().carryFlag());
    boolean carry = bcd8BitResult.carry;
    int result = bcd8BitResult.value;
    cpu.getCpuStatus().setCarryFlag(carry);

    cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), result));
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
  }

  protected void execute16BitBCDADC(Cpu65816 cpu)
  {
    Address dataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int value = cpu.get16BitData();
    int accumulator = cpu.getA();

    BCDResult bcd16BitResult = Binary.bcdSum16Bit(value, accumulator, cpu.getCpuStatus().carryFlag());
    boolean carry = bcd16BitResult.carry;
    int result = bcd16BitResult.value;
    cpu.getCpuStatus().setCarryFlag(carry);

    cpu.setA(result);
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(result);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
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
      case ADC_Immediate:                 // ADC Immediate
      {
        if (cpu.isMemory16Bit())
        {
          cpu.addToProgramAddress(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(2);
        break;
      }
      case ADC_Absolute:                 // ADC Absolute
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case ADC_AbsoluteLong:                 // ADC Absolute Long
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case ADC_DirectPage:                 // ADC Direct Page
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(3);
        break;
      }
      case ADC_DirectPageIndirect:                 // ADC Direct Page Indirect
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case ADC_DirectPageIndirectLong:                 // ADC Direct Page Indirect Long
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }

        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case ADC_AbsoluteIndexedWithX:                 // ADC Absolute Indexed, X
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case ADC_AbsoluteLongIndexedWithX:                 // ADC Absolute Long Indexed, X
      {
        cpu.addToProgramAddress(4);
        cpu.addToCycles(5);
        break;
      }
      case (0x79):                 // ADC Absolute Indexed Y
      {
        cpu.addToProgramAddress(3);
        cpu.addToCycles(4);
        break;
      }
      case ADC_DirectPageIndexedWithX:                 // ADC Direct Page Indexed, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case ADC_DirectPageIndexedIndirectWithX:                 // ADC Direct Page Indexed Indirect, X
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case ADC_DirectPageIndirectIndexedWithY:                 // ADC Direct Page Indirect Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(5);
        break;
      }
      case ADC_DirectPageIndirectLongIndexedWithY:                 // ADC Direct Page Indirect Long Indexed, Y
      {
        if (Binary.getLowByte(cpu.getDirectPage()) != 0)
        {
          cpu.addToCycles(1);
        }
        cpu.addToProgramAddress(2);
        cpu.addToCycles(6);
        break;
      }
      case ADC_StackRelative:                 // ADC Stack Relative
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(4);
        break;
      }
      case ADC_StackRelativeIndirectIndexedWithY:                 // ADC Stack Relative Indirect Indexed, Y
      {
        cpu.addToProgramAddress(2);
        cpu.addToCycles(7);
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

